package nus.cloud.laughelevator.videomanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import com.google.api.gax.paging.Page;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;


@RestController
public class HelloController {
	Storage  storage = StorageOptions.getDefaultInstance().getService();
    @RequestMapping("/")
    public String index() {
    	
    	
        return "Greetings from Spring Boot! and this ms is for Videomanagement Micro Service.";
    }
    
    private OutputStream outputStream;
    
    
    //Working code
    private void crateBucket(){
    	Bucket bucket = storage.create(BucketInfo.of("directed-hulling-3898"));
    	Page<Blob> getBlobs = bucket.list();
    	for (Blob blob : getBlobs.iterateAll()) {
    		System.out.println(blob);
    	}
    }
    
    private void getVideoFromCloudAndSaveLocally(){
    	try{
    	byte videosBytes [] = readBlobFromStringsWithGeneration("directed-hulling-3899","SampleVideo_1280x720_1mb.mp4",42);
    	
    	File targetFile = new File("C:\\Users\\Mohan\\Desktop\\CloudComputing\\success.mp4");
    	OutputStream outStream = new FileOutputStream(targetFile);
    	outStream.write(videosBytes);
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
    
    public byte[] readBlobFromStringsWithGeneration(String bucketName, String blobName,
    		long blobGeneration) {
    	byte[] content = storage.readAllBytes(bucketName, blobName);
    	return content;
    }
    

    public static final boolean SERVE_USING_BLOBSTORE_API = false;
    
    @RequestMapping(value="/getvideoone", method=RequestMethod.GET, 
    		consumes = {MediaType.ALL_VALUE},
    		produces = {MediaType.IMAGE_JPEG_VALUE})
    public void getVideosOne( HttpServletRequest req, HttpServletResponse res) {

    	System.out.println("========>"+req.getContentLength()+"===="+req.getContentType());
    	try {
    		System.out.println("Done!");
    		//sendVideosFromCloud(req,res);
    		getVideoFromCloud(req,res, "movie1.mp4");
    		System.out.println("Done!---2");

    	} catch (Exception e) 
    	{ e.printStackTrace();}

    }
    
    @RequestMapping(value="/getvideotwo", method=RequestMethod.GET, 
    		consumes = {MediaType.ALL_VALUE},
    		produces = {MediaType.IMAGE_JPEG_VALUE})
    public void getVideosTwo( HttpServletRequest req, HttpServletResponse res) {

    	System.out.println("========>"+req.getContentLength()+"===="+req.getContentType());
    	try {
    		System.out.println("Done!");
    		//sendVideosFromCloud(req,res);
    		getVideoFromCloud(req,res, "movie2.mp4");
    		System.out.println("Done!---2");

    	} catch (Exception e) 
    	{ e.printStackTrace();}

    }
    
    @RequestMapping(value="/getvideothree", method=RequestMethod.GET, 
    		consumes = {MediaType.ALL_VALUE},
    		produces = {MediaType.IMAGE_JPEG_VALUE})
    public void getVideosThree( HttpServletRequest req, HttpServletResponse res) {

    	System.out.println("========>"+req.getContentLength()+"===="+req.getContentType());
    	try {
    		System.out.println("Done!");
    		//sendVideosFromCloud(req,res);
    		getVideoFromCloud(req,res, "movie3.mp4");
    		System.out.println("Done!---2");

    	} catch (Exception e) 
    	{ e.printStackTrace();}

    }
    
    
    private void getVideoFromCloud(HttpServletRequest req, HttpServletResponse resp, String filename){
    	OutputStream out = null;
    	try{
    		out = resp.getOutputStream();
    		//File file = new File("C:\\Users\\Mohan\\Desktop\\CloudComputing\\success.mp4");
    		byte videosBytes [] = readBlobFromStringsWithGeneration("directed-hulling-3899",filename,42);
    		resp.setContentType("video/mp4");
    		//resp.set
    		resp.setContentLength((int)videosBytes.length);
    		resp.addHeader("content-disposition", "attachment; filename=\"" + filename +"\"");
    		resp.addHeader("filename", "SampleVideo1.mp4");
    		
    			out.write(videosBytes);
    		
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		
    		try {
    			if(out != null){
    				out.close();
    			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    

}
