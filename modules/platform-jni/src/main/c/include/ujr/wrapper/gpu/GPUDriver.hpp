#pragma once

#include <Ultralight/platform/GPUDriver.h>

#include "ujr/util/JniRef.hpp"

namespace ujr {
    /**
     * GPU driver adapter for Ultralight delegating to a Java instance.
     *
     * Buffers are handed to Java as direct buffers over the memory of Ultralight, without copying them.
     */
    class GPUDriver : public ultralight::GPUDriver {
    private:
        JniGlobalRef<jobject> j_driver;

    public:
        explicit GPUDriver(JniGlobalRef<jobject> j_driver);

        void BeginSynchronize() final;

        void EndSynchronize() final;

        uint32_t NextTextureId() final;

        void CreateTexture(uint32_t texture_id, ultralight::RefPtr<ultralight::Bitmap> bitmap, uint32_t flags) final;

        void UpdateTexture(
            uint32_t texture_id, ultralight::RefPtr<ultralight::Bitmap> bitmap, const ultralight::IntRect &dirty_rect
        ) final;

        void DestroyTexture(uint32_t texture_id) final;

        uint32_t NextRenderBufferId() final;

        void CreateRenderBuffer(uint32_t render_buffer_id, const ultralight::RenderBuffer &buffer) final;

        void DestroyRenderBuffer(uint32_t render_buffer_id) final;

        uint32_t NextGeometryId() final;

        void CreateGeometry(
            uint32_t geometry_id, const ultralight::VertexBuffer &vertices, const ultralight::IndexBuffer &indices
        ) final;

        void UpdateGeometry(
            uint32_t geometry_id, const ultralight::VertexBuffer &vertices, const ultralight::IndexBuffer &indices
        ) final;

        void DestroyGeometry(uint32_t geometry_id) final;

        void UpdateCommandList(const ultralight::CommandList &list) final;

        /**
         * Retrieves the underlying java driver instance.
         *
         * @return the java driver instance
         */
        [[nodiscard]] const JniGlobalRef<jobject> &get_j_driver() const;
    };
} // namespace ujr
