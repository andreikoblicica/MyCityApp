package com.example.community.institution;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionDTO {
    private Long id;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private String website;
    private String image;
}
