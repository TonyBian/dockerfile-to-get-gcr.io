#!/bin/bash

# Author        : Tony Bian <biantonghe@gmail.com>
# Last Modified : 2021-01-05 14:01
# Filename      : image_pull.sh

PRIVATE_URL=tonybian
PUBLIC_URL=k8s.gcr.io/sig-storage

IMAGES="
csi-node-driver-registrar:v2.0.1
csi-provisioner:v2.0.0
csi-snapshotter:v3.0.0
csi-attacher:v3.0.0
csi-resizer:v1.0.0
"

for image in ${IMAGES}; do
    docker pull ${PRIVATE_URL}/$image
    docker tag ${PRIVATE_URL}/$image ${PUBLIC_URL}/$image
    docker rmi ${PRIVATE_URL}/$image
done
