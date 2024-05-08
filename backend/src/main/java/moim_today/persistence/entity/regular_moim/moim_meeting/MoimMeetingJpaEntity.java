package moim_today.persistence.entity.regular_moim.moim_meeting;

import moim_today.domain.regular_moim.enums.AttendanceStatus;
import moim_today.global.annotation.Association;
import moim_today.global.base_entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Table(name = "moim_meeting")
@Entity
public class MoimMeetingJpaEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_meeting_id")
    private long id;

    @Association
    private long regularMoimId;

    @Association
    private long memberId;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

    protected MoimMeetingJpaEntity() {
    }

    @Builder
    private MoimMeetingJpaEntity(final long regularMoimId, final long memberId, final LocalDateTime startDateTime,
                                 final LocalDateTime endDateTime, final AttendanceStatus attendanceStatus) {
        this.regularMoimId = regularMoimId;
        this.memberId = memberId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.attendanceStatus = attendanceStatus;
    }
}