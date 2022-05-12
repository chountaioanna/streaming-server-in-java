package Assignment;

import java.io.File;
import java.util.ArrayList;

public class Server 
{
	private String[] format = {"avi","mp4","mkv"};
	private String[] resolution = {"1080p","720p","480p","360p","240p"};
	
	private static ArrayList<Video> videos = new ArrayList<Video>();
	private static ArrayList<Video> VideosToGenerate = new ArrayList<Video>();
	private static ArrayList<Video> already_have = new ArrayList<Video>();
	
	public static void main(String args[]) 
	{
		Server server = new Server();
		String DirectoryPath = System.getProperty("user.dir") + "/videos/";
		server.analyzeVideos(DirectoryPath); //turn videos in objects
		server.checkForMultipleVideos();
		server.getmissingVideos(); // get list of videos that ffmpeg needs
	}
	
	public void analyzeVideos (String DirectoryPath)
	{
//		File dir = new File(DirectoryPath);
//		File[] directoryListing = dir.listFiles();
//		if (directoryListing != null) 
//		{
//		    for (File child : directoryListing) 
//		    {
//		    	String[] child_details;
//		    	child_details = child.getName().split("-|\\.");
//		    	Video video = new Video(child_details[0],child_details[1],child_details[2]);
//		    	videos.add(video);	
//		    }
//		}
	}
	
	
	
	
	public void checkForMultipleVideos ()
	{
		for(Video video: videos)
		{
			ArrayList<Video> videos_by_name = new ArrayList<Video>();
			videos_by_name = video.searchByName(videos);
			if (videos_by_name.size() == 1)
			{
				continue;
			}
			else
			{
				// TODO: αν το video υπαρχει στην already_have,αγνοείται
				if( video.checkIfNamefexists(already_have))
				{
					continue;
				}
				// TODO: βρες την μεγαλυτερη resolution απο την λιστα με τα videos_by_name και κρατα αυτήν
				String max_resolution_value = videos_by_name.get(0).getResolution();
				for(int i=1; i<videos_by_name.size(); i++)
				{
					if(videos_by_name.get(i).hasBetterResolution(max_resolution_value))
					{
						max_resolution_value = videos_by_name.get(i).getResolution();
					}
				}
				// TODO: θέλω να πηγαίνει όλα τα objects του videos_by_name πλην του video με την max resolution στην λίστα με τα already_have
				for(Video item: videos_by_name)
				{
					if(item.getResolution().equals(max_resolution_value))
					{
						continue;
					}
					else
					{
						already_have.add(item);
					}
				}
			}
		}
	}
	

	public void getmissingVideos ()
	{
		for(Video video: videos)
		{
			String max_resolution_value = video.getResolution();
			int max_resolution_loc=0;
			for (int i=0; i<resolution.length; i++)
			{
				if(resolution[i].equals(max_resolution_value))
				{
					max_resolution_loc = i;
				}
			}
			VideosToGenerate.addAll(video.getMissingVideos(format,resolution,max_resolution_loc));
		}
		System.out.println("")
		VideosToGenerate.removeAll(already_have);
	}
	// TODO public void generateVideos
}



