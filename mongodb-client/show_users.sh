#!/bin/bash

# Author        : Tony Bian <biantonghe@gmail.com>
# Last Modified : 2020-05-07 01:50
# Filename      : show_users.sh

MONGO_HOST=$1
MONGO_DB=$2

expect <<EOF
spawn mongo --host ${MONGO_HOST}
expect {
 "*>" { send "use ${MONGO_DB}\rshow users\r" }
}
expect eof
EOF
