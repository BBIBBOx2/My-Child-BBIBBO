package com.publicapi.test.domain.hospital.dto;

import com.publicapi.test.domain.hospital.entity.HospitalEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface HospitalMapper {

    @Mapping(target = "id", ignore = true)
    HospitalEntity newEntity(HospitalDto hospital);

    @Named("E2D")
    HospitalDto toDto(HospitalEntity entity);

    @IterableMapping(qualifiedByName = "E2D")
    List<HospitalDto> map(List<HospitalEntity> entities);

}
