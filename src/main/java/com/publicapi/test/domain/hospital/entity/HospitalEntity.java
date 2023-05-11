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

    //응급실 운영여부
    private int isErOperating;

    //경도
    private Double longitude;

    //위도
    private Double latitude;

}
