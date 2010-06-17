#include "stdafx.h"
#include "Recognizer.h"

Recognizer::Recognizer()
: cpRecognizer(NULL), cpRecoCtxt(NULL), cpGrammar(NULL), hr(S_OK)
{

	grammarCount				= 0;
	CComPtr<ISpAudio>           cpAudio			= NULL;

    // create a new InprocRecognizer.
    //hr = CoCreateInstance(CLSID_SpInprocRecognizer, NULL ,CLSCTX_LOCAL_SERVER,
    //      CLSID_SpInprocRecognizer, (void**)&cpRecognizer);
    hr = cpRecognizer.CoCreateInstance(CLSID_SpInprocRecognizer);	
	if (SUCCEEDED(hr))
	{
	   // Set up the inproc recognizer audio input with an audio input object.
	   // Create the default audio input object.
	   hr = SpCreateDefaultObjectFromCategoryId(SPCAT_AUDIOIN, &cpAudio);
	}	
	if (SUCCEEDED(hr))
	{
	   // Set the audio input to our object.
	   hr = cpRecognizer->SetInput(cpAudio, TRUE);
	}
	if( SUCCEEDED(hr) )
    {
		// create a new Recognition context.
		hr = cpRecognizer->CreateRecoContext( &cpRecoCtxt);
	}
	if( SUCCEEDED(hr) )
    {
		hr = cpRecoCtxt->SetAudioOptions(SPAO_RETAIN_AUDIO, NULL, NULL);
	}	
	//if( SUCCEEDED(hr) ){
	//	// Set the Notivy.
	//	hr = cpRecoCtxt->SetNotifyWin32Event();
	//}
	if( SUCCEEDED(hr) )
    {
		hr = cpRecoCtxt->SetInterest(SPFEI(SPEI_RECOGNITION ), SPFEI(SPEI_RECOGNITION ) );
	}	
	cpAudio.Release();
}

Recognizer::~Recognizer()
{
	cpGrammar.Release();
	cpRecoCtxt.Release();
	cpRecognizer.Release();
	
	//::CoUninitialize();
	//std::cout<< "CoUninitialize" <<"\n";fflush(stdout);
}


void Recognizer::setGrammar( LPCWSTR grammarPath )
{
	
	if( SUCCEEDED(hr) ){
		hr = cpRecoCtxt->CreateGrammar( grammarCount++, &cpGrammar);
	}

	if( SUCCEEDED(hr) ){
		hr = cpGrammar->LoadCmdFromFile( grammarPath,SPLO_STATIC);
	}

	if( SUCCEEDED(hr) ){
		hr = cpGrammar->SetGrammarState(SPGS_ENABLED);
	}							
}

void Recognizer::pause()
{

		hr = cpRecoCtxt->Pause( NULL);				
}

void Recognizer::Resume()
{
	hr = cpRecoCtxt->Resume(NULL);
}

void Recognizer::startdictation()
{	
	USES_CONVERSION;
	CComPtr<ISpRecoResult>		cpResult;

	std::cout<< "Please speak now \n";
	fflush(stdout);

	if (  SUCCEEDED( hr = BlockForResult(cpRecoCtxt, &cpResult) ) )
	{		
            cpGrammar->SetGrammarState(SPGS_DISABLED);

            CSpDynamicString dstrText;

            if (SUCCEEDED(cpResult->GetText(SP_GETWHOLEPHRASE, SP_GETWHOLEPHRASE, 
                                            TRUE, &dstrText, NULL)))
            {
				std::cout<< "I heard: " << W2A(dstrText)<<"\n";
				fflush(stdout);
				cpResult.Release();   						
            }                  
            hr = cpGrammar->SetGrammarState(SPGS_ENABLED);
    }
}

HRESULT Recognizer::BlockForResult(ISpRecoContext * pRecoCtxt, ISpRecoResult ** ppResult)
{	
	USES_CONVERSION;

	HRESULT hr = S_OK;
	CSpEvent event;

	hr = cpGrammar->SetRuleState(NULL, NULL, SPRS_ACTIVE );

	if ( SUCCEEDED(hr)&& SUCCEEDED(hr = event.GetFrom(pRecoCtxt)) && hr == S_FALSE ) //
	{
		hr = pRecoCtxt->WaitForNotifyEvent(INFINITE);
	}
	
	hr = cpGrammar->SetRuleState(NULL, NULL, SPRS_INACTIVE );

	*ppResult = event.RecoResult();

	if (*ppResult)
	{
		(*ppResult)->AddRef();
	}

	return hr;
}