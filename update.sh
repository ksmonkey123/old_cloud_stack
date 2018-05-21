#!/bin/sh

mvn clean install -DskipTests
docker-compose build
docker-compose create
docker-compose restart
