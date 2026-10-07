package net.janrupf.ujr.api;

import net.janrupf.ujr.api.config.UlConfig;
import net.janrupf.ujr.api.config.UlEffectQuality;
import net.janrupf.ujr.api.config.UlFaceWinding;
import net.janrupf.ujr.api.config.UlFontHinting;

/**
 * Helper class to create a new {@link UlConfig} instance with a fluent API.
 */
public class UltralightConfigBuilder extends UlConfig {
    /**
     * Creates a new {@link UltralightConfigBuilder} instance with the default configuration.
     */
    public UltralightConfigBuilder() {
        super();
    }

    /**
     * Sets the cache path.
     * <p>
     * See {@link UlConfig#cachePath} for more information.
     *
     * @param cachePath the cache path to use
     * @return this
     */
    public UltralightConfigBuilder cachePath(String cachePath) {
        this.cachePath = cachePath;
        return this;
    }

    /**
     * Sets the resource path prefix.
     * <p>
     * See {@link UlConfig#resourcePathPrefix} for more information.
     *
     * @param resourcePathPrefix the resource path prefix to use
     * @return this
     */
    public UltralightConfigBuilder resourcePathPrefix(String resourcePathPrefix) {
        this.resourcePathPrefix = resourcePathPrefix;
        return this;
    }

    /**
     * Sets the face winding.
     * <p>
     * See {@link UlConfig#faceWinding} for more information.
     *
     * @param faceWinding the face winding to use
     * @return this
     */
    public UltralightConfigBuilder faceWinding(UlFaceWinding faceWinding) {
        this.faceWinding = faceWinding;
        return this;
    }

    /**
     * Sets the font hinting.
     * <p>
     * See {@link UlConfig#fontHinting} for more information.
     *
     * @param fontHinting the font hinting to use
     * @return this
     */
    public UltralightConfigBuilder fontHinting(UlFontHinting fontHinting) {
        this.fontHinting = fontHinting;
        return this;
    }

    /**
     * Sets the font gamma.
     * <p>
     * See {@link UlConfig#fontGamma} for more information.
     *
     * @param fontGamma the font gamma to use
     * @return this
     */
    public UltralightConfigBuilder fontGamma(double fontGamma) {
        this.fontGamma = fontGamma;
        return this;
    }

    /**
     * Sets the user stylesheet.
     * <p>
     * See {@link UlConfig#userStylesheet} for more information.
     *
     * @param userStylesheet the user stylesheet to use
     * @return this
     */
    public UltralightConfigBuilder userStylesheet(String userStylesheet) {
        this.userStylesheet = userStylesheet;
        return this;
    }

    /**
     * Sets the force repaint flag.
     * <p>
     * See {@link UlConfig#forceRepaint} for more information.
     *
     * @param forceRepaint the force repaint flag to use
     * @return this
     */
    public UltralightConfigBuilder forceRepaint(boolean forceRepaint) {
        this.forceRepaint = forceRepaint;
        return this;
    }

    /**
     * Sets whether paths render analytically.
     * <p>
     * See {@link UlConfig#enablePhoton} for more information.
     *
     * @param enablePhoton whether paths render analytically
     * @return this
     */
    public UltralightConfigBuilder enablePhoton(boolean enablePhoton) {
        this.enablePhoton = enablePhoton;
        return this;
    }

    /**
     * Sets whether text renders analytically.
     * <p>
     * See {@link UlConfig#enablePhotonText} for more information.
     *
     * @param enablePhotonText whether text renders analytically
     * @return this
     */
    public UltralightConfigBuilder enablePhotonText(boolean enablePhotonText) {
        this.enablePhotonText = enablePhotonText;
        return this;
    }

    /**
     * Sets the smallest glyph size that renders analytically.
     * <p>
     * See {@link UlConfig#photonTextMinPx} for more information.
     *
     * @param photonTextMinPx the size in pixels
     * @return this
     */
    public UltralightConfigBuilder photonTextMinPx(long photonTextMinPx) {
        this.photonTextMinPx = photonTextMinPx;
        return this;
    }

