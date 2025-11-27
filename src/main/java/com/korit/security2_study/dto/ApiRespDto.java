package com.korit.security2_study.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiRespDto <T>{
    private String status;
    private String message;
    private T date;
}
