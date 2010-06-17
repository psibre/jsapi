package org.jvoicexml.jsapi2.sapi;
import javax.speech.EngineList;
import javax.speech.EngineMode;
import javax.speech.recognition.RecognizerMode;
import javax.speech.spi.EngineListFactory;
import javax.speech.synthesis.SynthesizerMode;
import javax.speech.synthesis.Voice;

import org.jvoicexml.jsapi2.sapi.recognition.SapiRecognizerMode;
import org.jvoicexml.jsapi2.sapi.synthesis.SapiSynthesizerMode;

/**
 * Factory for the SAPI engines. 
 * @author Dirk Schnelle-Walka
 *
 */
public class SapiEngineListFactory implements EngineListFactory {
    static {
        System.loadLibrary("Jsapi2SapiBridge");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EngineList createEngineList(final EngineMode require) {
        if (require instanceof SynthesizerMode) {
            final SynthesizerMode mode = (SynthesizerMode) require;
            final Voice[] voices = sapiGetVoices();
            final SynthesizerMode[] features = new SynthesizerMode[] {
                    new SapiSynthesizerMode(null, mode.getEngineName(),
                            mode.getRunning(), mode.getSupportsLetterToSound(),
                            mode.getSupportsMarkup(), voices)
            };
            return new EngineList(features);
        }            
        if (require instanceof RecognizerMode) {
                final RecognizerMode[] features = new RecognizerMode[] {
                        new SapiRecognizerMode()
            };
            return new EngineList(features);
        }
        
        return null;
    }

    /**
     * Retrieves all voices.
     * @return all voices
     */
    private native Voice[] sapiGetVoices();

}