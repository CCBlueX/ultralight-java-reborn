################################
# Set up 3rdparty dependencies #
################################

# The Ultralight SDK can't be downloaded without an account, so it has to be supplied, either as the
# .7z archive or as an already extracted directory
set(ULTRALIGHT_SDK "$ENV{ULTRALIGHT_SDK}" CACHE FILEPATH "Ultralight SDK archive (.7z) or extracted directory")
if (NOT ULTRALIGHT_SDK)
    message(FATAL_ERROR "No Ultralight SDK given, pass -DULTRALIGHT_SDK=<archive or directory> or set ULTRALIGHT_SDK")
endif ()

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

# Set the OS information for ultralight
if (WIN32)
    set(ULTRALIGHT_OS_NAME "win")
    set(ULTRALIGHT_LINK_DIRECTORY "lib")
elseif (APPLE)
    set(ULTRALIGHT_OS_NAME "mac")
    set(ULTRALIGHT_LINK_DIRECTORY "bin")
elseif (UNIX)
    set(ULTRALIGHT_OS_NAME "linux")
    set(ULTRALIGHT_LINK_DIRECTORY "bin")
else ()
    message(FATAL_ERROR "Unsupported operating system")
endif ()

set(ULTRALIGHT_IDENT "${ULTRALIGHT_OS_NAME}-${ULTRALIGHT_ARCH}")

if (IS_DIRECTORY "${ULTRALIGHT_SDK}")
    set(ULTRALIGHT_DIR "${ULTRALIGHT_SDK}")
else ()
    if (NOT ULTRALIGHT_SDK MATCHES "${ULTRALIGHT_IDENT}")
        message(WARNING "The Ultralight SDK ${ULTRALIGHT_SDK} doesn't look like it is for ${ULTRALIGHT_IDENT}")
    endif ()

    # Extract the archive, again whenever it changes
    set(ULTRALIGHT_DIR "${CMAKE_CURRENT_BINARY_DIR}/ultralight-${ULTRALIGHT_IDENT}")
    set(ULTRALIGHT_VERSION_FILE "${ULTRALIGHT_DIR}/.version")
    file(SHA256 "${ULTRALIGHT_SDK}" ULTRALIGHT_SDK_HASH)

    if (EXISTS "${ULTRALIGHT_VERSION_FILE}")
        file(READ "${ULTRALIGHT_VERSION_FILE}" ULTRALIGHT_INSTALLED_HASH)
    endif ()

    if (NOT "${ULTRALIGHT_SDK_HASH}" STREQUAL "${ULTRALIGHT_INSTALLED_HASH}")
        file(REMOVE_RECURSE "${ULTRALIGHT_DIR}")
        file(ARCHIVE_EXTRACT INPUT "${ULTRALIGHT_SDK}" DESTINATION "${ULTRALIGHT_DIR}")
        file(WRITE "${ULTRALIGHT_VERSION_FILE}" "${ULTRALIGHT_SDK_HASH}")
    endif ()
endif ()

# Archives may wrap the SDK in a single top-level folder
if (NOT EXISTS "${ULTRALIGHT_DIR}/include/Ultralight/Ultralight.h")
    file(GLOB ULTRALIGHT_SDK_CHILDREN LIST_DIRECTORIES true "${ULTRALIGHT_DIR}/*")
    foreach (child IN LISTS ULTRALIGHT_SDK_CHILDREN)
        if (EXISTS "${child}/include/Ultralight/Ultralight.h")
            set(ULTRALIGHT_DIR "${child}")
        endif ()
    endforeach ()
endif ()

if (NOT EXISTS "${ULTRALIGHT_DIR}/include/Ultralight/Ultralight.h")
    message(FATAL_ERROR "${ULTRALIGHT_SDK} is not an Ultralight SDK")
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
