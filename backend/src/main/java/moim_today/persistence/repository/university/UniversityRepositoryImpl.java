package moim_today.persistence.repository.university;

import moim_today.global.error.NotFoundException;
import moim_today.persistence.entity.university.UniversityJpaEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static moim_today.global.constant.exception.UniversityExceptionConstant.UNIVERSITY_EMAIL_NOT_FOUND;
import static moim_today.global.constant.exception.UniversityExceptionConstant.UNIVERSITY_NOT_FOUND;

@Repository
public class UniversityRepositoryImpl implements UniversityRepository {

    private final UniversityJpaRepository universityJpaRepository;

    public UniversityRepositoryImpl(final UniversityJpaRepository universityJpaRepository) {
        this.universityJpaRepository = universityJpaRepository;
    }

    @Override
    public UniversityJpaEntity save(final UniversityJpaEntity universityJpaEntity) {
        return universityJpaRepository.save(universityJpaEntity);
    }

    @Override
    public UniversityJpaEntity getByName(final String universityName) {
        return universityJpaRepository.findByUniversityName(universityName)
                .orElseThrow(() -> new NotFoundException(universityName+UNIVERSITY_NOT_FOUND.message()));
    }

    @Override
    public UniversityJpaEntity getByEmail(final String email) {
        return universityJpaRepository.findByUniversityEmail(email)
                .orElseThrow(() -> new NotFoundException(UNIVERSITY_EMAIL_NOT_FOUND.message()));
    }

    @Override
    public boolean validateUniversityEmail(final String universityEmail) {
        return universityJpaRepository.existsByUniversityEmail(universityEmail);
    }

    @Override
    public Optional<UniversityJpaEntity> findByName(final String universityName) {
        return universityJpaRepository.findByUniversityName(universityName);
    }

    @Override
    public List<UniversityJpaEntity> findAll() {
        return universityJpaRepository.findAll();
    }

    @Override
    public Optional<UniversityJpaEntity> findById(final long id) {
        return universityJpaRepository.findById(id);
    }
}
