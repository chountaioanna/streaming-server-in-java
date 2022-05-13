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
	
	private static ArrayList<Video> videosList = new ArrayList<Video>();
	
	private static Map<Integer, Integer> resolution_map = new HashMap<>();
	
	public VideoFormatter(ArrayList<Video> videos)
	{
		videosList.addAll(videos);
		resolution_map.put(1080,1920);
		resolution_map.put(720,1280);
		resolution_map.put(480,640);
		resolution_map.put(360,480);
		resolution_map.put(240,426);
	}
	
	//TODO: complete ffmpwgwrapper
	
	public void generateVideos() 
	{
		String inputDir = System.getProperty("user.dir") + "/videos/";
		String outDir = System.getProperty("user.dir") + "/videos/";
		FFmpeg ffmpeg = null;
		FFprobe ffprobe = null;
		
		try {
			log.debug("Initialising FFMpegClient");
			ffmpeg = new FFmpeg("C:\\ffmpeg\\bin\\ffmpeg.exe");
			ffprobe = new FFprobe("C:\\ffmpeg\\bin\\ffprobe.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(Video video: videosList)
		{
			log.debug("Creating the transcoding");
			FFmpegBuilder builder = new FFmpegBuilder()
					.setInput(inputDir + "Earth-360p.avi")
					.overrideOutputFiles(true)
					.addOutput(outDir + "Earth-360p")
					.setFormat("mp4")
					.setVideoResolution(640, 480)
					.done();
			
			log.debug("Creating the executor");
			FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
			log.debug("Starting the transcoding");
			executor.createJob(builder).run();
			log.debug("Transcoding finished for video: " + video.getName());
		}
	}
}