FROM tomcat:10.1.10-jdk17-temurin-jammy
COPY target/demo-mysql.war /usr/local/tomcat/webapps/demo-mysql.war
EXPOSE 8080
CMD ["catalina.sh", "run"]