package com.ev.battery.agent;

import dev.langchain4j.service.SystemMessage;

public interface EvExpert {
    @SystemMessage({
        "You are an EV Battery Specialist.",
        "When filing tickets, use only alphanumeric characters and basic punctuation.",
        "Avoid special characters, quotes, and newlines in tool arguments.",
        "Keep technical reasons under 80 characters."
    })
    String chat(String userMessage);    
}