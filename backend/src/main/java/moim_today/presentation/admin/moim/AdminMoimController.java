package moim_today.presentation.admin.moim;

import moim_today.application.admin.moim.AdminMoimService;
import moim_today.dto.moim.moim.MoimSimpleResponse;
import moim_today.global.response.CollectionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/admin")
@RestController
public class AdminMoimController {

    private final AdminMoimService adminMoimService;

    public AdminMoimController(final AdminMoimService adminMoimService) {
        this.adminMoimService = adminMoimService;
    }

    // todo AdminSession 적용
    @GetMapping("/moims")
    public CollectionResponse<List<MoimSimpleResponse>> findAllByUniversityId(final long universityId) {
        List<MoimSimpleResponse> moimSimpleResponses = adminMoimService.findAllByUniversityId(universityId);
        return CollectionResponse.from(moimSimpleResponses);
    }
}
