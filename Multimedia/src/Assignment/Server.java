package Assignment;

import java.io.File;
import java.util.ArrayList;

public class Server 
{
	private static String InputPath = System.getProperty("user.dir") + "/videos/";
	private static String OutputPath = System.getProperty("user.dir") + "/output/";
	
	private static ArrayList<Video> videos = new ArrayList<Video>();
	
	public static void main(String args[]) 
	{
		Server server = new Server();
		server.analyzeVideos(InputPath);
		VideoProcessor processor = new VideoProcessor(videos);
		processor.createMissingVideosList(InputPath);
		ArrayList<Video> alreadyHaveVideos = new ArrayList<Video>();
		alreadyHaveVideos = VideoProcessor.getAlreadyHaveVideos();
		Server.refreshDirectory(InputPath, alreadyHaveVideos);
		VideoFormatter formatter = new VideoFormatter(videos);
		formatter.generateVideos(InputPath,OutputPath);
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