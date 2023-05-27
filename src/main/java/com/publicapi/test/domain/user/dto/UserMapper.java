package com.publicapi.test.domain.user.dto;

import com.publicapi.test.domain.user.entity.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    UserEntity update(@MappingTarget UserEntity entity, UserInfo userInfo);

}
