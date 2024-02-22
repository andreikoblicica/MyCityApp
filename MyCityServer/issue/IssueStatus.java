package com.example.community.issue;

public enum IssueStatus {
    OPENED("Opened"), CLOSED("Closed"), ASSIGNED("Assigned"), IN_PROGRESS("In Progress");

    private final String text;

    IssueStatus(String text){
        this.text=text;
    }

    public String getText(){
        return this.text;
    }

    public static IssueStatus fromString(String text) {
        for (IssueStatus accountType : IssueStatus.values()) {
            if (accountType.text.equalsIgnoreCase(text)) {
                return accountType;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        return text;
    }
}
