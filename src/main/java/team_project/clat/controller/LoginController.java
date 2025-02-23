package team_project.clat.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team_project.clat.dto.request.JoinReqDTO;
import team_project.clat.dto.request.LoginReqDTO;
import team_project.clat.dto.response.CommonResultResDTO;
import team_project.clat.dto.response.SocialLoginResDTO;
import team_project.clat.service.LoginService;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<CommonResultResDTO> login(){
        CommonResultResDTO commonResultResDTO = new CommonResultResDTO("200 OK", "로그인이 완료되었습니다.");
        return new ResponseEntity<>(commonResultResDTO, HttpStatus.OK);
    }

    @PostMapping("/social-redirect")
    public ResponseEntity<?> socialRedirect(@RequestBody LoginReqDTO loginReqDTO, HttpServletResponse response){
        SocialLoginResDTO socialLoginResDTO = loginService.socialLogin(loginReqDTO);

        //응답 설정
        response.setHeader("access", socialLoginResDTO.getAccess());
        response.setHeader(HttpHeaders.SET_COOKIE, createCookie("refresh", socialLoginResDTO.getRefresh()).toString());
        response.setStatus(HttpStatus.OK.value());

        return new ResponseEntity<>(HttpStatus.OK);
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
