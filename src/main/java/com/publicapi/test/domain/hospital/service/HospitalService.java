package com.publicapi.test.domain.hospital.service;

import com.publicapi.test.domain.hospital.dto.HospitalDto;
import com.publicapi.test.domain.hospital.dto.HospitalMapper;
import com.publicapi.test.domain.hospital.dto.HospitalResponse;
import com.publicapi.test.domain.hospital.entity.HospitalEntity;
import com.publicapi.test.domain.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalMapper hospitalMapper;
    private final HospitalRepository hospitalRepository;
    private final HospitalFromApiService hospitalFromApiService;

    public void syncHospitalInfo() {
        HospitalResponse response = hospitalFromApiService.requestHospitalInfo();
        List<HospitalDto> hospitals = response.getRow();
        for (HospitalDto hospital : hospitals) {
            registerHospital(hospital);
        }


    }

    public HospitalEntity registerHospital(HospitalDto hospital) {
        Optional<HospitalEntity> optionalHospital = hospitalRepository.findByHpId(hospital.getHpId());
        HospitalEntity hospitalEntity;

        if (optionalHospital.isEmpty()) {
            HospitalEntity newEntity = hospitalMapper.newEntity(hospital);
            hospitalEntity = hospitalRepository.save(newEntity);
        }
        else {
            hospitalEntity = optionalHospital.get();
        }

        return hospitalEntity;

    }

    public List<HospitalDto> getAllHospitals() {
        List<HospitalEntity> hospitalEntities = hospitalRepository.findAll();
        return hospitalMapper.map(hospitalEntities);
    }


    public List<HospitalEntity> getHospital(){
        return hospitalRepository.findAll();
    }
}
