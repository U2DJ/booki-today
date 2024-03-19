package booki_today.implement.file;

import booki_today.dto.file.FileAddRequest;
import booki_today.global.annotation.Implement;
import booki_today.global.error.FileUploadException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Implement
@Slf4j
public class FileUploader {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public FileUploader(final AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") final String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public void uploadFile(final FileAddRequest fileAddRequest) {
        String uploadFilePath = fileAddRequest.uploadFilePath();
        MultipartFile multipartFile = fileAddRequest.multipartFile();
        List<FileAddRequest> s3files = new ArrayList<>();

        String originalFileName = multipartFile.getOriginalFilename();
        String uploadFileName = getUuidFileName(originalFileName);
        String uploadFileUrl = "";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {

            String keyName = uploadFilePath + "/" + uploadFileName;

            amazonS3.putObject(
                    new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            uploadFileUrl = amazonS3.getUrl(bucketName, keyName).toString();

        } catch (IOException e) {
            throw new FileUploadException("파일 업로드에 실패했습니다.");
        }
    }

    public String getUuidFileName(final String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID() + "." + ext;
    }
}
