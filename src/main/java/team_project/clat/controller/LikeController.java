package team_project.clat.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team_project.clat.domain.Like;
import team_project.clat.domain.Member;
import team_project.clat.dto.LikeRequestDTO;
import team_project.clat.dto.LikeResponseDTO;
import team_project.clat.repository.LikeRepository;
import team_project.clat.service.LikeService;
import team_project.clat.service.TokenService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController{

    private final LikeService likeService;
    private final LikeRepository likeRepository;
    private final TokenService tokenService;

    @PostMapping()
    public LikeResponseDTO saveLike(@RequestBody LikeRequestDTO likeRequestDTO, HttpServletRequest request){

        log.info("이모티콘");

        Member findByMember = tokenService.getUsernameFromToken(request);  //멤버 찾음
        likeService.like(findByMember,likeRequestDTO.getMessageId(), likeRequestDTO.getEmoticon());

        List<Like> findLikeLists = likeRepository.findByMessageId(likeRequestDTO.getMessageId());

        return new LikeResponseDTO(findLikeLists);

    }

}
