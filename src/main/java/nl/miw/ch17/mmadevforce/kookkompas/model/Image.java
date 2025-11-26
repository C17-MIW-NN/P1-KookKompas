package nl.miw.ch17.mmadevforce.kookkompas.model;

import jakarta.persistence.*;
import org.springframework.http.MediaType;

/**
 * @author MMA Dev Force
 * Information regarding images
 */

@Entity
public class Image {

    @Id @GeneratedValue
    private Long imageId;

    @Column(unique = true)
    private String fileName;

    private String contentType;

    @Lob
    private byte[] data;

    @Override
    public String toString() {
        return "Image{" +
                "id=" + imageId +
                ", fileName='" + fileName + '\'' +
                ", contentType=" + contentType +
                '}';
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MediaType getContentType() {
        return MediaType.parseMediaType(contentType);
    }

    public void setContentType(MediaType contentType) {
        this.contentType = contentType.toString();
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
