package com.example.community.facility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;

    @Autowired
    public FacilityService(FacilityRepository facilityRepository){
        this.facilityRepository=facilityRepository;
    }

    public Facility find(Long id) {
        return facilityRepository.findById(id)
                .orElse(null);
    }

    public FacilityDTO findById(Long id){
        return FacilityBuilder.toDTO(find(id));
    }

    public List<FacilityDTO> findAll() {
        List<Facility> facilities = facilityRepository.findAll();
        return facilities.stream()
                .map(FacilityBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public List<FacilityDTO> findByType(String type) {
        List<Facility> facilities = facilityRepository.findByType(type);
        return facilities.stream()
                .map(FacilityBuilder::toDTO)
                .collect(Collectors.toList());
    }


    public FacilityDTO create(FacilityDTO facilityDTO) {
        Facility facility= FacilityBuilder.toEntity(facilityDTO);
        Facility saved=facilityRepository.save(facility);
        return FacilityBuilder.toDTO(saved);

    }

    public FacilityDTO update(FacilityDTO facilityDTO) {
        Facility facility = find(facilityDTO.getId());
       facility.setName(facilityDTO.getName());
       facility.setType(facilityDTO.getType().toString());
       facility.setDescription(facilityDTO.getDescription());
       facility.setLocation(facilityDTO.getLocation());
       facility.setWebsiteLink(facility.getWebsiteLink());
       facility.setImageUrl(facilityDTO.getImageUrl());
       return FacilityBuilder.toDTO(facilityRepository.save(facility));
    }

    public void delete(Long id){
        facilityRepository.deleteById(id);
    }
}
