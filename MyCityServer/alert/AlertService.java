package com.example.community.alert;


import com.example.community.institution.Institution;
import com.example.community.institution.InstitutionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final InstitutionRepository institutionRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository, InstitutionRepository institutionRepository){
        this.alertRepository=alertRepository;
        this.institutionRepository=institutionRepository;
    }

    public Alert findById(Long id) {
        return alertRepository.findById(id)
                .orElse(null);
    }

    public List<AlertDTO> findAll() {
        List<Alert> alerts = alertRepository.findAll();
        return alerts.stream()
                .map(AlertBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public List<AlertDTO> findByInstitutionId(Long id) {
        List<Alert> alerts = alertRepository.findByInstitutionId(id);
        return alerts.stream()
                .map(AlertBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public AlertDTO create(AlertDTO alertDTO) {
        Alert alert= AlertBuilder.toEntity(alertDTO);
        Institution institution=institutionRepository.findByName(alertDTO.getInstitution()).orElse(null);
        alert.setInstitution(institution);
        Alert saved=alertRepository.save(alert);
        return AlertBuilder.toDTO(saved);

    }

    public void delete(Long id){
        alertRepository.deleteById(id);
    }
}
