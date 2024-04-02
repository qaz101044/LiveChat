package LiveChat.controller;

import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired; // 자동 연결
import org.springframework.context.event.EventListener;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import LiveChat.repository.ChatRepository;
import LiveChat.dto.ChatDTO;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {
    // convertAndSend
    private final SimpMessageSendingOperations template;

    @Autowired
    ChatRepository repository;

    @MessageMapping("/chat/etnerUser")
    public void enterUser(@Payload ChatDTO chat, SimpMessageHeaderAccessor headerAccessor) {

        // 채팅방 유저 입장 => 1명 증가
        repository.addUserCnt(chat.getRoomdId());

        // 채팅방 유저 추가 및 UserUUID 반환
        String userUUID = repository.addUser(chat.getRoomdId(), chat.getSender());

        // 반환 결과를 userUUID 로 저장
        headerAccessor.getSessionAttributes().put("userUUID",userUUID);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomdId());

        chat.setMessage(chat.getSender() + " 님이 채팅방에 입장하셨습니다.");
        template.convertAndSend("/sub/chat/room/" + chat.getRoomdId(), chat);
    }

    // 유저 채팅
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatDTO chat) {
        log.info("CHAT {}", chat);
        chat.setMessage(chat.getMessage());
        template.convertAndSend("/sub/chat/room/" + chat.getRoomdId());
    }

    // EventListener를 통해서 유저 퇴장
    @EventListener
    public void userDisconnectThroughEventListener(SessionDisconnectEvent event) {
        log.info("DisconnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션을 참조해 uuid 와 roomID를 확인, 채팅방 유저 리스트와 room에서 해당 유저 삭제
        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAcessor {}", headerAccessor);

        // 채팅방 유저 퇴장 => 1명 감소
        repository.removeUserCnt(roomId);

        // 채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
        String username = repository.getUserName(roomId,userUUID);
        repository.delUser(roomId,userUUID);

        if (username != null) {
            log.info("User Disconnected : " + username);

            ChatDTO chat = ChatDTO.builder()
                    .type(ChatDTO.MessageType.LEFT)
                    .sender(username)
                    .message(username + " 님이 채팅방에서 퇴장하셨습니다.")
                    .build();

            template.convertAndSend("/sub/chat/room/" + roomId, chat);
        }
    }
}
