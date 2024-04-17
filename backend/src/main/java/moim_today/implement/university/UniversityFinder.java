package moim_today.implement.university;

import moim_today.dto.university.UniversityInfoResponse;
import moim_today.global.annotation.Implement;
import moim_today.persistence.entity.university.UniversityJpaEntity;
import moim_today.persistence.repository.university.UniversityRepository;

import java.util.List;
import java.util.Optional;

@Implement
public class UniversityFinder {

    private final UniversityRepository universityRepository;

    public UniversityFinder(final UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public List<UniversityInfoResponse> getAllUniversity() {
        List<UniversityJpaEntity> universityJpaEntities = universityRepository.findAll();
        return universityJpaEntities.stream()
                .map(UniversityInfoResponse::of)
                .toList();
    }

    public Optional<UniversityJpaEntity> findByName(String schoolName){
        return universityRepository.findByName(schoolName);
    }
}