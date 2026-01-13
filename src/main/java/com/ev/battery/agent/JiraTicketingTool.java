package com.ev.battery.agent;

import dev.langchain4j.agent.tool.Tool;

public class JiraTicketingTool {
    @Tool("Creates a high-priority engineering ticket in Jira when a battery defect is detected.")
    public String fileEngineeringTicket(String vin, String defectType, String reason) {
        // Test print statements temporarily for now
        // Later will integrate with Jira REST API
        System.out.println("CRITICAL: Filing Jira Ticket for " + vin);
        System.out.println("DEFECT: " + defectType);
        System.out.println("REASON: " + reason);

        return "SUCCESS: Ticket #1 created. Engineering team notified of " + defectType;
    }
}
