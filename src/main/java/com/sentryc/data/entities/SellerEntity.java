package com.sentryc.data.entities;


import com.sentryc.data.entities.enums.SellerState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "sellers")
public class SellerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private SellerState state;

    @ManyToOne
    @JoinColumn(name = "seller_info_id", referencedColumnName = "id")
    private SellerInfoEntity sellerInfo;

    @ManyToOne
    @JoinColumn(name = "producer_id", referencedColumnName = "id")
    private ProducerEntity producer;
}
