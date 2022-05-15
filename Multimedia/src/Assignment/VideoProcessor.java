package Assignment;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class VideoProcessor 
{
	private String[] format = {"avi","mp4","mkv"};
	private String[] resolution = {"1080p","720p","480p","360p","240p"};
	
	private static ArrayList<Video> videosToProcess = new ArrayList<Video>();
	private static ArrayList<Video> alreadyHaveVideos = new ArrayList<Video>();
	
	public VideoProcessor(ArrayList<Video> videos)
	{
		videosToProcess.addAll(videos);
	}
	
	
	public static ArrayList<Video> getAlreadyHaveVideos() 
	{
		return alreadyHaveVideos;
	}
	
	public void createMissingVideosList(String inputDir)
	{
		ArrayList<Video> missingVideoList = new ArrayList<Video>();
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
					missingVideoList.addAll(generateListForVideo(video,resolution_pointer,alreadyHaveVideos));
				}
				else
				{
					Video BestResolutionVideo = findBestResolutionVideo(same_files);
					int resolution_pointer = getResolutionPointer(BestResolutionVideo);
					missingVideoList.addAll(generateListForVideo(BestResolutionVideo,resolution_pointer,alreadyHaveVideos));
					addVideosFromListToOtherExceptOneVideo(same_files,alreadyHaveVideos,BestResolutionVideo);
				}
			}
		}
		writeListToFile("MissingVideos.txt",missingVideoList);
		
	}
	
	public void writeListToFile(String path,ArrayList<Video> videos)
	{
		try 
		{
		      FileWriter myWriter = new FileWriter(path);
		      for(Video video: videos)
		      {
		    	  myWriter.write(video.getName() + " " + video.getResolution() + " " + video.getFormat() + "\n");
			      
		      }
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		 } 
		catch (IOException e) 
		{
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
	}

	private int getResolutionPointer(Video video)
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
	
	private ArrayList<Video> generateListForVideo(Video video,int resolution_pointer,ArrayList<Video> alreadyHave)
	{
		ArrayList<Video> temp = new ArrayList<Video>();
		for (int j=0; j<format.length; j++)
		{
			for (int i=resolution_pointer; i<resolution.length; i++)
			{
				if(format[j].equals(video.getFormat()) && resolution[i].equals(video.getResolution()))
				{
					continue;
				}
				else if (checkIfExistsInList(alreadyHave,video.getName(),format[j],resolution[i]))
				{
					continue;
				}
				else
				{
					Video missingVideo = new Video(video.getName(),format[j],resolution[i]);
					temp.add(missingVideo);
				}
			}
		}
		return temp;
	}
	
	private boolean checkIfExistsInList(ArrayList<Video> alreadyHave,String name,String format,String resolution)
	{
		Video helper = new Video(name,resolution,format);
		boolean value = false;
		for(Video video: alreadyHave)
		{
			if(helper.isEqual(video))
			{
				value = true;
				break;
			}
		}
		return value;
	}
	
	
	private Video findBestResolutionVideo(ArrayList<Video> same_files)
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
	
	private void addVideosFromListToOtherExceptOneVideo(ArrayList<Video> fromlist,ArrayList<Video> tolist,Video exceptionVideo)
	{
		for(Video video: fromlist)
		{
			if(!video.isEqual(exceptionVideo))
			{
				tolist.add(video);
			}
		}
	}
}