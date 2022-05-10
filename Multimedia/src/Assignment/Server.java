package Assignment;

import java.net.ServerSocket;

import java.io.File;
import java.util.ArrayList;

public class Server 
{
	private static ServerSocket server;
	private static int port = 5000;
	
	private ArrayList<String> format;
	private ArrayList<String> resolution;
	
	public Server ()
	{
		format = new ArrayList<String>();
		format.add("avi");
	    format.add("mp4");
	    format.add("mkv");
	    
	    resolution = new ArrayList<String>();
	    resolution.add("240p");
	    resolution.add("360p");
	    resolution.add("480p");
	    resolution.add("720p");
	    resolution.add("1080p");
	}
	
	public static void main(String args[]) 
	{
		Server server = new Server();
		String DirectoryPath = System.getProperty("user.dir") + "/videos/";
		server.checkVideos(DirectoryPath);
		
	}
	
	public void checkVideos (String DirectoryPath)
	{
		File dir = new File(DirectoryPath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) 
		{
		    for (File child : directoryListing) 
		    {
		      System.out.println(child.getName());
		    }
		} 
		else 
		{
		    //TODO: Handle the case where dir is not really a directory or there are not videos
		}
	}
}