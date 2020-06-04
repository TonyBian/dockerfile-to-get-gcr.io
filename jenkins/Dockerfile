FROM jenkins/jenkins:lts

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN set -e \
        && /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

ENV JENKINS_ADMIN_USER=admin
ENV JENKINS_ADMIN_PASSWD=123456

COPY init_login.groovy /usr/share/jenkins/ref/init.groovy.d/set-user-security.groovy

ENV DOCKER_PLUGIN_IMAGE=jenkins/jnlp-slave:alpine
# ENV DOCKER_PLUGIN_VOLUMESSTRING=/tmp/jenkins/workspace:/tmp/jenkins/workspace
ENV DOCKER_PLUGIN_VOLUMESSTRING=/var/run/docker.sock:/var/run/docker.sock:ro
ENV DOCKER_PLUGIN_LABELSTRING=jnlp-docker-slave

COPY init_docker_plugin.groovy /usr/share/jenkins/ref/init.groovy.d/init_docker_plugin.groovy

HEALTHCHECK --interval=6s --timeout=3s --retries=10 \
    CMD curl --silent --fail localhost:8080/login || exit 1
