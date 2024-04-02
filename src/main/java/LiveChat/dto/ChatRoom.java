package LiveChat.dto;

import lombok.Data;

import java.util.UUID;
import java.util.HashMap;
@Data
public class ChatRoom {

    private String roomID; // 채팅방 ID
    private String roomName; // 채팅방 이름
    private long userCnt; // 채팅방 유저수

    private HashMap<String, String> userList = new HashMap<String, String>();

    public ChatRoom create(String roomName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomID = UUID.randomUUID().toString();
        chatRoom.roomName = roomName;

        return chatRoom;
    }

}
