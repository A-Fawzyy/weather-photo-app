#include <jni.h>
#include <string>

/*
 * Todo(Appsec): Need to encrypt the apiKey here and decrypt it in the runtime
 */
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_weatherpictureapp_utils_NativeLibs_apiKey(JNIEnv *env, jobject thiz) {
    std::string api_key = "f151b66d32a764cbe4b90cfd7ee77e0c";
    return env->NewStringUTF(api_key.c_str());
}
