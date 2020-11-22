package com.samplesecurity.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class UploadFile {
    @Id
    @GeneratedValue
    private Long id;

    private String fileName;                //예류.jpg

    private String saveFileName;            //uuid예류.jpg

    private String filePath;                // D:/image/uuid예류.jpg

    private String contentType;             // image/jpeg

    private long size;                      // 4476873 (byte)

    private LocalDateTime registerDate;


}
