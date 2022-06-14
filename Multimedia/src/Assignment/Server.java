package Assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;

public class Server 
{
	private static String[] format = {"avi","mp4","mkv"};
	private static String[] resolution = {"1080p","720p","480p","360p","240p"};
	
	private static String InputPath = System.getProperty("user.dir") + "/videos/";
	private static String OutputPath = System.getProperty("user.dir") + "/output/";
	
	private static ArrayList<Video> videos = new ArrayList<Video>();
	
	private static ServerSocket server;
	private static int port = 5000;
	
	private static Map<Integer, Double> recommended = new HashMap<>();
	
	public static void main(String args[]) 
	{
		Server server = new Server();
		server.analyzeVideos(InputPath);
		VideoProcessor processor = new VideoProcessor(videos,format,resolution);
		processor.createMissingVideosList(InputPath);
		ArrayList<Video> alreadyHaveVideos = new ArrayList<Video>();
		alreadyHaveVideos = VideoProcessor.getAlreadyHaveVideos();
		Server.refreshDirectory(InputPath, alreadyHaveVideos);
		VideoFormatter formatter = new VideoFormatter(videos);
		formatter.generateVideos(InputPath,OutputPath);
		server.moveFiles();
		
		try 
		{
			server.startServer();
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String getFormatList (String[] format)
	{
		String str = "";
		for(int i=0; i<format.length; i++)
		{
			str = str + "#" + format[i] ;
		}
		return str;
	}
	
	public void startServer() throws IOException, ClassNotFoundException 
	{
		server = new ServerSocket(port);
		ObjectOutputStream out = null;
		ObjectInputStream in = null;
		initialiseMap();
		String list = getFormatList(format);
		while (true) 
		{
			System.out.println("Listening for client requests...");
			Socket socket = server.accept();
			
			// server sends client the list with the available formats
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(list);
			
				// server receives the download speed of client and the desired format 
				in = new ObjectInputStream(socket.getInputStream());
				String msgReceived = (String) in.readObject();
				System.out.println(msgReceived);
				String[] msgParts = msgReceived.split("#"); 
				double SpeedClient = Double.parseDouble(msgParts[1]);
				String FormatClient = msgParts[2];
				
//			// server sends the list of available videos according to download speed and the chosen format
			String msgReply = getStreamingVideos(SpeedClient,FormatClient);
			System.out.println(msgReply);
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(msgReply);
			
				// server receives client choice
				in = new ObjectInputStream(socket.getInputStream());
				String moviechoice = (String) in.readObject();
				System.out.println(moviechoice);
				String[] streaming_details = moviechoice.split("#");
				//System.out.println(streaming_details[1] + " " + streaming_details[2]);
				
			String command = null;
			String command_client = null;
			
			if (streaming_details[2].equals("UDP"))
			{
				command = "ffmpeg -re -i " + streaming_details[1] + " -f avi udp://127.0.0.1:1234";
				command_client = "ffplay udp://127.0.0.1:1234";
			}
			
			if (streaming_details[2].equals("TCP"))
			{
				command = "ffmpeg -i " + streaming_details[1] + " -f avi tcp://127.0.0.1:1234?listen";
				command_client = "ffplay tcp://127.0.0.1:1234";
			}
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("cmd.exe","/c",command);
			processBuilder.directory(new File(OutputPath));
	        try 
	        {
	            Process process = processBuilder.start();
				out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(command_client);
	        } 
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        } 
				
			out.close();
			in.close();
			socket.close();
			if (msgReceived.equals("EXIT"))
			{
				break;
			}
		}
		System.out.println("Closing Socket server");
		server.close();
	}
	
	public String getStreamingVideos(double SpeedClient,String FormatClient)
	{
		String listofvideos = "";
		File dir = new File(OutputPath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) 
		{
		    for (File child : directoryListing) 
		    {
		    	String[] child_details;
		    	child_details = child.getName().split("-|\\.");
		    	Video video = new Video(child_details[0],child_details[1],child_details[2]);
		    	if(video.getFormat().equals(FormatClient))
				{
					if( getValueMapper(video) < SpeedClient )
					{
						listofvideos= listofvideos + "#" + video.getName() + "-" + video.getResolution();
					}
				}
		    }
		}
		else
		{
			System.out.println("No videos found");
		}
		return listofvideos;
	}
	
	private void initialiseMap()
	{
		recommended.put(1080,4.5);
		recommended.put(720,2.5);
		recommended.put(480,1.0);
		recommended.put(360,0.75);
		recommended.put(240,0.4);
	}
	
	private Double getValueMapper (Video video)
	{
		Double value;
		Integer key;
		key = video.getNumericalResolution();
		value = recommended.get(key);
		return value;
	}
	
	public void analyzeVideos (String DirectoryPath)
	{
		File dir = new File(DirectoryPath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) 
		{
		    for (File child : directoryListing) 
		    {
		    	String[] child_details;
		    	child_details = child.getName().split("-|\\.");
		    	Video video = new Video(child_details[0],child_details[1],child_details[2]);
		    	videos.add(video);	
		    }
		}
		else
		{
			System.out.println("No videos found");
		}
	}
	
	public static void refreshDirectory(String InputPath,ArrayList<Video> alreadyHaveVideos)
	{
		File dir = new File(InputPath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) 
		{
		    for (File child : directoryListing) 
		    {
		    	for(Video video: alreadyHaveVideos)
		    	{
		    		String[] child_details;
			    	child_details = child.getName().split("-|\\.");
			    	Video item = new Video(child_details[0],child_details[1],child_details[2]);
			    	if(item.isEqual(video))
			    	{
			    		child.delete();
			    	}
		    	}	
		    }
		}
	}
	
	public void moveFiles()
	{
		File dir = new File(InputPath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) 
		{
		    for (File child : directoryListing) 
		    {
		    	child.renameTo(new File(OutputPath + "/" + child.getName()));
		    }
		}
	}
	
	
}