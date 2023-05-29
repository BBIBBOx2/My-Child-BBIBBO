package com.publicapi.test.domain.oauth.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OauthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String baseUrl = "https://kauth.kakao.com";
    private static final String tokenReqUrl = "/oauth/token";
    private static final String infoReqUrl = "https://kapi.kakao.com/v2/user/me";
    private static final String redirectURI = "http://52.2.5.154:8080/oauth/kakao";


    @Value("${kakao.key}")
    private String apiKey;

    public String getAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", apiKey);
        params.add("redirect_uri", redirectURI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(baseUrl+tokenReqUrl, HttpMethod.POST, request, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());
        String accessToken = (String) jsonObject.get("access_token");

        return accessToken;
    }


    public String getUserIdByToken(String accessToken) {
        RestTemplate rest = new RestTemplate();


        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(headers);

        ResponseEntity<String> response = rest.exchange(
                infoReqUrl,
                HttpMethod.POST,
                request,
                String.class
        );
        JSONObject jsonObject = new JSONObject(response.getBody());
        System.out.println("jsonObject = " + jsonObject);
        String kakaoId = Integer.toString(jsonObject.getInt("id"));

        return kakaoId;

    }


}
