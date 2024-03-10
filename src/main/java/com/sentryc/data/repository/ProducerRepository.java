package com.sentryc.data.repository;

import com.sentryc.data.entities.ProducerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProducerRepository extends JpaRepository<ProducerEntity, UUID> {
}
