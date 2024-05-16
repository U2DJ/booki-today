package moim_today.implement.meeting.meeting_comment;

import moim_today.dto.meeting.meeting_comment.MeetingCommentResponse;
import moim_today.persistence.entity.meeting.meeting_comment.MeetingCommentJpaEntity;
import moim_today.persistence.entity.member.MemberJpaEntity;
import moim_today.util.ImplementTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static moim_today.util.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

class MeetingCommentFinderTest extends ImplementTest {

    @Autowired
    private MeetingCommentFinder meetingCommentFinder;
    
    @DisplayName("미팅의 댓글 목록을 조회한다.")
    @Test
    void findAllByMeetingIdTest(){
        // given1
        MemberJpaEntity memberJpaEntity = MemberJpaEntity.builder()
                .username(USERNAME.value())
                .memberProfileImageUrl(PROFILE_IMAGE_URL.value())
                .build();

        memberRepository.save(memberJpaEntity);
        long memberId = memberJpaEntity.getId();

        // given2
        long meetingId = MEETING_ID.longValue();
        long otherMeetingId = meetingId + 1;

        MeetingCommentJpaEntity commentJpaEntityA = MeetingCommentJpaEntity.builder()
                .meetingId(meetingId)
                .memberId(memberId)
                .contents(MEETING_COMMENT_CONTENTS.value())
                .build();

        MeetingCommentJpaEntity commentJpaEntityB = MeetingCommentJpaEntity.builder()
                .meetingId(meetingId)
                .memberId(memberId)
                .contents(MEETING_COMMENT_CONTENTS.value())
                .build();

        MeetingCommentJpaEntity commentJpaEntityC = MeetingCommentJpaEntity.builder()
                .meetingId(otherMeetingId)
                .memberId(memberId)
                .contents(MEETING_COMMENT_CONTENTS.value())
                .build();

        meetingCommentRepository.save(commentJpaEntityA);
        meetingCommentRepository.save(commentJpaEntityB);
        meetingCommentRepository.save(commentJpaEntityC);

        //when
        List<MeetingCommentResponse> findMeetingComments = meetingCommentFinder.findAllByMeetingId(meetingId);

        //then
        assertThat(findMeetingComments.size()).isEqualTo(2);
    }
}