FROM golang:1.13-alpine
RUN go get go.etcd.io/etcd/tools/benchmark
FROM alpine:edge
WORKDIR /root/
COPY --from=0 /go/bin/banchmark .
