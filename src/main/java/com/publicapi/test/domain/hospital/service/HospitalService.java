package com.publicapi.test.domain.hospital.service;

import com.publicapi.test.domain.hospital.dto.HospitalDetailDto;
import com.publicapi.test.domain.hospital.dto.HospitalDto;
import com.publicapi.test.domain.hospital.dto.HospitalMapper;
import com.publicapi.test.domain.hospital.dto.HospitalResponse;
import com.publicapi.test.domain.hospital.entity.HospitalEntity;
import com.publicapi.test.domain.hospital.entity.RegionEntity;
import com.publicapi.test.domain.hospital.repository.HospitalRepository;
import com.publicapi.test.domain.hospital.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalMapper hospitalMapper;
    private final HospitalRepository hospitalRepository;
    private final HospitalFromApiService hospitalFromApiService;
    private final HospitalDetailFromApiService hospitalDetailFromApiService;

    private final RegionRepository regionRepository;

    public void syncHospitalInfo() {
        for (int i = 15001; i < 19650; i+=1000) {
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
        updateHospitalDetail(hospitals);
    }

    void updateHospitalDetail(List<HospitalDetailDto> hospitals) {
        for (HospitalDetailDto hospital : hospitals) {
            Optional<HospitalEntity> optionalHospital = hospitalRepository.findByHpId(hospital.getHpId());
            if (optionalHospital.isPresent()) {
                HospitalEntity updateEntity = hospitalMapper.update(optionalHospital.get(), hospital);
                System.out.println("updateEntity = " + updateEntity.getName());
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

    public List<HospitalEntity> getHospitalByRegionName(String regionName) {
        List<HospitalEntity> filteredHospitalList = new ArrayList<>();
        RegionEntity region = regionRepository.findById(Long.parseLong(regionName)).orElseThrow();

        for (HospitalEntity hospital : getHospital()) {
            if (hospital.getAddress().contains(region.getName())) {
                filteredHospitalList.add(hospital);
            }
        }

        return filteredHospitalList;
    }

    public List<HospitalEntity> getHospitalByRegionNameAndIsOperating(String regionName) {
        List<HospitalEntity> filteredHospitalList = new ArrayList<>();
        RegionEntity region = regionRepository.findById(Long.parseLong(regionName)).orElseThrow();

        for (HospitalEntity hospital : getHospital()) {
            if (hospital.getAddress().contains(region.getName())&&hospital.getIsErOperating()==1) {
                filteredHospitalList.add(hospital);
            }
        }

        return filteredHospitalList;
    }

    public List<HospitalEntity> getHospitalByIsOperating(){
        List<HospitalEntity> filteredHospitalList = new ArrayList<>();
        for (HospitalEntity hospital : getHospital()) {
            if (hospital.getIsErOperating()==1) {
                filteredHospitalList.add(hospital);
            }
        }
        return filteredHospitalList;
    }

    public List<HospitalEntity> getHospitalSortedById(String hpId) {
        List<HospitalEntity> hospitalList = hospitalRepository.findAll();

        // hpId에 해당하는 병원 찾기
        HospitalEntity targetHospital = null;
        for (HospitalEntity hospital : hospitalList) {
            if (hospital.getHpId().equals(hpId)) {
                targetHospital = hospital;
                break;
            }
        }

        // targetHospital을 첫 번째로 위치
        if (targetHospital != null) {
            hospitalList.remove(targetHospital);
            hospitalList.add(0, targetHospital);
        }

        return hospitalList;
    }

    public int[] getIsOpening(int day, int currentTime, List<HospitalEntity> hospitalList) {
        int[] isOpeningArray = new int[hospitalList.size()];
         if (day == 0) {
            for (int i = 0; i < hospitalList.size(); i++) {
                HospitalEntity hospital = hospitalList.get(i);
                if (parseInt(hospital.getSunStart()) <= currentTime && currentTime <= parseInt(hospital.getSunClose())) {
                    isOpeningArray[i] = 1;
                } else {
                    isOpeningArray[i] = 0;
                }
            }
        }  else if (day == 1) {
             for (int i = 0; i < hospitalList.size(); i++) {
                 HospitalEntity hospital = hospitalList.get(i);
                 if (parseInt(hospital.getMonStart()) <= currentTime && currentTime <= parseInt(hospital.getMonClose())) {
                     isOpeningArray[i] = 1;
                 } else {
                     isOpeningArray[i] = 0;
                 }
             }
         }  else if (day == 2){
             for (int i = 0; i < hospitalList.size(); i++) {
                 HospitalEntity hospital = hospitalList.get(i);
                 if (parseInt(hospital.getTueStart()) <= currentTime && currentTime <= parseInt(hospital.getTueClose())) {
                     isOpeningArray[i] = 1;
                 } else {
                     isOpeningArray[i] = 0;
                 }
             }
         }else if (day == 3){
             for (int i = 0; i < hospitalList.size(); i++) {
                 HospitalEntity hospital = hospitalList.get(i);
                 if (parseInt(hospital.getWedStart()) <= currentTime && currentTime <= parseInt(hospital.getWedClose())) {
                     isOpeningArray[i] = 1;
                 } else {
                     isOpeningArray[i] = 0;
                 }
             }
         } else if (day == 4) {
             for (int i = 0; i < hospitalList.size(); i++) {
                 HospitalEntity hospital = hospitalList.get(i);
                 if (parseInt(hospital.getThurStart()) <= currentTime && currentTime <= parseInt(hospital.getThurClose())) {
                     isOpeningArray[i] = 1;
                 } else {
                     isOpeningArray[i] = 0;
                 }
             }
         } else if (day == 5) {
             for (int i = 0; i < hospitalList.size(); i++) {
                 HospitalEntity hospital = hospitalList.get(i);
                 if (parseInt(hospital.getFriStart()) <= currentTime && currentTime <= parseInt(hospital.getFriClose())) {
                     isOpeningArray[i] = 1;
                 } else {
                     isOpeningArray[i] = 0;
                 }
             }
         } else if (day == 6) {
             for (int i = 0; i < hospitalList.size(); i++) {
                 HospitalEntity hospital = hospitalList.get(i);
                 if (parseInt(hospital.getSatStart()) <= currentTime && currentTime <= parseInt(hospital.getSatClose())) {
                     isOpeningArray[i] = 1;
                 } else {
                     isOpeningArray[i] = 0;
                 }
             }
         }
        return isOpeningArray;
    }


        public static int parseInt(String str) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
}
