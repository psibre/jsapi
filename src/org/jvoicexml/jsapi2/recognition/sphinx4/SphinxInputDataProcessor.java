package org.jvoicexml.jsapi2.recognition.sphinx4;


import java.io.InputStream;
import java.io.IOException;
import edu.cmu.sphinx.frontend.util.DataUtil;
import edu.cmu.sphinx.frontend.BaseDataProcessor;
import edu.cmu.sphinx.frontend.Data;
import edu.cmu.sphinx.frontend.DataProcessingException;
import edu.cmu.sphinx.frontend.DataStartSignal;
import edu.cmu.sphinx.frontend.DataEndSignal;
import edu.cmu.sphinx.frontend.DoubleData;
import edu.cmu.sphinx.util.props.Configurable;
import edu.cmu.sphinx.util.props.PropertySheet;
import edu.cmu.sphinx.util.props.PropertyException;

/**
 * <p>Title: JSAPI2Engines</p>
 *
 * <p>Description: JSAPI 2.0 Engines implementations</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: INESC-ID L2F</p>
 *
 * @author Renato Cassaca
 * @version 1.0
 */
public class SphinxInputDataProcessor extends BaseDataProcessor implements Configurable {

    private InputStream inputStream;
    private long totalSamplesRead = 0;
    private boolean started = false;

    public SphinxInputDataProcessor() {
        super();
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Returns the processed Data output.
     *
     * @return an Data object that has been processed by this DataProcessor
     * @throws DataProcessingException if a data processor error occurs
     */
    public Data getData() throws DataProcessingException {

        /** @todo AudioFormat is hardcoded */
        int channels = 1;
        int sampleSizeInBytes = 2;
        boolean signed = true;
        int sampleRate = 16000;
        int frameSizeInBytes = sampleRate * sampleSizeInBytes * channels * 10 / 1000;

        if (started == false) {
            started = true;
            return new DataStartSignal(sampleRate);
        }


        long collectTime = System.currentTimeMillis();
        long firstSampleNumber = totalSamplesRead / channels;

        //Read data
        byte[] data = new byte[frameSizeInBytes];
        int numBytesRead;
        try {
            numBytesRead = inputStream.read(data);
        } catch (IOException ex) {
            throw new DataProcessingException(ex.getMessage());
        }
        if (numBytesRead == -1) {
            long duration = (long)
                            (((double) totalSamplesRead /
                              (double) sampleRate * 1000.0));

            return new DataEndSignal(duration);
        }


        if (numBytesRead != frameSizeInBytes) {
            if (numBytesRead % sampleSizeInBytes != 0) {
                //Is it an error?
                System.err.println("[JSAPI2] Sphinx ReadData: Incomplete sample read.");
            }

            byte[] shrinked = new byte[numBytesRead];
            System.arraycopy(data, 0, shrinked, 0, numBytesRead);
            data = shrinked;
        }


        totalSamplesRead += (numBytesRead / sampleSizeInBytes);

        //Convert it to double
        double[] samples = DataUtil.bytesToValues
                           (data, 0, data.length, sampleSizeInBytes, signed);

        return (new DoubleData
                (samples, (int) sampleRate,
                 collectTime, firstSampleNumber));
    }

    public void newProperties(PropertySheet ps) throws PropertyException {
        super.newProperties(ps);
    }
}