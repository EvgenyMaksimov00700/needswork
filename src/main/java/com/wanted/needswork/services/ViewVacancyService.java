package com.wanted.needswork.services;

import com.wanted.needswork.models.CurrentState;
import com.wanted.needswork.models.User;
import com.wanted.needswork.models.ViewVacancy;
import com.wanted.needswork.repository.ViewVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ViewVacancyService {
    @Autowired
    ViewVacancyRepository viewVacancyRepository;

    public List<ViewVacancy> getLastViewVacancies(Integer limit) {
        return viewVacancyRepository.findAll().stream()
                .sorted((v1, v2) -> v2.getViewDateTime().compareTo(v1.getViewDateTime())).limit(limit)
                .collect(Collectors.toList());
    }

    public ViewVacancy addViewVacancy(User user, BigInteger vacancyId) {
        ViewVacancy viewVacancy = new ViewVacancy(user, vacancyId);
        return viewVacancyRepository.save(viewVacancy);
    }

    public ViewVacancy deleteViewVacancy(ViewVacancy viewVacancy) {
        viewVacancyRepository.delete(viewVacancy);
        return viewVacancy;
    }

    public List<ViewVacancy> getViewVacancies(Integer limit, BigInteger userId, BigInteger vacancyId) {
        Stream<ViewVacancy> viewVacancies = viewVacancyRepository.findAll().stream();
        if (userId != null) {
            viewVacancies = viewVacancies.filter(v -> v.getUser().getId().equals(userId));
        }
        if (vacancyId != null) {
            viewVacancies = viewVacancies.filter(v -> v.getVacancyId().equals(vacancyId));
        }
        viewVacancies = viewVacancies.sorted((v1, v2) -> v2.getViewDateTime().compareTo(v1.getViewDateTime()));
        if (limit != null) {
            viewVacancies = viewVacancies.limit(limit);
        }
        return viewVacancies.collect(Collectors.toList());
    }

    public List<ViewVacancy> getForLastSeconds(Integer seconds, BigInteger userId, BigInteger vacancyId) {
        LocalDateTime now = LocalDateTime.now();
        return viewVacancyRepository.findByUserIdAndVacancyId(userId, vacancyId).stream()
                .filter(viewVacancy ->
                        Duration.between(viewVacancy.getViewDateTime(), now)
                                .getSeconds() <= seconds
                )
                .collect(Collectors.toList());
    }
    public Integer getViewsForVacancy (BigInteger vacancyId) {
        return viewVacancyRepository.countByVacancyId(vacancyId);
    }

}

