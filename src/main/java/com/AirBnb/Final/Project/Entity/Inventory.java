package com.AirBnb.Final.Project.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "unique_hotel_room_date",columnNames = {"hotel_id","room_id","date"}
        )
)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private LocalDate date;

    private String city;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private Integer reservedCount;

    @Column(nullable = false)
    private Integer bookedCount;

    @Column(nullable = false,precision = 7,scale = 2)
    private BigDecimal basePrice;

    @Column(nullable = false,precision = 7,scale = 2)
    private BigDecimal price;

    @Column(nullable = false,precision = 7,scale = 2)
    private BigDecimal surgeFactor;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean close;
}
