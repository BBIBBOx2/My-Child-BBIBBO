package com.publicapi.test.domain.map.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.publicapi.test.domain.map.config.OpenApi;
import com.publicapi.test.domain.map.dto.HospitalDto;
import com.publicapi.test.domain.map.dto.HospitalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class HospitalFromApiService {
    private final OpenApi openApi;

    private final RestTemplate restTemplate = new RestTemplate();


    public HospitalResponse requestHospitalInfo() {
        URI url = makeUrl(1, 10);
        RequestEntity<Void> RE = new RequestEntity<>(HttpMethod.GET, url);
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

//            response = restTemplate.exchange(RE, HospitalResponse.class).getBody();
//            Objects.requireNonNull(response);
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
