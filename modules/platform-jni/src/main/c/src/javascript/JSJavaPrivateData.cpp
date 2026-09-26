#include "ujr/javascript/JSJavaPrivateData.hpp"

#include <mutex>

namespace ujr {
    JSJavaPrivateData::JSJavaPrivateData(
        std::shared_ptr<const JSClassJavaSharedData> shared_data,
        const JniEnv &env,
        const JniStrongRef<jobject> &java_private_data
    )
        : shared_data(std::move(shared_data))
        , java_private_data(java_private_data.clone_as_global(env)) {
    }

    JSJavaPrivateData::~JSJavaPrivateData() = default;

    const std::shared_ptr<const JSClassJavaSharedData> &JSJavaPrivateData::get_shared_data() const {
        return shared_data;
    }

    const JniGlobalRef<jobject> &JSJavaPrivateData::get_java_private_data() const { return java_private_data; }

    JSClassJavaSharedData::JSClassJavaSharedData()
        : static_property_callbacks()
        , static_function_callbacks()
        , initialize_callback(JniGlobalRef<jobject>::null())
        , finalize_callback(JniGlobalRef<jobject>::null())
        , has_property_callback(JniGlobalRef<jobject>::null())
        , get_property_callback(JniGlobalRef<jobject>::null())
        , set_property_callback(JniGlobalRef<jobject>::null())
        , delete_property_callback(JniGlobalRef<jobject>::null())
        , get_property_names_callback(JniGlobalRef<jobject>::null())
        , call_as_function_callback(JniGlobalRef<jobject>::null())
        , call_as_constructor_callback(JniGlobalRef<jobject>::null())
        , has_instance_callback(JniGlobalRef<jobject>::null())
        , convert_to_type_callback(JniGlobalRef<jobject>::null())
        , class_name()
        , parent(nullptr) {}

    const JSClassJavaSharedData *
    JSClassJavaSharedData::find(JniGlobalRef<jobject> JSClassJavaSharedData::*callback, const JniEnv &env) const {
        for (const auto *data = this; data; data = data->parent) {
            if ((data->*callback).is_valid(env)) {
                return data;
            }
        }

        return nullptr;
    }

    const JSClassJavaProperty *JSClassJavaSharedData::find_static_property(const std::string &name) const {
        for (const auto *data = this; data; data = data->parent) {
            if (auto it = data->static_property_callbacks.find(name); it != data->static_property_callbacks.end()) {
                return &it->second;
            }
        }

        return nullptr;
    }

    const JniGlobalRef<jobject> *JSClassJavaSharedData::find_static_function(const std::string &name) const {
        for (const auto *data = this; data; data = data->parent) {
            if (auto it = data->static_function_callbacks.find(name); it != data->static_function_callbacks.end()) {
                return &it->second;
            }
        }

        return nullptr;
    }

    namespace {
        std::mutex class_registry_mutex;
        std::unordered_map<JSClassRef, const JSClassJavaSharedData *> class_registry;
    } // namespace

    void JSClassJavaSharedData::register_class(JSClassRef js_class, const JSClassJavaSharedData *data) {
        std::lock_guard lock(class_registry_mutex);
        class_registry[js_class] = data;
    }

    const JSClassJavaSharedData *JSClassJavaSharedData::of_class(JSClassRef js_class) {
        if (!js_class) {
            return nullptr;
        }

        std::lock_guard lock(class_registry_mutex);
        auto it = class_registry.find(js_class);
        return it == class_registry.end() ? nullptr : it->second;
    }

    const JSClassJavaSharedData *JSClassJavaSharedData::of_object(JSObjectRef object) {
        return object ? static_cast<const JSClassJavaSharedData *>(JSObjectGetPrivate(object)) : nullptr;
    }
} // namespace ujr