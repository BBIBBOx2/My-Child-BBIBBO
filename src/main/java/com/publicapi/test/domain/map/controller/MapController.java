package com.publicapi.test.domain.map.controller;

import com.publicapi.test.domain.map.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MapController {

    private final HospitalService hospitalService;

    @GetMapping("/apiTest")
    public void apiTest() {
        hospitalService.syncHospitalInfo();
    }

}
