## Stage 1 : build with maven builder image with native capabilities
FROM quay.io/quarkus/ubi-quarkus-native-image:22.3-java11@sha256:f64ba34acf265a995ef0e3040e33c4e8457b76ad25736e6023e7e81284aa0616 AS build
COPY --chown=quarkus:quarkus ./mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/
USER quarkus
WORKDIR /code
# renovate: datasource=maven depName=org.apache.maven.plugins:maven-dependency-plugin
RUN ./mvnw -B org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
COPY src /code/src
RUN ./mvnw package -Pnative

## Stage 2 : create the docker final image
FROM registry.access.redhat.com/ubi8/ubi-minimal:latest@sha256:621f5245fb3e8597a626163cdf1229e1f8311e07ab71bb1e9332014b51c59f9c
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
