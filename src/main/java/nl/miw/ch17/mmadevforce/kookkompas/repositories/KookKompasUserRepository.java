package nl.miw.ch17.mmadevforce.kookkompas.repositories;

import nl.miw.ch17.mmadevforce.kookkompas.model.KookKompasUser;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author MMA Dev Force
 */
public interface KookKompasUserRepository extends JpaRepository<KookKompasUser, Long> {
    Optional<KookKompasUser> findByUsername(String username);

    boolean existsByUsername(String username);

}
