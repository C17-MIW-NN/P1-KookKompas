package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.model.KookKompasUser;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.KookKompasUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author MMA Dev Force
 */
@Service
public class KookKompasUserService implements UserDetailsService {

    private final KookKompasUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public KookKompasUserService(KookKompasUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found."));
    }

    public void saveUser(KookKompasUser kookKompasUser) {
        kookKompasUser.setPassword(passwordEncoder.encode(kookKompasUser.getPassword()));
        repository.save(kookKompasUser);
    }

}
