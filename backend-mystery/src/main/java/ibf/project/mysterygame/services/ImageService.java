package ibf.project.mysterygame.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;
import ibf.project.mysterygame.models.game.MysteryCharacter;

@Service
public class ImageService {

    @Value("${digital.ocean.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3;


    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private static final String contentType = "image/png";
    private final String placeHolderCoverImgUrl = "https://final-project.sgp1.digitaloceanspaces.com/placeholder%20setting.png";
    private final String placeHolderCharacterImgUrl = "https://final-project.sgp1.digitaloceanspaces.com/placeholder%20character.png";

    

    public String generateSingleImage(ImageModel model, String prompt, String description) {
        try {
            Response<Image> response = model.generate(prompt);
            return uploadImageToS3(response.content().base64Data(), description);
        } catch (Exception e) {
            logger.error("Failed to generate cover image");
            // set placeholder url
            return placeHolderCoverImgUrl;
        }
    }

    public List<MysteryCharacter> generateCharacterImages(List<MysteryCharacter> characters, String artStyle, ImageModel model) {
        characters.forEach(character -> {
            String description = character.getImageDescription();
            String prompt = String.format("Generate an image in the style of %s with the description of %s", artStyle, description);
            try {
                Response<Image> response = model.generate(prompt);
                String base64Image = response.content().base64Data();
                String imgUrl = uploadImageToS3(base64Image, description);
                character.setImageUrl(imgUrl);
            } catch (Exception e) {
                // set placeholder url
                character.setImageUrl(placeHolderCharacterImgUrl);
                logger.error("Failed to generate image for character: " + character.getName(), e.getMessage());
            }
        });
        
        return characters;
    }

    public String uploadImageToS3(String base64Image, String description) {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        String imgUrl = "";
        try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {

            Map<String, String> userData = new HashMap<>();
            Date timestamp = new Date();
            userData.put("upload-timestamp", timestamp.toString());
            userData.put("description", description);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(imageBytes.length);
            metadata.setUserMetadata(userData);

            String key = UUID.randomUUID().toString().substring(0, 8);
            PutObjectRequest putReq = new PutObjectRequest(bucketName, key, inputStream, metadata);
            putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putReq);
            imgUrl =  s3.getUrl(bucketName, key).toString();
            
        } catch (Exception e) {
            logger.error("Error while uploading image to AmazonS3", e.getMessage(), e);
        }

        return imgUrl;
    }

    // test
    public String getImageFromS3(String key) {
        return s3.getUrl(bucketName, key).toString();
    }

}
