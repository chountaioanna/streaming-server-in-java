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
		ArrayList<Video> missingVideos = new ArrayList<Video>();
		missingVideos = processor.createMissingVideosList();
		for (Video video: missingVideos)
		{
			System.out.println(video.showVideoDetails());
		}
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
}