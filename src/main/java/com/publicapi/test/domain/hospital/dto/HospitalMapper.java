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

    @Mapping(target = "id", ignore = true)
    HospitalEntity update(@MappingTarget HospitalEntity entity, HospitalDetailDto hospital);

    @IterableMapping(qualifiedByName = "E2D")
    List<HospitalDto> map(List<HospitalEntity> entities);

}
