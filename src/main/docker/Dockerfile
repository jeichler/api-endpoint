## Stage 1 : build with maven builder image with native capabilities
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17@sha256:ac15a3e34fe0d336dcc238381fed4c2832848b15af199a866aac30339fece43d AS build
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
FROM registry.access.redhat.com/ubi9/ubi-minimal:9.5-1730489338@sha256:6907fbacb294ab6ba988f8bcc6bd5127f589966e5808fcb454de3e104983ae5b
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
