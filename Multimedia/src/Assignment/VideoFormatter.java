package Assignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	
	private static ArrayList<Video> missing_videos = new ArrayList<Video>();
	private static ArrayList<Video> DirectoryVideos = new ArrayList<Video>();
	private static Map<Integer, Integer> resolution_map = new HashMap<>();
	
	public VideoFormatter(ArrayList<Video> missingVideos,ArrayList<Video> Videos)
	{
		missing_videos.addAll(missingVideos);
		DirectoryVideos.addAll(Videos);
		resolution_map.put(1080,1920);
		resolution_map.put(720,1280);
		resolution_map.put(480,640);
		resolution_map.put(360,480);
		resolution_map.put(240,426);
	}
	
	public Integer getValueMapper (Video video)
	{
		Integer value;
		Integer key;
		key = video.getNumericalResolution();
		value = resolution_map.get(key);
		return value;
	}
	
	public void generateVideos(String inputDir,String outDir) 
	{
		FFmpeg ffmpeg = null;
		FFprobe ffprobe = null;
		try 
		{
			ffmpeg = new FFmpeg("C:\\ffmpeg\\bin\\ffmpeg.exe");
			ffprobe = new FFprobe("C:\\ffmpeg\\bin\\ffprobe.exe");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//TODO: MAKE IT WORK FOR 1 VIDEO
		Video video = new Video("Big_Rabbit","480p","mkv");
		ArrayList<Video> temp = new ArrayList<Video>();
		temp = video.SearchByName(missing_videos);
		for(Video item: temp)
		{
			//System.out.println(item.showVideoDetails());
			FFmpegBuilder builder = new FFmpegBuilder()
					.setInput(inputDir + video.getName() + "-" + video.getResolution() + "." + video.getFormat())
					.overrideOutputFiles(true)
					.addOutput(outDir + item.getName() + "-" + item.getFormat() + "." + item.getResolution())
					.setVideoResolution(getValueMapper(item), item.getNumericalResolution())
					.done();
			FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
			executor.createJob(builder).run();
			log.debug("Transcoding finished for video:" + item.getName() + "-" + item.getFormat() + "." + item.getResolution());
		}
	}
}