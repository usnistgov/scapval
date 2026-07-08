# SCAPVal User Guide

**Distribution build:** `${project.build.finalName}`

SCAPVal (the Security Content Automation Protocol Validator) is a Java command-line application
that checks whether SCAP content conforms to the conventions and recommendations in
[NIST SP 800-126 Rev.4](https://csrc.nist.gov/pubs/sp/800/126/r4/final) and its component-specification
annex [NIST SP 800-126A Rev.4](https://csrc.nist.gov/pubs/sp/800/126/a/r4/final), with support for
SCAP 1.2, 1.3, and 1.4.

> If you are new to SCAPVal, start with the [Quickstart](#3-quickstart). The bundled `README.txt`
> is a short quick reference; this guide is the complete documentation.

> **SCAP 1.1 support was removed**, in line with the SCAP 1.4 final release. If you need to
> validate SCAP 1.1 content, use a previous SCAPVal release.

## Contents

1. [Introduction](#1-introduction)
2. [Installation and requirements](#2-installation-and-requirements)
3. [Quickstart](#3-quickstart)
4. [Core concepts](#4-core-concepts)
5. [Command-line reference](#5-command-line-reference)
6. [Task walkthroughs](#6-task-walkthroughs)
7. [Reading the results](#7-reading-the-results)
8. [Troubleshooting and FAQ](#8-troubleshooting-and-faq)
9. [Requirements directory reference](#9-requirements-directory-reference)
10. [Feedback and contact](#10-feedback-and-contact)

---

## 1. Introduction

### What SCAPVal does

SCAPVal reports whether SCAP content is well-formed and conformant. It validates:

- **Source data streams**: the SCAP content a tool consumes.
- **Result content**: SCAP result data streams and Asset Reporting Format (ARF) reports.
- **Standalone components**: individual XCCDF, OVAL, or OCIL files, separate from a full SCAP
  data stream.

For each input it runs three kinds of checks:

1. **XML schema validation** against the bundled SCAP component schemas.
2. **Schematron rules** that encode the "derived requirements" of SP 800-126 and 800-126A, both
   within and across components.
3. **Custom Java checks** (for example, SCAP-version consistency and OVAL-version checks).

It then writes the findings to a machine-readable XML result and a human-readable HTML report.

### What SCAPVal does not do

- It is **not** a scanner or assessment engine: it does not evaluate content against a host or
  collect system state.
- It does **not** remediate or modify your content (except when you explicitly ask it to combine
  or sign content).
- **OVAL 6.0 is out of scope.** Only the bundled OVAL 5.x versions are validated (see
  [Core concepts](#4-core-concepts)).
- **SCAP 1.1 is not supported.** Support was removed in line with the SCAP 1.4 final release;
  use a previous SCAPVal release to validate SCAP 1.1 content.

### How this guide relates to the other docs

| Document | Purpose |
| --- | --- |
| `USER_GUIDE.md` (this file) | The complete user guide. |
| `README.txt` | Short quick reference bundled in the release: options, examples, signing workflow, changelog. |
| `NOTICE.txt` | License and third-party license notices. |
| `scapval -h` | The current list of command-line options for the installed version. |

---

## 2. Installation and requirements

### System requirements

- **Java Runtime Environment (JRE) 11 or higher.** The launch scripts verify the Java version
  before launch and report an error if the runtime is missing, invalid, or older than 11.
- No network access is required for validation; SCAP and OVAL schemas are bundled and resolved
  offline. Network access is only used with the optional `-online` parameter.

### Choosing the Java runtime

If the `JAVA_HOME` environment variable is set, the launch scripts use the JRE at that location.
Otherwise they use the `java` executable found on your `PATH`.

### Get and unpack the release

1. Download `scapval-<version>.zip`.
2. Unzip it to a working directory.
3. Change into the unzipped directory before running the commands below.

### What is in the release

| Path | Purpose |
| --- | --- |
| `scapval-<version>.jar` | The executable SCAPVal application. |
| `scapval.sh` | Launcher for macOS and Linux/Unix. |
| `scapval.bat` | Launcher for Windows. |
| `README.txt` | Quick reference (options, examples, signing, changelog). |
| `USER_GUIDE.md` | This user guide. |
| `NOTICE.txt` | License and third-party notices. |
| `lib/` | Dependency JARs used by SCAPVal. |
| `requirements/` | The SCAP requirement definitions used for validation, per version. |
| `data_feeds/` | Bundled CPE dictionary, CCE/NVD feed, and OVAL test-type list. |
| `SWIDTAG/` | The SWID software identification tag for this build. |

### Running SCAPVal

You can run the tool three equivalent ways:

```
# macOS / Linux
./scapval.sh <options>

# Windows
scapval.bat <options>

# Any platform, directly
java -jar scapval-<version>.jar <options>
```

The rest of this guide uses the `./scapval.sh` form; substitute `scapval.bat` on Windows.

### Verify the install

```
./scapval.sh -version   # prints the supported SCAP versions and the tool version
./scapval.sh -h         # prints the full list of command-line options
```

---

## 3. Quickstart

Validate a source data stream and read the results:

```
./scapval.sh -file source_data_stream_collection_sample.xml
```

SCAPVal auto-detects the SCAP version and content type, runs schema and Schematron validation, and
writes two files to the current working directory:

- `source_data_stream_collection_sample-validation-report.html`: a human-readable report; open it in a browser.
- `source_data_stream_collection_sample-validation-result.xml`: the machine-readable result.

Check the outcome from the shell exit code:

```
./scapval.sh -file source_data_stream_collection_sample.xml
echo $?   # 0 = passed (no failures); 1 = validation failure or a configuration/content problem
```

See [Reading the results](#7-reading-the-results) to interpret the report.

---

## 4. Core concepts

### SCAP versions and their component versions

SCAPVal validates each SCAP version against the component specification versions bound to it:

| SCAP | OVAL | XCCDF | OCIL | CPE | CCE | CVSS |
| --- | --- | --- | --- | --- | --- | --- |
| 1.2 | 5.10.1 | 1.2 | 2.0 | 2.3 | 5 | 2.0 |
| 1.3 | 5.11.2 | 1.2 | 2.0 | 2.3 | 5 | 3.0 |
| 1.4 | 5.12.3 | 1.2 | 2.0 | 2.3 | 5 | 3.0 |

The SCAP version is **auto-detected** from the content when `-scapversion` is omitted. It can also
be specified explicitly with `-scapversion 1.2|1.3|1.4`. If the specified version does not match
the version declared in the content, SCAPVal reports an error.

> **SCAP 1.1 support was removed**, in line with the SCAP 1.4 final release. Any attempt to
> validate 1.1 content (`-scapversion 1.1`, ZIP input, or a detected 1.1 data stream) fails with
> an error stating that a previous SCAPVal release is required.

### Use cases

A source data stream is validated for one SCAP "use case", read from the data stream's
`use-case` attribute. The `-usecase` option can name one explicitly; valid values are
`CONFIGURATION`, `VULNERABILITY`, `INVENTORY`, and `OTHER`.

### Content types

| Content type | How to validate it |
| --- | --- |
| Source data stream | `-file` |
| Result / ARF | `-resultfile` |
| Standalone component | `-componentfile` (XCCDF, OVAL, or OCIL) |
| Any of the above, detected automatically | `-auto` (a single XML file, or a directory) |

### Auto-detection (`-auto`)

`-auto` accepts any SCAP XML file, or a directory of XML files, and detects both the content type
(source, result, or component) and the SCAP version. When given a directory, it validates every
`*.xml` file in it and writes a batch summary. `-auto` does not accept ZIP input.

### Offline vs. online

- **Offline is the default.** OVAL content is validated against locally bundled OVAL Language
  schemas (OVAL 5.3 through 5.12.3); SCAP 1.4 validates embedded OVAL against OVAL 5.12.3. All
  schema references resolve to the bundled copies through an XML catalog, so no network access is
  needed. Only bundled OVAL versions are accepted; an unrecognized `schema_version` (for example,
  a future 5.12.x patch) is reported as unsupported.
- **`-online`** enables downloading the latest CCE/CPE dictionaries and resolving remote component
  references. `-maxsize` caps the per-download size in MiB (default 50 MiB).

### Validation layers and OVAL Schematron coverage

Every run performs XML schema validation and Schematron validation, plus custom Java checks.
Two coverage caveats surface as `NOT_TESTED` results:

- **OVAL 5.12 line Schematron is intentionally skipped**, pending vetted rules from the OVAL
  Community. OVAL 5.12.2/5.12.3 content is schema-validated but its OVAL Schematron requirement is
  reported as `NOT_TESTED`.
- **OVAL variables files** are schema-validated only; no OVAL variables Schematron is bundled for
  any version, so that Schematron requirement is reported as `NOT_TESTED`.

---

## 5. Command-line reference

Synopsis:

```
scapval <options>
```

The content and action options below are **mutually exclusive**: supply exactly one. Run
`scapval -h` for the current list.

### Content and action options (choose one)

| Option | Argument | Description |
| --- | --- | --- |
| `-file` | file | SCAP source XML file (SCAP 1.2, 1.3, 1.4). Only provide when validating source files. |
| `-resultfile` | file | SCAP result XML file (SCAP 1.2, 1.3, 1.4). Only provide when validating result files. |
| `-componentfile` | file | Validate an individual component file. XCCDF, OVAL (definitions, results, system characteristics, variables), and OCIL are supported. |
| `-auto` | file or directory | Validate an SCAP XML file or a directory of XML files, auto-detecting content type (source, result, or component) and SCAP version. |
| `-batchdir` | directory | **Deprecated** alias for `-auto` on a directory; will be removed in a future release. |
| `-createsigconfig` | 8 args | First step to sign content: create a signing configuration file. See [Sign content](#sign-content-tmsad). |
| `-signcontent` | file | Second step to sign content: path to the configuration file created by `-createsigconfig`. |
| `-validatesignature` | 3 args | Check the validity of signed content. |
| `-showcertificate` | 2 args | Show a certificate from a keystore. |
| `-listcertificatealias` | keystore | List the certificate aliases available in a keystore. |

### Modifier options

| Option | Argument | Description |
| --- | --- | --- |
| `-scapversion` | 1.2 / 1.3 / 1.4 | SCAP version to validate. Auto-detected if not specified. |
| `-usecase` | use case | The SCAP use case (see [Use cases](#use-cases)). |
| `-sourceds` | file | Source data stream to include with results; it will be included in the ARF report. |
| `-combinedoutput` | file | Write a copy of the final combined content SCAPVal validates against (combined remote resources and any `-sourceds`). |
| `-online` | (flag) | Enable download of the latest dictionaries and remote resolution of some components. |
| `-maxsize` | MiB | Override the maximum download size for remote references (default 50 MiB). |

### Built-in options

| Option | Argument | Description |
| --- | --- | --- |
| `-valresultfile` | file | Output XML result file location (see note below). |
| `-valreportfile` | file | Output HTML report file location (see note below). |
| `-debug` | (flag) | Enable verbose output. |
| `-quiet` | (flag) | Silence console output. |
| `-version`, `-v` | (flag) | Display the supported SCAP versions and the tool version, then exit. |
| `-h`, `--help` | (flag) | Display the available options, then exit. |

> **Note on default output names.** `-h` shows generic defaults of `validation-result.xml` /
> `validation-report.html` for `-valresultfile` / `-valreportfile`. In practice SCAPVal derives the
> names from the input file (`<input-prefix>-validation-result.xml` and
> `<input-prefix>-validation-report.html`) unless they are overridden with these options.

---

## 6. Task walkthroughs

Each walkthrough shows the command and the files it produces. Replace the sample filenames with
your own.

### Validate a source data stream

```
./scapval.sh -file my-source-datastream.xml
```

Produces `my-source-datastream-validation-result.xml` and `my-source-datastream-validation-report.html`.
For SCAP 1.2/1.3/1.4 the version and use case are auto-detected.

### Validate a result / ARF

```
./scapval.sh -resultfile my-arf-result.xml
```

To include the source data stream with the results, add `-sourceds`:

```
./scapval.sh -resultfile my-arf-result.xml -sourceds my-source.xml
```

### Validate a standalone component

```
./scapval.sh -componentfile my-xccdf.xml
./scapval.sh -componentfile my-oval-definitions.xml
./scapval.sh -componentfile my-ocil.xml
```

Supported standalone document types are XCCDF, OVAL (definitions, results, system characteristics,
variables), and OCIL. For OVAL variables, and for OVAL 5.12.x content generally, the OVAL
Schematron requirement is reported `NOT_TESTED` (see
[Validation layers](#validation-layers-and-oval-schematron-coverage)).

### Batch-validate a directory

```
./scapval.sh -auto /path/to/scap-content/
```

Every `*.xml` file in the directory is validated with content-type and version auto-detection. In
addition to each file's own result and report, SCAPVal writes `batch-validation-summary.html`
listing every file's Overall Pass/Fail outcome. (`-batchdir` is a deprecated alias for this.)

### Resolve remote references online

```
./scapval.sh -file my-source-datastream.xml -online -debug
```

`-online` downloads current dictionaries and merges remotely-referenced components before
validation. Use `-maxsize` to raise the download cap, and `-combinedoutput` to save exactly what
was validated:

```
./scapval.sh -file my-source-datastream.xml -online -maxsize 100 -combinedoutput combined.xml
```

If you omit `-combinedoutput`, a combined file named `<input-prefix>-with-data-stream.xml` is
written when content is combined.

### Sign content (TMSAD)

Signing is a two-step process. First create a signing configuration file with `-createsigconfig`
followed by **8 arguments, in this order**:

1. Output path for the configuration file to be written.
2. The SCAP 1.2 data stream to sign.
3. Output path for the signed SCAP data stream.
4. Digest algorithm: `SHA1`, `SHA256`, or `SHA512`.
5. Signature algorithm: `DSA_SHA1`, `RSA_SHA1`, or `RSA_SHA256`.
6. A Java Keystore (JKS) file, or `MSCAPI` to use a certificate installed in Windows.
7. The alias of the certificate used to sign.
8. `true` or `false`: whether external references should be signed.

```
./scapval.sh -createsigconfig scap-config.xml scap-data-stream.xml scap-data-stream-signed.xml \
  SHA256 RSA_SHA256 test.jks test false
```

Then sign, supplying passwords when prompted:

```
./scapval.sh -signcontent scap-config.xml
```

### Verify a signature

`-validatesignature` takes 3 arguments: the signed XML document, a JKS keystore (or `MSCAPI`), and
the alias of the trusted root certificate.

```
./scapval.sh -validatesignature scap-data-stream-signed.xml test.jks test-alias
```

### Inspect certificates

```
# Show one certificate: keystore (or MSCAPI) then alias
./scapval.sh -showcertificate test.jks test-alias

# List available aliases in a keystore (or MSCAPI)
./scapval.sh -listcertificatealias test.jks
```

---

## 7. Reading the results

### Output files

| File | When | Contents |
| --- | --- | --- |
| `<input-prefix>-validation-result.xml` | Always | Machine-readable results: every requirement and its status. |
| `<input-prefix>-validation-report.html` | Always | Human-readable report (title "SCAPVal Validation Report"). |
| `batch-validation-summary.html` | Batch (`-auto <dir>`) | Per-file Overall Pass/Fail summary with links to each detail report. |
| `<input-prefix>-with-data-stream.xml` | With `-sourceds` / `-combinedoutput` | The combined content that was validated. |

Files are written to the current working directory. Override the two main outputs with
`-valresultfile` and `-valreportfile`.

### Result-status vocabulary

| Status | Meaning | Fails the run? |
| --- | --- | --- |
| `PASS` | The requirement was checked and satisfied. | No |
| `FAIL` | The requirement was checked and violated. | **Yes** |
| `WARNING` | A recommended (`SHOULD`) requirement was not met. | No |
| `INFORMATIONAL` | Informational finding; no conformance impact. | No |
| `NOT_APPLICABLE` | The requirement does not apply to this content. | No |
| `NOT_TESTED` | The requirement was not evaluated (for example, OVAL 5.12 Schematron). | No |

Only a `FAIL` causes the run to fail. `WARNING`, `INFORMATIONAL`, `PASS`, `NOT_APPLICABLE`, and
`NOT_TESTED` do not.

### How severity is decided

The severity of a finding comes from two places:

- The Schematron rule's `flag`: `ERROR`, `WARNING`, or `INFO`.
- The requirement's `type` in the requirements files: `MUST` maps to `FAIL`, `SHOULD` maps to
  `WARNING`, and `INFORMATIONAL` maps to an informational result.

For example, a requirement can be relaxed from an error to a warning by changing its `type` from
`MUST` to `SHOULD`.

### Requirement-ID prefixes

Findings are grouped under derived-requirement IDs. The ID prefix indicates what is checked:

| Prefix | Meaning |
| --- | --- |
| `SRC-*` | Source data-stream requirements. |
| `RES-*` | Result data-stream / ARF requirements. |
| `A-*` | Additional recommendations from the SP 800-126A annex. |
| `TOOL-*` | Tool-conformance items (usually informational / not independently checked). |
| `COMP-1`, `COMP-1-1`, `COMP-1-2` | Standalone component: base, schema validation, and Schematron validation. |

`NIST-*` and `NISTIR-*` identifiers that appear in the requirements files are citation resources,
not result statuses.

### Anatomy of the result XML

A trimmed `validation-result.xml` (from a standalone OVAL definitions file with a schema error):

```xml
<assessment-results xmlns="http://csrc.nist.gov/ns/decima/results/1.0" start="..." end="...">
  <subjects>
    <subject id="sub1">
      <href>file:/path/to/oval-definitions.xml</href>       <!-- the file that was validated -->
    </subject>
  </subjects>
  <requirements>
    <requirement href="classpath:requirements/scapval-individual-component-requirements.xml" />
  </requirements>
  <results>
    <base-requirement id="COMP-1">
      <status>FAIL</status>                                 <!-- rolled-up status for the component -->
      <derived-requirement id="COMP-1-1">                   <!-- schema validation -->
        <status>FAIL</status>
        <test>
          <status>FAIL</status>
          <message>cvc-enumeration-valid: Value 'NOR' is not facet-valid ...
                   It must be a value from the enumeration.</message>
          <location line="41" column="32" subject-ref="sub1" xpath="/*[local-name()='oval_definitions' ...]" />
        </test>
      </derived-requirement>
      <derived-requirement id="COMP-1-2">                   <!-- OVAL Schematron: skipped for 5.12.x -->
        <status>NOT_TESTED</status>
      </derived-requirement>
    </base-requirement>
  </results>
</assessment-results>
```

Each `<test>` carries the failure `<message>` and a `<location>` with the `line`, `column`, and an
`xpath` pointing at the offending element. Use these to locate the problem in the content.

### The HTML report

`<input-prefix>-validation-report.html` (title "SCAPVal Validation Report") presents the same
information in human-readable form: a summary of counts by status, the arguments SCAPVal was run
with, and the per-requirement results. Each failing requirement lists its messages with the line,
column, and XPath of the offending element.

### The batch summary

In batch mode, `batch-validation-summary.html` lists one row per file with columns **File Name**,
**Result** (Overall Pass / Overall Fail), and a link to that file's **Detail Report**. Failing
files expand to per-failure detail with **Requirement**, **Message**, and **Location** columns.

### Exit codes

| Exit code | Meaning |
| --- | --- |
| `0` | Passed (no `FAIL` results). |
| `1` | A validation failure occurred, or there was a configuration/content problem. |
| `-1` | An unexpected runtime error occurred. |

In batch mode the run exits `1` if any file fails. These exit codes can be used to gate CI
scripts (`if ./scapval.sh -file content.xml; then ...`).

---

## 8. Troubleshooting and FAQ

### Enable diagnostics

For startup/runtime problems, set `SCAPVAL_DIAGNOSTICS` (or the system property
`scapval.diagnostics`) to `1`, `true`, `yes`, or `on`. SCAPVal then prints Java/runtime, classpath,
and launch-context information.

```
SCAPVAL_DIAGNOSTICS=1 ./scapval.sh -file my-source-datastream.xml
```

### Common problems

| Symptom | Cause and fix |
| --- | --- |
| `TMSADException: no such provider: BC` during signature validation | A known limitation with the BouncyCastle security provider on some environments (tracked in the project issues). |
| "Unable to find valid OVAL version" | The content declares an OVAL `schema_version` that is not one of the bundled versions. Only concrete, bundled OVAL 5.x versions are accepted. |
| Launch fails complaining about the Java version | The selected runtime is missing, invalid, or older than 11. Install a JRE 11+ or point `JAVA_HOME` at one. |
| SCAP 1.1 content is rejected | SCAP 1.1 support was removed, in line with the SCAP 1.4 final release. Use a previous SCAPVal release to validate SCAP 1.1 content. |
| Remote references are not resolved | Remote resolution is off by default. Add `-online` (and raise `-maxsize` if downloads are large). |

### FAQ

- **Does SCAPVal need internet access?** No. Validation is fully offline; `-online` is opt-in only.
- **Where do the output files go?** To the current working directory, named from the input file.
  Override with `-valresultfile` / `-valreportfile`.
- **Why are some results `NOT_TESTED`?** Some checks are intentionally not run, most commonly OVAL
  5.12 Schematron and OVAL variables Schematron, pending vetted community rules.
- **How do I see the tool and supported SCAP versions?** Run `./scapval.sh -version`.

---

## 9. Requirements directory reference

The requirements SCAPVal checks against ship in the `requirements/` directory of the release and
are referenced from every result file:

| File | Used for |
| --- | --- |
| `scapval-scap-1.2-requirements.xml` | SCAP 1.2 source and result validation. |
| `scapval-scap-1.3-requirements.xml` | SCAP 1.3 source and result validation. |
| `scapval-scap-1.4-requirements.xml` | SCAP 1.4 source and result validation. |
| `scapval-individual-component-requirements.xml` | Standalone component (`-componentfile`) validation. |

Each requirement carries an ID (the [prefixes above](#requirement-id-prefixes)) and a `type`
(`MUST`, `SHOULD`, `INFORMATIONAL`, ...) that determines the severity of a failure. The
`<derived-requirement id>` values in a result file map directly back to entries in these files.

---

## 10. Feedback and contact

Please send tool defect reports, enhancement requests, and other comments by email to
**scap@nist.gov**, or open an issue at
[github.com/usnistgov/scapval](https://github.com/usnistgov/scapval/issues).

- License and third-party notices: `NOTICE.txt`.
- Version-by-version changes: the Changelog in `README.txt`.
- Specifications: [NIST SP 800-126 Rev.4](https://csrc.nist.gov/pubs/sp/800/126/r4/final) and
  [NIST SP 800-126A Rev.4](https://csrc.nist.gov/pubs/sp/800/126/a/r4/final).
