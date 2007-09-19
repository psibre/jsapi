/*
 * File:    $HeadURL: https://svn.sourceforge.net/svnroot/jvoicexml/trunk/src/org/jvoicexml/Application.java$
 * Version: $LastChangedRevision$
 * Date:    $LastChangedDate $
 * Author:  $LastChangedBy$
 *
 * JSAPI - An independent reference implementation of JSR 113.
 *
 * Copyright (C) 2007 JVoiceXML group - http://jvoicexml.sourceforge.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package javax.speech;

import java.util.Vector;

public class AudioEvent extends SpeechEvent {
    public static int AUDIO_LEVEL = 1;

    public static int AUDIO_STARTED = AUDIO_LEVEL << 1;

    public static int AUDIO_STOPPED = AUDIO_STARTED << 1;

    public static int AUDIO_LEVEL_MIN = 0;

    public static int AUDIO_LEVEL_QUIET = Integer.MAX_VALUE / 3;

    public static int AUDIO_LEVEL_LOUD = Integer.MAX_VALUE / 3 * 2;

    public static int AUDIO_LEVEL_MAX = Integer.MAX_VALUE;

    public static int DEFAULT_MASK = AUDIO_STARTED | AUDIO_STOPPED;

    private int audioLevel;

    public AudioEvent(Engine source, int id) {
        this(source, id, AUDIO_LEVEL_MIN);
    }

    public AudioEvent(Engine source, int id, int audioLevel) {
        super(source, id);

        if ((audioLevel < AUDIO_LEVEL_MIN) || (audioLevel > AUDIO_LEVEL_MAX)) {
            throw new IllegalArgumentException("Audiolevel must be between "
                    + "AUDIO_LEVEL_MIN and AUDIO_LEVEL_MAX");
        }

        this.audioLevel = audioLevel;
    }

    public int getAudioLevel() {
        return audioLevel;
    }

    /**
     * {@inheritDoc}
     */
    protected Vector getParameters() {
        final Vector parameters = super.getParameters();

        final Integer level = new Integer(audioLevel);
        parameters.addElement(level);

        return parameters;
    }
}