package nl.miw.ch17.mmadevforce.kookkompas.service;

import nl.miw.ch17.mmadevforce.kookkompas.model.Image;
import nl.miw.ch17.mmadevforce.kookkompas.repositories.ImageRepository;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * @author MMA Dev Force
 * Managing functionalities from the images
 */

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void saveImage(MultipartFile file) throws IOException {

        MediaType contentType = MediaType.IMAGE_JPEG;
        if (file.getContentType() != null) {
            contentType = MediaType.parseMediaType(file.getContentType());
        }

        Image image = new Image();

        String fileName = file.getOriginalFilename();
        if (imageRepository.existsByFileName(fileName)) {
            fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        }

        image.setFileName(fileName);
        image.setContentType(contentType);
        image.setData(file.getBytes());

        imageRepository.save(image);
    }

    public Image getImage(String fileName) {
        return imageRepository.findByFileName(fileName)
                .orElseThrow(() -> new NoSuchElementException(fileName));
    }

    public void saveImage(ClassPathResource imageResource) throws IOException {
        Image image = new Image();
        image.setFileName(imageResource.getFilename());
        image.setContentType(MediaType.IMAGE_JPEG);
        image.setData(imageResource.getInputStream().readAllBytes());
        imageRepository.save(image);
    }
}
