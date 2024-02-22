package com.example.community.event;



public enum EventStatus {
    CREATED("Created"), APPROVED("Approved"), FINISHED("Finished");

    private final String text;

    EventStatus(String text){
        this.text=text;
    }

    public String getText(){
        return this.text;
    }

    public static EventStatus fromString(String text) {
        for (EventStatus eventStatus : EventStatus.values()) {
            if (eventStatus.text.equalsIgnoreCase(text)) {
                return eventStatus;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return text;
    }
}
