package com.wanted.needswork.services;

import com.wanted.needswork.DTO.response.EmployerResponseDTO;
import com.wanted.needswork.DTO.response.VacancyResponseDTO;
import com.wanted.needswork.models.Industry;
import com.wanted.needswork.repository.IndustryRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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

    public List<VacancyResponseDTO> fetchVacancies() {
        Dotenv dotenv = Dotenv.load();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(dotenv.get("API_HH_TOKEN")));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                API_URL + "vacancies", HttpMethod.GET, entity, Map.class);

        List<VacancyResponseDTO> result = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

            // Проверяем, есть ли ключ "items"
            if (responseBody.containsKey("items")) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) responseBody.get("items");

                for (Map<String, Object> item : items) {
                    BigInteger id = new BigInteger ((String) item.get("id"));
                    Map<String, Object> employer = (Map<String, Object>) item.get("employer");
                    String employerName = (String) employer.get("name");
                    Map<String, Object> logo_urls = (Map<String, Object>) employer.get("logo_urls");
                    String employerLogo = (String) logo_urls.get("original");
                    EmployerResponseDTO employeeResponseDTO = new EmployerResponseDTO(employerName, employerLogo);
                    String position = (String) item.get("name");
                    Map<String, Object> area = (Map<String, Object>) item.get("area");
                    String city = (String) area.get("city");
                    Map<String, Object> salary = (Map<String, Object>) item.get("salary");
                    Integer fromSalary = null;
                    Integer toSalary = null;
                    if (salary != null) {
                        fromSalary = (Integer) salary.get("from");
                        toSalary = (Integer) salary.get("to");
                    }
                    Map<String, Object> employment = (Map<String, Object>) item.get("employment_form");
                    String workScheduleValue = (String) employment.get("name");
                    String workSchedule;
                    if (workScheduleValue=="Полная") {
                        workSchedule = "Полная занятость";
                    }
                    else if (workScheduleValue=="Частичная") {
                        workSchedule = "Частичная занятость";
                    }
                    else if (workScheduleValue=="Проект или разовое задание") {
                        workSchedule = "Проект или разовое задание";
                    }
                    else if (workScheduleValue=="Вахта") {
                        workSchedule = "Вахта";
                    }
                    else {
                        workSchedule = "Гибкий график";
                    }

                    List<Map<String, Object>> workFormat = (List<Map<String, Object>>) item.get("work_format");
                    Boolean distantWork = false;
                    for (Map<String, Object> entry : workFormat) {
                        if (((String) entry.get("id")) == "REMOTE"){
                            distantWork = true;
                            break;
                        }



                    }
                    Map<String, Object> experience = (Map<String, Object>) item.get("experience");
                    String exp = (String) experience.get("name");
                    String address = null;
                    Map<String, Object> addressObject = (Map<String, Object>) item.get("address");
                    if (addressObject!= null) {
                        address = (String) addressObject.get("raw");
                    }
                    Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");
                    String responsibility = ((String) snippet.get("requirement"))+"\n\n"+ ((String) snippet.get("responsibility"));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
                    LocalDateTime createdDateTime = OffsetDateTime.parse((String) item.get("created_at"), formatter).toLocalDateTime();
                    LocalDateTime lastModifiedDateTime = OffsetDateTime.parse((String) item.get("published_at"), formatter).toLocalDateTime();
                    result.add( new VacancyResponseDTO(id, employeeResponseDTO, null, position,
                            city, fromSalary, toSalary, workSchedule, distantWork, address, exp, responsibility,
                             createdDateTime, lastModifiedDateTime, true));
                }
            }
        }
        return result;
    }
}
