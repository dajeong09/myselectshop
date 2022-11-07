package com.sparta.myselectshop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myselectshop.dto.ItemDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.List;
@Controller
public class ItemSearchController {

    @GetMapping("/api/search")
    @ResponseBody
    public List<ItemDto> getItems(@RequestParam String query) throws IOException {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", "zdqMoIkFaK8uKvC2oNY2");
        headers.add("X-Naver-Client-Secret", "LiZfsgtuD5");
        String body = "";
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        //네이버 쇼핑 API 호출 결과 -> naverApiResponseJson (JSON 형태)
        ResponseEntity<String> responseEntity = rest.exchange("https://openapi.naver.com/v1/search/shop.json?query=" + query, HttpMethod.GET, requestEntity, String.class);
        String naverApiResponseJson = responseEntity.getBody();
        //naverApiResponseJson (JSON 형태) -> itemDtoList (자바 객체 형태)
        // naverApiResponseJson 에서 우리가 사용할 데이터만 추출 -> List<ItemDto> 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode itemsNode = objectMapper.readTree(naverApiResponseJson).get("items");
        List<ItemDto> itemDtoList = objectMapper
                .readerFor(new TypeReference<List<ItemDto>>() {})
                .readValue(itemsNode);
        return itemDtoList;
    }
}