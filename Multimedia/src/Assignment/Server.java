package Assignment;

import java.io.File;
import java.util.ArrayList;

public class Server 
{
	private String[] format = {"avi","mp4","mkv"};
	private String[] resolution = {"1080p","720p","480p","360p","240p"};
	
	private static ArrayList<Video> availableVideos = new ArrayList<Video>();
	private static ArrayList<Video> missingVideos = new ArrayList<Video>();
	

	public static void main(String args[]) 
	{
		Server server = new Server();
		String DirectoryPath = System.getProperty("user.dir") + "/videos/";
		server.analyzeVideos(DirectoryPath); //turn videos in objects
		server.getMissingVideos(); // get list of videos that ffmpeg is needed
		for (Video video: missingVideos)
		{
			video.showVideoDetails();
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
		    	availableVideos.add(video);	
		    }
		}
	}
	
	public void getMissingVideos ()
	{
		ArrayList<Video> temp = new ArrayList<Video>();
		for(Video video: availableVideos)
		{
			// TODO: na ftiakso omorfo kvdika poy nmhn kanei ta idia pragmata dyo fores
			temp = video.searchByName(availableVideos); // lista me objects poy aforoyn ton idio titlo
			if( temp.size() == 1 ) 
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
				missingVideos.addAll(video.getMissingVideos(format,resolution,max_resolution_loc));
			}
			else
			{
				// TODO: bres thn megalyterh resolution apo thn lista me ta temp kai krata aythn
				String max_resolution_value = temp.get(0).getResolution();
				int max_resolution_loc=0;
				for(int i=1; i<temp.size(); i++)
				{
					if(temp.get(i).hasBetterResolution(max_resolution_value))
					{
						max_resolution_value = temp.get(i).getResolution();
						max_resolution_loc = i;
					}
				} 
				
				// TODO: thelo na pigainei ola ta objects toy temp plhn toy video me thn max resolution sthn lista already_have
				ArrayList<Video> already_have = new ArrayList<Video>();
				for(Video item: temp)
				{
					if(item.getResolution().equals(max_resolution_value))
					{
						continue;
					}
					else
					{
						already_have.add(item);
						temp.remove(item);
					}
				}
				// TODO: thelo na dhmioyrgei thn lista meta missing videos apo to video me thn megalyterh resolution
				missingVideos.addAll(video.getMissingVideos(format,resolution,max_resolution_loc));
				// TODO: thelo na diagraftoyn ta already have apo thn lista missingVideos 
				missingVideos.removeAll(already_have);
				// TODO: thelo na diagraftoyn ta already have apo thn lista available gia na glitoso thn epanalhpsh
				availableVideos.removeAll(already_have);
			}
		}
	}
}