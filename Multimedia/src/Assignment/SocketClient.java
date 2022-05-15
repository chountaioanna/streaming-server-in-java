package Assignment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class SocketClient 
{
	private static SpeedTestSocket speedTestSocket = new SpeedTestSocket();

	
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException 
	{
		// add a listener to wait for speedtest completion and progress
		speedTestSocket.addSpeedTestListener(new ISpeedTestListener() 
		{

		    @Override
		    public void onCompletion(SpeedTestReport report) 
		    {
		        // called when download/upload is complete
		        System.out.println("[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
		        System.out.println("[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
		    }

		    @Override
		    public void onError(SpeedTestError speedTestError, String errorMessage) 
		    {
		         // called when a download/upload error occur
		    }

		    @Override
		    public void onProgress(float percent, SpeedTestReport report) 
		    {
		        // called to notify download/upload progress
		        System.out.println("[PROGRESS] progress : " + percent + "%");
		        System.out.println("[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
		        System.out.println("[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
		    }

			@Override
			public void onInterruption() 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		speedTestSocket.startFixedDownload("http://ipv4.ikoula.testdebit.info/100M.iso", 10);
		
//		Socket socket = null;
//		ObjectOutputStream oos = null;
//		ObjectInputStream ois = null;
//		Scanner sc = new Scanner(System.in);
//        int s,am;
//        String name;
//		while (true) 
//		{
//			socket = new Socket("127.0.0.1", 5000);
//				oos = new ObjectOutputStream(socket.getOutputStream());
//				String msg = "";
//				System.out.println("1.LIST\n2.ADD\n3.DELETE\n4.EXIT");
//				s = sc.nextInt();
//				switch (s) 
//				{
//					case 1:
//						oos.writeObject("LIST");
//						break;
//					case 2:
//						System.out.println("AM:");
//						am = sc.nextInt();
//						sc.nextLine();
//						System.out.println("Name:");
//						name = sc.nextLine();
//						msg = "ADD#" + am + "#" + name;
//						break;
//					case 3:
//						System.out.println("AM:");
//						am = sc.nextInt();	
//						msg = "DEL#" + am;
//						break;
//					case 4:
//						msg = "EXIT";
//						break;
//					default:
//						break;
//				}
//				oos.writeObject(msg);			
//					ois = new ObjectInputStream(socket.getInputStream());
//					System.out.println((String) ois.readObject() + " \n");
//					ois.close();
//				oos.close();
//			socket.close();
//			if (s==4) break;
//		}
	}
}