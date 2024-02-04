## Stage 1 : build with maven builder image with native capabilities
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17@sha256:4726028d4d193a5f47e57ed2d51516864b3c1c30bdb0d26cd671ad893669dfc9 AS build
COPY --chown=quarkus:quarkus ./mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/
USER quarkus
WORKDIR /code
# renovate: datasource=maven depName=org.apache.maven.plugins:maven-dependency-plugin
ARG MVN_DEP_PLUGIN_VERSION=3.6.1
RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:${MVN_DEP_PLUGIN_VERSION}:go-offline
COPY src /code/src
RUN ./mvnw package -Pnative

## Stage 2 : create the docker final image
FROM registry.access.redhat.com/ubi9/ubi-minimal:9.3-1552@sha256:582e18f13291d7c686ec4e6e92d20b24c62ae0fc72767c46f30a69b1a6198055
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
