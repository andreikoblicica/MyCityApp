package com.example.community.issue;

import java.util.HashMap;
import java.util.Map;

import static com.example.community.issue.IssueType.*;

public class IssueTypeAssignment {
    private static final Map<IssueType, String> typeInstitutionMap;

    static {
        typeInstitutionMap = new HashMap<>();
        typeInstitutionMap.put(POWER_OUTAGE, "Electrica SA");
        typeInstitutionMap.put(WATER_OUTAGE, "Compania de Apă Someș");
        typeInstitutionMap.put(ROAD_ISSUE, "Primăria Cluj-Napoca");
        typeInstitutionMap.put(GARBAGE_ISSUE, "Supercom SA");
        typeInstitutionMap.put(FLOOD, "Primăria Cluj-Napoca");
        typeInstitutionMap.put(FALLEN_TREE, "Primăria Cluj-Napoca");
        typeInstitutionMap.put(PARK_PROBLEM, "Primăria Cluj-Napoca");
        typeInstitutionMap.put(ABANDONED_VEHICLE, "Primăria Cluj-Napoca");
        typeInstitutionMap.put(DAMAGED_PERSONAL_PROPERTY, "Poliția Municipiului Cluj-Napoca");
        typeInstitutionMap.put(DAMAGED_PUBLIC_PROPERTY, "Primăria Cluj-Napoca");
        typeInstitutionMap.put(OTHER, "Primăria Cluj-Napoca");
    }

    public static String getAssignedInstitution(String type) {
        return typeInstitutionMap.get(IssueType.fromString(type));
    }
}
