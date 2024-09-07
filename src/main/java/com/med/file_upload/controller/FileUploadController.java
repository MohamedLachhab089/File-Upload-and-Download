package com.med.file_upload.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

@RestController
public class FileUploadController {

    // Serve the HTML form for file upload
    @GetMapping("/upload")
    public String getUploadForm() {
        // Return the HTML form for file upload
        return "<html><body>" +
                "<h1>Upload a File</h1>" +
                "<form action='/upload' method='post' enctype='multipart/form-data'>" +
                "Choose file: <input type='file' name='file'><br><br>" +
                "<button type='submit'>Upload File</button>" +
                "</form></body></html>";
    }

    // File upload
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String sanitizedFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        File convertFile = new File("C:/Users/admin/Desktop/safe_upload/" + sanitizedFileName);
        convertFile.createNewFile();
        try (FileOutputStream fout = new FileOutputStream(convertFile)) {
            fout.write(file.getBytes());
        }
        return "File uploaded successfully";
    }

    //  File Download
    @GetMapping("/download")
    public ResponseEntity<Object> downloadFile() throws IOException {
        String filename = "C:/prof.png";
        File file = new File(filename);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        String mimeType = Files.probeContentType(file.toPath());
        mimeType = (mimeType == null) ? "application/octet-stream" : mimeType;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }
}
