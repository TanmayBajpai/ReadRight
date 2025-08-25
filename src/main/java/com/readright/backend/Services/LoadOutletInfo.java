package com.readright.backend.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.readright.backend.Records.OutletInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LoadOutletInfo {
    private Map<String, OutletInfo> outletInfoMap;

    @PostConstruct
    public void loadData() {
        try {
            Resource resource = new ClassPathResource("outlets.json");
            InputStream inputStream = resource.getInputStream();

            ObjectMapper objectMapper = new ObjectMapper();

            List<OutletInfo> outlets = objectMapper.readValue(inputStream, new TypeReference<>() {});

            this.outletInfoMap = outlets.stream().collect(Collectors.toMap(OutletInfo::domain, Function.identity()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<OutletInfo> findByDomain(String domain) {
        return Optional.ofNullable(outletInfoMap.get(domain));
    }

}
