package com.example.community.facility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDTO {
    private Long id;
    private String type;
    private String name;
    private String description;
    private String location;
    private String websiteLink;
    private String imageUrl;
}
