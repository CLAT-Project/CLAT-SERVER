package team_project.clat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import team_project.clat.domain.Member;
import team_project.clat.dto.request.FindMemberPasswordReqDTO;
import team_project.clat.exception.UserNotFoundException;
import team_project.clat.repository.MemberRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void findMemberPassword(FindMemberPasswordReqDTO findMemberPasswordReqDTO){

        Member findMember = memberRepository.findByUsername(findMemberPasswordReqDTO.getUsername());
        if(findMember==null){
            throw new UserNotFoundException("존재하지 않는 사용자입니다.");
        }

        String tempPWD = emailService.sendFindPasswordMail(findMemberPasswordReqDTO.getEmail(), LocalDateTime.now());
        findMember.memberPasswordSet(bCryptPasswordEncoder.encode(tempPWD));
        memberRepository.save(findMember);
    }
}
