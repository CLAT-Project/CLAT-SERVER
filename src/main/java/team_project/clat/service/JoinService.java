package team_project.clat.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team_project.clat.domain.Enum.UserStatus;
import team_project.clat.domain.Enum.UserType;
import team_project.clat.domain.Member;
import team_project.clat.domain.Token;
import team_project.clat.dto.response.CommonResultResDTO;
import team_project.clat.dto.request.JoinReqDTO;
import team_project.clat.dto.response.JoinResDTO;
import team_project.clat.dto.response.JoinResultResDTO;
import team_project.clat.exception.UsernameDataIntegrityViolationException;
import team_project.clat.jwt.JwtUtil;
import team_project.clat.repository.MemberRepository;
import team_project.clat.repository.TokenRepository;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public ResponseEntity<CommonResultResDTO> existsByUsername(JoinReqDTO joinReqDTO) {
        if(memberRepository.existsByUsername(joinReqDTO.getUsername())){
            CommonResultResDTO commonResultResDTO = new CommonResultResDTO("409 Conflict", "중복된 ID가 존재합니다.");
            return new ResponseEntity<>(commonResultResDTO, HttpStatus.CONFLICT);
        }else {
            CommonResultResDTO commonResultResDTO = new CommonResultResDTO("200 OK", "사용가능한 ID 입니다.");
            return new ResponseEntity<>(commonResultResDTO, HttpStatus.OK);
        }
    }

    public JoinResDTO joinProcess(JoinReqDTO joinReqDTO, MultipartFile file) throws IOException {

        String username = joinReqDTO.getUsername();
        String password = joinReqDTO.getPassword();
        String name = joinReqDTO.getName();
        String schoolName = joinReqDTO.getSchoolName();
        UserType userType = joinReqDTO.getUserType();

        Boolean isExist = memberRepository.existsByUsername(username);

        if(isExist){
            return null;
        }

        String fileDir = "/app/upload/";
        String fullPath = null;
        if(!file.isEmpty()){
            fullPath = fileDir + file.getOriginalFilename();
            file.transferTo(new File(fullPath));
        }

        Member member = Member.memberSet(name, username, bCryptPasswordEncoder.encode(password), schoolName, userType, fullPath, UserStatus.ACTIVE);

        try {
            memberRepository.save(member);
        }catch (DataIntegrityViolationException e){
            throw new UsernameDataIntegrityViolationException("중복된 ID가 존재합니다.");
        }


        //토큰 생성
        String access = jwtUtil.createJwt("access", username, userType.getDescription(), 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, userType.getDescription(), 86400000L);

        Token token = new Token(username, refresh, 86400000L);
        tokenRepository.save(token);

        JoinResultResDTO result = new JoinResultResDTO("200 OK", "회원가입이 완료되었습니다.", name);
        return new JoinResDTO(result, access, refresh);
    }

}
