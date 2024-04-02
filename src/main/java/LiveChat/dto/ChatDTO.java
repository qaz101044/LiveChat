package LiveChat.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    public enum MessageType {
        ENTER, TALKING, LEFT
    }

    private MessageType type;

    private String roomdId;
    private String sender;
    private String message;
    private String time;

}
