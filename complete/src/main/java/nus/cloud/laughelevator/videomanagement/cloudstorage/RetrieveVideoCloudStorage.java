package nus.cloud.laughelevator.videomanagement.cloudstorage;


import org.springframework.stereotype.Service;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class RetrieveVideoCloudStorage {

	public void retriveVideos(){
		// Instantiates a client
		Storage  storage = StorageOptions.getDefaultInstance().getService();

		// The name for the new bucket
		//byte[] picByte = Base64.decodeBase64(pic);
		//GcsFileOptions instance = GcsFileOptions.getDefaultInstance();

		try{
			Bucket bucket = storage.create(BucketInfo.of("directed-hulling-3899"));
			
			Page<Blob> getBlobs = bucket.list();
			for (Blob blob : getBlobs.iterateAll()) {
			  // do something with the blob
			}

			System.out.printf("=========Done==========");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
