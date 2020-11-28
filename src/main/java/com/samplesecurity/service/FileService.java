package com.samplesecurity.service;

import com.samplesecurity.dto.Board.AttachFileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    private final static String rootDirectory = "C:\\upload";

    public AttachFileDto uploadSingleFile(MultipartFile uploadFile) {
        String directoryPathByDate = getDirectoryPathByDate();
        File uploadPath = new File(rootDirectory, directoryPathByDate);

        createDirectory(uploadPath);

        String fileName = uploadFile.getOriginalFilename();

        String uuid = UUIDGenerator();
        fileName = uuid + "_" + fileName;

        File saveFile = new File(uploadPath, fileName);

        AttachFileDto attachFileDTO = AttachFileDto.builder()
                .fileName(fileName)
                .uploadPath(directoryPathByDate)
                .uuid(uuid)
                .build();

        try {
            uploadFile.transferTo(saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attachFileDTO;
    }

    public List<AttachFileDto> uploadFiles(MultipartFile[] uploadFiles) {
        List<AttachFileDto> listOfAttachments = new ArrayList<>();

        String directoryPathByDate = getDirectoryPathByDate();
        File uploadPath = new File(rootDirectory, directoryPathByDate);

        createDirectory(uploadPath);

        for (MultipartFile multipartFile : uploadFiles) {
            String fileName = multipartFile.getOriginalFilename();

            String uuid = UUIDGenerator();
            fileName = uuid + "_" + fileName;

            File saveFile = new File(uploadPath, fileName);

            AttachFileDto attachFileDTO = AttachFileDto.builder()
                    .fileName(fileName)
                    .uploadPath(directoryPathByDate)
                    .uuid(uuid)
                    .build();
            try {
                multipartFile.transferTo(saveFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            listOfAttachments.add(attachFileDTO);
        }
        return listOfAttachments;
    }

    private String getDirectoryPathByDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateFormat = sdf.format(date);
        return dateFormat.replace("-", File.separator);
    }

    private void createDirectory(File path) {
        if (!path.exists()) {
            path.mkdirs();
        }
    }

    private String UUIDGenerator() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
