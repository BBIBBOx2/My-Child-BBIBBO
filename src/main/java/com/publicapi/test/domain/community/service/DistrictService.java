package com.publicapi.test.domain.community.service;

import com.publicapi.test.domain.community.entity.District;
import com.publicapi.test.domain.community.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository districtRepository;

    public List<District> findAll() {
        return districtRepository.findAll();
    }
}
