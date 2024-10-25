CALL mvnw clean verify

SET version=0.0.5

CALL docker build -t rw00/everything-bot:%version% .

CALL docker push rw00/everything-bot:%version%
