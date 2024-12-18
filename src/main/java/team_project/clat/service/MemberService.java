package team_project.clat.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team_project.clat.domain.Member;
import team_project.clat.dto.request.FindMemberPasswordReqDTO;
import team_project.clat.dto.response.MemberResDTO;
import team_project.clat.exception.UserNotFoundException;
import team_project.clat.jwt.JwtUtil;
import team_project.clat.repository.MemberRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    public void findMemberPassword(FindMemberPasswordReqDTO findMemberPasswordReqDTO){

        Member findMember = memberRepository.findByUsername(findMemberPasswordReqDTO.getUsername());
        if(findMember==null){
            throw new UserNotFoundException("존재하지 않는 사용자입니다.");
        }

        String tempPWD = emailService.sendFindPasswordMail(findMemberPasswordReqDTO.getEmail(), LocalDateTime.now());
        findMember.memberPasswordSet(bCryptPasswordEncoder.encode(tempPWD));
        memberRepository.save(findMember);
    }

    public MemberResDTO findMember(HttpServletRequest request){
        String accessToken = request.getHeader("access");
        String username = jwtUtil.getUsername(accessToken);
        String userType = jwtUtil.getUserType(accessToken);

        Member findMember = memberRepository.findByUsername(username);

        return new MemberResDTO(findMember.getId(), findMember.getUsername());
    }
}
