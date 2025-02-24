package com.wanted.needswork.services;

import com.wanted.needswork.models.Industry;
import com.wanted.needswork.repository.IndustryRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service


public class HHService {
    @Autowired
    IndustryRepository industryRepository;

    private static final String API_URL = "https://api.hh.ru/";
    @Autowired
    private RestTemplate restTemplate;


    public void addIndustries(List<List<String>> industries) {
        for (int i = 0; i < industries.size(); i++) {
            String id = industries.get(i).get(0);
            String category = industries.get(i).get(1);
            String industry = industries.get(i).get(2);
            Industry industryNew = new Industry(id, category, industry);
            industryRepository.save(industryNew);
        }
    }

    public List<List<String>> fetchIndustries() {
        Dotenv dotenv = Dotenv.load();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(dotenv.get("API_HH_TOKEN")));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                API_URL + "industries", HttpMethod.GET, entity, List.class);

        List<List<String>> result = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            for (Object obj : response.getBody()) {
                Map<String, Object> industryGroup = (Map<String, Object>) obj;
                String groupId = (String) industryGroup.get("id");
                String groupName = (String) industryGroup.get("name");
                List<Map<String, String>> industries = (List<Map<String, String>>) industryGroup.get("industries");

                for (Map<String, String> industry : industries) {
                    List<String> entry = List.of(
                            industry.get("id"),
                            groupName,
                            industry.get("name")
                    );
                    result.add(entry);
                }
            }
        }
        return result;
    }
}
