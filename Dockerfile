# Stage 1: Compile pure Java 11 multilingual domain layer without Android SDK bloat
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /src
COPY app/src/main/java/com/mohavic/tp1_eniad/domain /src/domain
RUN mkdir -p /out \
    && javac --release 11 -encoding UTF-8 -d /out /src/domain/*.java

# Stage 2: Minimal hardened non-root JRE runtime for domain verification
FROM eclipse-temurin:17-jre-alpine AS runtime

RUN addgroup -g 10001 -S appgroup \
    && adduser -u 10001 -S -G appgroup -h /home/appuser appuser

WORKDIR /app
COPY --from=builder --chown=10001:10001 /out /app/classes
COPY --chown=10001:10001 app/src/main/res/values /app/res/values
COPY --chown=10001:10001 app/src/main/res/values-ar /app/res/values-ar
COPY --chown=10001:10001 app/src/main/res/values-en /app/res/values-en

USER 10001:10001

HEALTHCHECK --interval=30s --timeout=5s --start-period=5s --retries=3 \
  CMD java -cp /app/classes com.mohavic.tp1_eniad.domain.DomainHealthCheck || exit 1

CMD ["java", "-cp", "/app/classes", "com.mohavic.tp1_eniad.domain.DomainHealthCheck"]
