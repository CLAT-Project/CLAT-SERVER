package team_project.clat.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import team_project.clat.domain.Token;
import team_project.clat.dto.response.CommonResultResDTO;
import team_project.clat.dto.request.EmailReqDTO;
import team_project.clat.dto.request.JoinReqDTO;
import team_project.clat.dto.response.JoinResDTO;
import team_project.clat.dto.response.JoinResultResDTO;
import team_project.clat.repository.TokenRepository;
import team_project.clat.service.EmailService;
import team_project.clat.service.JoinService;

import team_project.clat.jwt.JwtUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Controller
@ResponseBody
@RequiredArgsConstructor
@Slf4j
public class JoinController {

    private final JoinService joinService;
    private final EmailService emailService;

    @PostMapping("/idCheck")
    public ResponseEntity<CommonResultResDTO> isDuplicateUsername(@RequestBody JoinReqDTO joinReqDTO){
        return joinService.existsByUsername(joinReqDTO);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<CommonResultResDTO> getEmailForVerification(@RequestBody EmailReqDTO.EmailForVerificationRequest request){
        LocalDateTime requestedAt = LocalDateTime.now();
        emailService.sendSimpleVerificationMail(request.getEmail(), requestedAt);
        CommonResultResDTO commonResultResDTO = new CommonResultResDTO("200 OK", "메일이 발송되었습니다.");
        return new ResponseEntity<>(commonResultResDTO, HttpStatus.OK);
    }

    @PostMapping("/verification-code")
    public ResponseEntity<CommonResultResDTO> verificationByCode(@RequestBody EmailReqDTO.VerificationCodeRequest request) {
        LocalDateTime requestedAt = LocalDateTime.now();
        emailService.verifyCode(request.getEmail(), request.getCode(), requestedAt);
        CommonResultResDTO commonResultResDTO = new CommonResultResDTO("200 OK", "인증이 완료되었습니다.");
        return new ResponseEntity<>(commonResultResDTO, HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(@Valid @RequestPart JoinReqDTO joinReqDTO,
                                                    BindingResult bindingResult,
                                                    @RequestPart MultipartFile file,
                                                    HttpServletResponse response) throws IOException {

        if(bindingResult.hasErrors()){
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            CommonResultResDTO commonResultResDTO = new CommonResultResDTO("400 BAD_REQUEST", errorMessage);
            return new ResponseEntity<>(commonResultResDTO, HttpStatus.BAD_REQUEST);
        }

        JoinResDTO joinResDTO = joinService.joinProcess(joinReqDTO, file);
        if(joinResDTO==null){
            CommonResultResDTO commonResultResDTO = new CommonResultResDTO("409 Conflict", "중복된 ID가 존재합니다.");
            return new ResponseEntity<>(commonResultResDTO, HttpStatus.CONFLICT);
        }

        //응답 설정
        response.setHeader("access", joinResDTO.getAccess());
        response.setHeader(HttpHeaders.SET_COOKIE, createCookie("refresh", joinResDTO.getRefresh()).toString());
        response.setStatus(HttpStatus.OK.value());

        return new ResponseEntity<>(joinResDTO.getJoinResultResDTO(), HttpStatus.OK);
    }

    private ResponseCookie createCookie(String key, String value) {

        return ResponseCookie
                .from(key, value)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .maxAge(24*60*60)
                .sameSite("None")
                .build();

    }
}
