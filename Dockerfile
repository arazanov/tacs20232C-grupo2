FROM maven:latest AS MAVEN_TOOL_CHAIN
COPY tp-tacs/pom.xml /tmp/
COPY tp-tacs/src /tmp/src/
WORKDIR /tmp/
RUN mvn package

FROM tomcat:latest
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/webapp*.war $CATALINA_HOME/webapps/webapp.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
