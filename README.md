## Gatling Benchmarking demo

## Prerequisites

- Java 17 or higher
- TD API key
- Treasuredata account that enables the TPCH catalog

```shell
$ export TD_API_KEY=your_api_key
```

## Run simulations with Gatling Maven Plugin

```shell
./mvnw gatling:test
```
or
```shell
./mvnw clean compile scala:testCompile gatling:test
```

## Run simulations with a docker image

```shell
docker run --rm -it \
  -v "$PWD/results:/app/target/gatling" \
  gatling-runner
```
or

```shell
docker run --rm -it \
  -v "$PWD/results:/app/target/gatling" \
  gatling-runner mvn gatling:test -Dgatling.simulationClass=simulations.BasicSimulation
```

## Tips

## Tune simulation configuration

```shell
$ vim src/main/resources/application.properties
```

## Links

- https://docs.gatling.io/reference/integrations/build-tools/maven-plugin/
- https://github.com/gatling/gatling-maven-plugin
- https://github.com/aliesbelik/awesome-gatling
- https://github.com/gatling/gatling-maven-plugin-demo-java/tree/main
  - https://github.com/gatling/gatling-maven-plugin-demo-java/blob/main/src/test/java/example/BasicSimulation.java
  - https://github.com/gatling/gatling-maven-plugin-demo-java/tree/main/src/test/resources