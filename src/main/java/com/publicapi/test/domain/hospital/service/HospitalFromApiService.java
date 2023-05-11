package com.publicapi.test.domain.hospital.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.publicapi.test.domain.hospital.config.OpenApi;
import com.publicapi.test.domain.hospital.dto.HospitalDto;
import com.publicapi.test.domain.hospital.dto.HospitalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class HospitalFromApiService {
    private final OpenApi openApi;

    private final RestTemplate restTemplate = new RestTemplate();


    public HospitalResponse requestHospitalInfo() {
        URI url = makeUrl(1, 10);
        ObjectMapper mapper = new ObjectMapper();

        HospitalResponse response = new HospitalResponse();
        try {
            String jsonString = restTemplate.getForObject(url, String.class);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
            JSONObject jsonResponse = (JSONObject) jsonObject.get("TbHospitalInfo");
            String jsonBody = jsonResponse.get("row").toString();
            List<HospitalDto> hospitals = mapper.readValue(jsonBody, new TypeReference<List<HospitalDto>>() {});
            response.setRow(hospitals);
            System.out.println("hospitals = " + hospitals);
        } catch (ParseException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return response;
    }


    private URI makeUrl(Integer start, Integer end) {
        return UriComponentsBuilder.fromUriString(OpenApi.BASE_URL)
                .path("/{KEY}/json/TbHospitalInfo/{start}/{end}")
                .build()
                .expand(openApi.getKey(), start, end)
                .encode()
                .toUri();
    }



}
