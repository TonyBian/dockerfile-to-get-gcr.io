#!/bin/bash

# Author        : Tony Bian <biantonghe@gmail.com>
# Last Modified : 2020-05-06 06:07
# Filename      : build.sh

TOOL_NAME=mongodb
TOOL_VERSION=v4.0.6

DOCKER_HUB=packages.glodon.com
DOCKER_HUB_PROJECT=docker-cornerstoneplatform-releases

DOCKER_HUB_USER=mcdev
DOCKER_HUB_PASSWD="1qaz!QAZ"

docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWD} ${DOCKER_HUB}

docker build -t ${DOCKER_HUB}/${DOCKER_HUB_PROJECT}/${TOOL_NAME}:${TOOL_VERSION} .

docker push ${DOCKER_HUB}/${DOCKER_HUB_PROJECT}/${TOOL_NAME}:${TOOL_VERSION}
