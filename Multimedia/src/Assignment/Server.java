package Assignment;

import java.io.File;
import java.util.ArrayList;

public class Server 
{
	private static ArrayList<Video> directory_videos = new ArrayList<Video>();
	
	public static void main(String args[]) 
	{
		Server server = new Server();
		String DirectoryPath = System.getProperty("user.dir") + "/videos/";
		server.analyzeVideos(DirectoryPath); //turn videos in objects
		VideoProcessor processor = new VideoProcessor(directory_videos);
		processor.createMissingVideosList();
		ArrayList<Video> missingVideos = new ArrayList<Video>();
		missingVideos = processor.getMissingVideos();
		ArrayList<Video> refresh = new ArrayList<Video>();
		refresh = processor.getAlreadyHaveVideos();
		refreshDirectory(DirectoryPath,refresh);
		VideoFormatter formatter = new VideoFormatter(missingVideos);
		
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
		    	directory_videos.add(video);	
		    }
		}
	}
	
	//TODO: DELETE EARTH 360P avi -> debug isEquals
	public static void refreshDirectory(String DirectoryPath,ArrayList<Video> videoList) // already_have
	{	
		File dir = new File(DirectoryPath);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) 
		{
		    for (File child : directoryListing) 
		    {
		    	for(Video list_item: videoList)
		    	{
		    		String[] child_details;
		    		child_details = child.getName().split("-|\\.");
			    	Video video = new Video(child_details[0],child_details[1],child_details[2]);
			    	if(video.isEqual(list_item))
			    	{
			    		child.delete();
			    	}
		    	}
		    }
		}
	}
}