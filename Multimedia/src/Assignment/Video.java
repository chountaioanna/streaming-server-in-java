package Assignment;

import java.util.ArrayList;

public class Video 
{
	private String name;
	private String resolution;
	private String format;
	
	//contructors
	public Video()
	{
		
	}
	public Video(String name,String resolution,String format) 
	{
		this.name = name;
		this.resolution = resolution;
		this.format = format;
	}
	
	
	public Video copy()
	{
		return this;
	}
	
	//getters
	public String getName() 
	{
		return name;
	}
	
	public String getResolution() 
	{
		return resolution;
	}
	
	
	public String getFormat() 
	{
		return format;
	}

	//methods
	public ArrayList<Video> SearchByName ( ArrayList<Video> listofvideos)
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
	
	public boolean isEqual (Video video)
	{
		boolean valuation = false;
		System.out.println(name.equals(video.getResolution()) && format.equals(video.getFormat()) && resolution.equals(video.getResolution()));
		if(name.equals(video.getResolution()) && format.equals(video.getFormat()) && resolution.equals(video.getResolution()))
		{
			System.out.println("why");
			valuation = true;
		}
		return valuation;
	}
	
	public boolean CheckIfNamefexists ( ArrayList<Video> listofvideos)
	{
		boolean itExists = false;
		for(Video video: listofvideos)
		{
			if(this.name.equals(video.name))
			{
				itExists = true;
				break;
			}
		}
		return itExists;
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
	
	
	public String showVideoDetails ()
	{
		String details = name + " " + resolution + " " + format;
		return details;
	}
}
