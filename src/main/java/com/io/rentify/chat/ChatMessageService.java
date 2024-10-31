package com.io.rentify.chat;

import com.io.rentify.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), chatMessage.getAdId(), true)
                .orElseThrow();
        chatMessage.setChatId(chatId);
        chatMessage.setAdId(chatMessage.getAdId());
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(Long adId, String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, adId, false);
        return chatId.map(id -> repository.findByChatIdAndAdId(id, adId)).orElse(new ArrayList<>());
    }

}