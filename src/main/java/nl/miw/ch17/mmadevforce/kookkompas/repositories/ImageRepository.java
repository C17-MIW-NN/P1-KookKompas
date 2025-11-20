package nl.miw.ch17.mmadevforce.kookkompas.repositories;

import nl.miw.ch17.mmadevforce.kookkompas.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author MMA Dev Force
 * Doel methode
 */
public interface ImageRepository extends JpaRepository <Image, Long> {
    boolean existsByFileName(String fileName);
    Optional<Image> findByFileName(String fileName);
}
