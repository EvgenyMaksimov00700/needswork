package com.wanted.needswork.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.wanted.needswork.models.City;
import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.Industry;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.repository.EmployerRepository;
import com.wanted.needswork.repository.IndustryRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service


public class HHService {
    @Autowired
    IndustryRepository industryRepository;

    private static final String API_URL = "https://api.hh.ru/";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    EmployerRepository employerRepository;
    @Autowired
    CityService cityService;

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

    public List<Map<String, String>> getArea(List<Map<String, Object>> areas) {
        List<Map<String, String>> city = new ArrayList<>();
        for (Map<String, Object> area : areas) {
            List<Map<String, Object>> currentArea = (List<Map<String, Object>>) area.get("areas");
            if (currentArea.isEmpty()) {
                Map<String, String> data = new HashMap<>();
                data.put("id", (String) area.get("id"));
                data.put("name", (String) area.get("name"));
                city.add(data);
            } else {
                city.addAll(getArea(currentArea));
            }
        }
        return city;
    }

    public List<Map<String, String>> fetchCities() {
        Dotenv dotenv = Dotenv.load();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(dotenv.get("API_HH_TOKEN")));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                API_URL + "areas", HttpMethod.GET, entity, List.class);

        List<Map<String, String>> result = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            result = getArea(response.getBody());
        }
        result.sort(Comparator.comparing(map -> map.get("name")));
        //for (Map<String, String> city: result) {
        //cityService.addCity(Integer.valueOf(city.get("id")), city.get("name"));
        //}
        return result;
    }

    private Vacancy parseVacancy(Map<String, Object> item) {
        BigInteger id = new BigInteger((String) item.get("id"));
        Map<String, Object> employerObject = (Map<String, Object>) item.get("employer");
        String employerName = (String) employerObject.get("name");
        Map<String, Object> logo_urls = (Map<String, Object>) employerObject.get("logo_urls");
        String employerLogo = null;
        if (logo_urls != null) {
            employerLogo = (String) logo_urls.get("original");
        }
        Map<String, Object> contacts = (Map<String, Object>) item.get("contacts");
        if (contacts == null) {
            return null;
        }
        String email = (String) contacts.get("email");
        if (email == null) {
            return null;
        }
        Employer employer = employerRepository.findByEmail(email);
        if (employer == null) {
            employer = new Employer(employerName, employerLogo, email);
        }


        String position = (String) item.get("name");
        Map<String, Object> area = (Map<String, Object>) item.get("area");
        String city = (String) area.get("name");
        Map<String, Object> salary = (Map<String, Object>) item.get("salary");
        Integer fromSalary = null;
        Integer toSalary = null;
        if (salary != null) {
            fromSalary = (Integer) salary.get("from");
            toSalary = (Integer) salary.get("to");
        }
        Map<String, Object> employment = (Map<String, Object>) item.get("schedule");
        String workScheduleValue = (String) employment.get("id");
        String workSchedule;
        Boolean distantWork = false;
        if (Objects.equals(workScheduleValue, "fullDay")) {
            workSchedule = "Полная занятость";
        } else if (Objects.equals(workScheduleValue, "shift")) {
            workSchedule = "Частичная занятость";
        } else if (Objects.equals(workScheduleValue, "remote")) {
            workSchedule = "Удаленная работа";
        } else {
            workSchedule = "Гибкий график";
        }

        List<Map<String, Object>> workFormat = (List<Map<String, Object>>) item.get("work_format");

        for (Map<String, Object> entry : workFormat) {
            if (((String) entry.get("id")) == "REMOTE") {
                distantWork = true;
                break;
            }

        }
        Map<String, Object> experience = (Map<String, Object>) item.get("experience");
        String exp = (String) experience.get("name");
        String address = null;
        Map<String, Object> addressObject = (Map<String, Object>) item.get("address");
        if (addressObject != null) {
            address = (String) addressObject.get("raw");
        }
        Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");
        String description = (String) item.get("description");
        String responsibility_total = "";
        if (snippet != null) {
            String requirement = ((String) snippet.get("requirement"));
            if (requirement != null) {
                responsibility_total = responsibility_total + requirement;
            }
            String responsibility = ((String) snippet.get("responsibility"));
            if (responsibility != null) {
                if (responsibility_total != "") {
                    responsibility_total = responsibility_total + "\n" + "\n";
                }
                responsibility_total = responsibility_total + responsibility;
            }
        } else if (description != null) {
            responsibility_total = description;
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        LocalDateTime createdDateTime = OffsetDateTime.parse((String) item.get("created_at"), formatter).toLocalDateTime();
        LocalDateTime lastModifiedDateTime = OffsetDateTime.parse((String) item.get("published_at"), formatter).toLocalDateTime();
        return new Vacancy(id, employer, null, position,
                city, fromSalary, toSalary, workSchedule, distantWork, address, exp, responsibility_total,
                createdDateTime, lastModifiedDateTime);
    }

    public List<Vacancy> fetchVacancies(String city, String industry, String company, String position, String salary, String experience, String workSchedule, String dateTime, Integer page) {
        Dotenv dotenv = Dotenv.load();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(dotenv.get("API_HH_TOKEN")));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "vacancies?currency=RUR&per_page=100&order_by=publication_time&page="+page;
        String text = "";

        if (!Objects.equals(city, "")) {
            city = city.substring(0, 1).toUpperCase() + city.substring(1);
            City cityObject = cityService.getCityByName(city);
            if (cityObject != null) {
                url += "&area=" + cityObject.getId();
            } else {
                url += "&area=113";
            }
        } else {
            url += "&area=113";
        }
        if (!Objects.equals(industry, "")) {
            url += "&industry=" + industry;

        }
        if (!Objects.equals(company, "")) {
            text += company+" ";

        }

        if (!Objects.equals(salary, "")) {
            url += "&salary=" + salary;

        }

        if (!Objects.equals(position, "")) {
             text+= position;

        }
        if (!text.isEmpty()){
            url += "&text=" + text;

        }

        if (!Objects.equals(experience, "")) {
            if (experience.contains("Нет опыта")) {
                url += "&experience=noExperience";

            }
            if (experience.contains("от 1 года до 3 лет")) {
                url += "&experience=between1And3";
            }
            if (experience.contains("от 3 до 6 лет")) {
                url += "&experience=between3And6";
            }
            if (experience.contains("Более 6 лет")) {
                url += "&experience=moreThan6";
            }

        }

        if (!Objects.equals(workSchedule, "")) {
            if (workSchedule.contains("Полная занятость")) {
                url += "&schedule=fullDay";

            }
            if (workSchedule.contains("Удаленка")) {
                url += "&schedule=remote";
            }
            if (workSchedule.contains("Частичная занятость")) {
                url += "&schedule=shift";
            }
            if (workSchedule.contains("Гибкий график")) {
                url += "&schedule=flexible";
            }

        }

        if (!Objects.equals(dateTime, "")) {
            if (Objects.equals(dateTime, "За месяц")) {
                url += "&period=30";

            }
            if (Objects.equals(dateTime, "За неделю")) {
                url += "&period=7";
            }
            if (Objects.equals(dateTime, "За 3 дня")) {
                url += "&period=3";
            }
            if (Objects.equals(dateTime, "Сутки")) {
                url += "&period=1";
            }


        }
        System.out.println(url);
        ResponseEntity<Map> response = restTemplate.exchange(
                API_URL + url, HttpMethod.GET, entity, Map.class);

        List<Vacancy> result = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

            // Проверяем, есть ли ключ "items"
            if (responseBody.containsKey("items")) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) responseBody.get("items");

                for (Map<String, Object> item : items) {
                    Vacancy vacancy = parseVacancy(item);
                    if (vacancy != null) {
                        result.add(vacancy);
                    }
                }
            }
        }
        return result;
    }

    public List<Vacancy> fetchVacancies() {
        Dotenv dotenv = Dotenv.load();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(dotenv.get("API_HH_TOKEN")));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                API_URL + "vacancies?currency=RUR&per_page=100&order_by=publication_time&area=113", HttpMethod.GET, entity, Map.class);

        List<Vacancy> result = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

            // Проверяем, есть ли ключ "items"
            if (responseBody.containsKey("items")) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) responseBody.get("items");

                for (Map<String, Object> item : items) {
                    Vacancy vacancy = parseVacancy(item);
                    if (vacancy != null) {
                        result.add(vacancy);
                    }
                }
            }
        }
        return result;
    }

    public Vacancy fetchVacancy(BigInteger vacancyId) {
        Dotenv dotenv = Dotenv.load();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(dotenv.get("API_HH_TOKEN")));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL + "vacancies/" + vacancyId, HttpMethod.GET, entity, Map.class);

            return parseVacancy((Map<String, Object>) response.getBody());
        } catch (HttpClientErrorException.NotFound errorException) {
            return null;
        }


    }

    public void sendResponseEmail(String recipientEmail, String buttonUrl, String vacancyName) {
        Dotenv dotenv = Dotenv.load();
        final String senderEmail = "awesome.iocc@mail.ru";
        final String senderPassword = dotenv.get("EMAIL_PASSWORD");
        final String smtpHost = "smtp.mail.ru";
        final String smtpPort = "587";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            // Подставляем название вакансии в тему письма
            message.setSubject("Отклик на вакансию: " + vacancyName);

            // Формируем тело письма с подстановкой названия вакансии
            String htmlContent = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/post.html")), StandardCharsets.UTF_8);
            htmlContent = htmlContent.replace("{{vacancyName}}", vacancyName).replace("{{buttonUrl}}", buttonUrl);
            byte[] imageBytes = Files.readAllBytes(Paths.get("src/main/resources/static/imag/logo.png"));
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            htmlContent = htmlContent.replace("{{logoImage}}", "data:image/png;base64," + base64Image);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println("Письмо отправлено на " + recipientEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Employer> fetchEmployers() {
        Dotenv dotenv = Dotenv.load();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(dotenv.get("API_HH_TOKEN")));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        List<Employer> result = new ArrayList<>();

        int currentPage = 0;
        int totalPages = 1; // будет обновлено после первого запроса

        while (currentPage < totalPages) {
            String url = "https://api.hh.ru/employers?per_page=100&page=" + currentPage;

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

                // обновляем общее число страниц (делается один раз — в первом успешном ответе)
                if (responseBody.containsKey("pages")) {
                    totalPages = ((Number) responseBody.get("pages")).intValue();
                }

                if (responseBody.containsKey("items")) {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) responseBody.get("items");

                    for (Map<String, Object> item : items) {
                        BigInteger id = new BigInteger(item.get("id").toString());
                        String name = String.valueOf(item.get("name"));
                        Employer employer = new Employer(id, name);
                        employerRepository.save(employer);
                        result.add(employer);
                    }
                }
            }

            currentPage++;
        }

        return result;
    }


    public void updateEnvToken(String newToken) {
        Path envPath = Paths.get(".env");

        try {
            List<String> lines = Files.readAllLines(envPath);

            List<String> updatedLines = lines.stream()
                    .map(line -> line.startsWith("API_HH_TOKEN=") ? "API_HH_TOKEN=" + newToken : line)
                    .collect(Collectors.toList());

            Files.write(envPath, updatedLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String updateToken(String code) {
        Dotenv dotenv = Dotenv.load();

        String clientId=dotenv.get("CLIENT_ID");
        String clientSecret=dotenv.get("CLIENT_SECRET");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code); // параметры
        body.add("client_id", clientId); // параметры
        body.add("client_secret", clientSecret); // параметры
        body.add("grant_type", "authorization_code"); // параметры

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                API_URL + "token",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, String> authData = response.getBody();
            String accessToken = authData.get("access_token");
            updateEnvToken(accessToken);
            return accessToken;
        }

        return null;

    }

}



