## Stage 1 : build with maven builder image with native capabilities
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17@sha256:7dd118e1afc9db82c962d87c54dbb5ede0b40517605a98c836efcff3ce578865 AS build
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
FROM registry.access.redhat.com/ubi9/ubi-minimal:9.3-1475@sha256:a340f4b9fb261a75c84666a3dccb88e193a116da3cebabaf9bcdc33609b61172
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
