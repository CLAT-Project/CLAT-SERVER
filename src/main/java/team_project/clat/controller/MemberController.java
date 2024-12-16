package team_project.clat.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team_project.clat.domain.Member;
import team_project.clat.dto.request.FindMemberPasswordReqDTO;
import team_project.clat.dto.request.MemberReqDTO;
import team_project.clat.dto.response.CommonResultResDTO;
import team_project.clat.jwt.JwtUtil;
import team_project.clat.repository.MemberRepository;
import team_project.clat.service.MemberService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<MemberReqDTO> findMember(HttpServletRequest request){
        String accessToken = request.getHeader("access");
        String username = jwtUtil.getUsername(accessToken);
        String userType = jwtUtil.getUserType(accessToken);

        Member findMember = memberRepository.findByUsername(username);

        MemberReqDTO memberReqDTO = new MemberReqDTO(username, findMember.getName(), findMember.getSchoolName(), userType);

        return new ResponseEntity<>(memberReqDTO, HttpStatus.OK);
    }

    @PostMapping("/member/findPwd")
    public ResponseEntity<?> findMemberPassword(@RequestBody FindMemberPasswordReqDTO findMemberPasswordReqDTO){
        memberService.findMemberPassword(findMemberPasswordReqDTO);
        CommonResultResDTO commonResultResDTO = new CommonResultResDTO("200 OK", "임시 비밀번호 발급이 완료되었습니다.");
        return new ResponseEntity<>(commonResultResDTO, HttpStatus.OK);
    }
}
