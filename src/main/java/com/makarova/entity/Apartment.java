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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "type_of_apartment", nullable = false)
    private String typeOfApartment;

    @Column(name = "rooms_count", nullable = false)
    private String roomsCount;

    @Column(name = "area", nullable = false)
    private Double area;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApartmentStatus status;

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "deal_type", nullable = false)
    private String dealType;

    @Column(name = "price_sale")
    private Integer priceSale;

    @Column(name = "price_rent")
    private Integer priceRent;

    @Column(name = "type_of_rent")
    private String typeOfRent;

    @Column(name = "main_photo_url")
    private String mainPhotoUrl;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}