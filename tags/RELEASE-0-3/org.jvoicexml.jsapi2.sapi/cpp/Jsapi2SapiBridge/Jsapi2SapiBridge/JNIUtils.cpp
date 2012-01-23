#include "stdafx.h"
#include "JNIUtils.h"
#include <log4cplus/logger.h>
#include "log4cplus/consoleappender.h"

static log4cplus::Logger logger =
    log4cplus::Logger::getInstance(_T("org.jvoicexml.sapi.cpp.JNI"));


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved)
{
	HRESULT hr = ::CoInitializeEx(NULL, COINIT_MULTITHREADED);
    if (FAILED(hr))
    {
        char buffer[1024];
        GetErrorMessage(buffer, sizeof(buffer), "Initializing COM failed!",
            hr);
        LOG4CPLUS_FATAL(logger, buffer);
        return JNI_ERR;
    }
    // TODO add this to a configuration file
    log4cplus::SharedAppenderPtr console(new log4cplus::ConsoleAppender(false, true));
    console->setName(LOG4CPLUS_TEXT("console"));
    log4cplus::Logger::getRoot().addAppender(console);

    LOG4CPLUS_INFO(logger, _T("Jsapi2SapiBridge successfully loaded"));
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *jvm, void *reserved)
{
	::CoUninitialize();
}


void ThrowJavaException(JNIEnv* env, char* exceptionClassName, char* message)
{
    jclass exception = env->FindClass(exceptionClassName);
    if (exception == 0) /* Unable to find the new exception class, give up. */
    {
        LOG4CPLUS_ERROR(logger, message);
        return;
    }
    env->ThrowNew(exception, message);
}

BOOL GetMethodId(JNIEnv* env, const char* className, const char* methodName,
                 const char* sig, jclass& clazz, jmethodID& methodId)
{
    clazz = env->FindClass(className);
    if (clazz == NULL)
    {
        char msg[512];
        _snprintf(msg, sizeof(msg), "Unable to %s!", className);
        ThrowJavaException(env, "java/lang/NullPointerException", msg);
        return FALSE;
    }
    methodId = env->GetMethodID(clazz, methodName, sig);
    if (methodId == NULL)
    {
        char msg[1024];
        _snprintf(msg, sizeof(msg), "Unable to find method '%s(%s)'!", methodName, sig);
        ThrowJavaException(env, "java/lang/NullPointerException", msg);
        return FALSE;
    }
    return TRUE;
}
