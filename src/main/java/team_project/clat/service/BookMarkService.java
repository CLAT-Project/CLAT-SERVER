package team_project.clat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team_project.clat.domain.BookMark;
import team_project.clat.domain.Member;
import team_project.clat.domain.Message;
import team_project.clat.dto.response.MessageBookMarkResDTO;
import team_project.clat.exception.DuplicateException;
import team_project.clat.exception.NotFoundException;
import team_project.clat.repository.BookMarkRepository;
import team_project.clat.repository.MessageRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final MessageRepository messageRepository;



    @Transactional
    public MessageBookMarkResDTO markBook(Long messageId, Member member) {  //북마크 생성
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new NotFoundException("해당 메시지가 없습니다"));

        validationBookMark(messageId, member);
        BookMark saveBookMark = bookMarkRepository.save(new BookMark(member, message));

        return new MessageBookMarkResDTO(saveBookMark.getId(), message.getId());
    }


    private void validationBookMark(Long messageId, Member member) {  //회원 북마크 검증
        boolean flag = bookMarkRepository.existsByMemberIdAndMessageId(member.getId(), messageId);

        if(flag){
            throw new DuplicateException("해당 멤버는 해당 메시지에 북마크 등록 상태입니다");
        }
    }


}
