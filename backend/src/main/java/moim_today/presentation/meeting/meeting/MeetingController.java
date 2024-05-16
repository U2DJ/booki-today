package moim_today.presentation.meeting.meeting;

import moim_today.application.meeting.meeting.MeetingService;
import moim_today.domain.meeting.enums.MeetingStatus;
import moim_today.domain.member.MemberSession;
import moim_today.dto.meeting.MeetingCreateResponse;
import moim_today.dto.meeting.meeting.MeetingCreateRequest;
import moim_today.dto.meeting.meeting.MeetingDetailResponse;
import moim_today.dto.meeting.meeting.MeetingSimpleResponse;
import moim_today.global.annotation.Login;
import moim_today.global.response.CollectionResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/meetings")
@RestController
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(final MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    public MeetingCreateResponse createMeeting(@RequestBody final MeetingCreateRequest meetingCreateRequest) {
        return meetingService.createMeeting(meetingCreateRequest);
    }

    @GetMapping("/{moimId}")
    public CollectionResponse<List<MeetingSimpleResponse>> findAllByMoimId(
            @Login final MemberSession memberSession,
            @PathVariable final long moimId,
            @RequestParam final MeetingStatus meetingStatus) {
        List<MeetingSimpleResponse> meetingSimpleResponses =
                meetingService.findAllByMoimId(moimId, memberSession.id(), meetingStatus);
        return CollectionResponse.of(meetingSimpleResponses);
    }

    @GetMapping("/detail/{meetingId}")
    public MeetingDetailResponse findDetailsByMoimId(@PathVariable final long meetingId) {
        return meetingService.findDetailsById(meetingId);
    }

    @DeleteMapping("/{meetingId}")
    public void deleteMeeting(@Login final MemberSession memberSession,
                              @PathVariable final long meetingId) {
        meetingService.deleteMeeting(memberSession.id(), meetingId);
    }
}
