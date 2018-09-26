#!/bin/bash

echo "Starting application..."

./mvnw spring-boot:run -Dspring-boot.run.arguments="$@"
