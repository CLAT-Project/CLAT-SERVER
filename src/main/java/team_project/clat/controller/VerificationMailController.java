package team_project.clat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import team_project.clat.dto.request.EmailReqDTO;
import team_project.clat.dto.response.CommonResultResDTO;
import team_project.clat.service.EmailService;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Controller
@ResponseBody
public class VerificationMailController { // 이메일 인증을 위한 컨트롤러
  private final EmailService verificationMailService;

  @PostMapping("/verify-email")
  public ResponseEntity<CommonResultResDTO> getEmailForVerification(@RequestBody EmailReqDTO.EmailForVerificationRequest request){
    LocalDateTime requestedAt = LocalDateTime.now();
    verificationMailService.sendSimpleVerificationMail(request.getEmail(), requestedAt);
    CommonResultResDTO commonResultResDTO = new CommonResultResDTO("200 OK", "메일이 발송되었습니다.");
    return new ResponseEntity<>(commonResultResDTO, HttpStatus.OK);
  }

  @PostMapping("/verification-code")
  public ResponseEntity<CommonResultResDTO> verificationByCode(@RequestBody EmailReqDTO.VerificationCodeRequest request) {
    LocalDateTime requestedAt = LocalDateTime.now();
    verificationMailService.verifyCode(request.getEmail(), request.getCode(), requestedAt);
    CommonResultResDTO commonResultResDTO = new CommonResultResDTO("200 OK", "인증이 완료되었습니다.");
    return new ResponseEntity<>(commonResultResDTO, HttpStatus.OK);
  }
}