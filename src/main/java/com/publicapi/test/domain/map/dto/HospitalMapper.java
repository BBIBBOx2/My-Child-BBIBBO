package com.publicapi.test.domain.map.dto;

import com.publicapi.test.domain.map.entity.HospitalEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface HospitalMapper {

    @Mapping(target = "id", ignore = true)
    HospitalEntity newEntity(HospitalDto hospital);

}
