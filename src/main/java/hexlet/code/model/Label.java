package hexlet.code.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "labels")
public class Label {

    @Id
    @SequenceGenerator(name = "label_seq",
            sequenceName = "label_seq",
            initialValue = 3,
            allocationSize = 5)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "label_seq")
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;

}
