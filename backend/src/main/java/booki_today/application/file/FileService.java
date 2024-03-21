package booki_today.application.file;

import booki_today.dto.file.FileAddRequest;
import booki_today.dto.file.FileDeleteRequest;
import booki_today.implement.file.FileRemover;
import booki_today.implement.file.FileUploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    private final FileUploader fileUploader;
    private final FileRemover fileRemover;

    public FileService(final FileUploader fileUploader, final FileRemover fileRemover) {
        this.fileUploader = fileUploader;
        this.fileRemover = fileRemover;
    }

    public void uploadFile(FileAddRequest fileAddRequest, MultipartFile multipartFile){
        fileUploader.uploadFile(fileAddRequest, multipartFile);
    }

    public void deleteFile(FileDeleteRequest fileDeleteRequest){
        fileRemover.deleteFile(fileDeleteRequest);
    }
}
