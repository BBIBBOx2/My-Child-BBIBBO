package com.publicapi.test.domain.hospital.controller;

import com.publicapi.test.domain.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
        return "hospital/hospital-map";
    }

}
