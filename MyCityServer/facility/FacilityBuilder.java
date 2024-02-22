package com.example.community.facility;


public class FacilityBuilder {
    public static FacilityDTO toDTO(Facility facility){
        return new FacilityDTO(facility.getId(),
                facility.getType(),
                facility.getName(),
                facility.getDescription(),
                facility.getLocation(),
                facility.getWebsiteLink(),
                facility.getImageUrl());
    }

    public static Facility toEntity(FacilityDTO facilityDTO){
        return new Facility(facilityDTO.getType(),
                facilityDTO.getName(),
                facilityDTO.getLocation(),
                facilityDTO.getDescription(),
                facilityDTO.getWebsiteLink(),
                facilityDTO.getImageUrl());
    }
}
