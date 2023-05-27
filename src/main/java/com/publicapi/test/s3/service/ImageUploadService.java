package com.publicapi.test.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    public String uploadImage(MultipartFile image) {
        String imageName = changedImageName();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());
        try {
            amazonS3Client.putObject(bucket, imageName, image.getInputStream(), metadata);
            CannedAccessControlList acl = CannedAccessControlList.PublicRead;
            amazonS3Client.setObjectAcl(bucket, imageName, acl);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("이미지 업로드 중 문제가 발생했습니다.");
        }

        return amazonS3Client.getUrl(bucket, imageName).toString();
    }

    private String changedImageName() {
        return UUID.randomUUID().toString();
    }
}
