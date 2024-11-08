package com.io.rentify.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySenderIdAndRecipientIdAndAdId(String senderId, String recipientId, Long adId);

    List<ChatRoom> findBySenderIdOrRecipientId(String senderId, String recipientId);

}