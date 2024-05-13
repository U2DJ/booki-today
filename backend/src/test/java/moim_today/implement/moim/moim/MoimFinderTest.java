package moim_today.implement.moim.moim;

import moim_today.dto.moim.moim.MoimMemberResponse;
import moim_today.dto.moim.moim.MoimDateResponse;
import moim_today.global.error.BadRequestException;
import moim_today.global.error.NotFoundException;
import moim_today.persistence.entity.member.MemberJpaEntity;
import moim_today.persistence.entity.moim.joined_moim.JoinedMoimJpaEntity;
import moim_today.persistence.entity.moim.moim.MoimJpaEntity;
import moim_today.util.ImplementTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.time.LocalDate;

import static moim_today.global.constant.exception.MoimExceptionConstant.MOIM_CAPACITY_ERROR;
import static moim_today.global.constant.exception.MoimExceptionConstant.MOIM_NOT_FOUND_ERROR;
import static moim_today.util.TestConstant.*;
import static org.assertj.core.api.Assertions.*;

class MoimFinderTest extends ImplementTest {

    @Autowired
    private MoimFinder moimFinder;

    private Random random = new Random();

    private final int MOIM_MEMBER_SIZE = 3;

    @DisplayName("모임을 조회하면 모임 엔티티를 반환한다.")
    @Test
    void getByIdTest() {
        //given
        MoimJpaEntity moimJpaEntity = MoimJpaEntity.builder()
                .title(MOIM_TITLE.value())
                .build();

        moimRepository.save(moimJpaEntity);

        //when
        MoimJpaEntity findMoimJpaEntity = moimFinder.getById(moimJpaEntity.getId());

        //then
        assertThat(findMoimJpaEntity).isExactlyInstanceOf(MoimJpaEntity.class);
        assertThat(findMoimJpaEntity.getTitle()).isEqualTo(MOIM_TITLE.value());
    }

    @DisplayName("getById로 모임을 조회할 때, 해당하는 모임이 없으면 예외를 발생시킨다.")
    @Test
    void getByIdThrowExceptionTest() {
        //when & then
        assertThatThrownBy(() -> moimFinder.getById(Long.parseLong(MOIM_ID.value())))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(MOIM_NOT_FOUND_ERROR.message());
    }

    @DisplayName("모임에 참여한 멤버들만 MoimMemberResponse로 반환한다")
    @Test
    void findJoinedMoimsTest() {
        // given1
        MemberJpaEntity savedMember1 = saveRandomMember();
        MemberJpaEntity savedMember2 = saveRandomMember();
        MemberJpaEntity savedMember3 = saveRandomMember();
        MemberJpaEntity savedMember4 = saveRandomMember();

        // given2
        MoimJpaEntity savedMoim = saveMoim(savedMember1.getId());

        // given3
        JoinedMoimJpaEntity saveJoinedMoim1 = saveJoinedMoim(savedMoim.getId(), savedMember1.getId());
        JoinedMoimJpaEntity saveJoinedMoim2 = saveJoinedMoim(savedMoim.getId(), savedMember2.getId());
        JoinedMoimJpaEntity saveJoinedMoim3 = saveJoinedMoim(savedMoim.getId(), savedMember3.getId());


        // when
        List<JoinedMoimJpaEntity> joinedMoimMembers = moimFinder.findJoinedMoims(savedMoim.getId());

        List<Long> moimMemberIds = new ArrayList<>();
        joinedMoimMembers.stream().forEach(m -> moimMemberIds.add(m.getMemberId()));

        // then
        assertThat(joinedMoimMembers.size()).isEqualTo(MOIM_MEMBER_SIZE);
        assertThat(joinedMoimMembers.get(random.nextInt(MOIM_MEMBER_SIZE)).getMemberId()).isIn(savedMember1.getId(),
                savedMember2.getId(),
                savedMember3.getId()
        );
        assertThat(savedMember4.getId()).isNotIn(moimMemberIds);
    }

