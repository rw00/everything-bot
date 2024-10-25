# AI Agent Guide for Everything Bot

## Architecture Overview
Everything Bot is a modular Telegram Bot platform built with
**Java 21** and **Spring Boot 3.5+**.

### Core Components
- **Bot Framework**: Custom abstract layer for different bot types:
    - `AbstractSelfRegisteringBot`: Base for all bots. Handles registration with `BotRegistrar`.
    - `AbstractChannelCronBasedBot`: For periodic updates to a channel via Cron.
    - `AbstractUserInteractionBasedBot`: For command-based user interaction.
- **BotRegistrar**: Manages the lifecycle and graceful shutdown of all bots.
- **BotBuilder**: Fluent API for configuring bot instances.
- **Spring AI**: Integrated with Ollama for dynamic content generation.
- **Infrastructure**: Redis for state persistence, Jsoup for scraping.

## Rules for AI Agents
1. **Be Concise**: Avoid verbosity. No trivial comments.
2. **Quality First**: Follow SOLID principles and Clean Code practices.
3. **Java 21 Patterns**: Use modern Java features (Records, Switch Expressions, Pattern Matching).
4. **Spring Best Practices**:
    - Use constructor injection.
    - Use `@RequiredArgsConstructor` (Lombok) for cleaner code.
    - Use `@Value` for configuration.
5. **Testing**: Always write tests to ensure quality.
6. **Bot Creation**:
    - Extend `AbstractChannelCronBasedBot` for push notifications.
    - Extend `AbstractUserInteractionBasedBot` for interactive commands.
    - Register new bots with `@Component`.

## Tech Debt & Suggestions
- **Configuration**: The manual builder pattern in `BotConfig` can be replaced with Lombok `@SuperBuilder` to reduce boilerplate.
- **Strategy Pattern**: The `commandFunctions` Map in `AbstractUserInteractionBasedBot` works well but could be refactored into a Strategy pattern for complex command sets.
