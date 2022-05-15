package Assignment;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;


public class Server 
{
	private static ServerSocket server;
	private static int port = 5000;
	
	private static String InputPath = System.getProperty("user.dir") + "/videos/";
	private static String OutputPath = System.getProperty("user.dir") + "/output/";
	
	private static ArrayList<Video> videos = new ArrayList<Video>();
	
	public static void main(String args[]) 
	{
		Server server = new Server();
		
		// First part: manage streaming files
		server.analyzeVideos(InputPath);
		VideoProcessor processor = new VideoProcessor(videos);
		processor.createMissingVideosList(InputPath); // create missingVideos txt
		
//		ArrayList<Video> alreadyHaveVideos = new ArrayList<Video>();
//		alreadyHaveVideos = VideoProcessor.getAlreadyHaveVideos();
//		Server.refreshDirectory(InputPath, alreadyHaveVideos);
//		VideoFormatter formatter = new VideoFormatter(videos);
//		formatter.generateVideos(InputPath,OutputPath);
		
		// Second part: communication between server and client
//		try 
//		{
//			server.startServer();
//		} 
//		catch (ClassNotFoundException e) 
//		{
//			e.printStackTrace();
//		} 
//		catch (IOException e) 
//		{
//			e.printStackTrace();
//		}
	}
	
//	public void startServer() throws IOException, ClassNotFoundException 
//	{
//
////		students = new Hashtable<Integer, Student>();
//		server = new ServerSocket(port);
//		while (true) 
//		{
//			System.out.println("Listening for client requests");
//			Socket socket = server.accept();
//			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//			String msgReceived = (String) ois.readObject();
//			System.out.println("Message Received: " + msgReceived);
////			String msgReply = "";
////			String[] msgParts = msgReceived.split("#"); 
////			if (msgParts[0].equals("LIST")) {
////				msgReply = "LIST#" + getStudentsData();
////			} else if (msgParts[0].equals("ADD")) {
////				int am = Integer.parseInt(msgParts[1]);
////				String name = msgParts[2];
////				Student std =  new Student(am,name);
////				students.put(am, std);
////				msgReply = "ADD_OK";
////			} else if (msgParts[0].equals("DEL")) {
////				int am = Integer.parseInt(msgParts[1]);
////				students.remove(am);
////				msgReply = "DEL_OK";
////			} else if (msgParts[0].equals("EXIT")) {
////				msgReply = "EXIT_OK";
////			} 
////			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
////			oos.writeObject(msgReply);
////			ois.close();
////			oos.close();
////			socket.close();
////			if (msgReply.equals("EXIT_OK")) break;
//		}
//		System.out.println("Closing Socket server");
//		server.close();
//	}
	
	
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
}