# create-file-list-mojo
> A custom Apache Maven Plugin. Required by SCAPVal.

## Overview
This Maven Plugin generates a list of files in a given directory.
SCAPVal utilizes this plugin in its .pom file to generate a list of schema files at build time.
This list of files is referenced by SCAPVal when loading schemas out of it's packaged .jar file.

## Build/Install
Apache Maven is used to build and install create-file-list-mojo. 
From the *create-file-list-mojo/* directory, run the following:

`mvn clean install` 
