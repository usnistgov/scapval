# Common Maven Commands

The following are common Maven commands for addressing a number of concerns.

## License Management 

To check NIST license is included in all source files:

mvn license:check

To add the NIST license to all source files:

mvn license:format

## Source Formatting

To check source formatting:

mvn formatter:validate

To reformat all Java sources:

mvn formatter:format

To check for other source issues (e.g., Javadoc):

mvn checkstyle:check

## Releasing a Version

To perform a release:

mvn -Prelease release:clean release:prepare release:perform