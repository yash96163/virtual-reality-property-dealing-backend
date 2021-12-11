package com.hashedin.virtualproperty.application.service;

import com.google.cloud.storage.*;
import com.hashedin.virtualproperty.application.exceptions.CustomException;
import com.hashedin.virtualproperty.application.response.FileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileStorageService {

    @Autowired
    AuthService authService;

    private final String bucketName = "virtual-reality";
    private Storage storage;
    private Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    public FileStorageService() {
        String projectId = "hu18-groupa-angular";
        this.storage = StorageOptions.newBuilder()
                .setProjectId(projectId).build().getService();
    }

    public FileResponse storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new CustomException("Invalid File");
        }
        if (!file.getContentType().startsWith("image")) {
            throw new CustomException("Please use image files only");
        }
        //generated unique name using system clock
        String fileName = "IMG" + System.currentTimeMillis() + this.getExtension(file.getOriginalFilename());
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        Blob b = storage.create(blobInfo, file.getBytes());
        BlobId id = b.getBlobId();
        this.logger.info("STORED FILE WITH NAME " + fileName);
        return new FileResponse(b.getMediaLink(), id.getGeneration().toString(), id.getName());
    }

    public void deleteFile(FileResponse file) throws Exception {
        // will delete in future
        BlobId id = BlobId.of(bucketName, file.name, Long.parseLong(file.id));
        this.logger.info("DELETING IMAGE WITH ID: " + file.id);
        if (!storage.delete(id)) {
            throw new CustomException("Failed to delete image with id " + file.id);
        }
    }

    private String getExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = "." + fileName.substring(i + 1);
        }
        return extension;
    }
}
