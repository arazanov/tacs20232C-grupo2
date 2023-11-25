FROM maven:latest AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package

FROM tomcat:latest
COPY server.xml /usr/local/tomcat/conf/
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/ROOT*.war $CATALINA_HOME/webapps/
CMD ["catalina.sh", "run"]