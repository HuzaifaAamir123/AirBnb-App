package com.AirBnb.Final.Project.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false,columnDefinition = "TEXT[]")
    private String[] photos;

    @Column(nullable = false,columnDefinition = "TEXT[]")
    private String[] amenities;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean active;

    @Embedded
    private HotelContactInfo contactInfo;

    @OneToMany(mappedBy = "hotel")
    private List<Room>rooms=new ArrayList<>();

    @OneToMany(mappedBy = "hotel")
    private List<Inventory>inventories=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
