package Assignment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import lab.LogUtils;

public class Client 
{ 
	
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException 
    {
    	double help = 2.1;
    	Socket socket = null;
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		Scanner sc = new Scanner(System.in);
		socket = new Socket("127.0.0.1", 5000);
		
			// Client receives list of available formats
			in = new ObjectInputStream(socket.getInputStream());
			String msgReceived = (String) in.readObject();
			System.out.println("Message Received: " + msgReceived);
			String[] msgParts = msgReceived.split("#"); 
			for(int i=1; i<msgParts.length; i++)
			{
				System.out.println( i + ". " + msgParts[i]);
			}
			
		// Client chooses format and sends it to server
		out = new ObjectOutputStream(socket.getOutputStream());
		String msgReply = "";
		int s = sc.nextInt();
		switch (s) 
		{
			case 1:
				msgReply = "#" + help + "#" + "avi";
				break;
			case 2:
				msgReply = "#" + help + "#" + "mp4";
				break;
			case 3:
				msgReply = "#" + help + "#" + "mkv";
				break;
			default:
				break;
		}
		out.writeObject(msgReply);
		
			// Client receives list of available videos (#name-resolution)
			in = new ObjectInputStream(socket.getInputStream());
			String listReceived = (String) in.readObject();
			System.out.println("Message Received: " + listReceived);
			String[] listReceivedParts = listReceived.split("#"); 
			for(int i=1; i<listReceivedParts.length; i++)
			{
				System.out.println( i + ". " + listReceivedParts[i]);
			}
		in.close();
		out.close();
		socket.close();
    }
    
    public void runSpeedTest()
    {
    	final String SPEED_TEST_SERVER_URI_DL = "http://ipv4.ikoula.testdebit.info/100M.iso";
    	final int REPORT_INTERVAL = 1000;
        final int SPEED_TEST_DURATION = 5000;

        Logger LOGGER = LogManager.getLogger(Client.class);
        
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
