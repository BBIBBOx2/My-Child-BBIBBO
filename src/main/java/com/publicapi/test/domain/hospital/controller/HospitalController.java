package com.publicapi.test.domain.hospital.controller;

import com.publicapi.test.domain.hospital.entity.HospitalEntity;
import com.publicapi.test.domain.hospital.entity.RegionEntity;
import com.publicapi.test.domain.hospital.repository.HospitalRepository;
import com.publicapi.test.domain.hospital.repository.RegionRepository;
import com.publicapi.test.domain.hospital.service.HospitalService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HospitalController {

    private final HospitalService hospitalService;
    private final RegionRepository regionRepository;
    private final HospitalRepository hospitalRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/")
    public String home() {
        return "hospital/hospital-map";
    }
    @GetMapping("/hospital")
    public String map() {
        return "hospital/hospital-map";
    }

    @GetMapping("/hospital/list")
    public String hospitalList(Model model, @RequestParam(required = false) String regionName, @RequestParam(required = false) Boolean isERChecked) {
        List<HospitalEntity> hospitalList;
        int[] isOpeningList = null;

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

        Date currentDate = new Date();
        int day = currentDate.getDay();
        int currentHour = currentDate.getHours()+9;  // 현재 시간(0-23)
        int currentMinute = currentDate.getMinutes();  // 현재 분(0-59)
        int currentTime = currentHour * 100 + currentMinute;

        isOpeningList = hospitalService.getIsOpening(day, currentTime, hospitalList);
        logger.info(String.valueOf(isOpeningList[2]));
        logger.info(String.valueOf(day));
        logger.info(String.valueOf(currentTime));

        List<RegionEntity> regions = regionRepository.findAll();
        model.addAttribute("regions", regions);
        model.addAttribute("hospitalList", hospitalList);
        model.addAttribute("regionName", regionName);
        model.addAttribute("isOpeningList", isOpeningList);

        return "list/hospital_list";
    }

    @GetMapping("/hospital/detail/{hpId}")
    public String hospitalDetailList(Model model, @PathVariable String hpId) {
        List<HospitalEntity> hospitalList = hospitalService.getHospitalSortedById(hpId); // 정렬된 리스트 가져오기
        List<RegionEntity> regions = regionRepository.findAll();
        int[] isOpeningList = null;

        Date currentDate = new Date();
        int day = currentDate.getDay();
        int currentHour = currentDate.getHours();  // 현재 시간(0-23)
        int currentMinute = currentDate.getMinutes();  // 현재 분(0-59)
        int currentTime = currentHour * 100 + currentMinute;

        isOpeningList = hospitalService.getIsOpening(day, currentTime, hospitalList);

        model.addAttribute("regions", regions);
        model.addAttribute("hospitalList", hospitalList);
        model.addAttribute("isOpeningList", isOpeningList);

        return "list/hospital_list";
    }
}
