## Stage 1 : build with maven builder image with native capabilities
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17@sha256:b411db6a6f8125d90bf644d4501b2e1e1c52ad926896d2c03d5e1fb9faa51470 AS build
COPY --chown=quarkus:quarkus ./mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/
USER quarkus
WORKDIR /code
# renovate: datasource=maven depName=org.apache.maven.plugins:maven-dependency-plugin
ARG MVN_DEP_PLUGIN_VERSION=3.8.1
RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:${MVN_DEP_PLUGIN_VERSION}:go-offline
COPY src /code/src
RUN ./mvnw package -Pnative

## Stage 2 : create the docker final image
FROM registry.access.redhat.com/ubi9/ubi-minimal:9.5-1736404155@sha256:b87097994ed62fbf1de70bc75debe8dacf3ea6e00dd577d74503ef66452c59d6
LABEL org.opencontainers.image.source="https://github.com/jeichler/api-endpoint"
WORKDIR /work/
COPY --from=build /code/target/*-runner /work/application

# set up permissions for user `1001`
RUN chmod 775 /work /work/application \
  && chown -R 1001 /work \
  && chmod -R "g+rwX" /work \
  && chown -R 1001:root /work

EXPOSE 8080
USER 1001

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
