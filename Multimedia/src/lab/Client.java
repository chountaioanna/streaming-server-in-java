package lab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class Client 
{
	/**
     * spedd examples server uri.
     */
    private final static String SPEED_TEST_SERVER_URI_DL = "http://ipv4.ikoula.testdebit.info/100M.iso";

    /**
     * amount of time between each speed test reports set to 1s.
     */
    private static final int REPORT_INTERVAL = 1000;

    /**
     * speed test duration set to 5s.
     */
    private static final int SPEED_TEST_DURATION = 5000;

    /**
     * logger.
     */
    private final static Logger LOGGER = LogManager.getLogger(Client.class);

    /**
     * Fixed time download main.
     *
     * @param args no args required
     */
    public static void main(final String[] args) {

        final SpeedTestSocket speedTestSocket = new SpeedTestSocket();

        //speedTestSocket.setUploadStorageType(UploadStorageType.FILE_STORAGE);

        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(final SpeedTestReport report) {
                //called when download/upload is complete
                LogUtils.logFinishedTask(report.getSpeedTestMode(), report.getTotalPacketSize(),
                        report.getTransferRateBit(),
                        report.getTransferRateOctet(), LOGGER);
            }

            @Override
            public void onError(final SpeedTestError speedTestError, final String errorMessage) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(errorMessage);
                }
            }

            @Override
            public void onProgress(final float percent, final SpeedTestReport downloadReport) {
                LogUtils.logSpeedTestReport(downloadReport, LOGGER);
            }

			@Override
			public void onInterruption() {
				// TODO Auto-generated method stub
				
			}
        });

        speedTestSocket.startFixedDownload(SPEED_TEST_SERVER_URI_DL,
                SPEED_TEST_DURATION, REPORT_INTERVAL);
    }
}
