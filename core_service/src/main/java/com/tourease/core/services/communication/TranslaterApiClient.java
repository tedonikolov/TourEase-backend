package com.tourease.core.services.communication;

import com.tourease.core.models.dto.TranslationVO;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class TranslaterApiClient {
    private final RestTemplate rapidApiRestTemplate;

    public String translate(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("from", "auto");
        map.add("to", "en");
        map.add("text", text);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<TranslationVO> response = rapidApiRestTemplate.exchange("https://google-translate113.p.rapidapi.com/api/v1/translator/text", HttpMethod.POST, request, TranslationVO.class);

        return response.getBody().trans();
    }
}
