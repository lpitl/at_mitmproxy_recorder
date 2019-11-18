FROM tomcat:8.5.16-jre8-alpine
MAINTAINER Petr Molokotin <lpitl@mail.ru>
EXPOSE 8080
COPY at-mitmproxy-recorder.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]