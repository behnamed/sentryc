package com.sentryc.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Entity
@ToString
@Table(name = "marketplace")
public class MarketplaceEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "marketplace", cascade = CascadeType.ALL)
    private List<SellerInfoEntity> sellerInfo;

}