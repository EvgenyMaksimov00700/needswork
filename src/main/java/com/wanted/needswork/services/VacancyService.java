package com.wanted.needswork.services;


import com.wanted.needswork.models.Employer;
import com.wanted.needswork.models.Industry;
import com.wanted.needswork.models.Vacancy;
import com.wanted.needswork.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class VacancyService {
    @Autowired
    VacancyRepository vacancyRepository;

    public List<Vacancy> getVacancy() {
        return vacancyRepository.findAll();
    }

    public Vacancy getVacancy(Integer vacancyId) {
        return vacancyRepository.findById(vacancyId).orElse(null);
    }

    public Vacancy addVacancy(Employer employer_id, Industry industry_id, String position, String city, Integer fromSalary, Integer toSalary,
                              String workShedule, Boolean distantWork, String address, String exp, String responsibility) {
        Vacancy vacancy = new Vacancy(employer_id, industry_id, position, city, toSalary, fromSalary, exp, responsibility, workShedule, distantWork, address);
        return vacancyRepository.save(vacancy);
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

    public List<Vacancy> getVacancyUser(Integer userId) {
        return vacancyRepository.findAllByEmployer_UserId(userId);
    }

    public void deleteVacancy(Integer vacancyId) {
    Optional<Vacancy> vacancy = vacancyRepository.findById(vacancyId);
    if (vacancy.isPresent()) {
            vacancyRepository.delete(vacancy.get());
        } else {
            System.out.println("Vacancy not found with id: " + vacancyId);
        }

    }
    /**
     * Вычисляет расстояние Левенштейна между двумя строками.
     *
     * @param str1 первая строка
     * @param str2 вторая строка
     * @return расстояние Левенштейна
     */
    public static int levenshteinDistance(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + (str1.charAt(i - 1) == str2.charAt(j - 1) ? 0 : 1)
                    );
                }
            }
        }
        return dp[len1][len2];
    }

    /**
     * Вычисляет коэффициент схожести между двумя строками.
     *
     * @param str1 первая строка
     * @param str2 вторая строка
     * @return коэффициент схожести (от 0.0 до 1.0)
     */
    public static double similarity(String str1, String str2) {
         str1 = str1.toLowerCase().trim();
         str2 = str1.toLowerCase().trim();
        if (str1.isEmpty() && str2.isEmpty()) {
            return 1.0;
        }
        if (str2.contains(str1)) {
            return 1.0;
        }
        int maxLength = Math.max(str1.length(), str2.length());
        if (maxLength == 0) {
            return 1.0;
        }
        int distance = levenshteinDistance(str1, str2);
        return 1.0 - (double) distance / maxLength;
    }
}


