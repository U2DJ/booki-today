package moim_today.presentation.member;

import moim_today.application.member.MemberService;
import moim_today.domain.member.MemberSession;
import moim_today.dto.member.MemberProfileResponse;
import moim_today.dto.member.PasswordRecoverRequest;
import moim_today.dto.member.PasswordUpdateRequest;
import moim_today.dto.member.ProfileUpdateRequest;
import moim_today.global.annotation.Login;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/password-recovery")
    public void recoverPassword(@RequestBody final PasswordRecoverRequest passwordRecoverRequest) {
        memberService.recoverPassword(passwordRecoverRequest);
    }

    @PatchMapping("/password")
    public void updatePassword(@Login final MemberSession memberSession,
                               @RequestBody final PasswordUpdateRequest passwordUpdateRequest) {
        memberService.updatePassword(memberSession, passwordUpdateRequest);
    }

    @GetMapping("/profile")
    public MemberProfileResponse getMemberProfile(@Login final MemberSession memberSession) {
        return memberService.getMemberProfile(memberSession);
    }

    @PatchMapping("/profile")
    public void updateProfile(@Login final MemberSession memberSession,
                              @RequestBody final ProfileUpdateRequest profileUpdateRequest) {
        memberService.updateProfile(memberSession.id(), memberSession.universityId(), profileUpdateRequest);
    }
}