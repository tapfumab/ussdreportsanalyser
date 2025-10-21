package zw.co.netone.ussdreportsanalyser.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Audited(targetAuditMode = NOT_AUDITED, withModifiedFlag = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends RootEntity implements UserDetails {
    private String firstName;
    private String lastName;
    private String email;
    private String username;

    @ManyToOne(optional = false)
    private Shop shop;

    private String cellNumber;

    @ManyToOne(optional = false)
    private Role role;

    @Builder.Default
    private boolean activeStatus = true;

    @Builder.Default
    private boolean accountNonExpired = true;

    @Builder.Default
    private boolean accountNonLocked = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;

    private LocalDateTime lastLoginDate;
    private LocalDateTime passwordChangedDate;
    private Integer failedLoginAttempts = 0;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + getRole().getName()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.activeStatus;
    }
}
