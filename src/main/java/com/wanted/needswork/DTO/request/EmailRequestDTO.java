package com.wanted.needswork.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
 public class EmailRequestDTO {
    @Getter
    String email;
    @Getter
    Integer responseID;
    @Getter
    String vacancyName;
}
