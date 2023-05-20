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

}
