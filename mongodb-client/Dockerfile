FROM alpine:edge

RUN apk --update add \
        bash \
        expect \
        mongodb \
        mongodb-tools \
    && cd /usr/bin \
    && rm -f mongod \
        mongodump \
        mongoexport \
        mongofiles \
        mongoimport \
        mongoreplay \
        mongos \
        mongostat \
        mongotop \
    && rm -rf /var/cache/apk/*

COPY show_dbs.sh /root/
COPY show_users.sh /root/
