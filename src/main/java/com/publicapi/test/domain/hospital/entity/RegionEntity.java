package com.publicapi.test.domain.hospital.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "region")
@Getter
@Setter
@NoArgsConstructor
public class RegionEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Override
    public String toString() {
        return "RegionEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
