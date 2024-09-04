package team_project.clat.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import team_project.clat.domain.ChatRoom;
import team_project.clat.domain.Message;
import team_project.clat.dto.ChatRoomMessageDTO;
import team_project.clat.dto.RoomKeyReq;
import team_project.clat.exception.DuplicateCourseChatRoomException;
import team_project.clat.repository.ChatRoomRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
class ChatRoomServiceTest {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private EntityManager em;





    @Test
    public void Fetchjoin을_사용한_메세지성능_최적화() throws Exception {

        //then

        ChatRoom chatRoom = chatRoomService.findFetchMessageCourseById(1L);

        for(Message  message: chatRoom.getMessageList()){
            System.out.println(message.getMessage());
        }


        //then
    }

    @Test
    public void fetchjoin_쿼리_보기() throws Exception {
       //given
        Optional<ChatRoom> fetchByCourseAndMessage = chatRoomRepository.findFetchByCourseAndMessage(1L);

        //when

        ChatRoom chatRoom = fetchByCourseAndMessage.get();

        System.out.println(chatRoom.getCourse().getRoom());

        List<Message> messageList = chatRoom.getMessageList();

        for(Message message : messageList){
            System.out.println(message.getMessage());
        }


        //then
    }

    @Test
    public void 방_입장() throws Exception {
       //given
        RoomKeyReq roomKeyReq = new RoomKeyReq(7L, 9154);

        //when

        boolean b = chatRoomService.validationRoom(roomKeyReq);

        //then

        Assertions.assertThat(b).isEqualTo(false);
    }

    @Test
    public void chatroom_에서_메서지와_이미지전체조회() throws Exception {
       //given
        ChatRoom chatRoom = chatRoomService.findFetchMessageAndImage(1L);
        //when

        ChatRoomMessageDTO chatRoomMessageDTO = new ChatRoomMessageDTO(chatRoom);
        //then
    }

    @Test
    public void 채빙방_같은방_메세지_조회() throws Exception {
       //given

        assertThrows(DuplicateCourseChatRoomException.class, ()->{
            chatRoomService.roomSaveValidation(2L, 2);
        });



       //then
    }



}