package com.example.planaula.security;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest,
                                                  ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        LocalDate localDate = LocalDate.now();
        errorAttributes.put("anio", localDate.getYear());
        errorAttributes.put("appName", "PlanAula");
        errorAttributes.put("supportEmail", "plan.aula.dev@gmail.com");

        return errorAttributes;
    }
}
