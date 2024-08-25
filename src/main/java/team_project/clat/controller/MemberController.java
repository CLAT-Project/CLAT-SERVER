package team_project.clat.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team_project.clat.domain.Member;
import team_project.clat.dto.MemberDto;
import team_project.clat.jwt.JwtUtil;
import team_project.clat.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @GetMapping("/members")
    public ResponseEntity<MemberDto> findMember(HttpServletRequest request){
        String accessToken = request.getHeader("access");
        String username = jwtUtil.getUsername(accessToken);
        String userType = jwtUtil.getUserType(accessToken);

        Member findMember = memberRepository.findByUsername(username);

        MemberDto memberDto = new MemberDto(username, findMember.getName(), findMember.getSchoolName(), userType);

        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }
}