package Assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
	private static Socket socket = null;
	private static String ip = "127.0.0.1";
	private static int port = 5000;
	private static double dspeed = 2.1;
	
	ObjectOutputStream out = null;
	ObjectInputStream in = null;
	
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException 
    {
    	double dspeed = 2.1;
    	ObjectOutputStream out = null;
    	ObjectInputStream in = null;
		Scanner sc = new Scanner(System.in);
		socket = new Socket(ip, port);
		
			// Client receives list of available formats
			in = new ObjectInputStream(socket.getInputStream());
			String msgReceived = (String) in.readObject();
			System.out.println("Message Received: " + msgReceived);
			String[] msgParts = msgReceived.split("#"); 
			int i = 1;
			for(i=1; i<msgParts.length; i++)
			{
				System.out.println( i + ". " + msgParts[i]);
			}
			
		// Client chooses format and sends it to server
		out = new ObjectOutputStream(socket.getOutputStream());
		String msgReply = "";
		int s = sc.nextInt();
		if( s < i)
		{
			msgReply = "#" + dspeed + "#" + msgParts[s];
		}
		out.writeObject(msgReply);
		
			// Client receives list of available videos (#name-resolution)
			in = new ObjectInputStream(socket.getInputStream());
			String listReceived = (String) in.readObject();
			System.out.println("Message Received: " + listReceived);
			String[] listReceivedParts = listReceived.split("#");
			int j = 1;
			for(j=1; j<listReceivedParts.length; j++)
			{
				System.out.println( j + ". " + listReceivedParts[j]);
			}
		
		// client sends to server the movie for streaming
		out = new ObjectOutputStream(socket.getOutputStream());
		String choice = "";
		int c = sc.nextInt();
		if( c < j )
		{
			choice = listReceivedParts[c];
		}
		out.writeObject(choice);
		
		ProcessBuilder processBuilder = new ProcessBuilder();
		
		processBuilder.command("cmd.exe","/c","ffplay udp://127.0.0.1:1234");

        try
        {
            Process process = processBuilder.start();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
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

        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() 
        {

            @Override
            public void onCompletion(final SpeedTestReport report) 
            {
                //called when download/upload is complete
                LogUtils.logFinishedTask(report.getSpeedTestMode(), report.getTotalPacketSize(),
                        report.getTransferRateBit(),
                        report.getTransferRateOctet(), LOGGER);
                
            }

            @Override
            public void onError(final SpeedTestError speedTestError, final String errorMessage) 
            {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(errorMessage);
                }
            }

            @Override
            public void onProgress(final float percent, final SpeedTestReport downloadReport) 
            {
                LogUtils.logSpeedTestReport(downloadReport, LOGGER);
            }

			@Override
			public void onInterruption() 
			{
				// TODO Auto-generated method stub
				
			}
        });

        speedTestSocket.startFixedDownload(SPEED_TEST_SERVER_URI_DL,
                SPEED_TEST_DURATION, REPORT_INTERVAL);
    }
}
