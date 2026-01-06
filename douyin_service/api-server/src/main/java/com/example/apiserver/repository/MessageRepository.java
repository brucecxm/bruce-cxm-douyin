package com.example.apiserver.repository;

import com.example.apiserver.entity.Message;
import com.example.apiserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // 获取两个用户之间的聊天记录
    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtAsc(User sender1, User receiver1, User sender2, User receiver2);
    
    // 获取用户的未读消息数量
    long countByReceiverAndIsReadFalse(User receiver);
    
    // 获取用户与某个特定用户之间的未读消息数量
    long countBySenderAndReceiverAndIsReadFalse(User sender, User receiver);
    
    // 将消息标记为已读
    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.sender = ?1 AND m.receiver = ?2 AND m.isRead = false")
    void updateBySenderAndReceiverAndIsReadFalse(User sender, User receiver);
    
    // 获取与用户有过聊天记录的所有用户
    @Query("SELECT DISTINCT u FROM User u WHERE u.id IN (SELECT m.sender.id FROM Message m WHERE m.receiver = ?1) OR u.id IN (SELECT m.receiver.id FROM Message m WHERE m.sender = ?1)")
    List<User> findChatUsersByUserId(User user);
    
    // 获取用户与特定用户之间的最新一条消息
    List<Message> findTop1BySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtDesc(User sender1, User receiver1, User sender2, User receiver2);
}