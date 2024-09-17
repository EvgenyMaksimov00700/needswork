package com.wanted.needswork.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class WebUpController {
    @GetMapping("/employer/reg/")
    public String getEmployerReg() {
        return "reg_employer";

    }

    @GetMapping("/employer/lk/")
    public String getEmployerLk() {
        return "lk_employer";
    }

    @GetMapping("/employer/vacancy/create")
    public String getCreateVacancyForm() {
        return "create_vacancy_form";
    }

    @GetMapping("/employer/my_vacancy7/show")
    public String getMyVacancy7() {
        return "my_vacancy7";
    }

    @GetMapping("/employer/reg_employer1/reg")
    public String getRegEmployer1() {
        return "reg_employer1";
    }

    @GetMapping("/employer/responses7/show")
    public String getResponses7() {
        return "responses7";
    }

    @GetMapping("/employer/vacancy/description")
    public String getDescription() {
        return "description_vacancy";
    }

    @GetMapping("/employer/vacancy/edit")
    public String editVacancy() {
        return "create_vacancy_edit";
    }
}
