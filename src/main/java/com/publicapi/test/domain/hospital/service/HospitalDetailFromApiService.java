package com.publicapi.test.domain.hospital.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.publicapi.test.domain.hospital.config.ErInfo;
import com.publicapi.test.domain.hospital.dto.HospitalDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.net.*;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class HospitalDetailFromApiService {

    private final ErInfo erInfo;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<HospitalDetailDto> requestHospitalDetail(String town) throws IOException, URISyntaxException {
        URI uri = makeUrl(town, 1, 30).toURI();
        HttpHeaders headers = makeHeader();
        HttpEntity request = new HttpEntity(headers);
        ObjectMapper mapper = new ObjectMapper();

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONObject items = jsonObject.getJSONObject("response").getJSONObject("body")
                .getJSONObject("items");
        System.out.println("items = " + items);
        List<HospitalDetailDto> hospitals = mapper.readValue(items.get("item").toString(),
                new TypeReference<List<HospitalDetailDto>>() {});

        return hospitals;
    }

    private HttpHeaders makeHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private URL makeUrl(String town, Integer page, Integer row) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(ErInfo.BASE_URL);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+erInfo.getKey()); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("STAGE1","UTF-8") + "=" + URLEncoder.encode("서울특별시", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("STAGE2","UTF-8") + "=" + URLEncoder.encode(town, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(page.toString(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode( row.toString(), "UTF-8"));
        return new URL(urlBuilder.toString());
    }

//    private URI makeUrl(String town, Integer page, Integer row) {
//        return UriComponentsBuilder.fromUriString(ErInfo.BASE_URL)
//                .queryParam(ErInfo.KEY_PARAM_NAME, erInfo.getKey())
//                .queryParam(ErInfo.SI_PARAM_NAME, "서울특별시")
//                .queryParam(ErInfo.GU_PARAM_NAME, town)
//                .queryParam(ErInfo.PAGE_PARAM_NAME, page)
//                .queryParam(ErInfo.ROW_PARAM_NAME, row)
//                .encode()
//                .build()
//                .toUri();
//    }



}
