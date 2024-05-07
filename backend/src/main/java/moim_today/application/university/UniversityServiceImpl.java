package moim_today.application.university;

import moim_today.dto.university.UniversityResponse;
import moim_today.implement.university.UniversityUpdater;
import moim_today.implement.university.UniversityFinder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityServiceImpl implements UniversityService{

    private final UniversityUpdater universityUpdater;
    private final UniversityFinder universityFinder;

    public UniversityServiceImpl(final UniversityUpdater universityUpdater, final UniversityFinder universityFinder) {
        this.universityUpdater = universityUpdater;
        this.universityFinder = universityFinder;
    }

    @Override
    public void fetchAllUniversity() {
        universityUpdater.fetchAllUniversity();
    }

    @Override
    public List<UniversityResponse> getUniversities() {
        return universityFinder.getAllUniversity();
    }
}