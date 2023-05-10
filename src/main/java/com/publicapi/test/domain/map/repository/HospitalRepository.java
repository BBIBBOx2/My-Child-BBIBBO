package com.publicapi.test.domain.map.repository;

import com.publicapi.test.domain.map.entity.HospitalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, Long> {

    Optional<HospitalEntity> findByHpId(String hpId);
}
