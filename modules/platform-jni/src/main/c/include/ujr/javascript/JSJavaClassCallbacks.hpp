#pragma once

#include <JavaScriptCore/JSObjectRef.h>

namespace ujr {
    /**
     * Utility bridge class for JavaScript Java callbacks.
     *
     * The callbacks find the Java side of the class through the private data of the object, see
     * JSClassJavaSharedData::of_object.
     */
    class JSJavaClassCallbacks {
    public:
        explicit JSJavaClassCallbacks() = delete;

        static JSValueRef get_static_property(
            JSContextRef ctx, JSObjectRef object, JSStringRef property_name, JSValueRef *exception
        );

        static bool set_static_property(
            JSContextRef ctx, JSObjectRef object, JSStringRef property_name, JSValueRef value, JSValueRef *exception
        );

        static void initialize(JSContextRef ctx, JSObjectRef object);

        static void finalize(JSObjectRef object);

        static bool has_property(JSContextRef ctx, JSObjectRef object, JSStringRef property_name);

        static JSValueRef get_property(
            JSContextRef ctx, JSObjectRef object, JSStringRef property_name, JSValueRef *exception
        );

        static bool set_property(
            JSContextRef ctx, JSObjectRef object, JSStringRef property_name, JSValueRef value, JSValueRef *exception
        );

        static bool delete_property(
            JSContextRef ctx, JSObjectRef object, JSStringRef property_name, JSValueRef *exception
        );

        static void get_property_names(
            JSContextRef ctx, JSObjectRef object, JSPropertyNameAccumulatorRef property_names
        );

        static JSValueRef call_as_function(
            JSContextRef ctx,
            JSObjectRef function,
            JSObjectRef this_object,
            size_t argument_count,
            const JSValueRef arguments[],
            JSValueRef *exception
        );

        static JSValueRef call_static_function(
            JSContextRef ctx,
            JSObjectRef function,
            JSObjectRef this_object,
            size_t argument_count,
            const JSValueRef arguments[],
            JSValueRef *exception
        );

        static JSObjectRef call_as_constructor(
            JSContextRef ctx,
            JSObjectRef constructor,
            size_t argument_count,
            const JSValueRef arguments[],
            JSValueRef *exception
        );

        static bool has_instance(
            JSContextRef ctx, JSObjectRef constructor, JSValueRef possible_instance, JSValueRef *exception
        );

        static JSValueRef convert_to_type(JSContextRef ctx, JSObjectRef object, JSType type, JSValueRef *exception);
    };
} // namespace ujr
