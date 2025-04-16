#!/bin/bash
set -e

./mvnw gatling:test -Dgatling.simulationClass=${SIMULATION_CLASS:-io.github.yuokada.simulations.BasicSimulation}

LATEST_DIR=$(ls -td target/gatling/* | head -1)
if [ -n "$S3_BUCKET" ]; then
  aws s3 cp --recursive "$LATEST_DIR" "s3://$S3_BUCKET/gatling-results/$(basename $LATEST_DIR)/"
fi
