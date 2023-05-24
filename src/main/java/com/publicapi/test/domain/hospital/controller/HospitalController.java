package com.publicapi.test.domain.hospital.controller;

import com.publicapi.test.domain.hospital.entity.HospitalEntity;
import com.publicapi.test.domain.hospital.entity.RegionEntity;
import com.publicapi.test.domain.hospital.repository.RegionRepository;
import com.publicapi.test.domain.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;
    private final RegionRepository regionRepository;

    @GetMapping("/apiTest")
    public void apiTest() {
        hospitalService.syncHospitalInfo();
    }

    @GetMapping("/hospital")
    public String map() {
        return "hospital/hospital-map";
    }

    @GetMapping("/hospital/list")
    public String hospitalList(Model model, @RequestParam(required = false) String regionName) {
        List<HospitalEntity> hospitalList;

        if (regionName != null) {
            hospitalList = hospitalService.getHospitalByRegionName(regionName);
        } else {
            hospitalList = hospitalService.getHospital();
        }

        List<RegionEntity> regions = regionRepository.findAll();
        model.addAttribute("regions", regions);
        model.addAttribute("hospitalList", hospitalList);


        return "list/hospital_list";
    }

    @GetMapping("/hospital/detail/{hpId}")
    public String hospitalDetailList(Model model, @PathVariable String hpId) {

        List<HospitalEntity> hospitalList = hospitalService.getHospitalSortedById(hpId); // 정렬된 리스트 가져오기

        List<RegionEntity> regions = regionRepository.findAll();
        model.addAttribute("regions", regions);
        model.addAttribute("hospitalList", hospitalList);

        return "list/hospital_list";
    }
}
