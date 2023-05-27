package com.publicapi.test.domain.hospital.controller;

import com.publicapi.test.domain.hospital.dto.HospitalDto;
import com.publicapi.test.domain.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MapController {

    private final HospitalService hospitalService;


    @RequestMapping(value = "/data.json", method = RequestMethod.GET)
    public List<HospitalDto> hospitalData() {
        List<HospitalDto> hospitals = hospitalService.getAllHospitals();
        return hospitals;
    }

    @GetMapping("/syncInfo")
    public void syncHospital() {
        hospitalService.syncHospitalInfo();
    }

    @GetMapping("/erapi")
    public void erApi() throws IOException, URISyntaxException {
        hospitalService.getHospitalDetail("강서구");
    }

}
