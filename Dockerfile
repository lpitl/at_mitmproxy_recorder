FROM tomcat:8.0
MAINTAINER Petr Molokotin <lpitl@mail.ru>
COPY ./build/libs/at-mitmproxy-recorder.war /usr/local/tomcat/webapps/