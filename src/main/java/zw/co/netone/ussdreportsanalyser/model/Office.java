package zw.co.netone.ussdreportsanalyser.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.envers.Audited;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "offices")
@Audited(targetAuditMode = NOT_AUDITED, withModifiedFlag = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Office extends  RootEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

}
