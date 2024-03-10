package com.sentryc.data.repository;

import com.sentryc.data.entities.MarketplaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketplaceRepository extends JpaRepository<MarketplaceEntity, String> {
}
