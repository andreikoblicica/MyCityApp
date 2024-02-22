package com.example.community.institution;

public class InstitutionBuilder {

    public static InstitutionDTO toDTO(Institution institution){
        return new InstitutionDTO(institution.getId(),
                institution.getName(),
                institution.getAddress(),
                institution.getEmail(),
                institution.getPhoneNumber(),
                institution.getWebsite(),
                institution.getImage());
    }

    public static Institution toEntity(InstitutionDTO institutionDTO){
        return new Institution(institutionDTO.getName(),
                institutionDTO.getAddress(),
                institutionDTO.getEmail(),
                institutionDTO.getPhoneNumber(),
                institutionDTO.getWebsite(),
                institutionDTO.getImage());

    }

    public static InvolvedInstitutionDTO toDTO(InvolvedInstitution involvedInstitution){
        return new InvolvedInstitutionDTO(involvedInstitution.getId(),
                involvedInstitution.getInvolvedInstitutionName(),
                involvedInstitution.getInvolvedUserName(),
                involvedInstitution.getStatus());
    }

    public static InvolvedInstitution toEntity(InvolvedInstitutionDTO involvedInstitutionDTO){
        return new InvolvedInstitution(involvedInstitutionDTO.getInstitutionName());
    }
}
