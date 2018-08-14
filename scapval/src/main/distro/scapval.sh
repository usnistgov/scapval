#!/bin/bash

if [ -z "$JAVA_HOME" ]; then
	# JAVA_HOME is not set just use the default version
	RUN="java"
else
	# JAVA_HOME is set make sure to use the specified Java version
	RUN="$JAVA_HOME/bin/java"
fi

# uncomment below to display the java version used to launch scapval
# $RUN -version
# echo ""

$RUN -Djava.protocol.handler.pkgs=sun.net.www.protocol -jar ${project.build.finalName}.${project.packaging} "$@"