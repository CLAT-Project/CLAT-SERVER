package team_project.clat.dto;

import lombok.Data;
import team_project.clat.domain.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MessageIncludeFileDTO {

    private Long messageId;

    private String senderName;


    private String message;

    private List<String> imageUrl;

    private LocalDateTime timestamp;

    private Long answerId;

    private String answer;

    public MessageIncludeFileDTO(Message message) {
        this.messageId = message.getId();

        if(message.getMember() == null){
            this.senderName = "";
        }
        else{
            this.senderName = message.getMember().getUsername();
        }
        this.imageUrl = message.getImages().stream().map(image -> image.getAccessUrl()).collect(Collectors.toList());
        this.timestamp = message.getCreatedDate();
        this.message = message.getMessage();
        if(message.getAnswer() == null){
            this.answer = "";
            this.answerId = null;
        }else{
            this.answerId = message.getAnswer().getId();
            this.answer = message.getAnswer().getAnswer();
        }

    }

    public MessageIncludeFileDTO() {
    }
}
