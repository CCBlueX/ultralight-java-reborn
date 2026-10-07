#pragma once

#include <Ultralight/platform/Clipboard.h>

#include "ujr/util/JniRef.hpp"

namespace ujr {
    /**
     * Clipboard adapter for Ultralight delegating to a Java instance, which only handles plain text.
     */
    class Clipboard : public ultralight::Clipboard {
    private:
        JniGlobalRef<jobject> j_clipboard;

    public:
        explicit Clipboard(JniGlobalRef<jobject> j_clipboard);

        void Clear() final;

        ultralight::RefPtr<ultralight::ClipboardData> Read() final;

        void Write(ultralight::RefPtr<ultralight::ClipboardData> data) final;

        /**
         * Retrieves the underlying java clipboard instance.
         *
         * @return the java clipboard instance
         */
        [[nodiscard]] const JniGlobalRef<jobject> &get_j_clipboard() const;
    };
} // namespace ujr
