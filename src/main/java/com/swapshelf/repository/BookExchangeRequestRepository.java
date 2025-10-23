package com.swapshelf.repository;

import com.swapshelf.entity.BookExchangeRequest;
import com.swapshelf.entity.User;
import com.swapshelf.entity.enums.ExchangeRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookExchangeRequestRepository extends JpaRepository<BookExchangeRequest, Long> {

    List<BookExchangeRequest> findByRequester(User user);

    List<BookExchangeRequest> findByBookOwner(User user);

    List<BookExchangeRequest> findByStatus(ExchangeRequestStatus status);
}
