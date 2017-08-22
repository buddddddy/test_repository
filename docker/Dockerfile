# Pull base image.
FROM mdlpsrv03.firstlinesoftware.ru:5000/java8

COPY mdlp.jar /data

# Define working directory.
WORKDIR /data

COPY entrypoint.sh /data/

# Define commonly used JAVA_HOME variable
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# Define default command.
ENTRYPOINT ["/data/entrypoint.sh"]
