package com.wanted.needswork.services;

import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.Industry;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.repository.EmployerRepository;
import com.wanted.needswork.repository.IndustryRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service


public class HHService {
    @Autowired
    IndustryRepository industryRepository;

    private static final String API_URL = "https://api.hh.ru/";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    EmployerRepository employerRepository;


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
        if (email == null){
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
        Map<String, Object> employment = (Map<String, Object>) item.get("employment_form");
        String workScheduleValue = (String) employment.get("name");
        String workSchedule;
        if (workScheduleValue == "Полная") {
            workSchedule = "Полная занятость";
        } else if (workScheduleValue == "Частичная") {
            workSchedule = "Частичная занятость";
        } else if (workScheduleValue == "Проект или разовое задание") {
            workSchedule = "Проект или разовое задание";
        } else if (workScheduleValue == "Вахта") {
            workSchedule = "Вахта";
        } else {
            workSchedule = "Гибкий график";
        }

        List<Map<String, Object>> workFormat = (List<Map<String, Object>>) item.get("work_format");
        Boolean distantWork = false;
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
        } else if(description != null){
            responsibility_total = description;
        }




        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        LocalDateTime createdDateTime = OffsetDateTime.parse((String) item.get("created_at"), formatter).toLocalDateTime();
        LocalDateTime lastModifiedDateTime = OffsetDateTime.parse((String) item.get("published_at"), formatter).toLocalDateTime();
        return new Vacancy(id, employer, null, position,
                city, fromSalary, toSalary, workSchedule, distantWork, address, exp, responsibility_total,
                createdDateTime, lastModifiedDateTime);
    }

    public List<Vacancy> fetchVacancies() {
        Dotenv dotenv = Dotenv.load();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(dotenv.get("API_HH_TOKEN")));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                API_URL + "vacancies?currency=RUR&per_page=100", HttpMethod.GET, entity, Map.class);

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
            String htmlContent = "<!DOCTYPE html>" +
                    "<html lang=\"ru\">" +
                    "<head>" +
                    "  <meta charset=\"UTF-8\">" +
                    "  <title>Отклик на вакансию: " + vacancyName + "</title>" +
                    "  <style>" +
                    "    body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }" +
                    "    .container { background-color: #ffffff; max-width: 600px; margin: 0 auto; padding: 30px; border: 1px solid #dddddd; text-align: center; }" +
                    "    .btn { display: inline-block; padding: 15px 25px; margin-top: 20px; font-size: 16px; color: #ffffff; background-color: #007BFF; text-decoration: none; border-radius: 5px; }" +
                    "    .btn:hover { background-color: #0056b3; }" +
                    "  </style>" +
                    "</head>" +
                    "<body>" +
                    "  <div class=\"container\">" +
                    "    <p>На вакансию <strong>" + vacancyName + "</strong> поступил отклик.</p>" +
                    "    <a href=\"" + buttonUrl + "\" class=\"btn\">Посмотрите резюме соискателя</a>" +
                    "  </div>" +
                    "</body>" +
                    "</html>";
            message.setContent(htmlContent, "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println("Письмо отправлено на " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
