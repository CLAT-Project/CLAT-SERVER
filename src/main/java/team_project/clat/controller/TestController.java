package team_project.clat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import team_project.clat.domain.Member;
import team_project.clat.repository.MemberRepository;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final MemberRepository memberRepository;

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        Member member = memberRepository.findByName("디카페인");

        if (member != null) {
            memberRepository.delete(member);
            return ResponseEntity.ok("Member '디카페인' has been successfully deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Member '디카페인' not found.");
        }

    }
}
