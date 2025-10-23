package com.swapshelf.service.exchangerequest;

import com.swapshelf.entity.BookExchangeRequest;
import com.swapshelf.entity.enums.ExchangeRequestStatus;
import com.swapshelf.repository.BookExchangeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("exchangeRequestService")
public class ExchangeRequestServiceImpl implements ExchangeRequestService{

    @Autowired
    private BookExchangeRequestRepository requestRepository;

    @Override
    public List<BookExchangeRequest> findAll() {
        return requestRepository.findAll();
    }


    @Override
    public List<BookExchangeRequest> findByStatus(ExchangeRequestStatus status) {
        return requestRepository.findByStatus(status);
    }
    // ExchangeRequestService.java
//    public long countExchangeRequests() {
//        return exchangeRequestRepository.count();
//    }
}
