package zw.co.netone.ussdreportsanalyser.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name ="subscriber")
public class Subscriber extends RootEntity {

    @Column(name ="msisdn", nullable = false , unique = true, length=30)
    private String msisdn;

    @Column(name= "passphrase")
    private String passphrase;

    @Column(name= "pin")
    private String pin;

    @Column(name= "source")
    private String source;

    private boolean isAccountLocked;

    @Enumerated(EnumType.STRING)
    private zw.co.netone.ussdreportsanalyser.enums.RegistrationStatus status;
    private int attempts;





}








