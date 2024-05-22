package kg.mkk.gov.contestinfoservice.contestParser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    Optional<Contest> findByUrl(String url);
}
