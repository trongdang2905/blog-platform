package com.group2.blogplatform.service;


import com.group2.blogplatform.exception.ExcessImageException;
import com.group2.blogplatform.exception.WrongTypeImageException;
import io.imagekit.client.ImageKitClient;

import io.imagekit.models.files.FileUploadParams;
import io.imagekit.models.files.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ImageKitService {

    private final ImageKitClient imageKitClient;

    public String uploadImage(MultipartFile image) throws IOException {

        if (image.isEmpty()) {
            return "";
        }

        if (!image.isEmpty() && !image.getContentType().equals("image/*")) {
            throw new WrongTypeImageException("Wrong image type");
        }

        if (image.getSize() > 25 * 1024 * 1024) {
            throw new ExcessImageException("Image size is not exceed 25 MB");
        }

        byte[] imageBytes = image.getBytes();
        FileUploadParams params = FileUploadParams.builder()
                .file(imageBytes)
                .fileName(LocalDate.now().toString())
                .build();

        FileUploadResponse response = imageKitClient.files().upload(params);
        return response.url().get().toString();
    }


}
