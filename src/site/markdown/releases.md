# Releases

## Contents

-   [Overview](#overview)
-   [Binary distribution](#binary-distribution) 
-   [Build System Dependency](#build-system-dependency)
-   [Source distribution](#source-distribution)

## Overview

This document discusses the types of releases.

## Binary Distribution

The binary distribution contains the files to run Jargyle from the command
line.

Requirements:

-   Java 9 or higher

Once you have installed the requirements for the binary distribution, be sure
to have the environment variable `JAVA_HOME` set to the Java home directory.

Releases for the binary distribution can be found
[here](https://github.com/jh3nd3rs0n/jargyle/releases).

## Build System Dependency

The build system dependency provides both the client API and the server API.

**Note:** The build system dependency is only available on GitHub for now.

Requirements:

-   Java 9 or higher

To declare the dependency in your build system, the definition to declare the
dependency can be found
[here](https://jh3nd3rs0n.github.io/jargyle/dependency-info.html).

To declare the dependency in your build system for only the client API, the
definition to declare the dependency can be found
[here](https://jh3nd3rs0n.github.io/jargyle/jargyle-client/dependency-info.html).

To declare the dependency in your build system for only the server API, the
definition to declare the dependency can be found
[here](https://jh3nd3rs0n.github.io/jargyle/jargyle-server/dependency-info.html).

## Source Distribution

The source distribution contains the files to run automated testing and to
build the binary and source distributions.

Requirements:

-   Java 9 or higher
-   Apache Maven 3.3.9 or higher

Once you have installed the requirements for the source distribution, be sure
to have the environment variable `JAVA_HOME` set to the Java home directory.

Releases for the source distribution can be found
[here](https://github.com/jh3nd3rs0n/jargyle/releases).