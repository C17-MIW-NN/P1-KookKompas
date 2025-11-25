package nl.miw.ch17.mmadevforce.kookkompas.repositories;

import nl.miw.ch17.mmadevforce.kookkompas.model.Category;
import nl.miw.ch17.mmadevforce.kookkompas.model.KookKompasUser;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository <Category, Long> {
    List<Category> findByCategoryNameAndOwner(String categoryName, KookKompasUser owner);
    List<Category> findByCategoryNameAndPublicVisibleTrue(String categoryName);
    Optional<Category> findByCategoryName(String categoryName);

    List<Category> findByCategoryNameContainingIgnoreCase(String query);

}
