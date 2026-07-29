#!/bin/bash

MINIMUM_JAVA_VERSION=11

fail() {
	echo "$1" >&2
	exit 1
}

diagnostics_enabled() {
	case "${SCAPVAL_DIAGNOSTICS:-}" in
		1|true|TRUE|yes|YES|on|ON)
			return 0
			;;
		*)
			return 1
			;;
	esac
}

print_launch_diagnostics() {
	local parent_process
	parent_process=$(ps -p "$PPID" -o command= 2>/dev/null)
	if [ -z "$parent_process" ]; then
		parent_process="<unavailable>"
	fi

	echo "SCAPVal launch diagnostics:" >&2
	echo "  script=$0" >&2
	echo "  interpreter=/bin/bash" >&2
	echo "  SHELL=${SHELL:-<unset>}" >&2
	echo "  parent process=$parent_process" >&2
	echo "  RUN=$RUN" >&2
	echo "  command -v java=$(command -v java 2>/dev/null || echo '<not found>')" >&2
	echo "  JAVA_HOME=${JAVA_HOME:-<unset>}" >&2
	echo "  JDK_JAVA_OPTIONS=${JDK_JAVA_OPTIONS:-<unset>}" >&2
	echo "  JAVA_TOOL_OPTIONS=${JAVA_TOOL_OPTIONS:-<unset>}" >&2
	echo "  CLASSPATH=${CLASSPATH:-<unset>}" >&2
	echo "  PATH=$PATH" >&2
	echo "  cached java -version output:" >&2
	printf '%s\n' "$JAVA_VERSION_OUTPUT" >&2
	echo "" >&2
}

if [ -z "$JAVA_HOME" ]; then
	# JAVA_HOME is not set just use the default version
	RUN="java"
else
	# JAVA_HOME is set make sure to use the specified Java version
	RUN="$JAVA_HOME/bin/java"
fi

if [ "$RUN" = "java" ]; then
	if ! command -v "$RUN" >/dev/null 2>&1; then
		fail "ERROR: Unable to find Java executable 'java' in PATH. SCAPVal requires Java ${MINIMUM_JAVA_VERSION}+."
	fi
elif [ ! -x "$RUN" ]; then
	fail "ERROR: Configured Java executable '$RUN' does not exist or is not executable. Set JAVA_HOME to a Java ${MINIMUM_JAVA_VERSION}+ installation."
fi

JAVA_VERSION_OUTPUT=$("$RUN" -version 2>&1)
JAVA_VERSION_STATUS=$?
if [ $JAVA_VERSION_STATUS -ne 0 ]; then
	fail "ERROR: Failed to run '$RUN -version'. Set JAVA_HOME to a valid Java ${MINIMUM_JAVA_VERSION}+ installation or install a newer Java runtime."
fi

IFS= read -r JAVA_VERSION_LINE <<EOF
$JAVA_VERSION_OUTPUT
EOF

if [[ "$JAVA_VERSION_LINE" =~ version\ \"([^\"]+)\" ]]; then
	JAVA_VERSION="${BASH_REMATCH[1]}"
else
	JAVA_VERSION=""
fi

if [ -z "$JAVA_VERSION" ]; then
	fail "ERROR: Unable to determine Java version from '$RUN'. Reported output: $JAVA_VERSION_LINE. SCAPVal requires Java ${MINIMUM_JAVA_VERSION}+."
fi

JAVA_MAJOR_VERSION=${JAVA_VERSION%%.*}
if [ "$JAVA_MAJOR_VERSION" = "1" ]; then
	JAVA_MAJOR_VERSION=${JAVA_VERSION#1.}
	JAVA_MAJOR_VERSION=${JAVA_MAJOR_VERSION%%.*}
fi

case "$JAVA_MAJOR_VERSION" in
	''|*[!0-9]*)
		fail "ERROR: Unable to parse Java version '$JAVA_VERSION' from '$RUN'. SCAPVal requires Java ${MINIMUM_JAVA_VERSION}+."
		;;
esac

if [ "$JAVA_MAJOR_VERSION" -lt "$MINIMUM_JAVA_VERSION" ]; then
	fail "ERROR: Java ${MINIMUM_JAVA_VERSION}+ is required, but '$RUN' reports version '$JAVA_VERSION'. Set JAVA_HOME to a newer Java runtime or install Java ${MINIMUM_JAVA_VERSION}+."
fi

# uncomment below to display the java version used to launch scapval
# $RUN -version
# echo ""

JAVA_OPTS=(-Djava.protocol.handler.pkgs=sun.net.www.protocol)

if diagnostics_enabled; then
	print_launch_diagnostics
	JAVA_OPTS+=(-Dscapval.diagnostics=true)
fi

SCAPVAL_HOME="$(cd -- "$(dirname -- "$0")" && pwd)"

"$RUN" "${JAVA_OPTS[@]}" -jar "$SCAPVAL_HOME/${project.build.finalName}.${project.packaging}" "$@"
