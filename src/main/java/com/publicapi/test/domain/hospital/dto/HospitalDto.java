package com.publicapi.test.domain.hospital.dto;

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

    //대표 전화
    @JsonAlias(value = {"DUTYTEL1"})
    private String telephone;

    //응급실 전화
    @JsonAlias(value = {"DUTYTEL3"})
    private String erTelephone;

    //진료시간(월요일)C
    @JsonAlias(value = {"DUTYTIME1C"})
    private String monClose;

    //진료시간(화요일)C
    @JsonAlias(value = {"DUTYTIME2C"})
    private String tueClose;

    //진료시간(수요일)C
    @JsonAlias(value = {"DUTYTIME3C"})
    private String wedClose;

    //진료시간(목요일)C
    @JsonAlias(value = {"DUTYTIME4C"})
    private String thurClose;

    //진료시간(금요일)C
    @JsonAlias(value = {"DUTYTIME5C"})
    private String friClose;

    //진료시간(토요일)C
    @JsonAlias(value = {"DUTYTIME6C"})
    private String satClose;

    //진료시간(알요일)C
    @JsonAlias(value = {"DUTYTIME7C"})
    private String sunClose;

    //진료시간(공휴일)C
    @JsonAlias(value = {"DUTYTIME8C"})
    private String holidayClose;

    //진료시간(월요일)S
    @JsonAlias(value = {"DUTYTIME1S"})
    private String monStart;

    //진료시간(화요일)S
    @JsonAlias(value = {"DUTYTIME2S"})
    private String tueStart;

    //진료시간(수요일)S
    @JsonAlias(value = {"DUTYTIME3S"})
    private String wedStart;

    //진료시간(목요일)S
    @JsonAlias(value = {"DUTYTIME4S"})
    private String thurStart;

    //진료시간(금요일)S
    @JsonAlias(value = {"DUTYTIME5S"})
    private String friStart;

    //진료시간(토요일)S
    @JsonAlias(value = {"DUTYTIME6S"})
    private String satStart;

    //진료시간(알요일)S
    @JsonAlias(value = {"DUTYTIME7S"})
    private String sunStart;

    //진료시간(공휴일)S
    @JsonAlias(value = {"DUTYTIME8S"})
    private String holidayStart;

    //우편번호
    @JsonAlias(value = {"POSTCDN1"})
    private String postNum;

    //응급실 운영여부
    @JsonAlias(value = {"DUTYERYN"})
    private int isErOperating;

    //비고
    @JsonAlias(value = {"DUTYETC"})
    private String etc;

    //기관 설명 상세
    @JsonAlias(value = {"DUTYINF"})
    private String info;

    //간이 약도
    @JsonAlias(value = {"DUTYMAPIMG"})
    private String simpleMap;




    //경도
    @JsonAlias(value = {"WGS84LON"})
    private Double longitude;

    //위도
    @JsonAlias(value = {"WGS84LAT"})
    private Double latitude;
}
