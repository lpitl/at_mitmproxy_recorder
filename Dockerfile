FROM tomcat:8.5.16-jre8-alpine
MAINTAINER Petr Molokotin <lpitl@mail.ru>
EXPOSE 8080
COPY ./build/libs/at-mitmproxy-recorder-0.2.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]