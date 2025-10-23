// Updated BookExchangeRequest.java to support two-way exchange

package com.swapshelf.entity;

import com.swapshelf.entity.enums.ExchangeRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookExchangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who initiated the request
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    // Book that the request is for (owned by the receiver of the request)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    // Optional message or note
    @Lob
    private String message;

    // Status of the request
    @Enumerated(EnumType.STRING)
    private ExchangeRequestStatus status = ExchangeRequestStatus.PENDING;

    // Book that the owner requests in return (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_exchange_book_id")
    private Book requestedExchangeBook;

    // Whether the requester has confirmed the exchange proposal from the owner
    private boolean requesterConfirmed;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
