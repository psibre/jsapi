/*
 * File:    $HeadURL: $
 * Version: $LastChangedRevision: $
 * Date:    $LastChangedDate: $
 * Author:  $LastChangedBy: $
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

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

/**
 * Test case for {@link javax.speech.AudioSegment}.
 * 
 * @author Dirk Schnelle
 */
public class AudioSegmentTest extends TestCase {
   
    /**
     * Test method for {@link javax.speech.AudioSegment#getLocator()}.
     */
    public void testGetLocator() {
        final String locator1 = "file:///user/smith/hello.wav";
        final String markup1 = 
            "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment1 = 
            new AudioSegment(locator1, markup1);
        assertEquals(locator1, segment1.getLocator());
        
        byte[] bytes = new byte[256];
        final ByteArrayInputStream stream2 = new ByteArrayInputStream(bytes);
        final String locator2 = "file:///user/smith/hello2.wav";
        final String markup2 = 
            "<speak xml:lang='en-US' version='1.0'>Hello 2</speak>";
        final AudioSegment segment2 = 
            new AudioSegment(stream2, locator2, markup2);
        assertEquals(locator2, segment2.getLocator());
    }

    /**
     * Test method for {@link javax.speech.AudioSegment#getMarkupText()}.
     */
    public void testGetMarkupText() {
        final String locator1 = "file:///user/smith/hello.wav";
        final String markup1 = 
            "<speak xml:lang='en-US' version='1.0'>Hello</speak>";
        final AudioSegment segment1 = 
            new AudioSegment(locator1, markup1);
        assertEquals(markup1, segment1.getMarkupText());
        
        byte[] bytes = new byte[256];
        final ByteArrayInputStream stream2 = new ByteArrayInputStream(bytes);
        final String locator2 = "file:///user/smith/hello2.wav";
        final String markup2 = 
            "<speak xml:lang='en-US' version='1.0'>Hello 2</speak>";
        final AudioSegment segment2 = 
            new AudioSegment(stream2, locator2, markup2);
        assertEquals(markup2, segment2.getMarkupText());
    }

    /**
     * Test method for {@link javax.speech.AudioSegment#getInputStream()}.
     */
    public void testGetInputStream() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link javax.speech.AudioSegment#isGettable()}.
     */
    public void testIsGettable() {
        fail("Not yet implemented");
    }

}
