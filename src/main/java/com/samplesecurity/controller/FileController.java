package com.samplesecurity.controller;

import com.samplesecurity.dto.Board.AttachFileDto;
import com.samplesecurity.dto.ProfileDto;
import com.samplesecurity.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @ResponseBody
    @PostMapping("/uploadFile")
    public ResponseEntity<List<AttachFileDto>> uploadFile(MultipartFile[] uploadFile) {
        List<AttachFileDto> attachFileDto = fileService.uploadFiles(uploadFile);
        return new ResponseEntity<>(attachFileDto, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/uploadProfile")
    public ResponseEntity<ProfileDto> uploadProfileImg(@RequestParam(value = "profileimg") MultipartFile uploadFile) {
        ProfileDto profileDto = fileService.uploadProfile(uploadFile);
        return new ResponseEntity<>(profileDto, HttpStatus.OK);
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {
        File file = new File("C:\\upload\\profile\\" + fileName);
        ResponseEntity<byte[]> result = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) {
        Resource resource = new FileSystemResource("C:\\upload\\" + fileName);
        if (!resource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String resourceName = resource.getFilename();
        String resourceOriginalName = resourceName.substring(resourceName.indexOf("-") + 1);
        HttpHeaders headers = new HttpHeaders();

        try {
            String downloadName = null;
            if (userAgent.contains("Trident")) {
                downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " "); //IE
            } else if (userAgent.contains("Edge")) {
                downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8"); //Edge
            } else {
                downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1"); //chrome
            }
            headers.add("Content-Disposition", "attachment; filename=" + downloadName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }


}
