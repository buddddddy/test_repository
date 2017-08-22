#!/bin/bash
java -server -Dlogging.config=config/logback.xml -Dfile.encoding=UTF-8 -jar mdlp.jar -Dconfig.location=/data/config/application.properties
