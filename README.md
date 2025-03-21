[![CodeQL](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml) [![Java CI with Maven (Mac OS Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml) [![Java CI with Maven (Ubuntu Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml) [![Java CI with Maven (Windows Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/581706f82bf945df84bc397da4cecee5)](https://www.codacy.com/gh/jh3nd3rs0n/jargyle/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/jargyle&amp;utm_campaign=Badge_Grade)

# Jargyle

## Contents

-   [Overview](#overview)
-   [Contributing Guidelines](#contributing-guidelines) 
-   [Directory Overview](#directory-overview) 
-   [Build Requirements](#build-requirements)
-   [Frequently Used Maven Commands](#frequently-used-maven-commands)

## Overview

Jargyle is a Java SOCKS5 API and server. It is inspired by
[JSocks](https://jsocks.sourceforge.net/),
[SocksLib](https://github.com/fengyouchao/sockslib),
[Esocks](https://github.com/fengyouchao/esocks) and
[Dante](https://www.inet.no/dante/index.html).

Jargyle is licensed under the
[MIT license](https://opensource.org/licenses/MIT).
Its license can be found [here](LICENSE).

This README file discusses how to get started in contributing and building 
Jargyle. More information about Jargyle can be found 
[here](https://jh3nd3rs0n.github.io/jargyle).

**Warning:** Jargyle is not production ready.

## Contributing Guidelines

The contributing guidelines can be found [here](CONTRIBUTING.md).

## Directory Overview

The following is a simple overview of the directory.

-   `.github/workflows/`: Contains GitHub workflow files that perform analyses 
    and tests when a push has been made to the GitHub repository

-   `docs/`: Contains the website/documentation

-   `jargyle-argmatey/`: Maven module for the extensible command line 
    interface. It is used for the Jargyle command line interface.

-   `jargyle-cli/`: Maven module for the Jargyle command line interface

-   `jargyle-client/`: Maven module for the SOCKS client API

-   `jargyle-common/`: Maven module for the public API used by all modules

-   `jargyle-distributions/`: Maven module for creating the binary and source 
    distributions

-   `jargyle-internal/`: Maven module for the internal API used by all modules

-   `jargyle-protocolbase/`: Maven module for the foundational API for the 
    SOCKS client API and the SOCKS server API

-   `jargyle-report-aggregate/`: Maven module for generating the aggregated
    test coverage reports

-   `jargyle-server/`: Maven module for the SOCKS server API

-   `jargyle-test-echo/`: Maven module for clients and servers that 
    send/receive data and receive/send back the same data. The clients and one 
    of the servers use the SOCKS client API. When testing the clients and one 
    of the servers, the SOCKS client API and the SOCKS server API are also 
    tested.

-   `jargyle-test-echo-server-performance/`: Maven module for performance 
    testing of servers that receive data and send back the same data. It 
    includes performance testing of the SOCKS server API.

-   `jargyle-test-help/`: Maven module for the API for help with testing

-   `jargyle-test-netty-example-socksproxy/`: Maven module for the modified 
    version of the Netty example SOCKS proxy. It is used for testing.

-   `src/site/`: Contains files used to generate `docs/`

-   `.gitignore`: List of directories and files for Git to ignore such as
    Eclipse and IntelliJ IDEA project directories and files

-   `CODE_OF_CONDUCT.md`: Code of conduct for contributing to this project

-   `CONTRIBUTING.md`: Contributing guidelines

-   `LICENSE`: The license for this project

-   `pom.xml`: The Maven POM file for this project

-   `README.md`: This README file

## Build Requirements

You will need the following to build Jargyle:

-   JDK 9 or higher
-   Apache Maven 3.3.9 or higher

Once you have installed the requirements, be sure to have the environment 
variable `JAVA_HOME` set to the Java home directory.

## Frequently Used Maven Commands

The following are Maven commands that are frequently used for this project.
These commands are to be executed at the top directory of Jargyle.

-   `mvn clean`: Deletes directories and files created by this project.

-   `mvn clean compile site:site site:stage site:deploy`: Performs a clean 
    build and produces the website/documentation. 
    
    The produced website/documentation can be found in `docs/`. 
    
    Included in the website/documentation is the reference documentation. The 
    reference documentation was generated from special annotations within in 
    the source code. Should any of these special annotations in the source 
    code be added, changed, moved, removed, or handled differently, you will 
    need to run the following commands:
    
    ```bash
    # Perform a build of the binary distribution
    mvn clean package -DskipTests=true
    # Run Jargyle to generate Markdown reference documents to the directory of Markdown reference documentation 
    jargyle-distributions/target/jargyle-5.0.0-SNAPSHOT-bin/jargyle-5.0.0-SNAPSHOT/bin/jargyle generate-reference-docs -d src/site/markdown/reference/
    # Produce the website/documentation with the updated reference documentation
    mvn compile site:site site:stage site:deploy
    ```
    
-   `mvn clean package -DskipTests=true`: Performs a clean build of the binary 
    and source distributions skipping the execution of all tests. 
    
    The built binary and source distributions can be found as directories and 
    in multiple archive formats in `jargyle-distributions/target/`.
    
-   `mvn clean test -Pcoverage`: Performs a clean build, executes all tests 
    except the integration tests, and produces the aggregated test coverage 
    reports.
    
    The aggregated test coverage reports can be found in
    `jargyle-report-aggregate/target/`.
    
    The option `-Pcoverage` can be removed if you do not want the aggregated 
    test coverage reports produced.
    
-   `mvn clean verify --projects=\!jargyle-test-echo-server-performance,\!jargyle-distributions -Pcoverage`: 
    Performs a clean build, executes all tests except the ones from the 
    project `jargyle-test-echo-server-performance`, skips building the binary 
    and source distributions, and produces the aggregated test coverage 
    reports.
    
    The argument `\!jargyle-test-echo-server-performance,` from the option 
    `--projects` can be removed if you want the performance tests to be 
    executed. If the performance tests are executed, the results can be found 
    in `jargyle-test-echo-server-performance/target/performance-results/`.
    
    The aggregated test coverage reports can be found in 
    `jargyle-report-aggregate/target/`. 
    
    The option `-Pcoverage` can be removed if you do not want the aggregated 
    test coverage reports produced.
    
    When executing the integration tests, the integration tests use only the 
    loopback address and the local port numbers assigned by the local system. 
    However, the following specified local port numbers need to be open:
    
    -   `1080`: This default port number is used for SOCKS servers set up for 
        testing.
    -   `8000`: This port number is used for a Kerberos Key Distribution Center
        (KDC) set up for testing.
    -   `9000`: This port number is used for a Kerberos Key Distribution Center
        (KDC) set up for testing. 
    