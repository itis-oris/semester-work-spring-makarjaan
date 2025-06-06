package com.makarova.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "apartment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String typeOfApartment;

    @Column(nullable = false)
    private String roomsCount;

    @Column(nullable = false)
    private Double area;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String dealType;

    private Integer priceSale;

    private Integer priceRent;

    private String typeOfRent;

    @Column(nullable = false)
    private Boolean isFavorite;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}