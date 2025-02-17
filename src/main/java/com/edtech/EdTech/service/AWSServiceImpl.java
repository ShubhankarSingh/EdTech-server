package com.edtech.EdTech.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.net.URL;

@Service
public class AWSServiceImpl {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucket-name}")
    private String s3BucketName;

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSServiceImpl.class);

    public String uploadFile(String fileName, byte[] fileData){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileData.length);

        s3Client.putObject(s3BucketName, fileName, new ByteArrayInputStream(fileData), metadata);
        LOGGER.info(String.format("Object added to S3 bucket %s", s3Client.getUrl(s3BucketName, fileName).toString()));
        return s3Client.getUrl(s3BucketName, fileName).toString();
    }

    public String getFileUrl(String fileName){
        URL fileUrl = s3Client.getUrl(s3BucketName, fileName);
        return fileUrl.toString();
    }

}
