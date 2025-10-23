package com.swapshelf.entity.enums;

public enum ExchangeRequestStatus {
    PENDING,
    OWNER_PROPOSED_EXCHANGE, // owner proposed a return book
    REQUESTER_CONFIRMED,     // requester confirmed the exchange
    IN_USE,                  // books are currently in use by opposite users
    COMPLETED,               // exchange successfully completed
    REJECTED,                // rejected by owner or requester
    CANCELLED                // withdrawn by requester
}