    @DisplayName("모임에 참여한 멤버들만 조회한다")
    @Test
    void findMoimMembersTest() {
        // given1
        MemberJpaEntity savedMember1 = saveRandomMember();
        MemberJpaEntity savedMember2 = saveRandomMember();
        MemberJpaEntity savedMember3 = saveRandomMember();
        MemberJpaEntity savedMember4 = saveRandomMember();

        // given2
        MoimJpaEntity savedMoim = saveMoim(savedMember1.getId());

        // given3
        List<JoinedMoimJpaEntity> joinedMoimJpaEntities = new ArrayList<>();
        joinedMoimJpaEntities.add(saveJoinedMoim(savedMoim.getId(), savedMember1.getId()));
        joinedMoimJpaEntities.add(saveJoinedMoim(savedMoim.getId(), savedMember2.getId()));
        joinedMoimJpaEntities.add(saveJoinedMoim(savedMoim.getId(), savedMember3.getId()));

        // when
        List<MoimMemberResponse> moimMembers = moimFinder.findMembersInMoim(joinedMoimJpaEntities, savedMoim.getMemberId());

        List<Long> moimMemberIds = new ArrayList<>();
        moimMembers.stream().forEach(m -> moimMemberIds.add(m.memberId()));

        // then
        assertThat(moimMembers.size()).isEqualTo(MOIM_MEMBER_SIZE);
        assertThat(moimMembers.get(random.nextInt(MOIM_MEMBER_SIZE)).memberId()).isIn(savedMember1.getId(),
                savedMember2.getId(),
                savedMember3.getId()
        );
        assertThat(savedMember4.getId()).isNotIn(moimMemberIds);
    }

    @DisplayName("멤버가 모임의 호스트인지 검사하는 테스트")
    @Test
    void isHost() {
        // given1
        MoimJpaEntity moimJpaEntity = MoimJpaEntity.builder()
                .memberId(1L)
                .build();

        MoimJpaEntity savedMoim = moimRepository.save(moimJpaEntity);

        List<JoinedMoimJpaEntity> joinedMoimJpaEntities = new ArrayList<>();

        // given2
        JoinedMoimJpaEntity joinedMoimJpaEntity1 = JoinedMoimJpaEntity.builder()
                .memberId(savedMoim.getMemberId())
                .moimId(savedMoim.getId())
                .build();
        JoinedMoimJpaEntity joinedMoimJpaEntity2 = JoinedMoimJpaEntity.builder()
                .memberId(2L)
                .moimId(savedMoim.getId())
                .build();
        JoinedMoimJpaEntity joinedMoimJpaEntity3 = JoinedMoimJpaEntity.builder()
                .memberId(3L)
                .moimId(savedMoim.getId())
                .build();

        joinedMoimJpaEntities.add(joinedMoimRepository.save(joinedMoimJpaEntity1));
        joinedMoimJpaEntities.add(joinedMoimRepository.save(joinedMoimJpaEntity2));
        joinedMoimJpaEntities.add(joinedMoimRepository.save(joinedMoimJpaEntity3));

        // expected
        assertThat(moimFinder.isHost(1L, savedMoim.getId())).isTrue();
        assertThat(moimFinder.isHost(2L, savedMoim.getId())).isFalse();
    }

    @DisplayName("모임 id로 모임명을 가져온다.")
    @Test
    void getTitleById() {
        // given
        MoimJpaEntity moimJpaEntity = MoimJpaEntity.builder()
                .title(MOIM_TITLE.value())
                .build();

        moimRepository.save(moimJpaEntity);

        // when
        String title = moimFinder.getTitleById(moimJpaEntity.getId());

        // then
        assertThat(title).isEqualTo(MOIM_TITLE.value());
    }

    @DisplayName("모임 id로 모임 기간을 가져온다.")
    @Test
    void findMoimDate() {
        // given
        MoimJpaEntity moimJpaEntity = MoimJpaEntity.builder()
                .startDate(LocalDate.of(2024, 3, 4))
                .endDate(LocalDate.of(2024, 6, 30))
                .build();

        moimRepository.save(moimJpaEntity);

        // when
        MoimDateResponse moimDateResponse = moimFinder.findMoimDate(moimJpaEntity.getId());

        // then
        assertThat(moimDateResponse.startDate()).isEqualTo(LocalDate.of(2024, 3, 4));
        assertThat(moimDateResponse.endDate()).isEqualTo(LocalDate.of(2024, 6, 30));
    }

