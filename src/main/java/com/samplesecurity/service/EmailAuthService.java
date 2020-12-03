package com.samplesecurity.service;

import com.samplesecurity.domain.EmailAuth;
import com.samplesecurity.repository.EmailAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final JavaMailSender sender;
    private final EmailAuthRepository emailAuthRepository;

    private static final String FROM_ADDRESS = "java.email.sender020@gmail.com";

    public boolean emailChecker(String email) {
        return emailAuthRepository.findByEmail(email).isPresent();
    }

    public boolean authCodeChecker(String authCode) {
        return emailAuthRepository.findByAuthCode(authCode).isPresent();
    }

    public void storeAuthCode(String email) {
        String authCode = sendAuthCode(email);
        EmailAuth emailAuth = EmailAuth.builder()
                .email(email)
                .authCode(authCode)
                .build();

        emailAuthRepository.save(emailAuth);
    }

    public String sendAuthCode(String email) {
        SimpleMailMessage simpleMailMessage;
        String authCode = "haha1122@@";
        try {
            simpleMailMessage =  new SimpleMailMessage();
            simpleMailMessage.setFrom(FROM_ADDRESS);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("회원가입 인증메일");
            simpleMailMessage.setText("인증 번호 : " + authCode);
            sender.send(simpleMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authCode;
    }

    private String getAuthCode() {
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        int num;
        int size = 6;

        while(stringBuffer.length() < size) {
            num = random.nextInt(10);
            stringBuffer.append(num);
        }
        return stringBuffer.toString();
    }

    private String getCode() {
        UUID uuid = UUID.randomUUID();
        String pieceOfUUID = uuid.toString().substring(0, 8);



        return pieceOfUUID + "!!";
    }
}
