#!/bin/bash

# Author        : Tony Bian <biantonghe@gmail.com>
# Last Modified : 2020-06-03 12:06
# Filename      : build.sh

set -eux

TOOL_NAME=jenkins
TOOL_VERSION=lts-with-init

DOCKER_HUB=packages.glodon.com
DOCKER_HUB_PROJECT=docker-cornerstoneplatform-releases

DOCKER_HUB_USER=mcdev
DOCKER_HUB_PASSWD="1qaz!QAZ"

docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWD} ${DOCKER_HUB}

docker build -t ${DOCKER_HUB}/${DOCKER_HUB_PROJECT}/${TOOL_NAME}:${TOOL_VERSION} .

docker push ${DOCKER_HUB}/${DOCKER_HUB_PROJECT}/${TOOL_NAME}:${TOOL_VERSION}
