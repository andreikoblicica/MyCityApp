package com.example.community.facility;



public enum FacilityType {
    PARK("Park"), MALL("Mall"), LIBRARY("Library"), CINEMA("Cinema"), SPORTS_FACILITY("Sports Facility"),
    MUSEUM("Museum"),ATTRACTION("Attraction");

    private final String text;

    FacilityType(String text){
        this.text=text;
    }

    public String getText(){
        return this.text;
    }

    public static FacilityType fromString(String text) {
        for (FacilityType facilityType : FacilityType.values()) {
            if (facilityType.text.equalsIgnoreCase(text)) {
                return facilityType;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return text;
    }
}
