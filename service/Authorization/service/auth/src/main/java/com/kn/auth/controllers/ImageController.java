package com.kn.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kn.auth.responses.ErrorResponse;
import com.kn.auth.responses.ResponseWrapper;
import com.kn.auth.services.MinioService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import io.minio.errors.MinioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/private/images")
@Tag(name = "Media", description = "Image and files uploading and downloading with minio")
public class ImageController {

    @Autowired
    private MinioService minioService;

    @PostMapping(value = "/upload")
    @Operation(summary = "Uploading image or multipart file", description = "Uploading file and checking if it's image in TODO")
    public ResponseEntity<ResponseWrapper<String>> upload(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseWrapper<String>(minioService.upload(file), null));
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | MinioException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper<String>(null,
                    ErrorResponse.builder()
                            .error(e.getMessage())
                            .exceptionMessage(e
                                    .getLocalizedMessage())
                            .column("file")
                            .build()));
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<String> getUrl(@PathVariable String fileName) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(minioService.getUrl(fileName));
        } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalArgumentException | IOException | MinioException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
    }
}
