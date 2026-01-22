package com.wanted.needswork.controllers;

import com.wanted.needswork.DTO.request.VacancyDTO;
import com.wanted.needswork.DTO.response.VacancyResponseDTO;
import com.wanted.needswork.models.City;
import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.Industry;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.services.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@RestController
public class VacancyController {
    @Autowired
    VacancyService vacancyService;
    @Autowired
    IndustryService industryService;
    @Autowired
    EmployerService employerService;
    @Autowired
    HHService hhService;
    @Autowired
    CityService cityService;
    @Autowired
    ViewVacancyService viewVacancyService;

    @GetMapping("/vacancy/showall")
    public ResponseEntity<List<VacancyResponseDTO>> showall() {
        List<Vacancy> vacancies = vacancyService.getVacancy();
        
        // Сортируем вакансии по дате создания (новые сверху)
        vacancies = vacancies.stream()
                .sorted(Comparator.comparing(Vacancy::getCreatedDateTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
        vacancies.addAll(hhService.fetchVacancies());
        List<VacancyResponseDTO> vacancyResponseDTOs = new ArrayList<>();
        for (Vacancy vacancy : vacancies) {
            vacancyResponseDTOs.add(vacancy.toResponseDTO());
        }

        return new ResponseEntity<>(vacancyResponseDTOs, HttpStatus.OK);
    }

    @GetMapping("/vacancy/{vacancyId}")
    public ResponseEntity<VacancyResponseDTO> getVacancyByID(@PathVariable BigInteger vacancyId) {
        Vacancy vacancy = vacancyService.getVacancy(vacancyId);
        if (vacancy == null) {
            vacancy = hhService.fetchVacancy(vacancyId);
        }
        if (vacancy == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Integer views = viewVacancyService.getViewsForVacancy (vacancy.getId());
        return new ResponseEntity<>(vacancy.toResponseDTO(views), HttpStatus.OK);

    }

    @PostMapping("/vacancy")
    public ResponseEntity<Vacancy> addVacancy(@RequestBody VacancyDTO vacancyDTO) {
        Employer employer = employerService.getEmployerByUserId(vacancyDTO.getEmployer_user_id());
        if (employer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Industry industry = industryService.getIndustry(vacancyDTO.getIndustry_id());
        if (industry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vacancyService.addVacancy(employer, industry, vacancyDTO.getPosition().replaceAll("[\"']", ""),
                vacancyDTO.getCity(), vacancyDTO.getFromSalary(), vacancyDTO.getToSalary(), vacancyDTO.getWorkSchedule(), vacancyDTO.getDistantWork(), vacancyDTO.getAddress(), vacancyDTO.getExp(), vacancyDTO.getResponsibility()), HttpStatus.OK);
    }

    @PutMapping("/vacancy/{vacancyId}")
    public ResponseEntity<Vacancy> updateVacancy(@RequestBody VacancyDTO vacancyDTO, @PathVariable BigInteger vacancyId) {
        Vacancy vacancy = vacancyService.getVacancy(vacancyId);
        if (vacancy == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Employer employer = employerService.getEmployerByUserId(vacancyDTO.getEmployer_user_id());
        if (employer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Industry industry = industryService.getIndustry(vacancyDTO.getIndustry_id());
        return new ResponseEntity<>(vacancyService.updateVacancy(vacancy, employer, industry, vacancyDTO.getPosition(),
                vacancyDTO.getCity(), vacancyDTO.getFromSalary(), vacancyDTO.getToSalary(), vacancyDTO.getWorkSchedule(), vacancyDTO.getDistantWork(), vacancyDTO.getAddress(), vacancyDTO.getExp(), vacancyDTO.getResponsibility()),
                HttpStatus.OK);
    }

    @GetMapping("/vacancy/city")
    public ResponseEntity<List<City>> getCities() {
        List<City> cities = cityService.getCities();
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @GetMapping("/vacancy/user/{userId}")
    public ResponseEntity<List<VacancyResponseDTO>> getVacancyByUser(@PathVariable BigInteger userId) {
        List<Vacancy> vacancyUser = vacancyService.getVacancyUser(userId);
        // Сортируем вакансии по дате создания (новые сверху)
        vacancyUser = vacancyUser.stream()
                .sorted(Comparator.comparing(Vacancy::getCreatedDateTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
        return new ResponseEntity<>(vacancyUser.stream().map(Vacancy::toResponseDTO).toList(), HttpStatus.OK);
    }

    @DeleteMapping("/vacancy/{vacancyId}")
    public ResponseEntity<VacancyResponseDTO> deleteVacancyByID(@PathVariable BigInteger vacancyId) {
        vacancyService.deleteVacancy(vacancyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/vacancy/reject/{vacancyId}")
    public ResponseEntity<VacancyResponseDTO> rejectVacancyById(@PathVariable BigInteger vacancyId) {
        Vacancy vacancy = vacancyService.getVacancy(vacancyId);
        String text = "Вакансия удалена, свяжитесь с администратором @cherchent";
        vacancyService.deleteVacancy(vacancyId);
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = String.format("{\"chat_id\":\"%d\", \"text\":\"%s\"}", vacancy.getEmployer().getUser().getId(), text);
        Dotenv dotenv=Dotenv.load();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.telegram.org/bot"+dotenv.get("TOKEN")+"/sendMessage"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Ответ от Telegram: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/vacancy/filter")
    public ResponseEntity<Map<String, Object>> filter(
            @RequestParam(value = "city", required = false, defaultValue = "") String city,
            @RequestParam(value = "industry", required = false, defaultValue = "") String industry,
            @RequestParam(value = "company", required = false, defaultValue = "") String company,
            @RequestParam(value = "position", required = false, defaultValue = "") String position,
            @RequestParam(value = "salary", required = false, defaultValue = "") String salary,
            @RequestParam(value = "exp", required = false, defaultValue = "") String exp,
            @RequestParam(value = "bus", required = false, defaultValue = "") String workSchedule,
            @RequestParam(value = "time", required = false, defaultValue = "") String date,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "cursor", required = false, defaultValue = "0") Integer cursor
    ) {


        List<Vacancy> vacancies = vacancyService.getVacancy();
        // Сортируем вакансии по дате создания (новые сверху)
        vacancies = vacancies.stream()
                .sorted(Comparator.comparing(Vacancy::getCreatedDateTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
        List<String> exps = List.of(exp.split(","));
        List<String> workSchedules = List.of(workSchedule.split(","));
        JSONArray jsonArray = null;
        if (!Objects.equals(position, "")) {
            jsonArray = vacancyService.filterVacancyByKeyword(position, vacancies);
        }
        List<VacancyResponseDTO> vacancyResponseDTOs = new ArrayList<>();
        final int targetPerPage = 20;

        // Локальная дедупликация в пределах одного ответа
        Set<BigInteger> usedIds = new java.util.HashSet<>();
        for (int i = 0; i < vacancies.size(); i++) {
            Vacancy vacancy = vacancies.get(i);
            boolean is_fits = true;
            if (vacancy.getCity() != null && !Objects.equals(city, "") && !Objects.equals(vacancy.getCity(), city.substring(0, 1).toUpperCase() + city.substring(1))) {
                is_fits = false;
            }
            if (vacancy.getIndustry() != null && !Objects.equals(industry, "") && !Objects.equals(vacancy.getIndustry().getId(), industry)) {
                is_fits = false;
            }
            if (vacancy.getEmployer() != null && !Objects.equals(company, "") && !Objects.equals(vacancy.getEmployer().getName(), company)) {
                is_fits = false;
            }
            if (!Objects.equals(position, "") && jsonArray != null && jsonArray.getJSONObject(i).getDouble("final_score") <= 0.6) {
                is_fits = false;
            }
            if (vacancy.getFromSalary() != null && !Objects.equals(salary, "") && vacancy.getFromSalary() < parseInt(salary)) {
                is_fits = false;
            }
            if (!Objects.equals(exp, "") && !exps.contains(vacancy.getExp())) {
                is_fits = false;
            }
            if (!Objects.equals(workSchedule, "") && !workSchedule.equals("Удаленка") && !workSchedules.contains(vacancy.getWorkSchedule()) || workSchedule.equals("Удаленка") && !vacancy.getDistantWork()) {
                is_fits = false;
            }
            if (!Objects.equals(date, "")) {
                LocalDateTime creationDate = vacancy.getCreatedDateTime();
                if (date.equals("Сутки") && ChronoUnit.DAYS.between(creationDate, LocalDateTime.now()) > 1) {
                    is_fits = false;

                }
                if (date.equals("За 3 дня") && ChronoUnit.DAYS.between(creationDate, LocalDateTime.now()) > 3) {
                    is_fits = false;

                }
                if (date.equals("За неделю") && ChronoUnit.DAYS.between(creationDate, LocalDateTime.now()) > 7) {
                    is_fits = false;
                }
                if (date.equals("За месяц") && ChronoUnit.DAYS.between(creationDate, LocalDateTime.now()) > 30) {
                    is_fits = false;
                }
            }

            if (is_fits) {
                if (!usedIds.contains(vacancy.getId()) && vacancyResponseDTOs.size() < targetPerPage) {
                    usedIds.add(vacancy.getId());
                    vacancyResponseDTOs.add(vacancy.toResponseDTO());
                }
            }
        }

        int nextCursor = cursor != null ? cursor : 0;

        if (page == 1) {
            int hhPageIndex = nextCursor;
            while (vacancyResponseDTOs.size() < targetPerPage) {
                try {
                    List<Vacancy> hhList = hhService
                            .fetchVacancies(city, industry, company, position, salary, exp, workSchedule, date, hhPageIndex);
                    if (hhList == null) {
                        break;
                    }
                    for (Vacancy v : hhList) {
                        if (!usedIds.contains(v.getId()) && vacancyResponseDTOs.size() < targetPerPage) {
                            usedIds.add(v.getId());
                            vacancyResponseDTOs.add(v.toResponseDTO());
                        }
                    }
                    hhPageIndex++;
                } catch (ResponseStatusException e) {
                    // Если возникла ошибка о превышении лимита 2000 элементов, возвращаем то, что уже есть
                    if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        break;
                    }
                    throw e;
                }
            }
            nextCursor = hhPageIndex;
        } else {
            List<VacancyResponseDTO> pageResult = new ArrayList<>();
            int hhPageIndex = Math.max(nextCursor, page - 1);
            while (pageResult.size() < targetPerPage) {
                try {
                    List<Vacancy> hhList = hhService
                            .fetchVacancies(city, industry, company, position, salary, exp, workSchedule, date, hhPageIndex);
                    if (hhList == null) {
                        break;
                    }
                    for (Vacancy v : hhList) {
                        if (!usedIds.contains(v.getId()) && pageResult.size() < targetPerPage) {
                            usedIds.add(v.getId());
                            pageResult.add(v.toResponseDTO());
                        }
                    }
                    hhPageIndex++;
                } catch (ResponseStatusException e) {
                    // Если возникла ошибка о превышении лимита 2000 элементов, возвращаем то, что уже есть
                    if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        break;
                    }
                    throw e;
                }
            }
            vacancyResponseDTOs = pageResult;
            nextCursor = hhPageIndex;
        }

        if (vacancyResponseDTOs.size() > targetPerPage) {
            vacancyResponseDTOs = vacancyResponseDTOs.subList(0, targetPerPage);
        }

        Map<String, Object> payload = new java.util.HashMap<>();
        payload.put("items", vacancyResponseDTOs);
        payload.put("cursor", nextCursor);

        return new ResponseEntity<>(payload, HttpStatus.OK);
    }
}

