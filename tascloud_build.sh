#!/bin/bash

DOCKER_BUILD_SENSOR_DIR="container/tascontainer"
DOCKER_BUILD_DB_DIR="container/tasmariadb"
PRODUCT_NAME="TASCLOUD"

MAKE=make

# webconsole build
make

rm -rf imgtool/CLOUDTAS-V100*

# deb build
make ubuntu

# install file copy for docker build
cp imgtool/CLOUDTAS-V100-R0-rev/* $DOCKER_BUILD_SENSOR_DIR


pushd $DOCKER_BUILD_DB_DIR
docker build --no-cache --tag tas-mariadb:v1.0 .
popd

pushd $DOCKER_BUILD_SENSOR_DIR
docker build --no-cache --tag tascloud:v1.0 .
popd