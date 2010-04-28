/*
 * File:    $HeadURL$
 * Version: $LastChangedRevision$
 * Date:    $Date$
 * Author:  $LastChangedBy$
 *
 * JVoiceXML - A free VoiceXML implementation.
 *
 * Copyright (C) 2005-2008 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public
 *  License as published by the Free Software Foundation; either
 *  version 2 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package org.jvoicexml.jsapi2.jse.recognition.sphinx4;


import java.util.logging.Level;
import java.util.logging.Logger;

import edu.cmu.sphinx.recognizer.Recognizer;

/**
 * Recognition thread to run the recognizer in parallel.
 *
 * @author Dirk Schnelle-Walka
 * @version $Revision$
 */
final class RecognitionThread
        extends Thread {
    /** Logger for this class. */
    private static final Logger LOGGER =
            Logger.getLogger(RecognitionThread.class.getName());

    /** The wrapper for the sphinx4 recognizer. */
    private Sphinx4Recognizer recognizer;

    /**
     * Creates a new object.
     * @param rec The wrapper for the sphinx4 recognizer.
     */
    public RecognitionThread(final Sphinx4Recognizer rec) {
        super("RecognitionThread");
        recognizer = rec;
        setDaemon(true);
    }

    /**
     * Runs this thread.
     */
    public void run() {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("recognition thread started");
        }

        final Recognizer rec = recognizer.getRecognizer();
        final boolean started;

        started = true;

        // send start of speech and processing event
        // @todo change this;
        recognizer.postStartOfSpeechEvent();
        recognizer.postProcessingEvent();
        if (started) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("start recognizing ..");
            }

            rec.recognize();
        }
        // send end of speech and listening event
        // @todo change this;
        recognizer.postEndOfSpeechEvent();
        recognizer.postListeningEvent();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("stopping recognition thread...");
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("recognition thread terminated");
        }
    }

    /**
     * Stop this recognition thread.
     */
    public void stopRecognition() {
        Thread thread = currentThread();
        thread.interrupt();
    }
}