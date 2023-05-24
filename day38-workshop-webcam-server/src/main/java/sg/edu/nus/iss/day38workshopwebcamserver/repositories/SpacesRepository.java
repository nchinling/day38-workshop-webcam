package sg.edu.nus.iss.day38workshopwebcamserver.repositories;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

@Repository
public class SpacesRepository {
    
    @Autowired
    private AmazonS3 s3;

    public URL upload(String title, MultipartFile file) throws IOException {

        // Add custom metadata
        Map<String, String> userData = new HashMap<>();
        // userData.put("comments", comments);
        // userData.put("filename", file.getOriginalFilename());
        userData.put("filename", title);
        userData.put("upload-date", (new Date()).toString());
  
        // Add object's metadata 
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(userData);
  
        // Generate a random key name
        String key = "images/%s".formatted(UUID.randomUUID().toString().substring(0, 8));
        String fileTitle = "images/"+title;
  
        // flintstones - bucket name
        // key - key
        // file bytes
        // metadata
        // PutObjectRequest putReq = new PutObjectRequest("flintstones", key, 
        //       file.getInputStream(), metadata);
        PutObjectRequest putReq = new PutObjectRequest("flintstones", fileTitle, 
        file.getInputStream(), metadata);
        // Make the file publically accessible
        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
  
        PutObjectResult result = s3.putObject(putReq);
        System.out.printf(">>> result: %s\n", result);
  
        return s3.getUrl("flintstones", key);
     }

}
