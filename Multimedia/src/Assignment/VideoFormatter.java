package Assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lab.TestFFMpeg;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

public class VideoFormatter 
{
	static Logger log = LogManager.getLogger(TestFFMpeg.class);
	
	private static ArrayList<Video> DirectoryVideos = new ArrayList<Video>();
	private static Map<Integer, Integer> resolution_map = new HashMap<>();
	private static ArrayList<Video> MissingVideos = new ArrayList<Video>();
	
	public VideoFormatter(ArrayList<Video> Videos)
	{
		DirectoryVideos.addAll(Videos);
		initialiseMap();
	}
	
	private void initialiseMap ()
	{
		resolution_map.put(1080,1920);
		resolution_map.put(720,1280);
		resolution_map.put(480,640);
		resolution_map.put(360,480);
		resolution_map.put(240,426);
	}
	
	private void readFile(String name)
	{
		try 
		{
			File myObj = new File(name);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) 
			{
				String data = myReader.nextLine();
				String[] details;
				details = data.split(" ");
				Video video = new Video(details[0],details[2],details[1]);
				MissingVideos.add(video);	
			}
			myReader.close();
			System.out.println("Successfully read from the file.");
	    } 
		catch (FileNotFoundException e) 
		{
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	}
	
	private Integer getValueMapper (Video video)
	{
		Integer value;
		Integer key;
		key = video.getNumericalResolution();
		value = resolution_map.get(key);
		return value;
	}
	
	public void generateVideos(String inputDir,String outDir) 
	{
		readFile("MissingVideos.txt");
		
		FFmpeg ffmpeg = null;
		FFprobe ffprobe = null;
		try 
		{
			log.debug("Initialising FFMpegClient");
			ffmpeg = new FFmpeg("C:\\ffmpeg\\bin\\ffmpeg.exe");
			ffprobe = new FFprobe("C:\\ffmpeg\\bin\\ffprobe.exe");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		File dir = new File(inputDir);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) 
		{
		    for (File child : directoryListing) 
		    {
		    	String[] details;
		    	details = child.getName().split("-|\\.");
		    	Video video = new Video(details[0],details[1],details[2]);
		    	if(video.CheckIfNamefexists(MissingVideos))
		    	{
		    		for(Video toBeCreated: MissingVideos)
			    	{
		    			if(toBeCreated.getName().equals(toBeCreated.getName()))
		    			{
		    				log.debug("Creating the transcoding");
			    			FFmpegBuilder builder = new FFmpegBuilder()

			    					  .setInput(inputDir + video.getName() + "-" + video.getResolution() + "." + video.getFormat())     // Filename, or a FFmpegProbeResult
			    					  .overrideOutputFiles(true) // Override the output if it exists

			    					  .addOutput(outDir + toBeCreated.getName() + "-" + toBeCreated.getResolution() + "." + toBeCreated.getFormat())   // Filename for the destination
			    					  
			    					  	.setVideoCodec("libx264")     // Video using x264
			    					    .setVideoFrameRate(24, 1)     // at 24 frames per second
			    					    .setVideoResolution(getValueMapper(toBeCreated),toBeCreated.getNumericalResolution()) // at specific resolution

			    					    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
			    					    .done();

			    					log.debug("Creating the executor");
			    					FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
			    					log.debug("Starting the transcoding");
			    					// Run a one-pass encode
			    					executor.createJob(builder).run();

			    					// Or run a two-pass encode (which is better quality at the cost of being slower)
			    					executor.createTwoPassJob(builder).run();
			    					log.debug("Transcoding finished");
		    			}
			    	}
		    	}
		    }
		}
	}
}