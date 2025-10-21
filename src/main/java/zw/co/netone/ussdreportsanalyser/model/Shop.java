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
@Table(name = "shops")
@Audited(targetAuditMode = NOT_AUDITED, withModifiedFlag = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop extends RootEntity {
    @Column(name = "shop_id", unique = true, nullable = false)
    private String shopId;

    @Column(nullable = false,unique = true, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(length = 20)
    private String zone;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

}
