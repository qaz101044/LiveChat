package LiveChat.repository;

import java.util.*;
import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import LiveChat.dto.ChatRoom;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ChatRepository {

    private Map<String, ChatRoom> chatRoomMap;

    @PostConstruct
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    // 전체 채팅방 조회
    public List<ChatRoom> findAllChatRoom() {

        // 최근 채팅방 순으로 생성
        List<ChatRoom> chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);

        return chatRooms;
    }

    // 채팅방 유저 리스트에 유저 추가
    public String addUser(String roomId, String userName) {
        ChatRoom room = chatRoomMap.get(roomId);
        String userUUID = UUID.randomUUID().toString();

        // 아이디 중복 확인
        room.getUserList().put(userUUID,userName);

        return userUUID;
    }

    public String delUser(String roomId, String userName) {

    }
    public void addUserCnt(String roomId) {
        ChatRoom room = chatRoomMap.get(roomId);
        room.setUserCnt(room.getUserCnt() + 1);
    }

    public void removeUserCnt(String roomId) {
    }

    public String getUserName(String roomdId, String userUUID) {

    }


}
