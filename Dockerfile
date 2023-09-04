FROM tomcat:latest
ADD tp-tacs/webapp.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
