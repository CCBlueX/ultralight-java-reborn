#pragma once

#ifndef _WIN32
#include <locale.h>
#endif

namespace ujr {
    /**
     * Switches the calling thread to the C locale for numbers while it lives.
     *
     * Ultralight parses the options of JavaScriptCore with the locale of the process when it creates the renderer and
     * views. The JVM takes that locale from the environment on Linux and macOS, and a decimal comma (de_DE for
     * example) breaks the options until JavaScriptCore aborts.
     */
    class CNumericLocale {
#ifndef _WIN32
    private:
        locale_t c_locale;
        locale_t previous;

    public:
        CNumericLocale()
            : c_locale(newlocale(LC_NUMERIC_MASK, "C", duplocale(uselocale(nullptr))))
            , previous(uselocale(c_locale)) {}

        ~CNumericLocale() {
            uselocale(previous);
            freelocale(c_locale);
        }
#else
    public:
        // The JVM leaves the C runtime in the C locale on Windows; user-provided so MSVC doesn't warn about an
        // unreferenced local
        CNumericLocale() {}
#endif

        CNumericLocale(const CNumericLocale &) = delete;

        CNumericLocale &operator=(const CNumericLocale &) = delete;
    };
} // namespace ujr
