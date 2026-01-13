package com.ev.battery.agent;

import dev.langchain4j.service.SystemMessage;

public interface EvExpert {
    @SystemMessage({
        "You are a professional Rivian EV Battery Specialist.",
        "Use the provided documentation to answer technical questions.",
        "If the answer isn't in the docs, politely state you don't have that specific data.",
        "Always specify which manual (e.g., 2022 vs 2026) you are referencing."
    })
    String chat(String userMessage);    
}