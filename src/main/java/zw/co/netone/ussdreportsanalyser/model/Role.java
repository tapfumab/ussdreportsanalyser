package zw.co.netone.ussdreportsanalyser.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Role extends RootEntity {
    private String name;
}
