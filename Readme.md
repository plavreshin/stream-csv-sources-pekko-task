# Pekko Http + Streams + Circe task implementation

## 0. Prerequisites

- sbt
- jvm

To run the project you need to have sbt installed.

## Implementation details

### High-level overview

The goal of this task is to expose an endpoint at `/evaluation` accepting multiple repeated `url` parameters.

```sh
GET http://localhost:9000/evaluation?url=https://gist.githubusercontent.com/plavreshin/5379f789f290a6355eecb47688498f12/raw/987f791b2ebc4ce5bcfbc2e915f63eebf0a42724/speakersValid.csv
```

Submitting following query should return a state of the evaluation process:

```json
{
  "mostSpeeches": "Caesare Collins",
  "mostSecurity": "Alexander Abel",
  "leastWordy": "Bernhard Belling"
}
```

### Stack used

- [Pekko-http](https://pekko.apache.org/docs/pekko-http/current/) - for exposing the endpoint
- [Pekko-streams](https://pekko.apache.org/docs/pekko-streams/current/) - for processing the data
- [Circe](https://circe.github.io/circe/) - for json serialization/deserialization
- [Kantan.csv](https://nrinaudo.github.io/kantan.csv/) - for csv parsing

### Tests

There is one test implemented for the `Resource` endpoint. It is located in `ResourceSpec.scala` file.
The goal is to test the endpoint with different csv files and check that the response is correct.

## Running locally

This project has sbt-revolver plugin to start & reload application on code changes.

To start application in development mode run:

```shell
sbt "~reStart"
```

### Project tree

Following is the project structure/tree layout:

```shell
├── src
│   ├── main
│   │   ├── resources
│   │   │   ├── application.conf
│   │   │   └── logback.xml
│   │   └── scala
│   │       └── com
│   │           └── github
│   │               └── plavreshin
│   │                   ├── app
│   │                   │   ├── Main.scala
│   │                   │   ├── Runtime.scala
│   │                   │   └── wiring
│   │                   │       ├── ServiceWiring.scala
│   │                   │       └── WebWiring.scala
│   │                   ├── domain
│   │                   │   ├── CsvFileError.scala
│   │                   │   ├── SpeechItem.scala
│   │                   │   └── SpeechStats.scala
│   │                   ├── model
│   │                   │   └── SpeechStatsJson.scala
│   │                   ├── service
│   │                   │   ├── SpeechService.scala
│   │                   │   ├── csv
│   │                   │   │   └── CsvParser.scala
│   │                   │   └── speech
│   │                   └── web
│   │                       ├── JsonCodecs.scala
│   │                       └── Resource.scala
│   └── test
│       ├── resources
│       │   ├── headerOnly.csv
│       │   ├── valid.csv
│       │   ├── valid2.csv
│       │   └── valid3.csv
│       └── scala
│           └── com
│               └── github
│                   └── plavreshin
│                       └── web
│                           └── ResourceSpec.scala
```
