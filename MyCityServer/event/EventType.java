package com.example.community.event;



public enum EventType {
    CONCERT("Concert"),
    PARTY("Party"),
    FESTIVAL("Festival"),
    SHOW("Show"),
    SPORTS("Sports"),
    EXHIBITION("Exhibition"),
    BOOK_LAUNCH("Book Launch"),
    SOCIAL("Social"),
    EDUCATIONAL("Educational"),
    BUSINESS("Business"),
    MOVIE("Movie"),
    DIVERSE("Diverse");

    private final String text;

    EventType(String text){
        this.text=text;
    }

    public String getText(){
        return this.text;
    }

    public static EventType fromString(String text) {
        for (EventType eventType : EventType.values()) {
            if (eventType.text.equalsIgnoreCase(text)) {
                return eventType;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return text;
    }
}
