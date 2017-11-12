package com.travix.medusa.busyflights.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Map;

@Component
public class BeanToMapConvertor {

    @Autowired
    private ObjectMapper objectMapper;

    public LinkedMultiValueMap<String, String> converBeanToMultiMap(Object requestObject) {
        Map<String, String> map = objectMapper.convertValue(requestObject, new TypeReference<Map<String,String>>() {});
        LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
        map.entrySet().forEach(e -> linkedMultiValueMap.add(e.getKey(), e.getValue()));
        return linkedMultiValueMap;
    }
}
