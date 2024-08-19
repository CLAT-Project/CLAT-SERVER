package team_project.clat.domain.Dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "강의실 만드는 DTO")
public class ChatRoomCreateDto {

    @NotEmpty
    @Schema(description = "ChatRoom roomName", example = "운영체제1-1방")
    private String roomName;

    @NotNull
    @Schema(description = "Course courseId", example = "1")
    private Long courseId;

    public ChatRoomCreateDto(String roomName, Long courseId) {
        this.roomName = roomName;
        this.courseId = courseId;
    }

    public ChatRoomCreateDto() {
    }
}