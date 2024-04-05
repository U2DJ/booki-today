package booki_today.persistence.entity.regular_moim.moim_record;

import booki_today.domain.regular_moim.GoalStatus;
import booki_today.global.annotation.Association;
import booki_today.persistence.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "moim_record")
@Entity
public class MoimRecordJpaEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moim_record_id")
    private long id;

    @Association
    private long moimMeetingId;

    @Association
    private long memberId;

    private String todayILearned;

    @Enumerated(EnumType.STRING)
    private GoalStatus goalStatus;

    protected MoimRecordJpaEntity() {
    }

    @Builder
    private MoimRecordJpaEntity(final long moimMeetingId, final long memberId,
                                final String todayILearned, final GoalStatus goalStatus) {
        this.moimMeetingId = moimMeetingId;
        this.memberId = memberId;
        this.todayILearned = todayILearned;
        this.goalStatus = goalStatus;
    }
}
