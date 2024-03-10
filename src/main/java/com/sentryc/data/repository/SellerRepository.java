package com.sentryc.data.repository;

import com.sentryc.data.entities.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<SellerEntity, UUID>, JpaSpecificationExecutor<SellerEntity> {

}
