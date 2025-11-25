package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.dto.NewKookKompasUserDTO;
import nl.miw.ch17.mmadevforce.kookkompas.model.KookKompasUser;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.KookKompasUserRepository;
import nl.miw.ch17.mmadevforce.kookkompas.service.mappers.KookKompasUserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MMA Dev Force
 */
@Service
public class KookKompasUserService implements UserDetailsService {

    private final KookKompasUserRepository kookKompasUserRepository;
    private final PasswordEncoder passwordEncoder;

    public KookKompasUserService(KookKompasUserRepository kookKompasUserRepository, PasswordEncoder passwordEncoder) {
        this.kookKompasUserRepository = kookKompasUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public KookKompasUser getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return kookKompasUserRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Ingelogde gebruiker niet gevonden"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return kookKompasUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found."));
    }

    public void saveUser(KookKompasUser kookKompasUser) {
        kookKompasUser.createShoppingListIfMissing();
        kookKompasUser.setPassword(passwordEncoder.encode(kookKompasUser.getPassword()));
        kookKompasUserRepository.save(kookKompasUser);
    }

    public List<KookKompasUser> getAllUsers() {
        return kookKompasUserRepository.findAll();
    }

    public boolean usernameInUse(String username) {
        return kookKompasUserRepository.existsByUsername(username);
    }

    public KookKompasUser save(KookKompasUser kookKompasUser) {
        return kookKompasUserRepository.save(kookKompasUser);
    }

    public void save(NewKookKompasUserDTO userDtoToBeSaved) {
        saveUser(KookKompasUserMapper.fromDTO(userDtoToBeSaved));
    }
}
