package com.samplesecurity.controller;

import com.samplesecurity.dto.Board.AttachFileDto;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class FileController {
    @PostMapping("/uploadFile")
    @ResponseBody
    public ResponseEntity<List<AttachFileDto>> uploadFile(MultipartFile[] uploadFile) {
        log.info("upload!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String uploadFolder = "C:\\upload";
        String uploadFolderPath = getFolder();
        File uploadPath = new File(uploadFolder, uploadFolderPath);
        List<AttachFileDto> list = new ArrayList<>();
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        for (MultipartFile multipartFile : uploadFile) {
            AttachFileDto attachFileDto = new AttachFileDto();
            log.info("Upload File Name: " + multipartFile.getOriginalFilename());
            String uploadFileName = multipartFile.getOriginalFilename();
            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);//IE 일때
            attachFileDto.setFileName(uploadFileName);

            UUID uuid = UUID.randomUUID();
            uploadFileName = uuid.toString() + "_" + uploadFileName;

            try {
                File saveFile = new File(uploadPath, uploadFileName);
                multipartFile.transferTo(saveFile);
                attachFileDto.setUuid(uuid.toString());
                attachFileDto.setUploadPath(uploadFolderPath);
                list.add(attachFileDto);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/display")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(String fileName) {
        File file = new File("C:\\upload\\" + fileName);
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
    @ResponseBody
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
                downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
            } else if (userAgent.contains("Edge")) {
                downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
            } else {
                downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
            }
            headers.add("Content-Disposition", "attachment; filename=" + downloadName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @DeleteMapping("/deleteFile")
    @ResponseBody
    public ResponseEntity<String> deleteFile(String fileName, String type) {
        log.info("deleteFile: " + fileName);
        File file;
        file = new File("C:\\upload\\" + URLDecoder.decode(fileName, StandardCharsets.UTF_8));
        file.delete();

        String largeFileName = file.getAbsolutePath().replace("s_", "");
        log.info("largeFileName: " + largeFileName);
        file = new File(largeFileName);
        file.delete();
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

    private String getFolder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String str = sdf.format(date);
        return str.replace("-", File.separator);
    }

}
