#!/bin/sh

git clone git@git.awae.ch:ksmonkey123/cloud.git
cd cloud
mvn clean install -DskipTests
docker-compose build
docker-compose create
docker-compose restart
