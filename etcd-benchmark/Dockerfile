FROM golang:1.13-alpine
RUN apk --update add git ca-certificates \
    && git config --global http.sslVerify false \
    && go get go.etcd.io/etcd/tools/benchmark

FROM alpine:edge
WORKDIR /root/
COPY --from=0 /go/bin/banchmark .
