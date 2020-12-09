package com.samplesecurity.service;

import com.samplesecurity.domain.EmailAuth;
import com.samplesecurity.repository.EmailAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
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
        String authCode = getCode();
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

    public void remove(String email) {
        emailAuthRepository.findByEmail(email)
                .ifPresent(item -> emailAuthRepository.delete(item));
    }

    private String getCode() {
        String numbers = "01234567890";
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String symbols = "!@#$%^&";

        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        int pickedIndex;
        int randomChoice;
        int numberCount = 0;
        int alphabetCount = 0;
        int symbolCount = 0;
        int codeLength = 12;
        char character;

        while(stringBuilder.length() < codeLength) {
            randomChoice = random.nextInt(3);
            if (randomChoice == 0) {
                if (numberCount < 4) {
                    pickedIndex = random.nextInt(numbers.length());
                    character = numbers.charAt(pickedIndex);
                    stringBuilder.append(character);
                    numberCount++;
                }
            } else if (randomChoice == 1) {
                if (alphabetCount < 4) {
                    pickedIndex = random.nextInt(alphabet.length());
                    character = alphabet.charAt(pickedIndex);
                    stringBuilder.append(character);
                    alphabetCount++;
                }
            } else {
                if (symbolCount < 4) {
                    pickedIndex = random.nextInt(symbols.length());
                    character = symbols.charAt(pickedIndex);
                    stringBuilder.append(character);
                    symbolCount++;
                }
            }
        }
        return stringBuilder.toString();
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
}
