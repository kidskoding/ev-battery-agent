package com.ev.battery.agent;

import java.util.List;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class App  {
    public static void main(String[] args) {
        String projectId = "PROJECT_ID";
        String location = "us-central1";

        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
            .project(projectId)
            .location(location)
            .modelName("gemini-2.0-flash")
            .build();

        VertexAiEmbeddingModel embeddingModel = VertexAiEmbeddingModel.builder()
            .project(projectId)
            .location(location)
            .modelName("text-embedding-004")
            .build();

        InMemoryEmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
            .embeddingModel(embeddingModel)
            .embeddingStore(store)
            .build();

        List<Document> documents = FileSystemDocumentLoader.loadDocuments("docs/");
        ingestor.ingest(documents);

        ContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
            .embeddingModel(embeddingModel)
            .embeddingStore(store)
            .maxResults(5)
            .build();

        EvExpert agent = AiServices.builder(EvExpert.class)
            .chatLanguageModel(model)
            .contentRetriever(retriever)
            .build();

        String response = agent.chat("What is the battery temperature limit for 2026 R1S?");
        System.out.println(response);
    }
}
