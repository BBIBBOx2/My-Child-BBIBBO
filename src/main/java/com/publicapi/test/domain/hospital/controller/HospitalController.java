package com.publicapi.test.domain.hospital.controller;

import com.publicapi.test.domain.hospital.entity.HospitalEntity;
import com.publicapi.test.domain.hospital.entity.RegionEntity;
import com.publicapi.test.domain.hospital.repository.HospitalRepository;
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
    private final HospitalRepository hospitalRepository;

    @GetMapping("/hospital")
    public String map() {
        return "hospital/hospital-map";
    }

    @GetMapping("/hospital/list")
    public String hospitalList(Model model, @RequestParam(required = false) String regionName, @RequestParam(required = false) Boolean isERChecked) {
        List<HospitalEntity> hospitalList;

        if (regionName == null || regionName.equals("시군구 선택")|| regionName.equals("1")) {
            if (isERChecked != null && isERChecked) {
                hospitalList = hospitalService.getHospitalByIsOperating();
            } else {
                hospitalList = hospitalService.getHospital();
            }
        } else {
            if (isERChecked != null && isERChecked) {
                hospitalList = hospitalService.getHospitalByRegionNameAndIsOperating(regionName);
            } else {
                hospitalList = hospitalService.getHospitalByRegionName(regionName);
            }
        }
        List<RegionEntity> regions = regionRepository.findAll();
        model.addAttribute("regions", regions);
        model.addAttribute("hospitalList", hospitalList);
        model.addAttribute("regionName", regionName);

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
