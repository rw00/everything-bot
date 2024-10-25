package com.rw.bots.everything.bot.impl.middaywalk.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rw.bots.everything.bot.common.SilentOperator;
import com.rw.bots.everything.bot.impl.middaywalk.entity.WeatherNowSummary;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AiMessageGenerator {
    private static final String GENERAL_PROMPT =
            """
                    Generate a message that encourages the reader to go out for a walk on a beautiful sunny day.
                    The tone should be positive and uplifting. It should not be longer than 2-3 sentences.
                    The message may mention the current weather conditions that are provided as input.
                    The output must be formatted as valid JSON.
                    Example output:

                    {
                    "output": "The weather is lovely today! It's 13°C and sunny. Enjoy the fresh air outside!"
                    }
                    """;
    private final OllamaChatModel chatClient;
    private final ObjectMapper objectMapper;

    public String generateMessage(WeatherNowSummary weatherNowSummary) throws Exception {
        UserMessage current = new UserMessage("Current weather conditions: "
                + objectMapper.writeValueAsString(weatherNowSummary));
        Prompt prompt = new Prompt(List.of(new SystemMessage(GENERAL_PROMPT), current));

        ChatResponse response = SilentOperator.call(() -> chatClient.call(prompt));

        return Optional.ofNullable(response).map(ChatResponse::getResult).map(Generation::getOutput)
                .map(AssistantMessage::getText).map(this::parseJson)
                .map(jsonNode -> jsonNode.get("output")).map(JsonNode::asText).orElse(null);
    }

    private JsonNode parseJson(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            log.warn("Failed to parse JSON: {}", json, e);
            return null;
        }
    }
}
