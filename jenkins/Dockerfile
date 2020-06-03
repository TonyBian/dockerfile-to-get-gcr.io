FROM jenkins/jenkins:lts

ENV JENKINS_ADMIN_USER=admin

ENV JENKINS_ADMIN_PASSWD=123456

COPY init_login.groovy /usr/share/jenkins/ref/init.groovy.d/set-user-security.groovy

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN set -e \
        && /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
