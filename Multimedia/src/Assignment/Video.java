package Assignment;

import java.util.ArrayList;

public class Video 
{
	private String name;
	private String resolution;
	private String format;
	

	public Video(String name,String resolution,String format) 
	{
		this.name = name;
		this.resolution = resolution;
		this.format = format;
	}
	
	public ArrayList<Video> getMissingVideos (String[] format_list, String[] resolution_list,int resolution_loc)
	{
		ArrayList<Video> missingVideos = new ArrayList<Video>();
		for (int j=0; j<format_list.length; j++)
		{
			for (int i=resolution_loc; i<resolution_list.length; i++)
			{
				if(format_list[j].equals(format) && resolution_list[i].equals(resolution))
				{
					continue;
				}
				else
				{
					Video video = new Video(name,format_list[j],resolution_list[i]);
					missingVideos.add(video);
				}
			}
		}
		return missingVideos;
	}
	
	public ArrayList<Video> searchByName ( ArrayList<Video> listofvideos)
	{
		ArrayList<Video> items_found = new ArrayList<Video>();
		for(Video video: listofvideos)
		{
			if(this.name.equals(video.name))
			{
				items_found.add(video);
			}
		}
		return items_found;
	}
	
	public ArrayList<Video> deleteVideo (ArrayList<Video> list)
	{
		for (Video video: list)
		{
			if( this.name.equals(video.name) && this.format.equals(video.format) && this.resolution.equals(video.resolution))
			{
				list.remove(video);
			}
		}
		return list;
	}
	
	public boolean hasBetterResolution (String other_resolution)
	{
		String object_substring = resolution.substring(0,resolution.length()-1);
		Integer  object_num_resolution = Integer.parseInt(object_substring);
		String item_substring = other_resolution.substring(0,other_resolution.length()-1);
		Integer  item_num_resolution = Integer.parseInt(item_substring);
		if ( object_num_resolution >= item_num_resolution)
		{
			return true;
		}
		return false;
	}
	
	
	public void showVideoDetails ()
	{
		System.out.println(name + " " + resolution + " " + format);
	}

	public String getResolution() 
	{
		return resolution;
	}
}
