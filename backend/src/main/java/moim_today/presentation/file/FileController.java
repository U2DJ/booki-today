package moim_today.presentation.file;

import moim_today.application.file.FileService;
import moim_today.domain.member.MemberSession;
import moim_today.dto.file.FileDeleteRequest;
import moim_today.dto.file.FileInfoResponse;
import moim_today.global.annotation.Login;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/files")
    public FileInfoResponse uploadFile(@Login final MemberSession memberSession,
                                       @RequestPart final MultipartFile file) {
        return fileService.uploadFile(memberSession, file);
    }

    @DeleteMapping("/files")
    public void deleteFile(@Login final MemberSession memberSession,
                           @RequestBody final FileDeleteRequest fileDeleteRequest){
        fileService.deleteFile(memberSession, fileDeleteRequest);
    }
}