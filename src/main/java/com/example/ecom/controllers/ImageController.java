package com.example.ecom.controllers;

import com.azure.storage.blob.*;
import com.azure.storage.blob.specialized.BlockBlobClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Value("${app.storage.connectionString}")
    private String connectionString;

    @Value("${app.storage.container}")
    private String containerName;

    @PutMapping(path = "/upload/{name}", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> upload(@PathVariable String name, @RequestBody byte[] body){
        BlobServiceClient svc = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        BlobContainerClient container = svc.getBlobContainerClient(containerName);
        if (!container.exists()) container.create();
        BlockBlobClient blob = container.getBlobClient(name).getBlockBlobClient();
        blob.upload(new ByteArrayInputStream(body), body.length, true);
        Map<String, Object> resp = new HashMap<>();
        resp.put("name", name);
        resp.put("url", blob.getBlobUrl());
        return resp;
    }
}
