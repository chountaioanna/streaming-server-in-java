package Assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
	
	ObjectOutputStream out = null;
	ObjectInputStream in = null;
	
	private double dspeed = 3.5;
	private static String chosen_format = null;
	private static String chosen_file = null;
	private static String chosen_resolution = null;
	private static String chosen_protocol = null;
	
	
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException 
    {
    	double dspeed = 3.5;
    	ObjectOutputStream out = null;
    	ObjectInputStream in = null;
		Scanner scanner = new Scanner(System.in);
		socket = new Socket(ip, port);
		
		// Client receives list of available formats
		in = new ObjectInputStream(socket.getInputStream());
		String msgReceived = (String) in.readObject();
		
		// Client prints received list
		System.out.println("Choose format: ");
		String[] msgParts = msgReceived.split("#"); 
		int i = 1;
		for(i=1; i<msgParts.length; i++)
		{
			System.out.println( i + ". " + msgParts[i]);
		}
			
		// Client chooses format
		out = new ObjectOutputStream(socket.getOutputStream());
		String msgReply = "";
		int choosen_format_number = scanner.nextInt();
		if( choosen_format_number < i)
		{
			chosen_format = msgParts[choosen_format_number];
		}
		// Client sends #dspeed#chosen_format to server
		msgReply = "#" + dspeed + "#" + chosen_format;
		out.writeObject(msgReply);
		
		
		
		// Client receives list of available videos (#name#resolution)
		in = new ObjectInputStream(socket.getInputStream());
		String listReceived = (String) in.readObject();
		
		// Client prints received list
		System.out.println("Choose file from the following list: ");
		ArrayList<Video> videoList = new ArrayList<Video> ();
		String[] listReceivedParts = listReceived.split("#");
		int j = 1;
		for(j=1; j<listReceivedParts.length; j++)
		{
			String[] part_details;
	    	part_details = listReceivedParts[j].split("-");
	    	Video video = new Video(part_details[0],part_details[1],chosen_format);
	    	videoList.add(video);
			System.out.println( j + ". " + video.getName() + " " + video.getResolution() + " " + video.getFormat());
		}
		//Client chooses movie for streaming
		out = new ObjectOutputStream(socket.getOutputStream());
		int c = scanner.nextInt();
		if( c <= j )
		{
			chosen_file = videoList.get(c-1).getName();
			chosen_resolution = videoList.get(c-1).getResolution();
		}
		
		//Client chooses protocol for streaming
		
		System.out.println("Choose protocol: ");
		System.out.println("1. TCP");
		System.out.println("2. UDP");
		System.out.println("3. RTP/UDP");
		System.out.println("4. I dont know");
		int o = scanner.nextInt();
		switch(o)
		{
			case 1:
				chosen_protocol = "TCP";
				break;
			case 2:
				chosen_protocol = "UDP";
				break;
			case 3:
				chosen_protocol = "RTP/UDP";
				break;
			default:
				if(chosen_resolution.equals("240p"))
				{
					chosen_protocol = "TCP";
				}
				else if(chosen_resolution.equals("360p") | chosen_resolution.equals("480p") )
				{
					chosen_protocol = "UDP";
				}
				else
				{
					chosen_protocol = "RTP/UDP";
				}
		}
		String msgReply1 = "#" + chosen_file + "-" + chosen_resolution + "." + chosen_format + "#" + chosen_protocol; 
		out.writeObject(msgReply1);
		
		// Client receives list of available videos (#name#resolution)
		in = new ObjectInputStream(socket.getInputStream());
		String command_client = (String) in.readObject();
		
		ProcessBuilder processBuilder = new ProcessBuilder();
		
		processBuilder.command("cmd.exe","/c",command_client);

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
	public double getDspeed() {
		return dspeed;
	}
	public void setDspeed(double dspeed) {
		this.dspeed = dspeed;
	}
	public String getChosen_file() {
		return chosen_file;
	}
	public void setChosen_file(String chosen_file) {
		this.chosen_file = chosen_file;
	}
	public String getChosen_format() {
		return chosen_format;
	}
	public void setChosen_format(String chosen_format) {
		this.chosen_format = chosen_format;
	}
	public String getChosen_protocol() {
		return chosen_protocol;
	}
	public void setChosen_protocol(String chosen_protocol) {
		this.chosen_protocol = chosen_protocol;
	}
}
