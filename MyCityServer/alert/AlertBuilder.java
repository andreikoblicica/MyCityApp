package com.example.community.alert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AlertBuilder {
    public static AlertDTO toDTO(Alert alert){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", Locale.ENGLISH);
        return new AlertDTO(alert.getId(),
                alert.getInstitution()!=null ? alert.getInstitution().getName() : "MyCity App",
                alert.getTitle(),
                alert.getDescription(),
                alert.getDateTime().format(formatter));
    }

    public static Alert toEntity(AlertDTO alertDTO){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return new Alert(alertDTO.getTitle(),
                alertDTO.getDescription(),
                LocalDateTime.parse(alertDTO.getDateTime(), formatter));
    }
}
