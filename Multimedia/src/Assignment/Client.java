package Assignment;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client 
{ 
	private static Socket socket = null;
	private static String ip = "127.0.0.1";
	private static int port = 5000;
	
	ObjectOutputStream out = null;
	ObjectInputStream in = null;
	
	private static String chosen_format = null;
	private static String chosen_file = null;
	private static String chosen_resolution = null;
	private static String chosen_protocol = null;
	
	
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException 
    {
    	double dspeed = 2.5;
    	ObjectOutputStream out = null;
    	ObjectInputStream in = null;
		try (Scanner scanner = new Scanner(System.in)) 
		{
			socket = new Socket(ip, port);
			System.out.println("You are now connected");
			
			// Client receives list of available formats
			in = new ObjectInputStream(socket.getInputStream());
			String msgReceived = (String) in.readObject();
			
			System.out.println("Enter internet connection speed: (example 2.5)");
			dspeed = scanner.nextDouble();
			
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
			System.out.println("3. I dont know");
			int o = scanner.nextInt();
			switch(o)
			{
				case 1:
					chosen_protocol = "TCP";
					break;
				case 2:
					chosen_protocol = "UDP";
					break;
				default:
					if(chosen_resolution.equals("240p"))
					{
						chosen_protocol = "TCP";
					}
					else 
					{
						chosen_protocol = "UDP";
					}
			}
		}
		
		String msgReply1 = "#" + chosen_file + "-" + chosen_resolution + "." + chosen_format + "#" + chosen_protocol; 
		out.writeObject(msgReply1);
		
		// Client receives command for process builder.
		in = new ObjectInputStream(socket.getInputStream());
		String command_client = (String) in.readObject();
		
		ProcessBuilder processBuilder = new ProcessBuilder();
		
		processBuilder.command("cmd.exe","/c",command_client);

        try
        {
            Process process = processBuilder.start();
            System.out.println("Starting streaming...");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
		in.close();
		out.close();
		socket.close();
    }
}
