package com.sentryc.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "seller_infos")
public class SellerInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "country")
    private String country;

    @Column(name = "url")
    private String url;

    @Column(name = "external_id")
    private String externalId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "marketplace_id", referencedColumnName = "id")
    private MarketplaceEntity marketplace;

    @OneToMany(mappedBy = "sellerInfo", cascade = CascadeType.REMOVE)
    private List<SellerEntity> sellers;
}
