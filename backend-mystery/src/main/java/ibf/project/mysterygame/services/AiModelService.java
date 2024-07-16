package ibf.project.mysterygame.services;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiImageModel;

@Service
public class AiModelService {

    private static final String OPENAI_CHAT_MODEL_NAME = "gpt-3.5-turbo";
    private static final String OPENAI_IMAGE_MODEL_NAME = "dall-e-2";

    // Testing
    private static final String OLLAMA_CHAT_MODEL_NAME = "llama3";
    private static final String OLLAMA_URL = "http://localhost:11434";

    
    @SessionScope
    public ChatLanguageModel buildOpenAIChatLanguageModel(String apiKey) {
        return OpenAiChatModel.builder()
            .apiKey(apiKey)
            .modelName(OPENAI_CHAT_MODEL_NAME)
            .temperature(1.0)
            .logRequests(true)
            .logResponses(true)
            .build();
    }

    @SessionScope
    public ChatLanguageModel buildOpenAIChatLanguageModelJson(String apiKey) {
        return OpenAiChatModel.builder()
            .apiKey(apiKey)
            .modelName(OPENAI_CHAT_MODEL_NAME)
            .responseFormat("json_object")
            .temperature(0.8)
            .logRequests(true)
            .logResponses(true)
            .build();
    }
    
    @SessionScope
    public ImageModel buildOpenAiImageModel(String apiKey) {
        return OpenAiImageModel.builder()
            .apiKey(apiKey)
            .modelName(OPENAI_IMAGE_MODEL_NAME)
            .responseFormat("b64_json")
            .logRequests(true)
            .logResponses(true)
            .build();
    }

    /* ----------- Testing -------------- */

    public ChatLanguageModel buildOllamaChatLanguageModel() {
        return OllamaChatModel.builder()
            .baseUrl(OLLAMA_URL)
            .modelName(OLLAMA_CHAT_MODEL_NAME)
            .temperature(1.0)
            .build();
    }


    public ChatLanguageModel buildOllamaChatLanguageModelJson() {
        return OllamaChatModel.builder()
            .baseUrl(OLLAMA_URL)
            .modelName(OLLAMA_CHAT_MODEL_NAME)
            .temperature(0.7)
            .format("json_object")
            .build();
    }

}
