package com.publicapi.test.domain.hospital.service;

import com.publicapi.test.domain.hospital.dto.HospitalDetailDto;
import com.publicapi.test.domain.hospital.dto.HospitalDto;
import com.publicapi.test.domain.hospital.dto.HospitalMapper;
import com.publicapi.test.domain.hospital.dto.HospitalResponse;
import com.publicapi.test.domain.hospital.entity.HospitalEntity;
import com.publicapi.test.domain.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalMapper hospitalMapper;
    private final HospitalRepository hospitalRepository;
    private final HospitalFromApiService hospitalFromApiService;
    private final HospitalDetailFromApiService hospitalDetailFromApiService;


    public void syncHospitalInfo() {
        for (int i = 5001; i < 10001; i+=1000) {
            System.out.println("syncHospitalInfo in i = " + i);
            HospitalResponse response = hospitalFromApiService.requestHospitalInfo(i, i+999);
            List<HospitalDto> hospitals = response.getRow();
            for (HospitalDto hospital : hospitals) {
                registerHospital(hospital);
            }
        }



//        HospitalResponse response = hospitalFromApiService.requestHospitalInfo(start, end);
//        List<HospitalDto> hospitals = response.getRow();
//        for (HospitalDto hospital : hospitals) {
//            registerHospital(hospital);
//        }


    }

    public HospitalEntity registerHospital(HospitalDto hospital) {
        Optional<HospitalEntity> optionalHospital = hospitalRepository.findByHpId(hospital.getHpId());
        HospitalEntity hospitalEntity=new HospitalEntity();

        if (optionalHospital.isEmpty()) {
            if (checkHospital(hospital)) {
                HospitalEntity newEntity = hospitalMapper.newEntity(hospital);
                hospitalEntity = hospitalRepository.save(newEntity);
                return hospitalEntity;

            } else {
                return hospitalEntity;
            }
        }
        else {
            hospitalEntity = optionalHospital.get();
        }

        return hospitalEntity;

    }

    public void getHospitalDetail(String town) throws IOException, URISyntaxException {
        List<HospitalDetailDto> hospitals = hospitalDetailFromApiService.requestHospitalDetail(town);
        System.out.println("hospitals in 실시간 응급실 API = " + hospitals);
        updateHospitalDetail(hospitals);
    }

    void updateHospitalDetail(List<HospitalDetailDto> hospitals) {
        for (HospitalDetailDto hospital : hospitals) {
            Optional<HospitalEntity> optionalHospital = hospitalRepository.findByHpId(hospital.getHpId());
            if (optionalHospital.isPresent()) {
                HospitalEntity updateEntity = hospitalMapper.update(optionalHospital.get(), hospital);
            }
        }

    }

    public Boolean checkHospital(HospitalDto hospital) {
//        String[] include = {"소아","신생아","유아"};
//        String[] exclude = {"안질환", "소아진료X"};

        if (hospital.getIsErOperating() == 1) {
            return Boolean.TRUE;
        }
        if (hospital.getEtc().contains("소아") && !hospital.getEtc().contains("진료X")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }

//        if (hospital.getEtc().contains("소아")) {
//
//            if (hospital.getEtc().contains("안질환"))
//        }


    }

    public List<HospitalDto> getAllHospitals() {
        List<HospitalEntity> hospitalEntities = hospitalRepository.findAll();
        return hospitalMapper.map(hospitalEntities);
    }


    public List<HospitalEntity> getHospital(){
        return hospitalRepository.findAll();
    }
}
