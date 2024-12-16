package team_project.clat.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import team_project.clat.domain.Message;
import team_project.clat.domain.QAnswer;
import team_project.clat.domain.QMessage;
import team_project.clat.dto.response.LikeResDTO;
import team_project.clat.dto.response.PageNationMessageResDTO;
import team_project.clat.repository.LikeRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static team_project.clat.domain.QAnswer.answer1;
import static team_project.clat.domain.QMember.member;
import static team_project.clat.domain.QMessage.message1;


public class MessageRepositoryImpl implements MessageRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    private final LikeRepository likeRepository;

    public MessageRepositoryImpl(EntityManager em, LikeRepository likeRepository) {
        this.queryFactory = new JPAQueryFactory(em);
        this.likeRepository = likeRepository;
    }

    @Override
    public Slice<PageNationMessageResDTO> findSliceNooOffSetByMessageAndChatRoom(Long chatRoomId, Long messageId,Pageable pageable) {

        QMessage message = message1;
        QAnswer answer = answer1;

        List<Message> result = queryFactory
                .selectFrom(message)
                .join(message.member, member).fetchJoin()
                .leftJoin(message.answer, answer).fetchJoin()
                .where(message.chatRoom.id.eq(chatRoomId)
                        .and(ltCommentId(messageId)))
                .limit(pageable.getPageSize() + 1)
                .orderBy(message.createdDate.desc())
                .fetch();

        List<Long> longs = result.stream().map(Message::getId).toList();

        Map<Long, List<LikeResDTO>> qslLikes = likeRepository.findByQslLikes(longs);


        List<PageNationMessageResDTO> list = result.stream().map(message3 -> {
            Long findByMessageId = message3.getId();

            List<LikeResDTO> likeResDTOS = qslLikes.getOrDefault(findByMessageId, Collections.emptyList());

            return new PageNationMessageResDTO(message3, likeResDTOS);

        }).collect(Collectors.toList());



        boolean hasNext = false;

        if(result.size() > pageable.getPageSize()){
            hasNext = true;
            list.remove(pageable.getPageSize());
        }


        return new SliceImpl<>(list,pageable,hasNext);


    }

    @Override
    public Slice<PageNationMessageResDTO> findSliceOffSetByMessageAndChatRoom(Long chatRoomId, Pageable pageable) {


        List<Message> result = queryFactory
                .selectFrom(message1)
                .join(message1.member, member).fetchJoin()
                .leftJoin(message1.answer, answer1).fetchJoin()
                .where(message1.chatRoom.id.eq(chatRoomId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(message1.createdDate.desc())
                .fetch();

        List<Long> longs = result.stream().map(Message::getId).toList();

        Map<Long, List<LikeResDTO>> qslLikes = likeRepository.findByQslLikes(longs);


        List<PageNationMessageResDTO> list = result.stream().map(message3 -> {
            Long findByMessageId = message3.getId();

            List<LikeResDTO> likeResDTOS = qslLikes.getOrDefault(findByMessageId, Collections.emptyList());

            return new PageNationMessageResDTO(message3, likeResDTOS);

        }).collect(Collectors.toList());


        boolean hasNext = false;

        if(result.size() > pageable.getPageSize()){
            hasNext = true;
            list.remove(pageable.getPageSize());
        }

        Collections.reverse(list);


        return new SliceImpl<>(list,pageable,hasNext);
    }

    private BooleanExpression ltCommentId(Long messageId) {
        return messageId == null ? null : message1.id.lt(messageId);
    }
}


