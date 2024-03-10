package com.sentryc.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "producers")
public class ProducerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToMany(mappedBy = "producer", cascade = CascadeType.REMOVE)
    private List<SellerEntity> sellers;

}
