package com.tourease.hotel.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.hotel.models.entities.Hotel;
import com.tourease.hotel.models.entities.Image;
import com.tourease.hotel.repositories.HotelRepository;
import com.tourease.hotel.repositories.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.tourease.hotel.utils.WebpImageConverter.convertToWebP;

@Service
@AllArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final HotelRepository hotelRepository;
    private static final String BASE_URL = "/hotel-service";


    public Image saveImage(MultipartFile image, Long hotelId){
        Hotel hotel = hotelRepository.findById(hotelId).get();

        try {
            byte[] bytes = convertToWebP(ImageIO.read(image.getInputStream()));
            Image imageEntity = imageRepository.save(Image.builder()
                    .type("webp")
                    .name(image.getOriginalFilename())
                    .image(bytes)
                    .hotel(hotel)
                    .build());
            imageEntity.setUrl(getImageUrl(imageEntity.getId()));

            return imageRepository.save(imageEntity);

        } catch (IOException e) {
            throw new CustomException(e.getMessage(), ErrorCode.Failed);
        }

    }

    public byte[] getImage(Long imageId) {
        Optional<Image> image = imageRepository.findById(imageId);

        if (image.isPresent()) {
            return image.get().getImage();
        }

        return new byte[0];
    }

    public List<String> getImages(Long hotelId){
        List<Image> images = imageRepository.findByHotel_Id(hotelId);
        return images.stream().map(Image::getUrl).toList();
    }

    private String getImageUrl(Long imageId) {
        return BASE_URL + "/hotel/image/" + imageId;
    }

    public void deleteImage(Long imageId) {
        imageRepository.deleteById(imageId);
    }
}