    /**
     * Sets the recycle delay.
     * <p>
     * See {@link UlConfig#recycleDelay} for more information.
     *
     * @param recycleDelay the recycle delay to use
     * @return this
     */
    public UltralightConfigBuilder recycleDelay(double recycleDelay) {
        this.recycleDelay = recycleDelay;
        return this;
    }

    /**
     * Sets the memory cache size.
     * <p>
     * See {@link UlConfig#memoryCacheSize} for more information.
     *
     * @param memoryCacheSize the memory cache size to use
     * @return this
     */
    public UltralightConfigBuilder memoryCacheSize(long memoryCacheSize) {
        this.memoryCacheSize = memoryCacheSize;
        return this;
    }

    /**
     * Sets the page cache size.
     * <p>
     * See {@link UlConfig#pageCacheSize} for more information.
     *
     * @param pageCacheSize the page cache size to use
     * @return this
     */
    public UltralightConfigBuilder pageCacheSize(long pageCacheSize) {
        this.pageCacheSize = pageCacheSize;
        return this;
    }

    /**
     * Sets the overwritten ram size.
     * <p>
     * See {@link UlConfig#overrideRamSize} for more information.
     *
     * @param overrideRamSize the ram size to use
     * @return this
     */
    public UltralightConfigBuilder overrideRamSize(long overrideRamSize) {
        this.overrideRamSize = overrideRamSize;
        return this;
    }

    /**
     * Sets the minimum large heap size.
     * <p>
     * See {@link UlConfig#minLargeHeapSize} for more information.
     *
     * @param minLargeHeapSize the minimum large heap size to use
     * @return this
     */
    public UltralightConfigBuilder minLargeHeapSize(long minLargeHeapSize) {
        this.minLargeHeapSize = minLargeHeapSize;
        return this;
    }

    /**
     * Sets the minimum small heap size.
     * <p>
     * See {@link UlConfig#minSmallHeapSize} for more information.
     *
     * @param minSmallHeapSize the minimum small heap size to use
     * @return this
     */
    public UltralightConfigBuilder minSmallHeapSize(long minSmallHeapSize) {
        this.minSmallHeapSize = minSmallHeapSize;
        return this;
    }

    /**
     * Sets the number of render threads.
     * <p>
     * See {@link UlConfig#numRendererThreads} for more information.
     *
     * @param numRenderThreads the number of render threads to use
     * @return this
     */
    public UltralightConfigBuilder numRendererThreads(long numRenderThreads) {
        this.numRendererThreads = numRenderThreads;
        return this;
    }

    /**
     * Sets the maximum update time.
     * <p>
     * See {@link UlConfig#maxUpdateTime} for more information.
     *
     * @param maxUpdateTime the maximum update time to use
     * @return this
     */
    public UltralightConfigBuilder maxUpdateTime(double maxUpdateTime) {
        this.maxUpdateTime = maxUpdateTime;
        return this;
    }

    /**
     * Sets the bitmap alignment.
     * <p>
     * See {@link UlConfig#bitmapAlignment} for more information.
     *
     * @param bitmapAlignment the bitmap alignment to use
     * @return this
     */
    public UltralightConfigBuilder bitmapAlignment(long bitmapAlignment) {
        this.bitmapAlignment = bitmapAlignment;
        return this;
    }

    /**
     * Sets the quality of effects such as blurs and shadows.
     * <p>
     * See {@link UlConfig#effectQuality} for more information.
     *
     * @param effectQuality the effect quality to use
     * @return this
     */
    public UltralightConfigBuilder effectQuality(UlEffectQuality effectQuality) {
        this.effectQuality = effectQuality;
        return this;
    }

    /**
     * Finishes building the configuration.
     * <p>
     * This method is a no-op and is only present for consistency with the other builders.
     *
     * @return this
     */
    public UlConfig build() {
        return this;
    }
}
