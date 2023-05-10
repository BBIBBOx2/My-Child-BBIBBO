package com.publicapi.test.domain.map.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HospitalDto {

    @JsonAlias(value = {"HPID"})
    private String hpId;

    @JsonAlias(value = {"DUTYNAME"})
    private String name;

    @JsonAlias(value = {"DUTYADDR"})
    private String address;

    @JsonAlias(value = {"DUTYTEL1"})
    private String telephone;

    //응급실 운영여부
    @JsonAlias(value = {"DUTYERYN"})
    private int isErOperating;

    //경도
    @JsonAlias(value = {"WGS84LON"})
    private Double longitude;

    //위도
    @JsonAlias(value = {"WGS84LAT"})
    private Double latitude;
}
