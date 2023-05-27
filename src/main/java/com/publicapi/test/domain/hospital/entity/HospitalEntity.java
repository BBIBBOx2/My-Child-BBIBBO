package com.publicapi.test.domain.hospital.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "hospital")
@Getter
@Setter
@NoArgsConstructor
public class HospitalEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String hpId;

    private String name;

    private String address;

    private String telephone;

    private String erTelephone;

    //진료시간(월요일)C
    private String monClose;

    //진료시간(화요일)C
    private String tueClose;

    //진료시간(수요일)C
    private String wedClose;

    //진료시간(목요일)C
    private String thurClose;

    //진료시간(금요일)C
    private String friClose;

    //진료시간(토요일)C
    private String satClose;

    //진료시간(알요일)C
    private String sunClose;

    //진료시간(공휴일)C
    private String holidayClose;

    //진료시간(월요일)S
    private String monStart;

    //진료시간(화요일)S
    private String tueStart;

    //진료시간(수요일)S
    private String wedStart;

    //진료시간(목요일)S
    private String thurStart;

    //진료시간(금요일)S
    private String friStart;

    //진료시간(토요일)S
    private String satStart;

    //진료시간(알요일)S
    private String sunStart;

    //진료시간(공휴일)S
    private String holidayStart;

    //우편번호
    private String postNum;

    private String simpleMap;

    //응급실 운영여부
    private int isErOperating;

    //비고
    private String etc;

    //기관 설명
    private String info;

    //경도
    private Double longitude;

    //위도
    private Double latitude;

    //가용 응급실 일반 병상
    private Integer hospitalBed;

    //가용 수술실
    private Integer operatingRoom;

    //CT 가용여부
    private String isCt;

    //MRI 가용여부
    private String isMri;

    //조영촬영기 가용여부
    private String isAngio;

    //인공호흡기 가용여부
    private String isVentilator;

    //구급차 가용여부
    private String isAmbulance;

    //응급실 당직의 연락처
    private String phone;

    //소아 당직의 연락
    private String childPhone;

}
