package moim_today.application.meeting.meeting_comment;

import moim_today.dto.meeting.meeting_comment.MeetingCommentCreateRequest;
import moim_today.dto.meeting.meeting_comment.MeetingCommentResponse;
import moim_today.dto.meeting.meeting_comment.MeetingCommentUpdateRequest;
import moim_today.implement.meeting.meeting.MeetingFinder;
import moim_today.implement.meeting.meeting_comment.MeetingCommentAppender;
import moim_today.implement.meeting.meeting_comment.MeetingCommentFinder;
import moim_today.implement.meeting.meeting_comment.MeetingCommentRemover;
import moim_today.implement.meeting.meeting_comment.MeetingCommentUpdater;
import moim_today.implement.moim.joined_moim.JoinedMoimFinder;
import moim_today.persistence.entity.meeting.meeting_comment.MeetingCommentJpaEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeetingCommentServiceImpl implements MeetingCommentService {

    private final MeetingCommentAppender meetingCommentAppender;
    private final MeetingCommentFinder meetingCommentFinder;
    private final MeetingCommentUpdater meetingCommentUpdater;
    private final MeetingCommentRemover meetingCommentRemover;
    private final MeetingFinder meetingFinder;
    private final JoinedMoimFinder joinedMoimFinder;

    public MeetingCommentServiceImpl(final MeetingCommentAppender meetingCommentAppender,
                                     final MeetingCommentFinder meetingCommentFinder,
                                     final MeetingCommentUpdater meetingCommentUpdater,
                                     final MeetingCommentRemover meetingCommentRemover,
                                     final MeetingFinder meetingFinder,
                                     final JoinedMoimFinder joinedMoimFinder) {
        this.meetingCommentAppender = meetingCommentAppender;
        this.meetingCommentFinder = meetingCommentFinder;
        this.meetingCommentUpdater = meetingCommentUpdater;
        this.meetingCommentRemover = meetingCommentRemover;
        this.meetingFinder = meetingFinder;
        this.joinedMoimFinder = joinedMoimFinder;
    }

    @Override
    public void createMeetingComment(final long memberId, final MeetingCommentCreateRequest meetingCommentCreateRequest) {
        long moimId = meetingFinder.getMoimIdByMeetingId(meetingCommentCreateRequest.meetingId());
        joinedMoimFinder.validateMemberInMoim(memberId, moimId);
        meetingCommentAppender.createMeetingComment(memberId, meetingCommentCreateRequest);
    }

    @Override
    public List<MeetingCommentResponse> findAllByMeetingId(final long memberId, final long meetingId) {
        long moimId = meetingFinder.getMoimIdByMeetingId(meetingId);
        joinedMoimFinder.validateMemberInMoim(memberId, moimId);
        return meetingCommentFinder.findAllByMeetingId(meetingId);
    }

    @Transactional
    @Override
    public void updateMeetingComment(final long memberId, final MeetingCommentUpdateRequest meetingCommentUpdateRequest) {
        long meetingCommentId = meetingCommentUpdateRequest.meetingCommentId();
        MeetingCommentJpaEntity meetingCommentJpaEntity = meetingCommentFinder.getById(meetingCommentId);
        meetingCommentJpaEntity.validateMember(memberId);
        meetingCommentUpdater.updateMeetingComment(meetingCommentId, meetingCommentUpdateRequest);
    }

    @Override
    public void deleteMeetingComment(final long memberId, final long meetingCommentId) {
        MeetingCommentJpaEntity meetingCommentJpaEntity = meetingCommentFinder.getById(meetingCommentId);
        meetingCommentJpaEntity.validateMember(memberId);
        meetingCommentRemover.deleteById(meetingCommentId);
    }
}