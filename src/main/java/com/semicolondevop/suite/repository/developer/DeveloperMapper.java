package com.semicolondevop.suite.repository.developer;

import com.semicolondevop.suite.client.dto.DeveloperDto;
import com.semicolondevop.suite.model.developer.Developer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DeveloperMapper {

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeveloperFromDto(DeveloperDto dto, @MappingTarget Developer developer);
}
