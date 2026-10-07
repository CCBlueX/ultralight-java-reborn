#include "ujr/wrapper/clipboard/Clipboard.hpp"
#include "net_janrupf_ujr_platform_jni_wrapper_clipboard_JNIUlClipboard_native_access.hpp"

#include <Ultralight/ClipboardData.h>

namespace ujr {
    Clipboard::Clipboard(JniGlobalRef<jobject> j_clipboard)
        : j_clipboard(std::move(j_clipboard)) {}

    void Clipboard::Clear() {
        using native_access::JNIUlClipboard;

        auto env = JniEnv::require_existing_from_thread();
        JNIUlClipboard::CLEAR.invoke(env, j_clipboard);
    }

    ultralight::RefPtr<ultralight::ClipboardData> Clipboard::Read() {
        using native_access::JNIUlClipboard;

        auto env = JniEnv::require_existing_from_thread();
        auto j_result = JNIUlClipboard::READ_PLAIN_TEXT.invoke(env, j_clipboard);

        if (!j_result.is_valid()) {
            return ultralight::ClipboardData::Create();
        }

        return ultralight::ClipboardData::Create(j_result.to_utf16());
    }

    void Clipboard::Write(ultralight::RefPtr<ultralight::ClipboardData> data) {
        using native_access::JNIUlClipboard;

        auto env = JniEnv::require_existing_from_thread();
        auto j_text = JniLocalRef<jstring>::from_utf16(env, data->AsText().utf16());

        JNIUlClipboard::WRITE_PLAIN_TEXT.invoke(env, j_clipboard, j_text);
    }

    const JniGlobalRef<jobject> &Clipboard::get_j_clipboard() const { return j_clipboard; }
} // namespace ujr
