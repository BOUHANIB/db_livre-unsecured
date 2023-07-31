package ma.emsi.db_livre.service;


import ma.emsi.db_livre.entities.Authority;
import ma.emsi.db_livre.entities.User;
import ma.emsi.db_livre.repositories.UserRepository;
import ma.emsi.db_livre.security.ActiveUserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private ActiveUserStore activeUserStore;
    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) {
        user.setUsername(user.getUsername().trim());
        Authority authority = new Authority();
        authority.setUser(user);
        authority.setAuthority("ROLE_USER");

        user.getAuthorities().add(authority);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        // For new users first login and to check for unsuccessful logouts.
        user.setLastLogout(String.valueOf(LocalDateTime.of(123, 4, 5, 6, 7)));

        userRepository.save(user);
    }

    public User findById(Long userId) {
        Optional<User> findById = userRepository.findById(userId);
        return findById.orElse(null);
    }

    public User findByUserName(String username) {
        Optional<User> findByUsername = userRepository.findByUsernameIgnoreCase(username);
        return findByUsername.orElse(null);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean isLoggedIn(User user) {
        if (user != null) {
            return activeUserStore.getUsers().contains(user.getUsername());
        }
        return false;
    }

    public boolean isAdmin(User user) {
        if (user != null) {
            Set<Authority> authorities = user.getAuthorities();
            for (Authority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateLastLogout(Long userId) {
        User user = findById(userId);
        user.setLastLogout(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm")));
        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}