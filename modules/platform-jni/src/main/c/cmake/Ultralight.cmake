################################
# Set up 3rdparty dependencies #
################################

# The Ultralight SDK, either as its .7z archive or an extracted directory. Without it, the version below is
# downloaded.
set(ULTRALIGHT_SDK "$ENV{ULTRALIGHT_SDK}" CACHE FILEPATH "Ultralight SDK archive (.7z) or extracted directory")

set(ULTRALIGHT_VERSION "1.4.0")

# Set the architecture information for ultralight
set(ULTRALIGHT_ARCH "" CACHE STRING "Override the Ultralight architecture (x64 or arm64)")
if (NOT ULTRALIGHT_ARCH)
    if (APPLE AND CMAKE_OSX_ARCHITECTURES)
        set(ULTRALIGHT_PROCESSOR "${CMAKE_OSX_ARCHITECTURES}")
    else ()
        set(ULTRALIGHT_PROCESSOR "${CMAKE_SYSTEM_PROCESSOR}")
    endif ()

    string(TOLOWER "${ULTRALIGHT_PROCESSOR}" ULTRALIGHT_PROCESSOR)
    if (ULTRALIGHT_PROCESSOR MATCHES "^(x86_64|amd64|x64)$")
        set(ULTRALIGHT_ARCH "x64")
    elseif (ULTRALIGHT_PROCESSOR MATCHES "^(aarch64|arm64)$")
        set(ULTRALIGHT_ARCH "arm64")
    else ()
        message(FATAL_ERROR "Unsupported processor ${ULTRALIGHT_PROCESSOR}, Ultralight only supports x64 and arm64")
    endif ()
endif ()

# Set the OS information for ultralight, ULTRALIGHT_PLATFORM is the name Ultralight's download API uses
if (WIN32)
    set(ULTRALIGHT_OS_NAME "win")
    set(ULTRALIGHT_PLATFORM "windows")
    set(ULTRALIGHT_LINK_DIRECTORY "lib")
elseif (APPLE)
    set(ULTRALIGHT_OS_NAME "mac")
    set(ULTRALIGHT_PLATFORM "macos")
    set(ULTRALIGHT_LINK_DIRECTORY "bin")
elseif (UNIX)
    set(ULTRALIGHT_OS_NAME "linux")
    set(ULTRALIGHT_PLATFORM "linux")
    set(ULTRALIGHT_LINK_DIRECTORY "bin")
else ()
    message(FATAL_ERROR "Unsupported operating system")
endif ()

set(ULTRALIGHT_IDENT "${ULTRALIGHT_OS_NAME}-${ULTRALIGHT_ARCH}")
set(ULTRALIGHT_PLATFORM "${ULTRALIGHT_PLATFORM}-${ULTRALIGHT_ARCH}")

if (IS_DIRECTORY "${ULTRALIGHT_SDK}")
    set(ULTRALIGHT_DIR "${ULTRALIGHT_SDK}")
else ()
    set(ULTRALIGHT_DIR "${CMAKE_CURRENT_BINARY_DIR}/ultralight-${ULTRALIGHT_IDENT}")
    set(ULTRALIGHT_VERSION_FILE "${ULTRALIGHT_DIR}/.version")

    if (ULTRALIGHT_SDK)
        file(SHA256 "${ULTRALIGHT_SDK}" ULTRALIGHT_SDK_VERSION)
    else ()
        set(ULTRALIGHT_SDK_VERSION "${ULTRALIGHT_VERSION}")
    endif ()

    if (EXISTS "${ULTRALIGHT_VERSION_FILE}")
        file(READ "${ULTRALIGHT_VERSION_FILE}" ULTRALIGHT_INSTALLED_VERSION)
    endif ()

    # Extract the SDK, again whenever it changes
    if (NOT "${ULTRALIGHT_SDK_VERSION}" STREQUAL "${ULTRALIGHT_INSTALLED_VERSION}")
        file(REMOVE_RECURSE "${ULTRALIGHT_DIR}")

        set(ULTRALIGHT_ARCHIVE "${ULTRALIGHT_SDK}")
        if (NOT ULTRALIGHT_ARCHIVE)
            set(ULTRALIGHT_ARCHIVE "${CMAKE_CURRENT_BINARY_DIR}/ultralight-${ULTRALIGHT_IDENT}.7z")
            file(DOWNLOAD
                    "https://ultralig.ht/api/v1/sdk/download?platform=${ULTRALIGHT_PLATFORM}&version=${ULTRALIGHT_VERSION}"
                    "${ULTRALIGHT_ARCHIVE}"
                    STATUS ULTRALIGHT_DOWNLOAD_STATUS
                    LOG ULTRALIGHT_DOWNLOAD_LOG
                    )

            list(GET ULTRALIGHT_DOWNLOAD_STATUS 0 ULTRALIGHT_DOWNLOAD_ERROR_CODE)
            list(GET ULTRALIGHT_DOWNLOAD_STATUS 1 ULTRALIGHT_DOWNLOAD_ERROR_MESSAGE)
            if (NOT ULTRALIGHT_DOWNLOAD_ERROR_CODE EQUAL 0)
                message(FATAL_ERROR "Failed to download Ultralight ${ULTRALIGHT_VERSION} for ${ULTRALIGHT_PLATFORM}: "
                        "${ULTRALIGHT_DOWNLOAD_ERROR_MESSAGE}\n\n${ULTRALIGHT_DOWNLOAD_LOG}")
            endif ()
        endif ()

        file(ARCHIVE_EXTRACT INPUT "${ULTRALIGHT_ARCHIVE}" DESTINATION "${ULTRALIGHT_DIR}")
        file(WRITE "${ULTRALIGHT_VERSION_FILE}" "${ULTRALIGHT_SDK_VERSION}")
    endif ()
endif ()

if (NOT EXISTS "${ULTRALIGHT_DIR}/include/Ultralight/Ultralight.h")
    message(FATAL_ERROR "${ULTRALIGHT_DIR} is not an Ultralight SDK")
endif ()

message(STATUS "Using the Ultralight SDK in ${ULTRALIGHT_DIR} for ${ULTRALIGHT_IDENT}")

# Add the ultralight target
add_library(ultralight INTERFACE)
target_include_directories(ultralight SYSTEM INTERFACE "${ULTRALIGHT_DIR}/include")
target_link_directories(ultralight INTERFACE "${ULTRALIGHT_DIR}/${ULTRALIGHT_LINK_DIRECTORY}")
target_link_libraries(ultralight INTERFACE AppCore Ultralight UltralightCore WebCore)

# The Ultralight runtime itself is not installed: its license doesn't allow passing it on outside of an
# application, so applications ship it to their users themselves
install(FILES "${ULTRALIGHT_DIR}/license/LICENSE.txt" "${ULTRALIGHT_DIR}/license/NOTICES.md"
        DESTINATION share/licenses/ultralight)

file(WRITE "${CMAKE_CURRENT_BINARY_DIR}/ultralight.ident" "${ULTRALIGHT_IDENT}")
