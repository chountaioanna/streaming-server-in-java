package lab;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

public class TestFFMpeg {
	
	static Logger log = LogManager.getLogger(TestFFMpeg.class);
	
	public static void main(String[] args) {
		
		String inputDir = System.getProperty("user.dir") + "/videos/";
		String outDir = System.getProperty("user.dir") + "/output/";
		FFmpeg ffmpeg = null;
		FFprobe ffprobe = null;
		
		try {
			log.debug("Initialising FFMpegClient");
			ffmpeg = new FFmpeg("C:\\ffmpeg\\bin\\ffmpeg.exe");
			ffprobe = new FFprobe("C:\\ffmpeg\\bin\\ffprobe.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}

		log.debug("Creating the transcoding");
		FFmpegBuilder builder = new FFmpegBuilder()
				.setInput(inputDir + "Earth-360p.avi")
				.addOutput(outDir + "Earth-360p.mp4")
				.done();
		
		log.debug("Creating the executor");
		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

		log.debug("Starting the transcoding");
		// Run a one-pass encode
		executor.createJob(builder).run();
		log.debug("Transcoding finished");
	}

}
