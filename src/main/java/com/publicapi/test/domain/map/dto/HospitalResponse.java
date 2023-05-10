package com.publicapi.test.domain.map.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HospitalResponse {

    private List<HospitalDto> row;

}
