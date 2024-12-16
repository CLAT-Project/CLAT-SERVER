package team_project.clat.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import team_project.clat.exception.MailVerifyException;
import team_project.clat.repository.VerificationCodeRepository;

import java.io.File;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    @Value("${spring.mail.username")
    private String serviceEmail;

    private final JavaMailSender mailSender;
    private final VerificationCodeRepository verificationCodeRepository;

    public void sendSimpleVerificationMail(String to, LocalDateTime sentAt) {

        String empty = verificationCodeRepository.findByTo(to).orElse("empty");
        Optional<VerificationCode> byCode = verificationCodeRepository.findByCode(empty);

        if(byCode.isPresent()){
            throw new MailVerifyException("이미 메일이 발송되었습니다.");
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(serviceEmail);
        mailMessage.setTo(to);
        mailMessage.setSubject(String.format("Email Verification For %s", to));


        VerificationCode verificationCode = new VerificationCode();
        String text = verificationCode.generateCodeMessage();


        verificationCodeRepository.save(verificationCode);
        verificationCodeRepository.toSave(to, text);

        mailMessage.setText(text);

        mailSender.send(mailMessage);
    }

    public void verifyCode(String to, String code, LocalDateTime verifiedAt) {
        VerificationCode verificationCode = verificationCodeRepository.findByCode(code)
                .orElseThrow(() -> new MailVerifyException("인증코드 올바르지않습니다."));

        if (verificationCode.isExpired(verifiedAt)) {
            verificationCodeRepository.remove(verificationCode);
            verificationCodeRepository.toRemove(to);
            throw new MailVerifyException("인증코드가 만료되었습니다.");
        }

        verificationCodeRepository.toRemove(to);
        verificationCodeRepository.remove(verificationCode);
    }

    public void sendEmailWithAttachment(String subject, String text, String attachmentPath) throws MessagingException{
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo("clat.supprt@gmail.com");
            helper.setSubject(subject);
            helper.setText(text);

            if (attachmentPath != null) {
                FileSystemResource file = new FileSystemResource(new File(attachmentPath));
                helper.addAttachment(file.getFilename(), file);
            }

            mailSender.send(message);
            log.info("메일 발송이 완료되었습니다.");
        } catch (MessagingException e) {
            log.error("메일 발송 중 오류 발생", e);
        }
    }

    public String sendFindPasswordMail(String to, LocalDateTime sentAt) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(serviceEmail);
        mailMessage.setTo(to);
        mailMessage.setSubject(String.format("CLAT 비밀번호 찾기 서비스 For %s", to));

        String tempPassword = generatePassword();
        mailMessage.setText(tempPassword);

        mailSender.send(mailMessage);

        return tempPassword;
    }

    public static String generatePassword() {
        String LOWER = "abcdefghijklmnopqrstuvwxyz";
        String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String DIGITS = "0123456789";
        String SPECIAL = "!@#$%^&*()-_=+<>?";

        String ALL_CHARACTERS = LOWER + UPPER + DIGITS + SPECIAL;

        SecureRandom random = new SecureRandom();  // 보안 강한 난수 생성기
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 14; i++) {
            int index = random.nextInt(ALL_CHARACTERS.length());
            password.append(ALL_CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