    @DisplayName("모임에 여석이 있는지 검사하고, 꽉 차면 에러를 발생시킨다.")
    @Test
    void validateCapacityThrowError() {
        // given1
        MemberJpaEntity member1 = saveRandomMember();
        MemberJpaEntity member2 = saveRandomMember();
        MemberJpaEntity member3 = saveRandomMember();
        long hostId = member1.getId();

        // given2
        MoimJpaEntity moimJpaEntity1 = MoimJpaEntity.builder()
                .memberId(hostId)
                .capacity(1)
                .build();

        MoimJpaEntity moimJpaEntity2 = MoimJpaEntity.builder()
                .memberId(hostId)
                .capacity(3)
                .build();

        MoimJpaEntity savedMoim1 = moimRepository.save(moimJpaEntity1);
        MoimJpaEntity savedMoim2 = moimRepository.save(moimJpaEntity2);

        // given3
        JoinedMoimJpaEntity jm1 = saveJoinedMoim(savedMoim1.getId(), hostId);
        JoinedMoimJpaEntity jm2 = saveJoinedMoim(savedMoim1.getId(), member2.getId());
        JoinedMoimJpaEntity jm3 = saveJoinedMoim(savedMoim1.getId(), member3.getId());
        JoinedMoimJpaEntity jm4 = saveJoinedMoim(savedMoim2.getId(), hostId);
        JoinedMoimJpaEntity jm5 = saveJoinedMoim(savedMoim2.getId(), member2.getId());
        JoinedMoimJpaEntity jm6 = saveJoinedMoim(savedMoim2.getId(), member3.getId());

        // expected
        assertThatThrownBy(() -> moimFinder.validateCapacity(savedMoim1))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(MOIM_CAPACITY_ERROR.message());
        assertThatThrownBy(() -> moimFinder.validateCapacity(savedMoim2))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(MOIM_CAPACITY_ERROR.message());
    }

    @DisplayName("모임에 여석이 있는지 검사하고, 여석이 있으면 에러를 발생시키지 않는다.")
    @Test
    void validateCapacityDoesNotThrowError() {
        // given1
        MemberJpaEntity member1 = saveRandomMember();
        MemberJpaEntity member2 = saveRandomMember();
        MemberJpaEntity member3 = saveRandomMember();
        long hostId = member1.getId();

        // given2
        MoimJpaEntity moimJpaEntity1 = MoimJpaEntity.builder()
                .memberId(hostId)
                .capacity(4)
                .build();

        MoimJpaEntity moimJpaEntity2 = MoimJpaEntity.builder()
                .memberId(hostId)
                .capacity(100)
                .build();

        MoimJpaEntity savedMoim1 = moimRepository.save(moimJpaEntity1);
        MoimJpaEntity savedMoim2 = moimRepository.save(moimJpaEntity2);

        // given3
        JoinedMoimJpaEntity jm1 = saveJoinedMoim(savedMoim1.getId(), hostId);
        JoinedMoimJpaEntity jm2 = saveJoinedMoim(savedMoim1.getId(), member2.getId());
        JoinedMoimJpaEntity jm3 = saveJoinedMoim(savedMoim1.getId(), member3.getId());
        JoinedMoimJpaEntity jm4 = saveJoinedMoim(savedMoim2.getId(), hostId);
        JoinedMoimJpaEntity jm5 = saveJoinedMoim(savedMoim2.getId(), member2.getId());
        JoinedMoimJpaEntity jm6 = saveJoinedMoim(savedMoim2.getId(), member3.getId());


        // expected
        assertThatCode(() -> moimFinder.validateCapacity(savedMoim1))
                .doesNotThrowAnyException();
        assertThatCode(() -> moimFinder.validateCapacity(savedMoim2))
                .doesNotThrowAnyException();
    }

    private MemberJpaEntity saveRandomMember() {
        MemberJpaEntity memberJpaEntity = MemberJpaEntity.builder()
                .username(USERNAME + String.valueOf(random.nextInt(10)))
                .build();

        return memberRepository.save(memberJpaEntity);
    }

    private MoimJpaEntity saveMoim(final long hostMemberId) {
        MoimJpaEntity moimJpaEntity = MoimJpaEntity.builder()
                .memberId(hostMemberId)
                .build();

        return moimRepository.save(moimJpaEntity);
    }

    private JoinedMoimJpaEntity saveJoinedMoim(final long moimId, final long joinMemberId) {
        JoinedMoimJpaEntity joinedMoimJpaEntity = JoinedMoimJpaEntity.builder()
                .memberId(joinMemberId)
                .moimId(moimId)
                .build();

        return joinedMoimRepository.save(joinedMoimJpaEntity);
    }
}