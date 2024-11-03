package com.io.rentify.chat;

import com.io.rentify.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
        chatMessage.setTimestamp(new Date());
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(Long adId, String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, adId, false);
        return chatId.map(id -> repository.findByChatIdAndAdId(id, adId)).orElse(new ArrayList<>());
    }
    public String findSenderId(Long adId, String recipientId) {
        List<ChatMessage> messages = repository.findByChatIdAndAdId(getChatId(recipientId, adId), adId);
        return messages.isEmpty() ? null : messages.get(0).getSenderId(); // Return the last sender
    }

    private String getChatId(String recipientId, Long adId) {
        return String.format("%s_%s", adId, recipientId);
    }
}