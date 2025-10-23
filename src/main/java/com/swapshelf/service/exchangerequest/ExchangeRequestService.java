package com.swapshelf.service.exchangerequest;

import com.swapshelf.entity.BookExchangeRequest;
import com.swapshelf.entity.enums.ExchangeRequestStatus;

import java.util.List;

public interface ExchangeRequestService {
    public List<BookExchangeRequest> findAll();
    List<BookExchangeRequest> findByStatus(ExchangeRequestStatus status);

}
