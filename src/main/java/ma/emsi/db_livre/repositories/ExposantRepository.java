package ma.emsi.db_livre.repositories;

import ma.emsi.db_livre.entities.Exposant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExposantRepository extends JpaRepository<Exposant,Long> {
    Page<Exposant> findByNomContains(String kw, Pageable pageable);

}
