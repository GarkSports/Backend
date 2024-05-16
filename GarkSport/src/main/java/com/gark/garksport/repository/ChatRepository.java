package com.gark.garksport.repository;

import com.gark.garksport.modal.Chat;
import com.gark.garksport.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findBySenderOrReceiverOrderByTimestampDesc(User sender, User receiver);

    List<Chat> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampDesc(User sender1, User receiver1, User sender2, User receiver2);


}
