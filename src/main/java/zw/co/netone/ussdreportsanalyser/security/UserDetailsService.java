package zw.co.netone.ussdreportsanalyser.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zw.co.netone.ussdreportsanalyser.repository.UserRepository;

@Service
public record UserDetailsService(UserRepository userRepository) implements
        org.springframework.security.core.userdetails.UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found."));
    }
}
