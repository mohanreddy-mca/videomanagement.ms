package nus.cloud.laughelevator.videomanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;


@RestController
public class HelloController {
    
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot! and this ms is for Videomanagement Micro Service.";
    }
    
    private OutputStream outputStream;
    

    public static final boolean SERVE_USING_BLOBSTORE_API = false;
    
    @RequestMapping(value="/getvideos", method=RequestMethod.GET, 
    		consumes = {MediaType.ALL_VALUE},
    		produces = {MediaType.IMAGE_JPEG_VALUE})
    public void getVideosList( HttpServletRequest req, HttpServletResponse res) {
    	
    	System.out.println("========>"+req.getContentLength()+"===="+req.getContentType());
    	  try {

    			System.out.println("Done!");
    			
    			// Send Response
    			sendVideosFromCloud(req,res);
    			System.out.println("Done!---2");

    	  } catch (Exception e) 
    	  { e.printStackTrace();}
    	  
    	  
    	
        //return "Greetings from Spring Boot! and this ms is for Receive Face Detection Images from UI and saves into cloud.";
    }
    
    private void sendVideosFromCloud(HttpServletRequest req, HttpServletResponse resp){

    	try{
    		
    		 GcsFilename fileName = getFileName(req);
    		    if (SERVE_USING_BLOBSTORE_API) {
    		      BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    		      BlobKey blobKey = blobstoreService.createGsBlobKey(
    		          "/gs/" + fileName.getBucketName() + "/" + fileName.getObjectName());
    		      blobstoreService.serve(blobKey, resp);
    		    } else {
    		      GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
    		      copy(Channels.newInputStream(readChannel), resp.getOutputStream());
    		    }
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
    private void sendVideoResponse(HttpServletRequest req, HttpServletResponse resp){

    	try{
    		
    		File file = new File("C:\\Users\\Mohan\\Desktop\\CloudComputing\\sampleVideo.mp4");
    		resp.setContentType(MediaType.ALL_VALUE);
    		resp.setContentLength((int)file.length());

    		FileInputStream in = new FileInputStream(file);
    		OutputStream out = resp.getOutputStream();

    		// Copy the contents of the file to the output stream
    		byte[] buf = new byte[1024];
    		
    	  /*  StringBuilder sb = new StringBuilder();
    	    sb.append("data:video/mp4;base64,");
    	    sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(v_byte, false)));
    	    String videoUrl = sb.toString();
    	    resp.setAttribute("videoUrl",videoUrl );
    		resp.*/
    		
    		int count = 0;
    		while ((count = in.read(buf)) >= 0) {
    			out.write(buf, 0, count);
    		}
    		
    		
    		out.close();
    		in.close();
    		
    		
    				    
    				    
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    /**
     * This is where backoff parameters are configured. Here it is aggressively retrying with
     * backoff, up to 10 times but taking no more that 15 seconds total to do so.
     */
    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
        .initialRetryDelayMillis(10)
        .retryMinAttempts(1)
        .retryMaxAttempts(2)
        .totalRetryPeriodMillis(15000)
        .build());

    /**Used below to determine the size of chucks to read in. Should be > 1kb and < 10MB */
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;


    private GcsFilename getFileName(HttpServletRequest req) {
       /* String[] splits = req.getRequestURI().split("/", 4);
        if (!splits[0].equals("") || !splits[1].equals("gcs")) {
          throw new IllegalArgumentException("The URL is not formed as expected. " +
              "Expecting /gcs/<bucket>/<object>");
        }*/
        return new GcsFilename("directed-hulling-3899", "SampleVideo_1280x720_1mb.mp4");
      }

      /**
       * Transfer the data from the inputStream to the outputStream. Then close both streams.
       */
      private void copy(InputStream in, OutputStream out) throws IOException {
        try {
          byte[] buf = new byte[1024];;
          int count = 0;
  		while ((count = in.read(buf)) >= 0) {
  			out.write(buf, 0, count);
  		}
        } finally {
          in.close();
          out.close();
        }
      }

}
