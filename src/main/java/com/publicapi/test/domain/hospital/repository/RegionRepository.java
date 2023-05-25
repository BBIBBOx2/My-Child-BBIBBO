package com.publicapi.test.domain.hospital.repository;

import com.publicapi.test.domain.hospital.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<RegionEntity, Long> {
}