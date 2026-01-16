package com.ev.battery.agent;

import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class App {
    Dotenv dotenv = Dotenv.load();
    String projectId = dotenv.get("GCLOUD_PROJECT_ID");
    String jiraToken = dotenv.get("JIRA_TOKEN");

    public static EvExpert createAgent(String projectId, String location) {
        VertexAiGeminiChatModel model = VertexAiGeminiChatModel.builder()
            .project(projectId)
            .location(location)
            .modelName("gemini-2.0-flash")
            .temperature(0.0f)
            .topP(0.95f)
            .build();

        VertexAiEmbeddingModel embeddingModel = VertexAiEmbeddingModel.builder()
            .project(projectId)
            .location(location)
            .modelName("text-embedding-004")
            .publisher("google")
            .build();

        InMemoryEmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();
        
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
            .documentSplitter(DocumentSplitters.recursive(500, 50))
            .embeddingModel(embeddingModel)
            .embeddingStore(store)
            .build();
        ingestor.ingest(FileSystemDocumentLoader.loadDocuments("docs/"));

        ContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
            .embeddingModel(embeddingModel)
            .embeddingStore(store)
            .maxResults(5)
            .build();

        return AiServices.builder(EvExpert.class)
            .chatLanguageModel(model)
            .contentRetriever(retriever)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
            .tools(new JiraTicketingTool())
            .build();
    }

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        
        String projectId = dotenv.get("GCLOUD_PROJECT_ID");
        EvExpert agent = createAgent(projectId, "us-central1");

        String telemetry = "CRITICAL DATA: VIN_789, Battery Temp: 55°C, Voltage: 3.1V. Driving mode.";
        String result = agent.chat("Analyze this: " + telemetry + ". If this violates 2026 R1S safety, file a ticket.");
        System.out.println(result);
    }
}
