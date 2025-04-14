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

## Tips

## Tune simulation configuration

```shell
$ vim src/main/resources/application.properties
```

## Links

- https://docs.gatling.io/reference/integrations/build-tools/maven-plugin/
- https://github.com/gatling/gatling-maven-plugin
- https://github.com/aliesbelik/awesome-gatling
