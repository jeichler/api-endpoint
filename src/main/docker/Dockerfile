## Stage 1 : build with maven builder image with native capabilities
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17@sha256:c63c50959eb0a9c48f54a25040b1d5f767186b086590a6c727e7dac4042bf779 AS build
COPY --chown=quarkus:quarkus ./mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/
USER quarkus
WORKDIR /code
# renovate: datasource=maven depName=org.apache.maven.plugins:maven-dependency-plugin
ARG MVN_DEP_PLUGIN_VERSION=3.6.0
RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:${MVN_DEP_PLUGIN_VERSION}:go-offline
COPY src /code/src
RUN ./mvnw package -Pnative

## Stage 2 : create the docker final image
FROM registry.access.redhat.com/ubi9/ubi-minimal:9.2-717@sha256:35c99977ee5baa359bdc80f9ccc360644d2dbccb7462ca0fd97a23170a00cfd1
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
