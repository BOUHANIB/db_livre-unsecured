package ma.emsi.db_livre.repositories;

import ma.emsi.db_livre.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCase(String username);

    @Query("select u from User u"
            + " left join fetch u.authorities"
            + " where u.username = :username")
    User findByUsername(String username);

    @Query("select u from User u"
            + " left join fetch u.authorities"
            + " where u.email = :email")
    User findByEmail(String email);

}
