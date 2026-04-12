FROM eclipse-temurin:24

VOLUME /app/data/runtime/

RUN apt-get update
RUN apt-get install unzip
RUN wget https://www.yourkit.com/download/docker/YourKit-JavaProfiler-2025.3-docker.zip -P /tmp/ && \
  unzip /tmp/YourKit-JavaProfiler-2025.3-docker.zip -d /usr/local && \
  rm /tmp/YourKit-JavaProfiler-2025.3-docker.zip

ADD kronos-boot/build/install/kronos-boot /app
ADD data/ /app/data
ADD server.properties /app/server.properties
WORKDIR /app

EXPOSE 43594:43594
ENV KRONOS_BOOT_OPTS='"-Xmx16g" "-agentpath:/usr/local/YourKit-JavaProfiler-2025.3/bin/linux-x86-64/libyjpagent.so=port=10001,listen=all"'
CMD ["bin/kronos-boot"]
