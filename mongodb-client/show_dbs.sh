#!/bin/bash

# Author        : Tony Bian <biantonghe@gmail.com>
# Last Modified : 2020-05-06 08:43
# Filename      : show_dbs.sh

MONGO_HOST=$1

expect <<EOF
spawn mongo --host ${MONGO_HOST}
expect {
 "*>" { send "show dbs\r" }
}
expect eof
EOF
