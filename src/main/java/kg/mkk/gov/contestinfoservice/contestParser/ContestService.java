package kg.mkk.gov.contestinfoservice.contestParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContestService {

    private final ContestRepository contestRepository;

    @Autowired
    public ContestService(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    public void createContest(Contest contest) {
        contestRepository.save(contest);
    }

}
