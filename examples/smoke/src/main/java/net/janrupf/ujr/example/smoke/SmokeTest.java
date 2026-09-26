package net.janrupf.ujr.example.smoke;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import net.janrupf.ujr.api.*;
import net.janrupf.ujr.api.bitmap.UltralightBitmap;
import net.janrupf.ujr.api.bitmap.UltralightBitmapSurface;
import net.janrupf.ujr.api.clipboard.UltralightClipboard;
import net.janrupf.ujr.api.filesystem.UltralightFilesystem;
import net.janrupf.ujr.api.javascript.JavaScriptException;
import net.janrupf.ujr.api.listener.UltralightLoadListener;
import net.janrupf.ujr.api.util.NioUltralightBuffer;
import net.janrupf.ujr.api.util.UltralightBuffer;
import net.janrupf.ujr.core.UltralightJavaReborn;
import net.janrupf.ujr.core.platform.PlatformEnvironment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Renders a page from a local server and checks the result, which covers loading the natives and the Ultralight
 * runtime, listeners, JavaScript, cookies, fetch, WebSockets and painting.
 * <p>
 * Exits with a non-zero status if anything doesn't work.
 */
public final class SmokeTest {
    private static final String EXPECTED_TITLE = "ok:injected:session=smoke:pong";

    public static void main(String[] args) {
        try {
            run();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static void run() throws Exception {
        ServerSocket webSocketServer = new ServerSocket(0, 50, InetAddress.getLoopbackAddress());
        Thread webSocketThread = new Thread(() -> serveWebSockets(webSocketServer), "WebSocket server");
        webSocketThread.setDaemon(true);
        webSocketThread.start();

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        httpServer.createContext("/", exchange -> {
            exchange.getResponseHeaders().add("Set-Cookie", "session=smoke; HttpOnly; Path=/");
            respond(exchange, "text/html", page(webSocketServer.getLocalPort()));
        });
        httpServer.createContext("/echo", exchange -> {
            String cookie = exchange.getRequestHeaders().getFirst("Cookie");
            respond(exchange, "text/plain", cookie == null ? "no cookie" : cookie);
        });
        httpServer.start();

        UltralightJavaReborn ujr = new UltralightJavaReborn(PlatformEnvironment.load());
        ujr.activate();

        UltralightPlatform platform = UltralightPlatform.instance();
        platform.setLogger((level, message) -> System.out.println("[Ultralight " + level + "] " + message));
        platform.usePlatformFontLoader();
        platform.setFilesystem(new ResourceFilesystem());
        platform.setClipboard(new NoClipboard());
        platform.setConfig(new UltralightConfigBuilder()
                .cachePath(Files.createTempDirectory("ujr-smoke").toString())
                .resourcePathPrefix(ResourceFilesystem.PREFIX)
                .bitmapAlignment(0)
                .build());

        UltralightRenderer renderer = UltralightRenderer.getOrCreate();
        UltralightView view = renderer.createView(64, 64, new UltralightViewConfigBuilder().build());
        view.setLoadListener(new UltralightLoadListener() {
            @Override
            public void onWindowObjectReady(UltralightView view, long frameId, boolean isMainFrame, String url) {
                // Runs before the scripts of the page, which read it
                if (isMainFrame) {
                    try {
                        view.evaluateScript("window.injected = 'injected';");
                    } catch (JavaScriptException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        });
        view.loadURL("http://127.0.0.1:" + httpServer.getAddress().getPort() + "/");

        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);
        while (!EXPECTED_TITLE.equals(view.title()) && System.nanoTime() < deadline) {
            frame(renderer);
        }

        // A few more frames, so the green background is painted
        for (int i = 0; i < 10; i++) {
            frame(renderer);
        }

        String title = view.title();
        int[] pixel = centerPixel((UltralightBitmapSurface) view.surface());

        ujr.cleanup();
        httpServer.stop(0);
        webSocketServer.close();

        System.out.println("Title: " + title);
        System.out.printf("Center pixel (BGRA): %d %d %d %d%n", pixel[0], pixel[1], pixel[2], pixel[3]);

        if (!EXPECTED_TITLE.equals(title)) {
            System.err.println("Smoke test failed: expected the title " + EXPECTED_TITLE);
            System.exit(1);
        }

        if (pixel[0] > 50 || pixel[1] < 200 || pixel[2] > 50) {
            System.err.println("Smoke test failed: the page was not painted green");
            System.exit(1);
        }

        System.out.println("Smoke test passed");
        System.exit(0);
    }

    private static void frame(UltralightRenderer renderer) throws InterruptedException {
        renderer.update();
        renderer.refreshDisplay(0);
        renderer.render();
        Thread.sleep(10);
    }

    private static int[] centerPixel(UltralightBitmapSurface surface) {
        UltralightBitmap bitmap = surface.bitmap();
        int offset = (int) (bitmap.height() / 2 * bitmap.rowBytes() + bitmap.width() / 2 * 4);

        try (UltralightBuffer pixels = bitmap.lockPixels()) {
            ByteBuffer buffer = pixels.asByteBuffer();
            return new int[]{
                    buffer.get(offset) & 0xFF,
                    buffer.get(offset + 1) & 0xFF,
                    buffer.get(offset + 2) & 0xFF,
                    buffer.get(offset + 3) & 0xFF
            };
        }
    }

    private static String page(int webSocketPort) {
        return "<!doctype html><html><head><title>loading</title>" +
                "<style>html, body { margin: 0; height: 100%; background: #ff0000; }</style></head><body><script>" +
                "fetch('/echo').then(response => response.text()).then(cookie => {" +
                "  const socket = new WebSocket('ws://127.0.0.1:" + webSocketPort + "/');" +
                "  socket.onopen = () => socket.send('ping');" +
                "  socket.onmessage = event => {" +
                "    document.documentElement.style.background = '#00ff00';" +
                "    document.body.style.background = '#00ff00';" +
                "    document.title = 'ok:' + window.injected + ':' + cookie + ':' + event.data;" +
                "  };" +
                "  socket.onerror = () => { document.title = 'websocket error'; };" +
                "}).catch(error => { document.title = 'fetch error: ' + error; });" +
                "</script></body></html>";
    }

    private static void respond(HttpExchange exchange, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", contentType + "; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    /**
     * A WebSocket server which answers the first message with "pong".
     */
    private static void serveWebSockets(ServerSocket server) {
        while (!server.isClosed()) {
            try (Socket socket = server.accept()) {
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                String key = null;
                String line;
                while (!(line = readLine(in)).isEmpty()) {
                    if (line.toLowerCase().startsWith("sec-websocket-key:")) {
                        key = line.substring(line.indexOf(':') + 1).trim();
                    }
                }

                out.write(("HTTP/1.1 101 Switching Protocols\r\n" +
                        "Upgrade: websocket\r\n" +
                        "Connection: Upgrade\r\n" +
                        "Sec-WebSocket-Accept: " + acceptKey(key) + "\r\n\r\n").getBytes(StandardCharsets.US_ASCII));
                out.flush();

                // Read the client's frame, which is always masked
                in.read();
                int length = in.read() & 0x7F;
                byte[] mask = readFully(in, 4);
                byte[] payload = readFully(in, length);
                for (int i = 0; i < payload.length; i++) {
                    payload[i] ^= mask[i % 4];
                }

                if ("ping".equals(new String(payload, StandardCharsets.UTF_8))) {
                    byte[] pong = "pong".getBytes(StandardCharsets.UTF_8);
                    out.write(new byte[]{(byte) 0x81, (byte) pong.length});
                    out.write(pong);
                    out.flush();
                }

                // Keep the connection open until the client goes away
                //noinspection StatementWithEmptyBody
                while (in.read() != -1) { /* discard */ }
            } catch (IOException ignored) {
                // The server was closed, or a client misbehaved
            }
        }
    }

    private static String acceptKey(String key) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-1")
                    .digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes(StandardCharsets.US_ASCII));
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String readLine(InputStream in) throws IOException {
        ByteArrayOutputStream line = new ByteArrayOutputStream();
        int read;
        while ((read = in.read()) != -1 && read != '\n') {
            if (read != '\r') {
                line.write(read);
            }
        }
        return new String(line.toByteArray(), StandardCharsets.US_ASCII);
    }

    private static byte[] readFully(InputStream in, int length) throws IOException {
        byte[] bytes = new byte[length];
        int offset = 0;
        while (offset < length) {
            int read = in.read(bytes, offset, length - offset);
            if (read == -1) {
                throw new IOException("Connection closed");
            }
            offset += read;
        }
        return bytes;
    }

    /**
     * Serves the resources of the Ultralight runtime, such as the ICU data and the CA certificates.
     */
    private static final class ResourceFilesystem implements UltralightFilesystem {
        static final String PREFIX = "$built-in/resources/";

        @Override
        public boolean fileExists(String path) {
            return resource(path) != null;
        }

        @Override
        public String getFileMimeType(String path) {
            return "application/octet-stream";
        }

        @Override
        public String getFileCharset(String path) {
            return "utf-8";
        }

        @Override
        public UltralightBuffer openFile(String path) throws IOException {
            URI resource = resource(path);
            if (resource == null) {
                return null;
            }

            byte[] bytes = Files.readAllBytes(Paths.get(resource));
            ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            return new NioUltralightBuffer(buffer);
        }

        private static URI resource(String path) {
            return path.startsWith(PREFIX) ? UltralightResources.getResource(path.substring(PREFIX.length())) : null;
        }
    }

    private static final class NoClipboard implements UltralightClipboard {
        @Override
        public void clear() {
        }

        @Override
        public String readPlainText() {
            return "";
        }

        @Override
        public void writePlainText(String text) {
        }
    }
}
