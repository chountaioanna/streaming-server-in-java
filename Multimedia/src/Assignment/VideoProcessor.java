package Assignment;

import java.util.ArrayList;
import java.util.Iterator;

public class VideoProcessor 
{
	private String[] format = {"avi","mp4","mkv"};
	private String[] resolution = {"1080p","720p","480p","360p","240p"};
	
	private static ArrayList<Video> videosToProcess = new ArrayList<Video>();
	private ArrayList<Video> missingVideos = new ArrayList<Video>();
	private static ArrayList<Video> alreadyHaveVideos = new ArrayList<Video>();
	
	public VideoProcessor(ArrayList<Video> videos)
	{
		videosToProcess.addAll(videos);
	}
	
	public static ArrayList<Video> getAlreadyHaveVideos() 
	{
		return alreadyHaveVideos;
	}

	public ArrayList<Video> getMissingVideos() 
	{
		return missingVideos;
	}

	public static ArrayList<Video> getVideos() 
	{
		return videosToProcess;
	}
	
	public void createMissingVideosList()
	{
		for(Video video: videosToProcess)
		{
			if (video.CheckIfNamefexists(alreadyHaveVideos))
			{
				continue;
			}
			else
			{
				ArrayList<Video> same_files = new ArrayList<Video>();
				same_files = video.SearchByName(videosToProcess);
				if (same_files.size() == 1)
				{
					int resolution_pointer = getResolutionPointer(video);
					generateListForVideo(video,resolution_pointer);
				}
				else
				{
					Video BestResolutionVideo = findBestResolutionVideo(same_files);
					int resolution_pointer = getResolutionPointer(BestResolutionVideo);
					generateListForVideo(BestResolutionVideo,resolution_pointer);
					addVideosFromListToOtherExceptOneVideo(same_files,alreadyHaveVideos,BestResolutionVideo);
				}
			}
		}
		deleteAlreadyHaveVideosFromMissingVideos();
	}
	
	public void deleteAlreadyHaveVideosFromMissingVideos()
	{
		System.out.println("AlreadyHave");
		for(Video video: alreadyHaveVideos)
		{
			System.out.println(video.showVideoDetails());
		}
		System.out.println("DEBUGGING");
		for(Video video: alreadyHaveVideos) 
		{
			for (Iterator<Video> i = missingVideos.iterator(); i.hasNext(); ) 
			{
			    Video value=(Video)i.next();
			    if(video.isEqual(value)) 
			    {
			    	System.out.println("hi");
			        i.remove();
			    }
			}
		}
		System.out.println("MissingVideos");
		for(Video video: missingVideos)
		{
			System.out.println(video.showVideoDetails());
		}
	}
	
	public void addVideosFromListToOtherExceptOneVideo(ArrayList<Video> fromlist,ArrayList<Video> tolist,Video exceptionVideo)
	{
		for(Video video: fromlist)
		{
			if(!video.isEqual(exceptionVideo))
			{
				tolist.add(video);
			}
		}
	}
	
	public Video findBestResolutionVideo(ArrayList<Video> same_files)
	{
		Video found_video = same_files.get(0).copy();
		String max_resolution = same_files.get(0).getResolution();
		for(Video video: same_files)
		{
			boolean isTrue = video.hasBetterResolution(max_resolution);
			if (isTrue)
			{
				max_resolution = video.getResolution();
				found_video = video.copy();
			}
		}
		return found_video;
	}
	
	public int getResolutionPointer(Video video)
	{
		int return_value=-1;
		String resolution_value = video.getResolution();
		for(int i=0; i<resolution.length; i++)
		{
			if(resolution_value.equals(resolution[i]))
			{
				return_value = i;
			}
		}
		return return_value;
	}
	
	public void generateListForVideo(Video video,int resolution_pointer)
	{
		for (int j=0; j<format.length; j++)
		{
			for (int i=resolution_pointer; i<resolution.length; i++)
			{
				if(format[j].equals(video.getFormat()) && resolution[i].equals(video.getResolution()))
				{
					continue;
				}
				else
				{
					Video missingVideo = new Video(video.getName(),format[j],resolution[i]);
					missingVideos.add(missingVideo);
				}
			}
		}
	}
}