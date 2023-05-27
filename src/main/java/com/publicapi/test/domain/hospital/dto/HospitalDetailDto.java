package com.publicapi.test.domain.hospital.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HospitalDetailDto {

    @JsonAlias(value = {"hpid"})
    private String hpId;

    //기관명
    @JsonAlias(value = {"dutyname"})
    private String name;

    //가용 응급실 일반 병상
    @JsonAlias(value = {"hvec"})
    private Integer hospitalBed;

    //가용 수술실
    @JsonAlias(value = {"hvoc"})
    private Integer operatingRoom;

    //CT 가용여부
    @JsonAlias(value = {"hvctayn"})
    private String isCt;

    //MRI 가용여부
    @JsonAlias(value = {"hvmriayn"})
    private String isMri;

    //조영촬영기 가용여부
    @JsonAlias(value = {"hvangioayn"})
    private String isAngio;

    //인공호흡기 가용여부
    @JsonAlias(value = {"hvventiayn"})
    private String isVentilator;

    //구급차 가용여부
    @JsonAlias(value = {"hvamyn"})
    private String isAmbulance;

    //응급실 당직의 연락처
    @JsonAlias(value = {"hv1"})
    private String phone;

    //소아 당직의 연락
    @JsonAlias(value = {"hv12"})
    private String childPhone;



}
