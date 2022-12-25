package com.icolak.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class MapperUtil {

    private final ModelMapper mapper;

    public MapperUtil(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public <T> T convert(Object objectToBeConverted, T convertedObject) {
        return mapper.map(objectToBeConverted, (Type) convertedObject.getClass());
    }
}
