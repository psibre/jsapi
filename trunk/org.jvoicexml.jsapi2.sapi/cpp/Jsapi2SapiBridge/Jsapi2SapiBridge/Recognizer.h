#pragma once

#include <jni.h>

class Recognizer
{
public:

    Recognizer(HWND hwnd, JNIEnv *env, jobject rec);
    virtual ~Recognizer();

    HRESULT Resume();
    HRESULT Pause();

    HRESULT LoadGrammar(const wchar_t* grammar);
    HRESULT LoadGrammarFile(LPCWSTR grammarPath);
    LPWSTR RecognitionHappened();
	
	LPWSTR startRecognition();

    void StartDictation();

    HRESULT BlockForResult(ISpRecoContext * pRecoCtxt, ISpRecoResult ** ppResult);

    HRESULT	hr;

private:
    void Recognized(LPWSTR utterance);

    int							grammarCount;	
    CComPtr<ISpRecognizer>		cpRecognizerEngine;
    CComPtr<ISpRecoContext>		cpRecoCtxt;
    CComPtr<ISpRecoGrammar>		cpGrammar;
    JNIEnv* jenv;
    jobject jrec;
};		
