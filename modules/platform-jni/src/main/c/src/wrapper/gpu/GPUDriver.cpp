#include "ujr/wrapper/gpu/GPUDriver.hpp"
#include "net_janrupf_ujr_platform_jni_wrapper_gpu_JNIUlGPUDriver_native_access.hpp"

#include <cstddef>

#include "ujr/Bitmap.hpp"

// The Java side reads the commands directly from memory, see UlCommand and UlGPUState
static_assert(sizeof(ultralight::Command) == 794);
static_assert(offsetof(ultralight::Command, gpu_state) == 1);
static_assert(offsetof(ultralight::Command, geometry_id) == 782);
static_assert(offsetof(ultralight::Command, indices_count) == 786);
static_assert(offsetof(ultralight::Command, indices_offset) == 790);
static_assert(offsetof(ultralight::GPUState, viewport_width) == 0);
static_assert(offsetof(ultralight::GPUState, viewport_height) == 4);
static_assert(offsetof(ultralight::GPUState, transform) == 8);
static_assert(offsetof(ultralight::GPUState, enable_texturing) == 72);
static_assert(offsetof(ultralight::GPUState, enable_blend) == 73);
static_assert(offsetof(ultralight::GPUState, shader_type) == 74);
static_assert(offsetof(ultralight::GPUState, render_buffer_id) == 75);
static_assert(offsetof(ultralight::GPUState, texture_1_id) == 79);
static_assert(offsetof(ultralight::GPUState, texture_2_id) == 83);
static_assert(offsetof(ultralight::GPUState, texture_3_id) == 87);
static_assert(offsetof(ultralight::GPUState, uniform_scalar) == 91);
static_assert(offsetof(ultralight::GPUState, uniform_vector) == 123);
static_assert(offsetof(ultralight::GPUState, clip_size) == 251);
static_assert(offsetof(ultralight::GPUState, clip) == 252);
static_assert(offsetof(ultralight::GPUState, enable_scissor) == 764);
static_assert(offsetof(ultralight::GPUState, scissor_rect) == 765);

namespace ujr {
    namespace {
        JniLocalRef<jobject> direct_buffer(const JniEnv &env, void *data, size_t size) {
            return JniLocalRef<jobject>::wrap(env, env->NewDirectByteBuffer(data, static_cast<jlong>(size)));
        }
    } // namespace

    GPUDriver::GPUDriver(JniGlobalRef<jobject> j_driver)
        : j_driver(std::move(j_driver)) {}

    void GPUDriver::BeginSynchronize() {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        JNIUlGPUDriver::BEGIN_SYNCHRONIZE.invoke(env, j_driver);
    }

    void GPUDriver::EndSynchronize() {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        JNIUlGPUDriver::END_SYNCHRONIZE.invoke(env, j_driver);
    }

    uint32_t GPUDriver::NextTextureId() {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        return static_cast<uint32_t>(JNIUlGPUDriver::NEXT_TEXTURE_ID.invoke(env, j_driver));
    }

    void GPUDriver::CreateTexture(uint32_t texture_id, ultralight::RefPtr<ultralight::Bitmap> bitmap) {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        auto j_bitmap = Bitmap::wrap(env, std::move(bitmap));
        JNIUlGPUDriver::CREATE_TEXTURE.invoke(env, j_driver, static_cast<jint>(texture_id), j_bitmap);
    }

    void GPUDriver::UpdateTexture(uint32_t texture_id, ultralight::RefPtr<ultralight::Bitmap> bitmap) {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        auto j_bitmap = Bitmap::wrap(env, std::move(bitmap));
        JNIUlGPUDriver::UPDATE_TEXTURE.invoke(env, j_driver, static_cast<jint>(texture_id), j_bitmap);
    }

    void GPUDriver::DestroyTexture(uint32_t texture_id) {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        JNIUlGPUDriver::DESTROY_TEXTURE.invoke(env, j_driver, static_cast<jint>(texture_id));
    }

    uint32_t GPUDriver::NextRenderBufferId() {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        return static_cast<uint32_t>(JNIUlGPUDriver::NEXT_RENDER_BUFFER_ID.invoke(env, j_driver));
    }

    void GPUDriver::CreateRenderBuffer(uint32_t render_buffer_id, const ultralight::RenderBuffer &buffer) {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        JNIUlGPUDriver::CREATE_RENDER_BUFFER.invoke(
            env,
            j_driver,
            static_cast<jint>(render_buffer_id),
            static_cast<jint>(buffer.texture_id),
            static_cast<jint>(buffer.width),
            static_cast<jint>(buffer.height)
        );
    }

    void GPUDriver::DestroyRenderBuffer(uint32_t render_buffer_id) {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        JNIUlGPUDriver::DESTROY_RENDER_BUFFER.invoke(env, j_driver, static_cast<jint>(render_buffer_id));
    }

    uint32_t GPUDriver::NextGeometryId() {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        return static_cast<uint32_t>(JNIUlGPUDriver::NEXT_GEOMETRY_ID.invoke(env, j_driver));
    }

    void GPUDriver::CreateGeometry(
        uint32_t geometry_id, const ultralight::VertexBuffer &vertices, const ultralight::IndexBuffer &indices
    ) {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        auto j_vertices = direct_buffer(env, vertices.data, vertices.size);
        auto j_indices = direct_buffer(env, indices.data, indices.size);

        JNIUlGPUDriver::CREATE_GEOMETRY.invoke(
            env,
            j_driver,
            static_cast<jint>(geometry_id),
            static_cast<jint>(vertices.format),
            j_vertices,
            j_indices
        );
    }

    void GPUDriver::UpdateGeometry(
        uint32_t geometry_id, const ultralight::VertexBuffer &vertices, const ultralight::IndexBuffer &indices
    ) {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        auto j_vertices = direct_buffer(env, vertices.data, vertices.size);
        auto j_indices = direct_buffer(env, indices.data, indices.size);

        JNIUlGPUDriver::UPDATE_GEOMETRY.invoke(
            env,
            j_driver,
            static_cast<jint>(geometry_id),
            static_cast<jint>(vertices.format),
            j_vertices,
            j_indices
        );
    }

    void GPUDriver::DestroyGeometry(uint32_t geometry_id) {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        JNIUlGPUDriver::DESTROY_GEOMETRY.invoke(env, j_driver, static_cast<jint>(geometry_id));
    }

    void GPUDriver::UpdateCommandList(const ultralight::CommandList &list) {
        using native_access::JNIUlGPUDriver;

        auto env = JniEnv::require_existing_from_thread();
        auto j_commands = direct_buffer(env, list.commands, list.size * sizeof(ultralight::Command));

        JNIUlGPUDriver::UPDATE_COMMAND_LIST.invoke(env, j_driver, static_cast<jint>(list.size), j_commands);
    }

    const JniGlobalRef<jobject> &GPUDriver::get_j_driver() const { return j_driver; }
} // namespace ujr
