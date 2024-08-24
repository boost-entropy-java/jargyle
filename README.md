[![CodeQL](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml) [![Java CI with Maven (Mac OS Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml) [![Java CI with Maven (Ubuntu Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml) [![Java CI with Maven (Windows Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/581706f82bf945df84bc397da4cecee5)](https://www.codacy.com/gh/jh3nd3rs0n/jargyle/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/jargyle&amp;utm_campaign=Badge_Grade)

# Jargyle

Jargyle is a Java SOCKS5 API and server that uses SSL/TLS, DTLS, and SOCKS5
server chaining. It is inspired by [JSocks](https://jsocks.sourceforge.net/),
[SocksLib](https://github.com/fengyouchao/sockslib),
[Esocks](https://github.com/fengyouchao/esocks) and
[Dante](https://www.inet.no/dante/index.html).

You can find more information about Jargyle 
[here](https://jh3nd3rs0n.github.io/jargyle).

## Contents

-   [License](#license)
-   [Contributing Guidelines](#contributing-guidelines) 
-   [Directory Overview](#directory-overview) 
-   [Build Requirements](#build-requirements)
-   [Frequently Used Maven Commands](#frequently-used-maven-commands)

## License

Jargyle is licensed under the
[MIT license](https://opensource.org/licenses/MIT).
Its license can be found [here](LICENSE).

## Contributing Guidelines

The contributing guidelines can be found [here](CONTRIBUTING.md).

## Directory Overview

The following is a simple overview of the directory.

`.github`: Contains GitHub workflow files that perform tests and analysis
when a push has been made to the GitHub repository

`docs`: Contains the website/documentation

`echo`: Maven module for an API of clients and servers that send/receive
data and receive/send the same data (This module is used internally for
performing integration testing on the SOCKS client and server API)

`echo-performance-test`: Maven module for performance tests for the echo
API and the SOCKS client and server API (The results can be found in
`echo-performance-test/target/performance-results/`)

`jargyle-cli`: Maven module for the Jargyle command line interface API

`jargyle-client`: Maven module for the SOCKS client API

`jargyle-common`: Maven module for the public API used by all modules

`jargyle-distribution`: Maven module for creating the binary distribution

`jargyle-internal`: Maven module for the internal API used by all modules

`jargyle-protocolbase`: Maven module for the foundational API for the
SOCKS client and server API

`jargyle-report-aggregate`: Maven module for generating the aggregated
test coverage reports

`jargyle-server`: Maven module for the SOCKS server API

`src/site`: Contains files used to generate `docs/`

`test-help`: Maven module for the test help API (This module is used
internally to help with testing)

`.gitignore`: List of directories and files for Git to ignore such as
Eclipse and IntelliJ IDEA project directories and files

`CODE_OF_CONDUCT.md`: Code of conduct for contributing to this project

`CONTRIBUTING.md`: Contributing guidelines

`LICENSE`: The license for this project

`pom.xml`: The Maven POM file for this project

`README.md`: This README file

## Build Requirements

You will need the following to build Jargyle:

-   JDK 9 or higher
-   Apache Maven 3.3.9 or higher

Once you have installed the requirements, be sure to have the environment 
variable `JAVA_HOME` set to the Java home directory.

## Frequently Used Maven Commands

The following are Maven commands that are frequently used for this project.
These commands are to be executed at the top directory of Jargyle.

`mvn clean`: Deletes directories and files created by this project.

`mvn clean compile site:site site:stage site:deploy`: Performs a clean build 
and produces the website/documentation. The website/documentation can be found 
in `docs/`. 

Included in the website/documentation is the reference documentation. The 
reference documentation was generated from special annotations within in the 
source code. Should any of these special annotations in the source code be 
added, changed, moved, or removed, you will need to run the following commands 
before running the above command:

```bash
# This command is necessary if the binary distribution is not built
mvn clean package -DskipTests=true
# Change to the directory of Markdown reference documentation
cd src/site/markdown/reference/
# Run Jargyle to generate Markdown reference documents in the present directory 
../../../../jargyle-distribution/target/jargyle-distribution-5.0.0-SNAPSHOT-bin/bin/jargyle generate-reference-docs
# Change back to the top directory of Jargyle
cd ../../../../
```

`mvn clean package -DskipTests=true`: Performs a clean build of the binary 
distribution while skipping all tests. The built binary distribution can be 
found as a directory and in multiple archive formats in 
`jargyle-distribution/target/`.

`mvn clean test -Pcoverage`: Performs a clean build and execution of all tests 
except integration tests and also produces test coverage reports. The test 
coverage reports can be found in `jargyle-report-aggregate/target/`. The option 
`-Pcoverage` can be removed if you do not want test coverage reports produced.

`mvn clean verify -Pcoverage`: Performs a clean build and
execution of all tests including integration tests and also produces test
coverage reports. The test coverage reports can be found in
`jargyle-report-aggregate/target/`. The option `-Pcoverage` can be removed if
you do not want test coverage reports produced.
