package kg.mkk.gov.contestinfoservice.contestParser;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Entity
@Getter
@Setter
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String url;
    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "contest_tags", joinColumns = @JoinColumn(name = "contest_id"))
    @MapKeyColumn(name = "tag_key")
    @Column(name = "tag_value")
    @Enumerated(EnumType.STRING)
    private Map<ETag, String> tags;


}
