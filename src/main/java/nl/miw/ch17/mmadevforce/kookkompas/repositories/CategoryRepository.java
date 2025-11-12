package nl.miw.ch17.mmadevforce.kookkompas.repositories;

import nl.miw.ch17.mmadevforce.kookkompas.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository <Category, Long> {

}
