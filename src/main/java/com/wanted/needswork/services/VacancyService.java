package com.wanted.needswork.services;
import org.json.JSONArray;
import org.json.JSONObject;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;



import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.Industry;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.repository.VacancyRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service

public class VacancyService {
    @Autowired
    VacancyRepository vacancyRepository;

    public List<Vacancy> getVacancy() {
        return vacancyRepository.findAll();
    }

    public Vacancy getVacancy(BigInteger vacancyId) {
        return vacancyRepository.findById(vacancyId).orElse(null);
    }

    public Vacancy addVacancy(Employer employer_id, Industry industry_id, String position, String city, Integer fromSalary, Integer toSalary,
                              String workShedule, Boolean distantWork, String address, String exp, String responsibility) {
        Vacancy vacancy = new Vacancy(
                employer_id,
                industry_id,
                position,
                city,
                toSalary,
                fromSalary,
                exp,
                responsibility,
                workShedule,
                distantWork,
                address
        );

        // сохраняем в БД
        Vacancy savedVacancy = vacancyRepository.save(vacancy);
        Dotenv dotenv = Dotenv.load();

        // теперь формируем сообщение
        String textMessage = String.format("Опубликована новая вакансия %s!", savedVacancy.getPosition());

        // создаём inline-кнопки
        String requestBody = String.format(
                "{" +
                        "\"chat_id\":\"%d\", " +
                        "\"text\":\"%s\", " +
                        "\"parse_mode\":\"HTML\", " +
                        "\"reply_markup\":{" +
                        "\"inline_keyboard\":[[" +
                        "{\"text\":\"Открыть вакансию\",\"web_app\":{\"url\":\"https://tworker.ru/vacancy/description?id=%d\"}}" +
                        "],[" +
                        "{\"text\":\"Подтвердить\",\"callback_data\":\"vacancy|check|approve|%d\"}," +
                        "{\"text\":\"Отклонить\",\"callback_data\":\"vacancy|check|reject|%d\"}" +
                        "]]" +
                        "}" +
                        "}",
                1537263486,
                textMessage,
                savedVacancy.getId(),
                savedVacancy.getId(),
                savedVacancy.getId()
        );

        System.out.println("Отправляемое тело: " + requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.telegram.org/bot" + dotenv.get("TOKEN") + "/sendMessage"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Ответ от Telegram: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return savedVacancy;
    }


    public Vacancy updateVacancy(Vacancy vacancy, Employer employer_id, Industry industry_id, String position, String city,
                                 Integer fromSalary, Integer toSalary, String workSchedule, Boolean distantWork, String address, String exp, String responsibility) {
        if (employer_id != null) {
            vacancy.setEmployer(employer_id);
        }
        if (industry_id != null) {
            vacancy.setIndustry(industry_id);
        }
        if (position != null) {
            vacancy.setPosition(position);
        }
        if (city != null) {
            vacancy.setCity(city);
        }
        if (toSalary != null) {
            vacancy.setToSalary(toSalary);
        }
        if (fromSalary != null) {
            vacancy.setFromSalary(fromSalary);
        }
        if (exp != null) {
            vacancy.setExp(exp);
        }
        if (responsibility != null) {
            vacancy.setResponsibility(responsibility);
        }
        if (workSchedule != null) {
            vacancy.setWorkSchedule(workSchedule);
        }
        if (distantWork != null) {
            vacancy.setDistantWork(distantWork);
        }
        if (address != null) {
            vacancy.setAddress(address);
        }


        return vacancyRepository.save(vacancy);
    }

    public List<String> getCities() {
        String filePath = "src/main/resources/static/text/cities.txt";

        // Список для хранения городов
        List<String> cityList = new ArrayList<>();

        // Чтение файла


        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                cityList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityList;
    }

    public List<Vacancy> getVacancyUser(BigInteger userId) {
        return vacancyRepository.findAllByEmployer_UserId(userId);
    }

    public void deleteVacancy(BigInteger vacancyId) {
        Optional<Vacancy> vacancy = vacancyRepository.findById(vacancyId);
        if (vacancy.isPresent()) {
            vacancyRepository.delete(vacancy.get());
        } else {
            System.out.println("Vacancy not found with id: " + vacancyId);
        }

    }


    public JSONArray filterVacancyByKeyword(String keyword, List<Vacancy> vacancies) {
        HttpClient client = HttpClient.newHttpClient();
        StringBuilder requestBody = new StringBuilder();
        requestBody.append(String.format("{\"keyword\":\"%s\",\"vacancies\":[", keyword));
        String regex = "\n";
        for (int i = 0; i < vacancies.size(); i++) {
            Vacancy vacancy = vacancies.get(i);
            requestBody.append(String.format("{\"id\":%d,\"vacancy_title\":\"%s\",\"vacancy_description\":\"%s\"}",
                    vacancy.getId(),
                    vacancy.getPosition(),
                    vacancy.getResponsibility().replaceAll(regex," ")
                            .replaceAll("\\\\", "\\\\\\\\")
                            .replaceAll("\"", "\\\\\"")));
            if (i < vacancies.size() - 1) {
                requestBody.append(",");
            }
        }
        requestBody.append("]}");

        System.out.println("Отправляем запрос в модель: " + requestBody);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://212.34.133.246:5000/similarity"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Ответ от Telegram: " + response.body());

            // Парсинг ответа
            if (response != null && response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                if (jsonResponse.has("results")) {
                    JSONArray resultArray = jsonResponse.getJSONArray("results");
                    return resultArray;
                } else {
                    System.out.println("Ответ не содержит ожидаемого массива 'result'.");
                    return null;
                }
            }
            else {
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}

