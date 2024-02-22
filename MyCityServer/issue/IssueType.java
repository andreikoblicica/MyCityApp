package com.example.community.issue;



public enum IssueType {
    POWER_OUTAGE("Power Outage"),
    WATER_OUTAGE("Water Outage"),
    STREETLIGHT_OUTAGE("Streetlight Outage"),
    ROAD_ISSUE("Road Issue"),
    GARBAGE_ISSUE("Garbage Issue"),
    FLOOD("Flood"),
    FALLEN_TREE("Fallen Tree"),
    PARK_PROBLEM("Park Problem"),
    ABANDONED_VEHICLE("Abandoned Vehicle"),
    DAMAGED_PERSONAL_PROPERTY("Damaged Personal Property"),
    DAMAGED_PUBLIC_PROPERTY("Damaged Public Property"),
    OTHER("Other");

    private final String text;

    IssueType(String text){
        this.text=text;
    }

    public String getText(){
        return this.text;
    }

    public static IssueType fromString(String text) {
        for (IssueType issueType : IssueType.values()) {
            if (issueType.text.equalsIgnoreCase(text)) {
                return issueType;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return text;
    }
}
