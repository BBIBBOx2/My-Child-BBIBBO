package com.publicapi.test.domain.hospital.controller;

import com.publicapi.test.domain.hospital.entity.HospitalEntity;
import com.publicapi.test.domain.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping("/apiTest")
    public void apiTest() {
        hospitalService.syncHospitalInfo();
    }

    @GetMapping("/hospital")
    public String map() {
        return "/hospital/hospital-map";

    }

    @GetMapping("/hospital/list")
    public String hospitalList(Model model) {
        List<HospitalEntity> hospitalList = hospitalService.getHospital();
        model.addAttribute("hospitalList", hospitalList);
        return "list/hospital_list";
    }

}
