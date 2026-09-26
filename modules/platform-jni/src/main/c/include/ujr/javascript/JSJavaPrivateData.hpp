#pragma once

#include <JavaScriptCore/JSObjectRef.h>

#include <memory>
#include <string>
#include <unordered_map>

#include "ujr/util/JniRef.hpp"

namespace ujr {
    /**
     * Callbacks for a JavaScript property.
     */
    struct JSClassJavaProperty {
        JniGlobalRef<jobject> get_property;
        JniGlobalRef<jobject> set_property;
    };

    /**
     * Callbacks and lookup data for a JavaScript class.
     */
    struct JSClassJavaSharedData {
        explicit JSClassJavaSharedData();

        /**
         * Map of property names to property callbacks.
         */
        std::unordered_map<std::string, JSClassJavaProperty> static_property_callbacks;

        /**
         * Map of function names to function callbacks.
         */
        std::unordered_map<std::string, JniGlobalRef<jobject>> static_function_callbacks;

        JniGlobalRef<jobject> initialize_callback;
        JniGlobalRef<jobject> finalize_callback;
        JniGlobalRef<jobject> has_property_callback;
        JniGlobalRef<jobject> get_property_callback;
        JniGlobalRef<jobject> set_property_callback;
        JniGlobalRef<jobject> delete_property_callback;
        JniGlobalRef<jobject> get_property_names_callback;
        JniGlobalRef<jobject> call_as_function_callback;
        JniGlobalRef<jobject> call_as_constructor_callback;
        JniGlobalRef<jobject> has_instance_callback;
        JniGlobalRef<jobject> convert_to_type_callback;

        /**
         * The name of the class.
         */
        std::string class_name;

        /**
         * The shared data of the parent class, if the parent class was created by us.
         */
        const JSClassJavaSharedData *parent;

        /**
         * Finds the nearest class in the hierarchy, starting at this class, which has the given callback set.
         *
         * @param callback the callback to look for
         * @param env the JNI environment to use
         * @return the shared data of the class with the callback, or nullptr if no class has it
         */
        [[nodiscard]] const JSClassJavaSharedData *
        find(JniGlobalRef<jobject> JSClassJavaSharedData::*callback, const JniEnv &env) const;

        /**
         * Finds the callbacks of a static property in the class hierarchy.
         *
         * @param name the name of the property
         * @return the callbacks, or nullptr if no class has the property
         */
        [[nodiscard]] const JSClassJavaProperty *find_static_property(const std::string &name) const;

        /**
         * Finds the callback of a static function in the class hierarchy.
         *
         * @param name the name of the function
         * @return the callback, or nullptr if no class has the function
         */
        [[nodiscard]] const JniGlobalRef<jobject> *find_static_function(const std::string &name) const;

        /**
         * Remembers the shared data of a class, so objects of the class can be created with it.
         *
         * @param js_class the class
         * @param data the shared data of the class, which lives as long as the program
         */
        static void register_class(JSClassRef js_class, const JSClassJavaSharedData *data);

        /**
         * Looks up the shared data of a class.
         *
         * @param js_class the class, may be nullptr
         * @return the shared data, or nullptr if the class was not created by us
         */
        [[nodiscard]] static const JSClassJavaSharedData *of_class(JSClassRef js_class);

        /**
         * Looks up the shared data of the class an object was created with.
         *
         * Ultralight 1.4 no longer passes the class to callbacks, so objects of our classes carry the shared data
         * of their class as their private data.
         *
         * @param object the object
         * @return the shared data, or nullptr if the object was not created from one of our classes
         */
        [[nodiscard]] static const JSClassJavaSharedData *of_object(JSObjectRef object);
    };

    class JSJavaPrivateData {
    private:
        std::shared_ptr<const JSClassJavaSharedData> shared_data;
        JniGlobalRef<jobject> java_private_data;

    public:
        explicit JSJavaPrivateData(
            std::shared_ptr<const JSClassJavaSharedData> shared_data,
            const JniEnv &env,
            const JniStrongRef<jobject> &java_private_data
        );

        ~JSJavaPrivateData();

        /**
         * Retrieves the shared data for the class.
         *
         * @return the shared data
         */
        [[nodiscard]] const std::shared_ptr<const JSClassJavaSharedData> &get_shared_data() const;

        /**
         * Retrieves the java private data.
         *
         * @return the java private data
         */
        [[nodiscard]] const JniGlobalRef<jobject> &get_java_private_data() const;
    };
} // namespace ujr
