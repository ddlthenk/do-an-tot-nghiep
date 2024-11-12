FROM ubuntu:latest
LABEL authors="devtam"

ENTRYPOINT ["top", "-b"]