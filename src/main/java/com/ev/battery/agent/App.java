package com.ev.battery.agent;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class App  {
    public static void main(String[] args) {
        ChatLanguageModel model = VertexAiGeminiChatModel.builder()
            .project("PROJECT_ID")
            .location("us-central1")
            .modelName("gemini-2.0-flash")
            .build();

        VertexAiEmbeddingModel embeddingModel = VertexAiEmbeddingModel.builder()
            .project("PROJECT_ID")
            .location("us-central-1")
            .modelName("text-embedding-004")
            .build();

        InMemoryEmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();

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
