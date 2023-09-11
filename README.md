# Jargyle 

[![CodeQL](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/codeql-analysis.yml) [![Java CI with Maven (Mac OS Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_macos_latest.yml) [![Java CI with Maven (Ubuntu Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_ubuntu_latest.yml) [![Java CI with Maven (Windows Latest)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml/badge.svg)](https://github.com/jh3nd3rs0n/jargyle/actions/workflows/maven_windows_latest.yml) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/581706f82bf945df84bc397da4cecee5)](https://www.codacy.com/gh/jh3nd3rs0n/jargyle/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/jargyle&amp;utm_campaign=Badge_Grade)

## Contents

-   [1. About](#1-about)
-   [2. License](#2-license)
-   [3. Requirements](#3-requirements)
-   [4. Releases](#4-releases)
-   [5. Automated Testing](#5-automated-testing)
-   [6. Building](#6-building)
-   [7. Running Jargyle](#7-running-jargyle)
-   [7. 1. Usage](#7-1-usage)
-   [7. 1. 1. manage-socks5-users Usage](#7-1-1-manage-socks5-users-usage)
-   [7. 1. 2. new-server-config-file Usage](#7-1-2-new-server-config-file-usage)
-   [7. 1. 3. start-server Usage](#7-1-3-start-server-usage)
-   [7. 1. 4. Settings Help](#7-1-4-settings-help)
-   [7. 2. Creating a Configuration File](#7-2-creating-a-configuration-file)
-   [7. 3. Supplementing a Configuration File With Command Line Options](#7-3-supplementing-a-configuration-file-with-command-line-options)
-   [7. 4. Combining Configuration Files](#7-4-combining-configuration-files)
-   [7. 5. Running With a Configuration File](#7-5-running-with-a-configuration-file)
-   [7. 6. Running With a Monitored Configuration File](#7-6-running-with-a-monitored-configuration-file)
-   [7. 7. Using SSL/TLS for TCP Traffic Between Jargyle and Its Clients](#7-7-using-ssl-tls-for-tcp-traffic-between-jargyle-and-its-clients)
-   [7. 8. Using DTLS for UDP Traffic Between Jargyle and Its Clients](#7-8-using-dtls-for-udp-traffic-between-jargyle-and-its-clients)
-   [7. 9. Using SOCKS5 Authentication](#7-9-using-socks5-authentication)
-   [7. 9. 1. Using No Authentication](#7-9-1-using-no-authentication)
-   [7. 9. 2. Using Username Password Authentication](#7-9-2-using-username-password-authentication)
-   [7. 9. 2. 1. Managing Users](#7-9-2-1-managing-users)
-   [7. 9. 2. 1. 1. Adding Users](#7-9-2-1-1-adding-users)
-   [7. 9. 2. 1. 2. List All Users](#7-9-2-1-2-list-all-users)
-   [7. 9. 2. 1. 3. Removing a User](#7-9-2-1-3-removing-a-user)
-   [7. 9. 3. Using GSS-API Authentication](#7-9-3-using-gss-api-authentication)
-   [7. 10. Chaining to Another SOCKS Server](#7-10-chaining-to-another-socks-server)
-   [7. 10. 1. Using SSL/TLS for TCP Traffic Through SOCKS Server Chaining](#7-10-1-using-ssl-tls-for-tcp-traffic-through-socks-server-chaining)
-   [7. 10. 2. Using DTLS for UDP Traffic Through SOCKS Server Chaining](#7-10-2-using-dtls-for-udp-traffic-through-socks-server-chaining)
-   [7. 10. 3. Using SOCKS5 Authentication](#7-10-3-using-socks5-authentication)
-   [7. 10. 3. 1. Using No Authentication](#7-10-3-1-using-no-authentication)
-   [7. 10. 3. 2. Using Username Password Authentication](#7-10-3-2-using-username-password-authentication)
-   [7. 10. 3. 3. Using GSS-API Authentication](#7-10-3-3-using-gss-api-authentication)
-   [7. 10. 4. Resolving Host Names Through SOCKS5 Server Chaining](#7-10-4-resolving-host-names-through-socks5-server-chaining)
-   [7. 11. Chaining to a Specified Chain of Other SOCKS Servers](#7-11-chaining-to-a-specified-chain-of-other-socks-servers)
-   [7. 12. Chaining to Multiple Specified Chains of Other SOCKS Servers](#7-12-chaining-to-multiple-specified-chains-of-other-socks-servers)
-   [7. 13. Using Rules to Manage Traffic](#7-13-using-rules-to-manage-traffic)
-   [7. 13. 1. Rule Conditions](#7-13-1-rule-conditions)
-   [7. 13. 2. Rule Results](#7-13-2-rule-results)
-   [7. 13. 3. Allowing or Denying Traffic](#7-13-3-allowing-or-denying-traffic)
-   [7. 13. 4. Allowing a Limited Number of Simultaneous Instances of Traffic](#7-13-4-allowing-a-limited-number-of-simultaneous-instances-of-traffic)
-   [7. 13. 5. Routing Traffic](#7-13-5-routing-traffic)
-   [7. 13. 6. Redirecting the Desired Destination](#7-13-6-redirecting-the-desired-destination)
-   [7. 13. 7. Configuring Sockets](#7-13-7-configuring-sockets)
-   [7. 13. 8. Configuring Relay Settings](#7-13-8-configuring-relay-settings)
-   [7. 13. 9. Limiting Relay Bandwidth](#7-13-9-limiting-relay-bandwidth)
-   [7. 14. Common Value Syntaxes](#7-14-common-value-syntaxes)
-   [7. 14. 1. Address Range](#7-14-1-address-range)
-   [7. 14. 2. Port Range](#7-14-2-port-range)
-   [7. 15. Miscellaneous Notes](#7-15-miscellaneous-notes)
-   [7. 15. 1. Multiple Settings of the Same Name](#7-15-1-multiple-settings-of-the-same-name)
-   [7. 15. 2. The SOCKS5 RESOLVE Command](#7-15-2-the-socks5-resolve-command)
-   [7. 15. 3. The Doc XML Element](#7-15-3-the-doc-xml-element)

## 1. About

Jargyle is a complete SOCKS5 server. It is complete in the sense that it completely follows the specifications for [SOCKS5](https://www.rfc-editor.org/rfc/rfc1928), the [username password authentication for SOCKS5](https://www.rfc-editor.org/rfc/rfc1929), and the [GSS-API authentication method for SOCKS5](https://www.rfc-editor.org/rfc/rfc1961).

It has the following additional features:

-   Chain to multiple specified chains of SOCKS servers
-   Use SSL/TLS and DTLS for TCP and UDP traffic from clients and SOCKS servers
-   Resolve host names through an additional unofficial SOCKS5 command called RESOLVE

It also has a rule system that allows you to manage traffic in the following ways:

-   Allow or deny traffic
-   Allow a limited number of simultaneous instances of traffic
-   Route traffic through multiple selectable routes
-   Redirect the desired destination
-   Configure sockets
-   Configure relay settings
-   Limit relay bandwidth

**IMPLEMENTATION DETAIL**: Jargyle uses multiple threads for handling client connections. Under Java 19, it can use virtual threads instead of OS threads. To enable the use of virtual threads under Java 19, add the command line option `--enable-preview` to environment variable `JARGYLE_OPTS`

```bash
    
    export JARGYLE_OPTS="--enable-preview"
    
```

## 2. License

Jargyle is licensed under the [MIT license](https://github.com/jh3nd3rs0n/jargyle/blob/master/LICENSE). Licenses of third party dependencies are listed [here](https://github.com/jh3nd3rs0n/jargyle/blob/master/LICENSE_3RD_PARTY).

## 3. Requirements

For automated testing, building, and running Jargyle under the source distribution:

-   Apache Maven&#8482; 3.3.9 or higher 
-   Java&#8482; SDK 9 or higher

For running Jargyle under the binary distribution:

-   Java&#8482; Runtime Environment 9 or higher

After installation of the requirements, be sure to have the environment variable `JAVA_HOME` set to the location of the installed Java&#8482; SDK or the installed Java&#8482; Runtime Environment.

## 4. Releases

Releases for the source and binary distributions can be found [here](https://github.com/jh3nd3rs0n/jargyle/releases).

## 5. Automated Testing

To run automated testing, run the following commands:

```bash
    
    cd jargyle
    mvn clean verify
    
```

## 6. Building

To build and package Jargyle, run the following command:

```bash
    
    mvn clean package
    
```

After running the aforementioned command, the built binary distribution can be found in the following path:

```text
    
    jargyle-distribution/target/jargyle-distribution-VERSION-bin/
    
```

Where `VERSION` is the actual version.

## 7. Running Jargyle 

To run Jargyle without any command line arguments, you can run the following command:

```bash
    
    ./bin/jargyle start-server
    
```

The aforementioned command will run Jargyle on port 1080 at address 0.0.0.0 using no authentication.

### 7. 1. Usage

The following is the command line help for Jargyle (displayed when using the command line option `--help`):

```text
    
    Usage: jargyle COMMAND
           jargyle --help
           jargyle --version
    
    COMMANDS:
      manage-socks5-users USER_REPOSITORY_CLASS_NAME:INITIALIZATION_STRING COMMAND
          Manage SOCKS5 users
      new-server-config-file [OPTIONS] FILE
          Create a new server configuration file based on the provided options
      start-server [OPTIONS] [MONITORED_CONFIG_FILE]
          Start the SOCKS server
    
    OPTIONS:
      --help, -h
          Print this help and exit
      --version, -V
          Print version information and exit
    
    
    
```

#### 7. 1. 1. manage-socks5-users Usage

The following is the command line help for the command `manage-socks5-users` (displayed when using the command `manage-socks5-users --help`):

```text
    
    Usage: jargyle manage-socks5-users USER_REPOSITORY_CLASS_NAME:INITIALIZATION_STRING COMMAND
           jargyle manage-socks5-users --help
    
    COMMANDS:
      add
          Add user(s) through an interactive prompt
      list
          List users to standard output
      remove NAME
          Remove user by name
    
    OPTIONS:
      --help, -h
          Print this help and exit
    
    
```

#### 7. 1. 2. new-server-config-file Usage

The following is the command line help for the command `new-server-config-file` (displayed when using the command `new-server-config-file --help`):

```text
    
    Usage: jargyle new-server-config-file [OPTIONS] FILE
           jargyle new-server-config-file --help
           jargyle new-server-config-file --settings-help
    
    OPTIONS:
      --config-file=FILE, -f FILE
          A configuration file
      --enter-chaining-dtls-key-store-pass
          Enter through an interactive prompt the password for the key store for the DTLS connections to the other SOCKS server
      --enter-chaining-dtls-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the DTLS connections to the other SOCKS server
      --enter-chaining-socks5-userpassauth-pass
          Enter through an interactive prompt the password to be used to access the other SOCKS5 server
      --enter-chaining-ssl-key-store-pass
          Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the other SOCKS server
      --enter-chaining-ssl-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the other SOCKS server
      --enter-dtls-key-store-pass
          Enter through an interactive prompt the password for the key store for the DTLS connections to the SOCKS server
      --enter-dtls-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the DTLS connections to the SOCKS server
      --enter-ssl-key-store-pass
          Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the SOCKS server
      --enter-ssl-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the SOCKS server
      --help, -h
          Print this help and exit
      --setting=NAME=VALUE, -s NAME=VALUE
          A setting for the SOCKS server
      --settings-help, -H
          Print the list of available settings for the SOCKS server and exit
    
    
```

#### 7. 1. 3. start-server Usage

The following is the command line help for the command `start-server` (displayed when using the command `start-server --help`):

```text
    
    Usage: jargyle start-server [OPTIONS] [MONITORED_CONFIG_FILE]
           jargyle start-server --help
           jargyle start-server --settings-help
    
    OPTIONS:
      --config-file=FILE, -f FILE
          A configuration file
      --enter-chaining-dtls-key-store-pass
          Enter through an interactive prompt the password for the key store for the DTLS connections to the other SOCKS server
      --enter-chaining-dtls-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the DTLS connections to the other SOCKS server
      --enter-chaining-socks5-userpassauth-pass
          Enter through an interactive prompt the password to be used to access the other SOCKS5 server
      --enter-chaining-ssl-key-store-pass
          Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the other SOCKS server
      --enter-chaining-ssl-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the other SOCKS server
      --enter-dtls-key-store-pass
          Enter through an interactive prompt the password for the key store for the DTLS connections to the SOCKS server
      --enter-dtls-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the DTLS connections to the SOCKS server
      --enter-ssl-key-store-pass
          Enter through an interactive prompt the password for the key store for the SSL/TLS connections to the SOCKS server
      --enter-ssl-trust-store-pass
          Enter through an interactive prompt the password for the trust store for the SSL/TLS connections to the SOCKS server
      --help, -h
          Print this help and exit
      --setting=NAME=VALUE, -s NAME=VALUE
          A setting for the SOCKS server
      --settings-help, -H
          Print the list of available settings for the SOCKS server and exit
    
    
```

#### 7. 1. 4. Settings Help

The following is a list of available settings for the SOCKS server (displayed when using either the commands `new-server-config-file --settings-help` or `start-server --settings-help`):

```text
    
    SETTINGS:
    
      GENERAL SETTINGS:
    
        backlog=INTEGER_BETWEEN_0_AND_2147483647
            The maximum length of the queue of incoming client connections to the SOCKS server (default is 50)
    
        bindHost=HOST
            The default binding host name or address for all sockets (default is 0.0.0.0)
    
        bindTcpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of default binding port ranges for all TCP sockets (default is 0)
    
        bindUdpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of default binding port ranges for all UDP sockets (default is 0)
    
        clientSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of socket settings for the client socket
    
        externalFacingBindHost=HOST
            The default binding host name or address for all external-facing sockets
    
        externalFacingBindTcpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of default binding port ranges for all external-facing TCP sockets
    
        externalFacingBindUdpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of default binding port ranges for all external-facing UDP sockets
    
        externalFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of default socket settings for all external-facing sockets
    
        internalFacingBindHost=HOST
            The default binding host name or address for all internal-facing sockets
    
        internalFacingBindTcpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of default binding port ranges for all internal-facing TCP sockets
    
        internalFacingBindUdpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of default binding port ranges for all internal-facing UDP sockets
    
        internalFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of default socket settings for all internal-facing sockets
    
        lastRouteId=ROUTE_ID
            The ID for the last and unassigned route (default is lastRoute)
    
        port=INTEGER_BETWEEN_0_AND_65535
            The port for the SOCKS server
    
        routeSelectionLogAction=LOG_ACTION
            The logging action to take if a route is selected
    
        routeSelectionStrategy=SELECTION_STRATEGY
            The selection strategy for the next route (default is CYCLICAL)
    
        rule=[RULE_CONDITION1[ RULE_CONDITION2[ ...]]] [RULE_RESULT1[ RULE_RESULT2[ ...]]]
            A rule for the SOCKS server (default is firewallAction=ALLOW)
    
        socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of default socket settings for all sockets
    
        socksServerBindHost=HOST
            The binding host name or address for the SOCKS server socket
    
        socksServerBindPortRanges[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of binding port ranges for the SOCKS server socket
    
        socksServerSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of socket settings for the SOCKS server socket
    
      CHAINING GENERAL SETTINGS:
    
        chaining.clientBindHost=HOST
            The binding host name or address for the client socket that is used to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 0.0.0.0)
    
        chaining.clientBindPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of binding port ranges for the client socket that is used to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 0)
    
        chaining.clientConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647
            The timeout in milliseconds on waiting for the client socket to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 60000)
    
        chaining.clientSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of socket settings for the client socket that is used to connect to the other SOCKS server (used for the SOCKS5 command RESOLVE and UDP ASSOCIATE)
    
        chaining.routeId=ROUTE_ID
            The ID for a route through a chain of other SOCKS servers. This setting also marks the current other SOCKS server as the last SOCKS server in the chain of other SOCKS servers
    
        chaining.socksServerUri=SCHEME://HOST[:PORT]
            The URI of the other SOCKS server
    
      CHAINING DTLS SETTINGS:
    
        chaining.dtls.enabled=true|false
            The boolean value to indicate if DTLS connections to the other SOCKS server are enabled (default is false)
    
        chaining.dtls.enabledCipherSuites=[DTLS_CIPHER_SUITE1[ DTLS_CIPHER_SUITE2[ ...]]]
            The space separated list of acceptable cipher suites enabled for DTLS connections to the other SOCKS server
    
        chaining.dtls.enabledProtocols=[DTLS_PROTOCOL1[ DTLS_PROTOCOL2[ ...]]]
            The space separated list of acceptable protocol versions enabled for DTLS connections to the other SOCKS server
    
        chaining.dtls.keyStoreFile=FILE
            The key store file for the DTLS connections to the other SOCKS server
    
        chaining.dtls.keyStorePassword=PASSWORD
            The password for the key store for the DTLS connections to the other SOCKS server
    
        chaining.dtls.keyStoreType=TYPE
            The type of key store file for the DTLS connections to the other SOCKS server (default is PKCS12)
    
        chaining.dtls.maxPacketSize=INTEGER_BETWEEN_1_AND_2147483647
            The maximum packet size for the DTLS connections to the other SOCKS server (default is 32768)
    
        chaining.dtls.protocol=PROTOCOL
            The protocol version for the DTLS connections to the other SOCKS server (default is DTLSv1.2)
    
        chaining.dtls.trustStoreFile=FILE
            The trust store file for the DTLS connections to the other SOCKS server
    
        chaining.dtls.trustStorePassword=PASSWORD
            The password for the trust store for the DTLS connections to the other SOCKS server
    
        chaining.dtls.trustStoreType=TYPE
            The type of trust store file for the DTLS connections to the other SOCKS server (default is PKCS12)
    
      CHAINING SOCKS5 SETTINGS:
    
        chaining.socks5.clientUdpAddressAndPortUnknown=true|false
            The boolean value to indicate that the client UDP address and port for sending UDP datagrams to the other SOCKS5 server is unknown (default is false)
    
        chaining.socks5.gssapiauth.mechanismOid=SOCKS5_GSSAPIAUTH_MECHANISM_OID
            The object ID for the GSS-API authentication mechanism to the other SOCKS5 server (default is 1.2.840.113554.1.2.2)
    
        chaining.socks5.gssapiauth.necReferenceImpl=true|false
            The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS5 server use the NEC reference implementation (default is false)
    
        chaining.socks5.gssapiauth.protectionLevels=SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL1[ SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL2[ ...]]
            The space separated list of acceptable protection levels after GSS-API authentication with the other SOCKS5 server (The first is preferred. The remaining are acceptable if the server does not accept the first.) (default is REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)
    
        chaining.socks5.gssapiauth.serviceName=SOCKS5_GSSAPIAUTH_SERVICE_NAME
            The GSS-API service name for the other SOCKS5 server
    
        chaining.socks5.methods=[SOCKS5_METHOD1[ SOCKS5_METHOD2[ ...]]]
            The space separated list of acceptable authentication methods to the other SOCKS5 server (default is NO_AUTHENTICATION_REQUIRED)
    
        chaining.socks5.useResolveCommand=true|false
            The boolean value to indicate that the RESOLVE command is to be used on the other SOCKS5 server for resolving host names (default is false)
    
        chaining.socks5.userpassauth.password=PASSWORD
            The password to be used to access the other SOCKS5 server
    
        chaining.socks5.userpassauth.username=USERNAME
            The username to be used to access the other SOCKS5 server
    
      CHAINING SSL SETTINGS:
    
        chaining.ssl.enabled=true|false
            The boolean value to indicate if SSL/TLS connections to the other SOCKS server are enabled (default is false)
    
        chaining.ssl.enabledCipherSuites=[SSL_CIPHER_SUITE1[ SSL_CIPHER_SUITE2[ ...]]]
            The space separated list of acceptable cipher suites enabled for SSL/TLS connections to the other SOCKS server
    
        chaining.ssl.enabledProtocols=[SSL_PROTOCOL1[ SSL_PROTOCOL2[ ...]]]
            The space separated list of acceptable protocol versions enabled for SSL/TLS connections to the other SOCKS server
    
        chaining.ssl.keyStoreFile=FILE
            The key store file for the SSL/TLS connections to the other SOCKS server
    
        chaining.ssl.keyStorePassword=PASSWORD
            The password for the key store for the SSL/TLS connections to the other SOCKS server
    
        chaining.ssl.keyStoreType=TYPE
            The type of key store file for the SSL/TLS connections to the other SOCKS server (default is PKCS12)
    
        chaining.ssl.protocol=PROTOCOL
            The protocol version for the SSL/TLS connections to the other SOCKS server (default is TLSv1.2)
    
        chaining.ssl.trustStoreFile=FILE
            The trust store file for the SSL/TLS connections to the other SOCKS server
    
        chaining.ssl.trustStorePassword=PASSWORD
            The password for the trust store for the SSL/TLS connections to the other SOCKS server
    
        chaining.ssl.trustStoreType=TYPE
            The type of trust store file for the SSL/TLS connections to the other SOCKS server (default is PKCS12)
    
      DTLS SETTINGS:
    
        dtls.enabled=true|false
            The boolean value to indicate if DTLS connections to the SOCKS server are enabled (default is false)
    
        dtls.enabledCipherSuites=[DTLS_CIPHER_SUITE1[ DTLS_CIPHER_SUITE2[ ...]]]
            The space separated list of acceptable cipher suites enabled for DTLS connections to the SOCKS server
    
        dtls.enabledProtocols=[DTLS_PROTOCOL1[ DTLS_PROTOCOL2[ ...]]]
            The space separated list of acceptable protocol versions enabled for DTLS connections to the SOCKS server
    
        dtls.keyStoreFile=FILE
            The key store file for the DTLS connections to the SOCKS server
    
        dtls.keyStorePassword=PASSWORD
            The password for the key store for the DTLS connections to the SOCKS server
    
        dtls.keyStoreType=TYPE
            The type of key store file for the DTLS connections to the SOCKS server (default is PKCS12)
    
        dtls.maxPacketSize=INTEGER_BETWEEN_1_AND_2147483647
            The maximum packet size for the DTLS connections to the SOCKS server (default is 32768)
    
        dtls.needClientAuth=true|false
            The boolean value to indicate that client authentication is required for DTLS connections to the SOCKS server (default is false)
    
        dtls.protocol=PROTOCOL
            The protocol version for the DTLS connections to the SOCKS server (default is DTLSv1.2)
    
        dtls.trustStoreFile=FILE
            The trust store file for the DTLS connections to the SOCKS server
    
        dtls.trustStorePassword=PASSWORD
            The password for the trust store for the DTLS connections to the SOCKS server
    
        dtls.trustStoreType=TYPE
            The type of trust store file for the DTLS connections to the SOCKS server (default is PKCS12)
    
        dtls.wantClientAuth=true|false
            The boolean value to indicate that client authentication is requested for DTLS connections to the SOCKS server (default is false)
    
      SOCKS5 SETTINGS:
    
        socks5.gssapiauth.necReferenceImpl=true|false
            The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation (default is false)
    
        socks5.gssapiauth.protectionLevels=SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL1[ SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL2[ ...]]
            The space separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.) (default is REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)
    
        socks5.methods=[SOCKS5_METHOD1[ SOCKS5_METHOD2[ ...]]]
            The space separated list of acceptable authentication methods in order of preference (default is NO_AUTHENTICATION_REQUIRED)
    
        socks5.onBind.inboundSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of socket settings for the inbound socket
    
        socks5.onBind.listenBindHost=HOST
            The binding host name or address for the listen socket if the provided host address is all zeros
    
        socks5.onBind.listenBindPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of binding port ranges for the listen socket if the provided port is zero
    
        socks5.onBind.listenSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of socket settings for the listen socket
    
        socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
            The buffer size in bytes for relaying the data
    
        socks5.onBind.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
            The timeout in milliseconds on relaying no data
    
        socks5.onBind.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed
    
        socks5.onBind.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed
    
        socks5.onCommand.bindHost=HOST
            The binding host name or address for all sockets
    
        socks5.onCommand.bindTcpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of binding port ranges for all TCP sockets
    
        socks5.onCommand.bindUdpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of binding port ranges for all UDP sockets
    
        socks5.onCommand.externalFacingBindHost=HOST
            The binding host name or address for all external-facing sockets
    
        socks5.onCommand.externalFacingBindTcpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of binding port ranges for all external-facing TCP sockets
    
        socks5.onCommand.externalFacingBindUdpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of binding port ranges for all external-facing UDP sockets
    
        socks5.onCommand.externalFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of socket settings for all external-facing sockets
    
        socks5.onCommand.internalFacingBindHost=HOST
            The binding host name or address for all internal-facing sockets
    
        socks5.onCommand.internalFacingBindTcpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of binding port ranges for all internal-facing TCP sockets
    
        socks5.onCommand.internalFacingBindUdpPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of binding port ranges for all internal-facing UDP sockets
    
        socks5.onCommand.internalFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of socket settings for all internal-facing sockets
    
        socks5.onCommand.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
            The buffer size in bytes for relaying the data (default is 1024)
    
        socks5.onCommand.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
            The timeout in milliseconds on relaying no data (default is 60000
    
        socks5.onCommand.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed
    
        socks5.onCommand.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed
    
        socks5.onCommand.socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of socket settings for all sockets
    
        socks5.onConnect.prepareServerFacingSocket=true|false
            The boolean value to indicate if the server-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect) (default is false)
    
        socks5.onConnect.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
            The buffer size in bytes for relaying the data
    
        socks5.onConnect.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
            The timeout in milliseconds on relaying no data
    
        socks5.onConnect.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed
    
        socks5.onConnect.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed
    
        socks5.onConnect.serverFacingBindHost=HOST
            The binding host name or address for the server-facing socket
    
        socks5.onConnect.serverFacingBindPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of binding port ranges for the server-facing socket
    
        socks5.onConnect.serverFacingConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647
            The timeout in milliseconds on waiting for the server-facing socket to connect (default is 60000)
    
        socks5.onConnect.serverFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of socket settings for the server-facing socket
    
        socks5.onUdpAssociate.clientFacingBindHost=HOST
            The binding host name or address for the client-facing UDP socket
    
        socks5.onUdpAssociate.clientFacingBindPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of binding port ranges for the client-facing UDP socket
    
        socks5.onUdpAssociate.clientFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of socket settings for the client-facing UDP socket
    
        socks5.onUdpAssociate.peerFacingBindHost=HOST
            The binding host name or address for the peer-facing UDP socket
    
        socks5.onUdpAssociate.peerFacingBindPortRanges=[PORT_RANGE1[ PORT_RANGE2[ ...]]]
            The space separated list of binding port ranges for the peer-facing UDP socket
    
        socks5.onUdpAssociate.peerFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[ ...]]]
            The space separated list of socket settings for the peer-facing UDP socket
    
        socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
            The buffer size in bytes for relaying the data
    
        socks5.onUdpAssociate.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
            The timeout in milliseconds on relaying no data
    
        socks5.onUdpAssociate.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed
    
        socks5.onUdpAssociate.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed
    
        socks5.userpassauth.userRepository=CLASS_NAME:INITIALIZATION_STRING
            The user repository used for username password authentication
    
      SSL SETTINGS:
    
        ssl.enabled=true|false
            The boolean value to indicate if SSL/TLS connections to the SOCKS server are enabled (default is false)
    
        ssl.enabledCipherSuites=[SSL_CIPHER_SUITE1[ SSL_CIPHER_SUITE2[ ...]]]
            The space separated list of acceptable cipher suites enabled for SSL/TLS connections to the SOCKS server
    
        ssl.enabledProtocols=[SSL_PROTOCOL1[ SSL_PROTOCOL2[ ...]]]
            The space separated list of acceptable protocol versions enabled for SSL/TLS connections to the SOCKS server
    
        ssl.keyStoreFile=FILE
            The key store file for the SSL/TLS connections to the SOCKS server
    
        ssl.keyStorePassword=PASSWORD
            The password for the key store for the SSL/TLS connections to the SOCKS server
    
        ssl.keyStoreType=TYPE
            The type of key store file for the SSL/TLS connections to the SOCKS server (default is PKCS12)
    
        ssl.needClientAuth=true|false
            The boolean value to indicate that client authentication is required for SSL/TLS connections to the SOCKS server (default is false)
    
        ssl.protocol=PROTOCOL
            The protocol version for the SSL/TLS connections to the SOCKS server (default is TLSv1.2)
    
        ssl.trustStoreFile=FILE
            The trust store file for the SSL/TLS connections to the SOCKS server
    
        ssl.trustStorePassword=PASSWORD
            The password for the trust store for the SSL/TLS connections to the SOCKS server
    
        ssl.trustStoreType=TYPE
            The type of trust store file for the SSL/TLS connections to the SOCKS server (default is PKCS12)
    
        ssl.wantClientAuth=true|false
            The boolean value to indicate that client authentication is requested for SSL/TLS connections to the SOCKS server (default is false)
    
    SETTING VALUE SYNTAXES:
    
      FIREWALL_ACTIONS:
    
        ALLOW
    
        DENY
    
      GENERAL_RULE_CONDITIONS:
    
        clientAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the client address
    
        socksServerAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the SOCKS server address the client connected to
    
      GENERAL_RULE_RESULTS:
    
        bindHost=HOST
            Specifies the binding host name or address for all sockets
    
        bindTcpPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for all TCP sockets (can be specified multiple times with each rule result specifying another port range)
    
        bindUdpPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for all UDP sockets (can be specified multiple times with each rule result specifying another port ranges)
    
        clientSocketSetting=SOCKET_SETTING
            Specifies a socket setting for the client socket (can be specified multiple times with each rule result specifying another socket setting)
    
        externalFacingBindHost=HOST
            Specifies the binding host name or address for all external-facing sockets
    
        externalFacingBindTcpPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule result specifying another port range)
    
        externalFacingBindUdpPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)
    
        externalFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule result specifying another socket setting)
    
        firewallAction=FIREWALL_ACTION
            Specifies the firewall action to take
    
        firewallActionAllowLimit=INTEGER_BETWEEN_0_AND_2147483647
            Specifies the limit on the number of simultaneous instances of the rule's firewall action ALLOW
    
        firewallActionAllowLimitReachedLogAction=LOG_ACTION
            Specifies the logging action to take if the limit on the number of simultaneous instances of the rule's firewall action ALLOW has been reached
    
        firewallActionLogAction=LOG_ACTION
            Specifies the logging action to take if the firewall action is applied
    
        internalFacingBindHost=HOST
            Specifies the binding host name or address for all internal-facing sockets
    
        internalFacingBindTcpPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for all internal-facing TCP sockets (can be specified multiple times with each rule result specifying another port range)
    
        internalFacingBindUdpPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for all internal-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)
    
        internalFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for all internal-facing sockets (can be specified multiple times with each rule result specifying another socket setting)
    
        routeSelectionLogAction=LOG_ACTION
            Specifies the logging action to take if a route ID is selected
    
        routeSelectionStrategy=SELECTION_STRATEGY
            Specifies the selection strategy for the next route ID
    
        selectableRouteId=ROUTE_ID
            Specifies the ID for a selectable route (can be specified multiple times with each rule result specifying another ID for a selectable route)
    
        socketSetting=SOCKET_SETTING
            Specifies a socket setting for all sockets (can be specified multiple times with each rule result specifying another socket setting)
    
      LOG_ACTIONS:
    
        LOG_AS_WARNING
            Log message as a warning message
    
        LOG_AS_INFO
            Log message as an informational message
    
      SCHEMES:
    
        socks5
            SOCKS protocol version 5
    
      SELECTION_STRATEGIES:
    
        CYCLICAL
            Select the next in the cycle
    
        RANDOM
            Select the next at random
    
      SOCKET_SETTINGS:
    
        IP_TOS=INTEGER_BETWEEN_0_AND_255
            The type-of-service or traffic class field in the IP header for a TCP or UDP socket
    
        PERF_PREF=3_DIGITS_EACH_BETWEEN_0_AND_2
            Performance preferences for a TCP socket described by three digits whose values indicate the relative importance of short connection time, low latency, and high bandwidth
    
        SO_BROADCAST=true|false
            Can send broadcast datagrams
    
        SO_KEEPALIVE=true|false
            Keeps a TCP socket alive when no data has been exchanged in either direction
    
        SO_LINGER=INTEGER_BETWEEN_0_AND_2147483647
            Linger on closing the TCP socket in seconds
    
        SO_OOBINLINE=true|false
            Can receive TCP urgent data
    
        SO_RCVBUF=INTEGER_BETWEEN_1_AND_2147483647
            The receive buffer size
    
        SO_REUSEADDR=true|false
            Can reuse socket address and port
    
        SO_SNDBUF=INTEGER_BETWEEN_1_AND_2147483647
            The send buffer size
    
        SO_TIMEOUT=INTEGER_BETWEEN_0_AND_2147483647
            The timeout in milliseconds on waiting for an idle socket
    
        TCP_NODELAY=true|false
            Disables Nagle's algorithm
    
      SOCKS5_COMMANDS:
    
        CONNECT
            A request to the SOCKS server to connect to another server
    
        BIND
            A request to the SOCKS server to bind to another address and port in order to receive an inbound connection
    
        UDP_ASSOCIATE
            A request to the SOCKS server to associate a UDP socket for sending and receiving datagrams
    
        RESOLVE
            A request to the SOCKS server to resolve a host name
    
      SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS:
    
        NONE
            No protection
    
        REQUIRED_INTEG
            Required per-message integrity
    
        REQUIRED_INTEG_AND_CONF
            Required per-message integrity and confidentiality
    
      SOCKS5_METHODS:
    
        NO_AUTHENTICATION_REQUIRED
            No authentication required
    
        GSSAPI
            GSS-API authentication
    
        USERNAME_PASSWORD
            Username password authentication
    
      SOCKS5_RULE_CONDITIONS:
    
        socks5.command=SOCKS5_COMMAND
            Specifies the SOCKS5 command
    
        socks5.desiredDestinationAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the desired destination address
    
        socks5.desiredDestinationPort=PORT|PORT1-PORT2
            Specifies the desired destination port
    
        socks5.method=SOCKS5_METHOD
            Specifies the negotiated SOCKS5 method
    
        socks5.secondServerBoundAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the second server bound address
    
        socks5.secondServerBoundPort=PORT|PORT1-PORT2
            Specifies the second server bound port
    
        socks5.serverBoundAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the server bound address
    
        socks5.serverBoundPort=PORT|PORT1-PORT2
            Specifies the server bound port
    
        socks5.udp.inbound.desiredDestinationAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the UDP inbound desired destination address
    
        socks5.udp.inbound.desiredDestinationPort=PORT|PORT1-PORT2
            Specifies the UDP inbound desired destination port
    
        socks5.udp.inbound.sourceAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the UDP inbound source address
    
        socks5.udp.inbound.sourcePort=PORT|PORT1-PORT2
            Specifies the UDP inbound source port
    
        socks5.udp.outbound.desiredDestinationAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the UDP outbound desired destination address
    
        socks5.udp.outbound.desiredDestinationPort=PORT|PORT1-PORT2
            Specifies the UDP outbound desired destination port
    
        socks5.udp.outbound.sourceAddress=ADDRESS|ADDRESS1-ADDRESS2|regex:REGULAR_EXPRESSION
            Specifies the UDP outbound source address
    
        socks5.udp.outbound.sourcePort=PORT|PORT1-PORT2
            Specifies the UDP outbound source port
    
        socks5.user=USER
            Specifies the user if any after the negotiated SOCKS5 method
    
      SOCKS5_RULE_RESULTS:
    
        socks5.desiredDestinationAddressRedirect=ADDRESS
            Specifies the desired destination address redirect
    
        socks5.desiredDestinationPortRedirect=PORT
            Specifies the desired destination port redirect
    
        socks5.desiredDestinationRedirectLogAction=LOG_ACTION
            Specifies the logging action to take if the desired destination is redirected
    
        socks5.onBind.inboundSocketSetting=SOCKET_SETTING
            Specifies a socket setting for the inbound socket (can be specified multiple times with each rule result specifying another socket setting)
    
        socks5.onBind.listenBindHost=HOST
            Specifies the binding host name or address for the listen socket if the provided host address is all zeros
    
        socks5.onBind.listenBindPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for the listen socket if the provided port is zero (can be specified multiple times with each rule result specifying another port range)
    
        socks5.onBind.listenSocketSetting=SOCKET_SETTING
            Specifies a socket setting for the listen socket (can be specified multiple times with each rule result specifying another socket setting)
    
        socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the buffer size in bytes for relaying the data
    
        socks5.onBind.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the timeout in milliseconds on relaying no data
    
        socks5.onBind.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed
    
        socks5.onBind.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed
    
        socks5.onCommand.bindHost=HOST
            Specifies the binding host name or address for all sockets
    
        socks5.onCommand.bindTcpPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for all TCP sockets (can be specified multiple times with each rule result specifying another port range)
    
        socks5.onCommand.bindUdpPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for all UDP sockets (can be specified multiple times with each rule result specifying another port range)
    
        socks5.onCommand.externalFacingBindHost=HOST
            Specifies the binding host name or address for all external-facing sockets
    
        socks5.onCommand.externalFacingBindTcpPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule result specifying another port range)
    
        socks5.onCommand.externalFacingBindUdpPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)
    
        socks5.onCommand.externalFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule result specifying another socket setting)
    
        socks5.onCommand.internalFacingBindHost=HOST
            Specifies the binding host name or address for all internal-facing sockets
    
        socks5.onCommand.internalFacingBindTcpPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for all internal-facing TCP sockets (can be specified multiple times with each rule result specifying another port range)
    
        socks5.onCommand.internalFacingBindUdpPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for all internal-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)
    
        socks5.onCommand.internalFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for all internal-facing sockets (can be specified multiple times with each rule result specifying another socket setting)
    
        socks5.onCommand.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the buffer size in bytes for relaying the data
    
        socks5.onCommand.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the timeout in milliseconds on relaying no data
    
        socks5.onCommand.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed
    
        socks5.onCommand.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed
    
        socks5.onCommand.socketSetting=SOCKET_SETTING
            Specifies a socket setting for all sockets (can be specified multiple times with each rule result specifying another socket setting)
    
        socks5.onConnect.prepareServerFacingSocket=true|false
            Specifies the boolean value to indicate if the server-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)
    
        socks5.onConnect.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the buffer size in bytes for relaying the data
    
        socks5.onConnect.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the timeout in milliseconds on relaying no data
    
        socks5.onConnect.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed
    
        socks5.onConnect.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed
    
        socks5.onConnect.serverFacingBindHost=HOST
            Specifies the binding host name or address for the server-facing socket
    
        socks5.onConnect.serverFacingBindPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for the server-facing socket (can be specified multiple times with each rule result specifying another port range)
    
        socks5.onConnect.serverFacingConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the timeout in milliseconds on waiting for the server-facing socket to connect
    
        socks5.onConnect.serverFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for the server-facing socket (can be specified multiple times with each rule result specifying another socket setting)
    
        socks5.onUdpAssociate.clientFacingBindHost=HOST
            Specifies the binding host name or address for the client-facing UDP socket
    
        socks5.onUdpAssociate.clientFacingBindPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule result specifying another port range)
    
        socks5.onUdpAssociate.clientFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule result specifying another socket setting)
    
        socks5.onUdpAssociate.peerFacingBindHost=HOST
            Specifies the binding host name or address for the peer-facing UDP socket
    
        socks5.onUdpAssociate.peerFacingBindPortRange=PORT|PORT1-PORT2
            Specifies a binding port range for the peer-facing UDP socket (can be specified multiple times with each rule result specifying another port range)
    
        socks5.onUdpAssociate.peerFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for the peer-facing UDP socket (can be specified multiple times with each rule result specifying another socket setting)
    
        socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the buffer size in bytes for relaying the data
    
        socks5.onUdpAssociate.relayIdleTimeout=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the timeout in milliseconds on relaying no data
    
        socks5.onUdpAssociate.relayInboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed
    
        socks5.onUdpAssociate.relayOutboundBandwidthLimit=INTEGER_BETWEEN_1_AND_2147483647
            Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed
    
    
```

### 7. 2. Creating a Configuration File

You can create a configuration file by using the command `new-server-config-file`

The following command creates an empty configuration file:

```bash
    
    ./bin/jargyle new-server-config-file empty_configuration.xml
    
```

`empty_configuration.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings/>
    </configuration>
    
```

The following command creates a configuration file with the port number, the number of allowed backlogged incoming client connections, and no authentication required:

```bash
    
    ./bin/jargyle new-server-config-file \
        --setting=port=1234 \
        --setting=backlog=100 \
        --setting=socks5.methods=NO_AUTHENTICATION_REQUIRED \
        configuration.xml
    
```

`configuration.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings>
            <setting>
                <name>port</name>
                <value>1234</value>
            </setting>
            <setting>
                <name>backlog</name>
                <value>100</value>
            </setting>
            <setting>
                <name>socks5.methods</name>
                <socks5.methods>
                    <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
                </socks5.methods>
            </setting>
        </settings>
    </configuration>
    
```
  
### 7. 3. Supplementing a Configuration File With Command Line Options

You can supplement an existing configuration file with command line options.

The following command adds one command line option after the existing configuration file:

```bash
    
    ./bin/jargyle new-server-config-file \
        --config-file=configuration.xml \
        --setting=socksServerSocketSettings=SO_TIMEOUT=0 \
        supplemented_configuration.xml
    
```

`supplemented_configuration.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings>
            <setting>
                <name>port</name>
                <value>1234</value>
            </setting>
            <setting>
                <name>backlog</name>
                <value>100</value>
            </setting>
            <setting>
                <name>socks5.methods</name>
                <socks5.methods>
                    <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
                </socks5.methods>
            </setting>
            <setting>
                <name>socksServerSocketSettings</name>
                <socketSettings>
                    <socketSetting>
                        <name>SO_TIMEOUT</name>
                        <value>0</value>
                    </socketSetting>
                </socketSettings>
            </setting>
        </settings>
    </configuration>
    
```

### 7. 4. Combining Configuration Files

You can combine multiple configuration files into one configuration file.

The following command combines the two earlier configuration files into one:

```bash
    
    ./bin/jargyle new-server-config-file \
        --config-file=configuration.xml \
        --config-file=supplemented_configuration.xml \
        combined_configuration.xml
    
```

`combined_configuration.xml`:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings>
            <setting>
                <name>port</name>
                <value>1234</value>
            </setting>
            <setting>
                <name>backlog</name>
                <value>100</value>
            </setting>
            <setting>
                <name>socks5.methods</name>
                <socks5.methods>
                    <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
                </socks5.methods>
            </setting>
            <setting>
                <name>port</name>
                <value>1234</value>
            </setting>
            <setting>
                <name>backlog</name>
                <value>100</value>
            </setting>
            <setting>
                <name>socks5.methods</name>
                <socks5.methods>
                    <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
                </socks5.methods>
            </setting>
            <setting>
                <name>socksServerSocketSettings</name>
                <socketSettings>
                    <socketSetting>
                        <name>SO_TIMEOUT</name>
                        <value>0</value>
                    </socketSetting>
                </socketSettings>
            </setting>
        </settings>
    </configuration>
    
```

Although the redundant settings in the combined configuration file are unnecessary, the result configuration file is for demonstration purposes only. (See [Multiple Settings of the Same Name](#7-15-1-multiple-settings-of-the-same-name) for more information.)

### 7. 5. Running With a Configuration File

To run Jargyle with a configuration file, you can use the following command:

```bash
    
    ./bin/jargyle start-server --config-file=configuration.xml
    
```

Also the configuration file can be supplemented with command line options and/or can be combined with multiple configuration files.

### 7. 6. Running With a Monitored Configuration File

You can run Jargyle with a configuration file to be monitored for any changes to be applied to the running configuration.

To run Jargyle with a monitored configuration file, you can use the following command:

```bash
    
    ./bin/jargyle start-server configuration.xml
    
```

Unlike the command line option `--config-file`, the monitored configuration file cannot be supplemented with command line options and cannot be combined with multiple configuration files.

The following are the settings in the monitored configuration file that will have no effect if changed during the running configuration:

-   `backlog`
-   `port`
-   `socksServerBindHost`
-   `socksServerBindPortRanges`
-   `socksServerSocketSettings`

A restart of Jargyle would be required if you want any of the changed aforementioned settings to be applied to the running configuration.

### 7. 7. Using SSL/TLS for TCP Traffic Between Jargyle and Its Clients

You can use SSL/TLS for TCP traffic between Jargyle and its clients. By default SSL/TLS for TCP traffic between Jargyle and its clients is disabled. To enable SSL/TLS for TCP traffic between Jargyle and its clients, you will need to have the setting `ssl.enabled` set to `true`. In addition, you will need to have the setting `ssl.keyStoreFile` to specify Jargyle's key store file (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `ssl.keyStorePassword` to specify the password for Jargyle's key store file.

```bash
    
    ./bin/jargyle start-server \
        --setting=ssl.enabled=true \
        --setting=ssl.keyStoreFile=server.jks \
        --setting=ssl.keyStorePassword=password
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-ssl-key-store-pass` instead. It will provide an interactive prompt for you to enter the password.

```bash
    
    ./bin/jargyle start-server \
        --setting=ssl.enabled=true \
        --setting=ssl.keyStoreFile=server.jks \
        --enter-ssl-key-store-pass
    
```

If you want to have the client authenticate using SSL/TLS, you will need to have the setting `ssl.needClientAuth` set to `true`. In addition, you will need to have the setting `ssl.trustStoreFile` to specify the client's key store file to be used as a trust store (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `ssl.trustStorePassword` to specify the password for the client's trust store file.

```bash
    
    ./bin/jargyle start-server \
        --setting=ssl.enabled=true \
        --setting=ssl.keyStoreFile=server.jks \
        --setting=ssl.keyStorePassword=password \
        --setting=ssl.needClientAuth=true \
        --setting=ssl.trustStoreFile=client.jks \
        --setting=ssl.trustStorePassword=drowssap
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-ssl-trust-store-pass` instead. It will provide an interactive prompt for you to enter the password.

```bash
    
    ./bin/jargyle start-server \
        --setting=ssl.enabled=true \
        --setting=ssl.keyStoreFile=server.jks \
        --enter-ssl-key-store-pass \
        --setting=ssl.needClientAuth=true \
        --setting=ssl.trustStoreFile=client.jks \
        --enter-ssl-trust-store-pass
    
```

### 7. 8. Using DTLS for UDP Traffic Between Jargyle and Its Clients

You can use DTLS for UDP traffic between Jargyle and its clients. By default DTLS for UDP traffic between Jargyle and its clients is disabled. To enable DTLS for UDP traffic between Jargyle and its clients, you will need to have the setting `dtls.enabled` set to `true`. In addition, you will need to have the setting `dtls.keyStoreFile` to specify Jargyle's key store file (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `dtls.keyStorePassword` to specify the password for Jargyle's key store file.

```bash
    
    ./bin/jargyle start-server \
        --setting=dtls.enabled=true \
        --setting=dtls.keyStoreFile=server.jks \
        --setting=dtls.keyStorePassword=password
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-dtls-key-store-pass` instead. It will provide an interactive prompt for you to enter the password.

```bash
    
    ./bin/jargyle start-server \
        --setting=dtls.enabled=true \
        --setting=dtls.keyStoreFile=server.jks \
        --enter-dtls-key-store-pass
    
```

If you want to have the client authenticate using DTLS, you will need to have the setting `dtls.needClientAuth` set to `true`. In addition, you will need to have the setting `dtls.trustStoreFile` to specify the client's key store file to be used as a trust store (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `dtls.trustStorePassword` to specify the password for the client's trust store file.

```bash
    
    ./bin/jargyle start-server \
        --setting=dtls.enabled=true \
        --setting=dtls.keyStoreFile=server.jks \
        --setting=dtls.keyStorePassword=password \
        --setting=dtls.needClientAuth=true \
        --setting=dtls.trustStoreFile=client.jks \
        --setting=dtls.trustStorePassword=drowssap
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-dtls-trust-store-pass` instead. It will provide an interactive prompt for you to enter the password.

```bash
    
    ./bin/jargyle start-server \
        --setting=dtls.enabled=true \
        --setting=dtls.keyStoreFile=server.jks \
        --enter-dtls-key-store-pass \
        --setting=dtls.needClientAuth=true \
        --setting=dtls.trustStoreFile=client.jks \
        --enter-dtls-trust-store-pass
    
```

### 7. 9. Using SOCKS5 Authentication

Jargyle has the following SOCKS5 authentication methods to choose from:

-   `NO_AUTHENTICATION_REQUIRED`: No authentication required
-   `GSSAPI`: GSS-API authentication
-   `USERNAME_PASSWORD`: Username password authentication

On the command line, you can have one or more of the aforementioned authentication methods set in the setting `socks5.methods` as a space separated list. In the configuration file, you can have one or more of the aforementioned authentication methods set in the setting `socks5.methods` as a `<socks5.methods/>` XML element with one or more `<socks5.method/>` XML elements each specifying an authentication method.

Partial command line example:

```text
    
    "--setting=socks5.methods=NO_AUTHENTICATION_REQUIRED GSSAPI"
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.methods</name>
        <socks5.methods>
            <socks5.method>GSSAPI</socks5.method>
            <socks5.method>USERNAME_PASSWORD</socks5.method>
        </socks5.methods>
    </setting>
    
```

If not set, the default value for the setting `socks5.methods` is set to `NO_AUTHENTICATION_REQUIRED`

#### 7. 9. 1. Using No Authentication

Because the default value for the setting `socks5.methods` is set to `NO_AUTHENTICATION_REQUIRED`, it is not required for `NO_AUTHENTICATION_REQUIRED` to be included in the setting `socks5.methods`.

However, if other authentication methods are to be used in addition to `NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be included in the setting `socks5.methods`

Partial command line example:

```text
    
    "--setting=socks5.methods=NO_AUTHENTICATION_REQUIRED GSSAPI USERNAME_PASSWORD"
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.methods</name>
        <socks5.methods>
            <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
            <socks5.method>GSSAPI</socks5.method>
            <socks5.method>USERNAME_PASSWORD</socks5.method>
        </socks5.methods>
    </setting>
    
```

#### 7. 9. 2. Using Username Password Authentication

To use username password authentication, you will need to have the setting `socks5.methods` to have `USERNAME_PASSWORD` included.

Partial command line example:

```text
    
    --setting=socks5.methods=USERNAME_PASSWORD
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.methods</name>
        <socks5.methods>
            <socks5.method>USERNAME_PASSWORD</socks5.method>
        </socks5.methods>
    </setting>
    
```

Also, you will need to have the setting `socks5.userpassauth.userRepository` to specify the name of the class that extends `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository` along with an initialization string value.

The following are two provided classes you can use:

-   `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.StringSourceUserRepository`
-   `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.CsvFileSourceUserRepository`

`com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.StringSourceUserRepository`: This class handles the storage of the users from the initialization string value of a space separated list of username password values.

Each username password value in the space separated list must be of the following format:

```text
    
    USERNAME:PASSWORD
    
```

Where `USERNAME` is the username and `PASSWORD` is the password.

If the username or the password contains a colon character (`:`), then each colon character must be replaced with the URL encoding character `%3A`.

If the username or the password contains a space character, then each space character must be replaced with the URL encoding character `+` or `%20`.

If the username or the password contains a plus sign character (`+`) not used for URL encoding, then each plus sign character not used for URL encoding must be replaced with the URL encoding character `%2B`.

If the username or the password contains a percent sign character (`%`) not used for URL encoding, then each percent sign character not used for URL encoding must be replaced with the URL encoding character `%25`.

Partial command line example:

```text
    
    "--setting=socks5.methods=USERNAME_PASSWORD" \
    "--setting=socks5.userpassauth.userRepository=com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.StringSourceUserRepository:Aladdin:opensesame Jasmine:mission%3Aimpossible"
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.methods</name>
        <socks5.methods>
            <socks5.method>USERNAME_PASSWORD</socks5.method>
        </socks5.methods>
    </setting>
    <setting>
        <name>socks5.userpassauth.userRepository</name>
        <socks5.userpassauth.userRepository>
            <className>com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.StringSourceUserRepository</className>
            <initializationString>Aladdin:opensesame Jasmine:mission%3Aimpossible</initializationString>
        </socks5.userpassauth.userRepository>
    </setting>
    
```

`com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.CsvFileSourceUserRepository`: This class handles the storage of the users from a CSV file whose name is provided as an initialization string value. The users from the CSV file are loaded onto memory. Because of this, you will need at least as much memory as the size of the CSV file. If the CSV file does not exist, it will be created and used. If the CSV file does exist, the existing CSV file will be used. To manage users under a user repository, see [Managing Users](#7-9-2-1-managing-users).

Partial command line example:

```text
    
    --setting=socks5.methods=USERNAME_PASSWORD \
    --setting=socks5.userpassauth.userRepository=com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.CsvFileSourceUserRepository:users.csv
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.methods</name>
        <socks5.methods>
            <socks5.method>USERNAME_PASSWORD</socks5.method>
        </socks5.methods>
    </setting>    
    <setting>
        <name>socks5.userpassauth.userRepository</name>
        <socks5.userpassauth.userRepository>
            <className>com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.CsvFileSourceUserRepository</className>
            <initializationString>users.csv</initializationString>
        </socks5.userpassauth.userRepository>
    </setting>
    
```

#### 7. 9. 2. 1. Managing Users

You can manage users by first specifying a user repository that handles the storage of the users. To specify a user repository, you will have to specify the name of the class that extends `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository` along with an initialization string value.

The following is one provided class you can use:

-   `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.CsvFileSourceUserRepository`: This class handles the storage of the users from a CSV file whose name is provided as an initialization string value. The users from the CSV file are loaded onto memory. Because of this, you will need at least as much memory as the size of the CSV file. If the CSV file does not exist, it will be created and used. If the CSV file does exist, the existing CSV file will be used. 

##### 7. 9. 2. 1. 1. Adding Users

To add users to a user repository, you would run the following command:

```bash
    
    ./bin/jargyle manage-socks5-users USER_REPOSITORY_CLASS_NAME:INITIALIZATION_STRING add
    
```

Where `USER_REPOSITORY_CLASS_NAME` is the name of the class that extends `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository` and `INITIALIZATION_STRING` is the initialization string value.

Once you have run the command, an interactive prompt will ask you for the new user's name, password, and re-typed password. It will repeat the process to add another user if you want to continue to enter another user. If you do not want to enter any more users, the new users will be saved.

```text
    
    ./bin/jargyle manage-socks5-users com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.CsvFileSourceUserRepository:users.csv add
    User
    Name: Aladdin
    Password: 
    Re-type password:
    User 'Aladdin' added.
    Would you like to enter another user? ('Y' for yes): Y
    User
    Name: Jasmine
    Password: 
    Re-type password:
    User 'Jasmine' added.
    Would you like to enter another user? ('Y' for yes): Y
    User
    Name: Abu
    Password: 
    Re-type password:
    User 'Abu' added.
    Would you like to enter another user? ('Y' for yes): Y
    User
    Name: Jafar
    Password: 
    Re-type password:
    User 'Jafar' added.
    Would you like to enter another user? ('Y' for yes): n
    
```

##### 7. 9. 2. 1. 2. List All Users

To list all users from a user repository, you would run the following command:

```bash
    
    ./bin/jargyle manage-socks5-users USER_REPOSITORY_CLASS_NAME:INITIALIZATION_STRING list
    
```

Where `USER_REPOSITORY_CLASS_NAME` is the name of the class that extends `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository` and `INITIALIZATION_STRING` is the initialization string value.

Once you have run the command, it will list all the users from the user repository.

```text
    
    ./bin/jargyle manage-socks5-users com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.CsvFileSourceUserRepository:users.csv list
    Aladdin
    Jasmine
    Abu
    Jafar
    
```

##### 7. 9. 2. 1. 3. Removing a User

To remove a user from a user repository, you would run the following command:

```bash
    
    ./bin/jargyle manage-socks5-users USER_REPOSITORY_CLASS_NAME:INITIALIZATION_STRING remove NAME
    
```

Where `USER_REPOSITORY_CLASS_NAME` is the name of the class that extends `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository`, `INITIALIZATION_STRING` is the initialization string value, and `NAME` is the specified name of the user to be removed from the user repository.

Once you have run the command, the user of the specified name will be removed from the user repository.

```text
    
    ./bin/jargyle manage-socks5-users com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.CsvFileSourceUserRepository:users.csv remove Jafar
    User 'Jafar' removed
    
```

#### 7. 9. 3. Using GSS-API Authentication

To use GSS-API authentication, you will need to have the setting `socks5.methods` to have `GSSAPI` included.

Partial command line example:

```text
    
    --setting=socks5.methods=GSSAPI
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>socks5.methods</name>
        <socks5.methods>
            <socks5.method>GSSAPI</socks5.method>
        </socks5.methods>
    </setting>
    
```

Also, you will need to specify Java system properties to use a security mechanism that implements the GSS-API (for example, Kerberos is a security mechanism that implements the GSS-API).

The following is a sufficient example of using the Kerberos security mechanism:

```bash
    
    export JARGYLE_OPTS="-Djavax.security.auth.useSubjectCredsOnly=false -Djava.security.auth.login.config=login.conf -Djava.security.krb5.conf=krb5.conf"
    ./bin/jargyle start-server --setting=socks5.methods=GSSAPI 
    
```

The Java system property `-Djavax.security.auth.useSubjectCredsOnly=false` disables JAAS-based authentication to obtain the credentials directly and lets the underlying security mechanism obtain them instead.

The Java system property `-Djava.security.auth.login.config=login.conf` provides a JAAS configuration file for the underlying security mechanism.

`login.conf`:

```text
    
    com.sun.security.jgss.accept {
      com.sun.security.auth.module.Krb5LoginModule required
      principal="rcmd/127.0.0.1"
      useKeyTab=true
      keyTab="rcmd.keytab"
      storeKey=true;
    };
    
```

In `login.conf`, `rcmd/127.0.0.1` is a service principal that is created by a Kerberos administrator specifically for a SOCKS5 server with the service name `rcmd` residing at the address `127.0.0.1`. (In a production environment, the address `127.0.0.1` should be replaced by the name of the machine of where the SOCKS5 server resides.) 

Also in `login.conf`, `rcmd.keytab` is a keytab file also created by a Kerberos administrator that contains the aforementioned service principal and its respective encrypted key.  

The Java system property `-Djava.security.krb5.conf=krb5.conf` provides the Kerberos configuration file that points to the Kerberos Key Distribution Center (KDC) for authentication.   

`krb5.conf`:

```text
    
    [libdefaults]
        kdc_realm = EXAMPLE.COM
        default_realm = EXAMPLE.COM
        udp_preference_limit = 4096
        kdc_tcp_port = 12345
        kdc_udp_port = 12345
    
    [realms]
        EXAMPLE.COM = {
            kdc = 127.0.0.1:12345
        }
    
```

In `krb5.conf`, a KDC is defined as running at the address `127.0.0.1` on port `12345` with its realm as `EXAMPLE.COM`. (In a production environment, the address `127.0.0.1` should be replaced by the actual address or name of the machine of where the KDC resides. Also, in a production environment, the realm `EXAMPLE.COM` should be replaced by an actual realm provided by a Kerberos administrator.)  

### 7. 10. Chaining to Another SOCKS Server

You can have Jargyle chained to another SOCKS server, meaning that its traffic can be routed through another SOCKS server. To have Jargyle chained to another SOCKS server, you will need to specify the other SOCKS server as a URI in the setting `chaining.socksServerUri`

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:23456</value>
    </setting>
    
```

Please note that the scheme in the URI specifies the SOCKS protocol to be used when accessing the other SOCKS server (`socks5`), the address or name of the machine of where the other SOCKS server resides (`127.0.0.1`), and the port number of the other SOCKS server (`23456`). In the aforementioned examples, the SOCKS protocol version 5 is used. At this time, the only supported scheme for the URI format is `socks5`

#### 7. 10. 1. Using SSL/TLS for TCP Traffic Through SOCKS Server Chaining

You can use SSL/TLS for TCP traffic through SOCKS server chaining under the following condition: 

-   The other SOCKS server accepts SSL/TLS connections.

By default SSL/TLS for TCP traffic through SOCKS server chaining is disabled. To enable SSL/TLS for TCP traffic through SOCKS server chaining, you will need to have the setting `chaining.ssl.enabled` set to `true`. In addition, you will need to have the setting `chaining.ssl.trustStoreFile` to specify the server's key store file used as a trust store (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `chaining.ssl.trustStorePassword` to specify the password for the server's trust store file.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --setting=chaining.ssl.trustStorePassword=password
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-chaining-ssl-trust-store-pass` instead. It will provide an interactive prompt for you to enter the password.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --enter-chaining-ssl-trust-store-pass
    
```

If the other SOCKS server wants the client (Jargyle) to authenticate using SSL/TLS, you will need to have the setting `chaining.ssl.keyStoreFile` to specify the client's key store file (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `chaining.ssl.keyStorePassword` to specify the password for the client's key store file.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.keyStoreFile=client.jks \
    --setting=chaining.ssl.keyStorePassword=drowssap \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --setting=chaining.ssl.trustStorePassword=password    
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-chaining-ssl-key-store-pass` instead. It will provide an interactive prompt for you to enter the password.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.ssl.enabled=true \
    --setting=chaining.ssl.keyStoreFile=client.jks \
    --enter-chaining-ssl-key-store-pass \
    --setting=chaining.ssl.trustStoreFile=server.jks \
    --enter-chaining-ssl-trust-store-pass
    
```

#### 7. 10. 2. Using DTLS for UDP Traffic Through SOCKS Server Chaining

You can use DTLS for UDP traffic through SOCKS server chaining under the following condition: 

-   The other SOCKS server accepts DTLS connections.

By default DTLS for UDP traffic through SOCKS server chaining is disabled. To enable DTLS for UDP traffic through SOCKS server chaining, you will need to have the setting `chaining.dtls.enabled` set to `true`. In addition, you will need to have the setting `chaining.dtls.trustStoreFile` to specify the server's key store file used as a trust store (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `chaining.dtls.trustStorePassword` to specify the password for the server's trust store file.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.trustStoreFile=server.jks \
    --setting=chaining.dtls.trustStorePassword=password
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-chaining-dtls-trust-store-pass` instead. It will provide an interactive prompt for you to enter the password.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.trustStoreFile=server.jks \
    --enter-chaining-dtls-trust-store-pass
    
```

If the other SOCKS server wants the client (Jargyle) to authenticate using DTLS, you will need to have the setting `chaining.dtls.keyStoreFile` to specify the client's key store file (this file would need to be created by Java's keytool utility). Also, you will need to have the setting `chaining.dtls.keyStorePassword` to specify the password for the client's key store file.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.keyStoreFile=client.jks \
    --setting=chaining.dtls.keyStorePassword=drowssap \
    --setting=chaining.dtls.trustStoreFile=server.jks \
    --setting=chaining.dtls.trustStorePassword=password    
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-chaining-dtls-key-store-pass` instead. It will provide an interactive prompt for you to enter the password.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.dtls.enabled=true \
    --setting=chaining.dtls.keyStoreFile=client.jks \
    --enter-chaining-dtls-key-store-pass \
    --setting=chaining.dtls.trustStoreFile=server.jks \
    --enter-chaining-dtls-trust-store-pass
    
```

#### 7. 10. 3. Using SOCKS5 Authentication

Jargyle has the following SOCKS5 authentication methods to choose from for accessing the other SOCKS5 server:

-   `NO_AUTHENTICATION_REQUIRED`: No authentication required
-   `GSSAPI`: GSS-API authentication
-   `USERNAME_PASSWORD`: Username password authentication

On the command line, you can have one or more of the aforementioned authentication methods set in the setting `chaining.socks5.methods` as a space separated list. In the configuration file, you can have one or more of the aforementioned authentication methods set in the setting `chaining.socks5.methods` as a `<socks5.methods/>` XML element with one or more `<socks5.method/>` XML elements each specifying an authentication method.

Partial command line example:

```text
    
    "--setting=chaining.socks5.methods=NO_AUTHENTICATION_REQUIRED GSSAPI"
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socks5.methods</name>
        <socks5.methods>
            <socks5.method>GSSAPI</socks5.method>
            <socks5.method>USERNAME_PASSWORD</socks5.method>
        </socks5.methods>
    </setting>
    
```

If not set, the default value for the setting `chaining.socks5.methods` is set to `NO_AUTHENTICATION_REQUIRED`

##### 7. 10. 3. 1. Using No Authentication

Because the default value for the setting `chaining.socks5.methods` is set to `NO_AUTHENTICATION_REQUIRED`, it is not required for `NO_AUTHENTICATION_REQUIRED` to be included in the setting `chaining.socks5.methods`.

However, if other authentication methods are to be used in addition to `NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be included in the setting `chaining.socks5.methods`

Partial command line example:

```text
    
    "--setting=chaining.socks5.methods=NO_AUTHENTICATION_REQUIRED GSSAPI USERNAME_PASSWORD"
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socks5.methods</name>
        <socks5.methods>
            <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
            <socks5.method>GSSAPI</socks5.method>
            <socks5.method>USERNAME_PASSWORD</socks5.method>
        </socks5.methods>
    </setting>
    
```

##### 7. 10. 3. 2. Using Username Password Authentication

To use username password authentication, you will need to have the setting `chaining.socks5.methods` to have `USERNAME_PASSWORD` included. 

Partial command line example:

```text
    
    --setting=chaining.socks5.methods=USERNAME_PASSWORD
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socks5.methods</name>
        <socks5.methods>
            <socks5.method>USERNAME_PASSWORD</socks5.method>
        </socks5.methods>
    </setting>
    
```

Also, you will need to have the settings `chaining.socks5.userpassauth.username` and `chaining.socks5.userpassauth.password` respectively specify the username and password for the other SOCKS5 server.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassauth.username=Aladdin \
    --setting=chaining.socks5.userpassauth.password=opensesame
    
```

If you do not want to have the password appear in any script or in any part of the command line history for security reasons, you can use the command line option `--enter-chaining-socks5-userpassauth-pass` instead. It will provide an interactive prompt for you to enter the password.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.methods=USERNAME_PASSWORD \
    --setting=chaining.socks5.userpassauth.username=Aladdin \
    --enter-chaining-socks5-userpassauth-pass
    
```

##### 7. 10. 3. 3. Using GSS-API Authentication

To use GSS-API authentication, you will need to have the setting `chaining.socks5.methods` to have `GSSAPI` included.

Partial command line example:

```text
    
    --setting=chaining.socks5.methods=GSSAPI
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socks5.methods</name>
        <socks5.methods>
            <socks5.method>GSSAPI</socks5.method>
        </socks5.methods>
    </setting>
    
```

Also, you will need to specify Java system properties to use a security mechanism that implements the GSS-API (for example, Kerberos is a security mechanism that implements the GSS-API), and you will also need to specify the GSS-API service name for the other SOCKS5 server.

The following is a sufficient example of using the Kerberos security mechanism:

```bash
    
    export JARGYLE_OPTS="-Djavax.security.auth.useSubjectCredsOnly=false -Djava.security.auth.login.config=login.conf -Djava.security.krb5.conf=krb5.conf"
    ./bin/jargyle start-server \
        --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
        --setting=chaining.socks5.methods=GSSAPI \
        --setting=chaining.socks5.gssapiauth.serviceName=rcmd/127.0.0.1 
    
```

The Java system property `-Djavax.security.auth.useSubjectCredsOnly=false` disables JAAS-based authentication to obtain the credentials directly and lets the underlying security mechanism obtain them instead.

The Java system property `-Djava.security.auth.login.config=login.conf` provides a JAAS configuration file for the underlying security mechanism.

`login.conf`:

```text
    
    com.sun.security.jgss.initiate  {
      com.sun.security.auth.module.Krb5LoginModule required
      principal="alice"
      useKeyTab=true
      keyTab="alice.keytab"
      storeKey=true;
    };
    
```

In `login.conf`, `alice` is a principal that is created by a Kerberos administrator. 

Also in `login.conf`, `alice.keytab` is a keytab file also created by a Kerberos administrator that contains the aforementioned principal and its respective encrypted key.  

The Java system property `-Djava.security.krb5.conf=krb5.conf` provides the Kerberos configuration file that points to the Kerberos Key Distribution Center (KDC) for authentication.   

`krb5.conf`:

```text
    
    [libdefaults]
        kdc_realm = EXAMPLE.COM
        default_realm = EXAMPLE.COM
        udp_preference_limit = 4096
        kdc_tcp_port = 12345
        kdc_udp_port = 12345
    
    [realms]
        EXAMPLE.COM = {
            kdc = 127.0.0.1:12345
        }
    
```

In `krb5.conf`, a KDC is defined as running at the address `127.0.0.1` on port `12345` with its realm as `EXAMPLE.COM`. (In a production environment, the address `127.0.0.1` should be replaced by the actual address or name of the machine of where the KDC resides. Also, in a production environment, the realm `EXAMPLE.COM` should be replaced by an actual realm provided by a Kerberos administrator.)

The command line option `--setting=chaining.socks5.gssapiauth.serviceName=rcmd/127.0.0.1` is the GSS-API service name (or the Kerberos service principal) for the other SOCKS5 server residing at the address `127.0.0.1`. (In a production environment, the address `127.0.0.1` should be replaced by the name of the machine of where the other SOCKS5 server resides.)

#### 7. 10. 4. Resolving Host Names Through SOCKS5 Server Chaining

Before discussing host name resolution through SOCKS5 server chaining, a brief explanation of Jargyle's internals:

Jargyle uses sockets to interact with the external world.

-   Under the CONNECT command, it uses a socket that connects to the desired target server. In this documentation, this socket is called the server-facing socket.
-   Under the BIND command, it uses a socket that listens for an inbound socket. In this documentation, this socket is called the listen socket.
-   Under the UDP ASSOCIATE command, it uses a UDP socket that sends and receives datagram packets to and from peer UDP sockets. In this documentation, this UDP socket is called the peer-facing UDP socket.

Jargyle also uses a host resolver to resolve host names for the aforementioned sockets and for [the RESOLVE command](#7-15-2-the-socks5-resolve-command).

When Jargyle is chained to another SOCKS5 server, the aforementioned sockets that Jargyle uses become SOCKS5-enabled, meaning that their traffic is routed through the other SOCKS5 server.

It is similar for the host resolver. When Jargyle is chained to another SOCKS5 server, the host resolver that Jargyle uses becomes SOCKS5-enabled, meaning that it can use the other SOCKS5 server to resolve host names provided that the other SOCKS5 server supports the SOCKS5 RESOLVE command. However, this functionality for the host resolver is disabled by default making the host resolver resolve host names through the local system.

Therefore, default host name resolution through SOCKS5 server chaining is performed but has the following limitations:

Default host name resolution through SOCKS5 server chaining OCCURS ONLY...

-   ...under the CONNECT command when the server-facing socket makes an extemporaneous outbound connection. Preparation is omitted for the server-facing socket. Such preparation includes applying the specified socket settings for the server-facing socket, resolving the target host name before connecting, and setting the specified timeout in milliseconds on waiting for the server-facing socket to connect. The host resolver is not used in resolving the target host name. When the server-facing socket is SOCKS5-enabled, the target host name is resolved by the other SOCKS5 server and not through the local system.

Default host name resolution through SOCKS5 server chaining DOES NOT OCCUR...

-   ...under the CONNECT command when the server-facing socket makes a prepared outbound connection. Preparation for the server-facing socket includes resolving the target host name before connecting. The host resolver is used in resolving the target host name. Because of its default functionality, the host resolver resolves the target host name through the local system.
-   ...under the BIND command when resolving the binding host name for the listen socket. The host resolver is used in resolving the binding host name for the listen socket. Because of its default functionality, the host resolver resolves the binding host name for the listen socket through the local system.
-   ...under the UDP ASSOCIATE command when resolving the host name for an outbound datagram packet. The host resolver is used in resolving the host name for an outbound datagram packet. Because of its default functionality, the host resolver resolves the host name for an outbound datagram packet through the local system.
-   ...under the RESOLVE command when resolving the provided host name. The host resolver is used in resolving the provided host name. Because of its default functionality, the host resolver resolves the provided host name through the local system.

If you prefer to have host name resolution through SOCKS5 server chaining without the aforementioned limitations, you would need to set the setting `chaining.socks5.useResolveCommand` to `true`. This setting enables the host resolver to use the SOCKS5 RESOLVE command on the other SOCKS5 server to resolve host names. This setting can only be used if the other SOCKS5 server supports the SOCKS5 RESOLVE command.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.useResolveCommand=true
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:23456</value>
    </setting>
    <setting>
        <name>chaining.socks5.useResolveCommand</name>
        <value>true</value>
    </setting>
    
```

### 7. 11. Chaining to a Specified Chain of Other SOCKS Servers

You can have Jargyle chained to a specified chain of other SOCKS servers, meaning that its traffic can be routed through the specified chain of the other SOCKS servers. To have Jargyle chained to a specified chain of other SOCKS servers, you will need to have the setting `chaining.socksServerUri` specified multiple times with each setting specifying a SOCKS server as a URI.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:65432 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:34567
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:23456</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:65432</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:34567</value>
    </setting>        
    
```

To specify the settings regarding a SOCKS server in the chain, the settings regarding a SOCKS server will need to be placed after that specified SOCKS server but before the next specified SOCKS server if any.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:23456 \
    --setting=chaining.socks5.methods=GSSAPI \
    --setting=chaining.socks5.gssapiauth.serviceName=rcmd/127.0.0.1 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:65432 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:34567 \
    --setting=chaining.socks5.useResolveCommand=true
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:23456</value>
    </setting>
    <setting>
        <name>chaining.socks5.methods</name>
        <socks5.methods>
            <socks5.method>GSSAPI</socks5.method>
        </socks5.methods>
    </setting>
    <setting>
        <name>chaining.socks5.gssapiauth.serviceName</name>
        <value>rcmd/127.0.0.1</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:65432</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:34567</value>
    </setting>        
    <setting>
        <name>chaining.socks5.useResolveCommand</name>
        <value>true</value>
    </setting>    
    
```

The known limitations of Jargyle chained to a specified chain of other SOCKS servers include the following:

-   Only TCP traffic can be routed through the chain. Jargyle will attempt to route any UDP traffic through the last SOCKS server of the chain.

### 7. 12. Chaining to Multiple Specified Chains of Other SOCKS Servers

You can have Jargyle chained to multiple specified chains of other SOCKS servers, meaning that its traffic can be routed through one of the specified chains of other SOCKS servers. To have Jargyle chained to multiple specified chains of other SOCKS servers, you will need to have a route ID assigned at the end of each [chain](#7-11-chaining-to-a-specified-chain-of-other-socks-servers) by using the setting `chaining.routeId`

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:11111 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:11112 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:11113 \
    --setting=chaining.routeId=alpha \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:22221 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:22222 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:22223 \
    --setting=chaining.routeId=beta \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:33331 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:33332 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:33333 \
    --setting=chaining.routeId=delta
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:11111</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:11112</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:11113</value>
    </setting>
    <setting>
        <name>chaining.routeId</name>
        <value>alpha</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:22221</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:22222</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:22223</value>
    </setting>
    <setting>
        <name>chaining.routeId</name>
        <value>beta</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:33331</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:33332</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:33333</value>
    </setting>
    <setting>
        <name>chaining.routeId</name>
        <value>delta</value>
    </setting>
    
```

From the aforementioned examples: 

-   The chain consisting of `socks5://127.0.0.1:11111`, `socks5://127.0.0.1:11112`, and `socks5://127.0.0.1:11113` is assigned the route ID of `alpha`
-   The chain consisting of `socks5://127.0.0.1:22221`, `socks5://127.0.0.1:22222`, and `socks5://127.0.0.1:22223` is assigned the route ID of `beta`
-   The chain consisting of `socks5://127.0.0.1:33331`, `socks5://127.0.0.1:33332`, and `socks5://127.0.0.1:33333` is assigned the route ID of `delta`

There is another route that is assigned a route ID. That route is the direct route. The direct route uses no chain to route the traffic through. It is assigned by default a route ID of `lastRoute`.

To omit the direct route from being included, have the last chain not assigned a route ID from the setting `chaining.routeId`.

Partial command line example:

```text
    
    --setting=chaining.socksServerUri=socks5://127.0.0.1:11111 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:11112 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:11113 \
    --setting=chaining.routeId=alpha \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:22221 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:22222 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:22223 \
    --setting=chaining.routeId=beta \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:33331 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:33332 \
    --setting=chaining.socksServerUri=socks5://127.0.0.1:33333
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:11111</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:11112</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:11113</value>
    </setting>
    <setting>
        <name>chaining.routeId</name>
        <value>alpha</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:22221</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:22222</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:22223</value>
    </setting>
    <setting>
        <name>chaining.routeId</name>
        <value>beta</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:33331</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:33332</value>
    </setting>
    <setting>
        <name>chaining.socksServerUri</name>
        <value>socks5://127.0.0.1:33333</value>
    </setting>
    
```

From the aforementioned examples: 

-   The chain consisting of `socks5://127.0.0.1:11111`, `socks5://127.0.0.1:11112`, and `socks5://127.0.0.1:11113` is assigned the route ID of `alpha`
-   The chain consisting of `socks5://127.0.0.1:22221`, `socks5://127.0.0.1:22222`, and `socks5://127.0.0.1:22223` is assigned the route ID of `beta`
-   The chain consisting of `socks5://127.0.0.1:33331`, `socks5://127.0.0.1:33332`, and `socks5://127.0.0.1:33333` is assigned by default the route ID of `lastRoute`

To change the route ID assigned to the last route, you can set the setting `lastRouteId` to the route ID you want assigned to the last route.

Partial command line example:

```text
    
    --setting=lastRouteId=omega
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>lastRouteId</name>
        <value>omega</value>
    </setting>
    
```

You can also set the setting `routeSelectionStrategy` to specify the selection strategy for the next route. The default is `CYCLICAL`.

Partial command line example:

```text
    
    --setting=routeSelectionStrategy=RANDOM
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>routeSelectionStrategy</name>
        <value>RANDOM</value>
    </setting>
    
```

You can also set the setting `routeSelectionLogAction` to specify the logging action to take if a route is selected.

Partial command line example:

```text
    
    --setting=routeSelectionLogAction=LOG_AS_INFO
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>routeSelectionLogAction</name>
        <value>LOG_AS_INFO</value>
    </setting>
    
```

### 7. 13. Using Rules to Manage Traffic

A rule consists of the following:

-   Rule conditions: fields that altogether evaluate as true if they match a specific instance of traffic
-   Rule results: fields that are applied if the aforementioned rule conditions evaluate as true for matching a specific instance of traffic

On the command line, a rule consists of a space separated list of both rule conditions and rule results. In the configuration file, a rule is expressed as a `<rule/>` XML element with a `<ruleConditions/>` XML element and a `<ruleResults/>` XML element. See [Rule Conditions](#7-13-1-rule-conditions) and [Rule Results](#7-13-2-rule-results) for more information.

To specify a rule, you would need to have the setting `rule` specify the rule. 

Partial command line example:

```text
    
    --setting=rule=firewallAction=ALLOW
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions/>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allows all forms of traffic -->
    </setting>
    
```

To specify multiple rules, you would need to have the setting `rule` specified multiple times with each setting specifying another rule.

Partial command line example:

```text
    
    "--setting=rule=socks5.command=CONNECT socks5.desiredDestinationPort=80 socks5.desiredDestinationPort=443 firewallAction=ALLOW" \
    "--setting=rule=socks5.command=CONNECT firewallAction=DENY" \
    --setting=rule=firewallAction=ALLOW
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions>
                <ruleCondition>
                    <name>socks5.command</name>
                    <value>CONNECT</value>
                </ruleCondition>
                <ruleCondition>
                    <name>socks5.desiredDestinationPort</name>
                    <value>80</value>
                </ruleCondition>
                <ruleCondition>
                    <name>socks5.desiredDestinationPort</name>
                    <value>443</value>
                </ruleCondition>
            </ruleConditions>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allows the CONNECT command to any server on port 80 or 443 -->
    </setting>
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions>
                <ruleCondition>
                    <name>socks5.command</name>
                    <value>CONNECT</value>
                </ruleCondition>
            </ruleConditions>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>DENY</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Denies the CONNECT command to any server on any other port -->
    </setting>
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions/>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allow anything else -->
    </setting>    
    
```

When a specific instance of traffic is matched by the first rule, that rule is applied and the rest of the rules are ignored. Therefore it is best to have more specific rules specified first and have less specific rules specified last.

#### 7. 13. 1. Rule Conditions

On the command line, rule conditions consist of a space separated list of rule conditions. Each rule condition consists of the syntax of `NAME=VALUE` where `NAME` is expressed as the name of the rule condition and `VALUE` is expressed as the value assigned to the rule condition. In the configuration file, rule conditions are expressed in a `<ruleConditions/>` XML element with zero to many `<ruleCondition/>` XML elements. Each `<ruleCondition/>` XML element contains a `<name/>` XML element for the name of the rule condition and the `<value/>` XML element of the value assigned to the rule condition.

Partial command line example:

```text
    
    clientAddress=127.0.0.1 clientAddress=0:0:0:0:0:0:0:1
    
```

Partial configuration file example:

```xml
    
    <ruleConditions>
        <ruleCondition>
            <name>clientAddress</name>
            <value>127.0.0.1</value>
        </ruleCondition>
        <ruleCondition>
            <name>clientAddress</name>
            <value>0:0:0:0:0:0:0:1</value>
        </ruleCondition>
    </ruleConditions>
    
```

Rule conditions within a rule are evaluated as true if each group of one or more rule conditions with the same name is evaluated as true. A group of one or more rule conditions with the same name is evaluated as true if at least one of the rule conditions within that group is evaluated as true. Zero rule conditions within a rule are evaluated as true.

Partial configuration file examples:

```xml
    
    <ruleConditions>
        <ruleCondition>
            <name>clientAddress</name>
            <value>127.0.0.1</value>
        </ruleCondition>
        <ruleCondition>
            <name>clientAddress</name>
            <value>0:0:0:0:0:0:0:1</value>
        </ruleCondition>
    </ruleConditions>
    <!-- evaluates as true if the client address is the IPv4 loopback address or the IPv6 loopback address -->
    
```

```xml
    
    <ruleConditions>
        <ruleCondition>
            <name>socks5.method</name>
            <value>USERNAME_PASSWORD</value>
        </ruleCondition>
        <ruleCondition>
            <name>socks5.user</name>
            <value>guest</value>
        </ruleCondition>
        <ruleCondition>
            <name>socks5.user</name>
            <value>specialuser</value>
        </ruleCondition>
    </ruleConditions>
    <!-- evaluates as true if username password authentication was used and the user is either 'guest' or 'specialuser' -->
    
```

```xml
    
    <ruleConditions/>
    <!-- evaluates as true since there are no rule conditions given -->
    
```

A complete listing of rule conditions can be found in the [settings help](#7-1-4-settings-help) under `SETTING VALUE SYNTAXES` > `GENERAL_RULE_CONDITIONS` and `SETTING VALUE SYNTAXES` > `SOCKS5_RULE_CONDITIONS`.

#### 7. 13. 2. Rule Results

On the command line, rule results consist of a space separated list of rule results. Each rule result consists of the syntax of `NAME=VALUE` where `NAME` is expressed as the name of the rule result and `VALUE` is expressed as the value assigned to the rule result. In the configuration file, rule result are expressed in a `<ruleResults/>` XML element with zero to many `<ruleResult/>` XML elements. Each `<ruleResult/>` XML element contains a `<name/>` XML element for the name of the rule result and the `<value/>` XML element of the value assigned to the rule result.

Partial command line example:

```text
    
    firewallAction=ALLOW firewallActionLogAction=LOG_AS_INFO
    
```

Partial configuration file example:

```xml
    
    <ruleResults>
        <ruleResult>
            <name>firewallAction</name>
            <value>ALLOW</value>
        </ruleResult>
        <ruleResult>
            <name>firewallActionLogAction</name>
            <value>LOG_AS_INFO</value>
        </ruleResult>
    </ruleResults>
    
```

Unless otherwise stated, if a rule result of the same name appears more than once in the space separated list or in the `<ruleResults/>` XML element, then only the last rule result of the same name is recognized.

A complete listing of rule results can be found in the [settings help](#7-1-4-settings-help) under `SETTING VALUE SYNTAXES` > `GENERAL_RULE_RESULTS` and `SETTING VALUE SYNTAXES` > `SOCKS5_RULE_RESULTS`.

#### 7. 13. 3. Allowing or Denying Traffic

To allow or deny a specific instance of traffic, you will need the following rule result:

-   `firewallAction`

The value given to the rule result must be either of the following values:

-   `ALLOW`
-   `DENY`

This rule result can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.command`
-   `socks5.desiredDestinationAddress`
-   `socks5.desiredDestinationPort`
-   `socks5.method`
-   `socks5.secondServerBoundAddress`
-   `socks5.secondServerBoundPort`
-   `socks5.serverBoundAddress`
-   `socks5.serverBoundPort`
-   `socks5.udp.inbound.desiredDestinationAddress`
-   `socks5.udp.inbound.desiredDestinationPort`
-   `socks5.udp.inbound.sourceAddress`
-   `socks5.udp.inbound.sourcePort`
-   `socks5.udp.outbound.desiredDestinationAddress`
-   `socks5.udp.outbound.desiredDestinationPort`
-   `socks5.udp.outbound.sourceAddress`
-   `socks5.udp.outbound.sourcePort`
-   `socks5.user`
-   `socksServerAddress`

**Note**: for any other rule results to be applied, the rule result `firewallAction` with the value of `ALLOW` must be present.

You can also specify the logging action to take if the rule result `firewallAction` is applied by adding the following rule result:

-   `firewallActionLogAction`

The rule result `firewallActionLogAction` is optional.

Partial command line example:

```text
    
    "--setting=rule=socks5.command=BIND socks5.command=UDP_ASSOCIATE firewallAction=DENY firewallActionLogAction=LOG_AS_WARNING" \
    --setting=rule=firewallAction=ALLOW
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions>
                <ruleCondition>
                    <name>socks5.command</name>
                    <value>BIND</value>
                </ruleCondition>
                <ruleCondition>
                    <name>socks5.command</name>
                    <value>UDP_ASSOCIATE</value>
                </ruleCondition>
            </ruleConditions>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>DENY</value>
                </ruleResult>
                <ruleResult>
                    <name>firewallActionLogAction</name>
                    <value>LOG_AS_WARNING</value>
                </ruleResult>                
            </ruleResults>
        </rule>
        <!-- Deny any BIND or UDP ASSOCIATE commands and log as a warning message if they are denied -->
    </setting>
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions/>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allow anything else -->
    </setting>    
    
```

#### 7. 13. 4. Allowing a Limited Number of Simultaneous Instances of Traffic

To allow a limited number of simultaneous specific instances of traffic, you will need the following rule result:

-   `firewallActionAllowLimit`

The value given to the rule result must be an integer between 0 (inclusive) and 2147483647 (inclusive)

This rule result can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.command`
-   `socks5.desiredDestinationAddress`
-   `socks5.desiredDestinationPort`
-   `socks5.method`
-   `socks5.secondServerBoundAddress`
-   `socks5.secondServerBoundPort`
-   `socks5.serverBoundAddress`
-   `socks5.serverBoundPort`
-   `socks5.user`
-   `socksServerAddress`

You can also specify the logging action to take if the limit on the number of simultaneous specific instances of traffic has been reached by adding the following rule result:

-   `firewallActionAllowLimitReachedLogAction`

The rule result `firewallActionAllowLimitReachedLogAction` is optional.

Partial command line example:

```text
    
    "--setting=rule=socks5.method=USERNAME_PASSWORD socks5.user=guest firewallAction=ALLOW firewallActionAllowLimit=50 firewallActionAllowLimitReachedLogAction=LOG_AS_INFO" \
    --setting=rule=firewallAction=ALLOW
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions>
                <ruleCondition>
                    <name>socks5.method</name>
                    <value>USERNAME_PASSWORD</value>
                </ruleCondition>
                <ruleCondition>
                    <name>socks5.user</name>
                    <value>guest</value>
                </ruleCondition>
            </ruleConditions>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
                <ruleResult>
                    <name>firewallActionAllowLimit</name>
                    <value>50</value>
                </ruleResult>                
                <ruleResult>
                    <name>firewallActionAllowLimitReachedLogAction</name>
                    <value>LOG_AS_INFO</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allow the user 'guest' from username password authentication 50 simultaneous connections and log as an informational message that the limit has been reached -->
    </setting>
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions/>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allow anything else -->
    </setting>    
    
```

#### 7. 13. 5. Routing Traffic

To route traffic, you can use the following rule results:

-   `routeSelectionStrategy`: Specifies the selection strategy for the next route (This rule result is optional. If this rule result is not specified, the setting `routeSelectionStrategy` is used.)
-   `selectableRouteId`: Specifies the ID for a selectable [route](#7-12-chaining-to-multiple-specified-chains-of-other-socks-servers) (This rule result is optional. This rule result can be specified multiple times with each rule result specifying another ID for a selectable route. If this rule result is not specified, all of the routes defined by the settings `chaining.routeId` and `lastRouteId` are selectable.)

These rule results can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.command`
-   `socks5.desiredDestinationAddress`
-   `socks5.desiredDestinationPort`
-   `socks5.method`
-   `socks5.user`
-   `socksServerAddress`

You can also specify the logging action to take if a route is selected by adding the following rule result:

-   `routeSelectionLogAction`

The rule result `routeSelectionLogAction` is optional. If the rule result is not specified, the setting `routeSelectionLogAction` is used.

Partial command line example:

```text
    
    "--setting=rule=socks5.command=CONNECT firewallAction=ALLOW routeSelectionStrategy=RANDOM selectableRouteId=alpha selectableRouteId=beta selectableRouteId=delta routeSelectionLogAction=LOG_AS_INFO" \
    "--setting=rule=firewallAction=ALLOW routeSelectionStrategy=CYCLICAL selectableRouteId=omega "
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions>
                <ruleCondition>
                    <name>socks5.command</name>
                    <value>CONNECT</value>
                </ruleCondition>
            </ruleConditions>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
                <ruleResult>
                    <name>routeSelectionStrategy</name>
                    <value>RANDOM</value>
                </ruleResult>
                <ruleResult>
                    <name>selectableRouteId</name>
                    <value>alpha</value>
                </ruleResult>
                <ruleResult>
                    <name>selectableRouteId</name>
                    <value>beta</value>
                </ruleResult>
                <ruleResult>
                    <name>selectableRouteId</name>
                    <value>delta</value>
                </ruleResult>
                <ruleResult>
                    <name>routeSelectionLogAction</name>
                    <value>LOG_AS_INFO</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Randomly select either route 'alpha', 'beta' or 'delta' when allowing the CONNECT command and log as an informational message the route that has been selected -->
    </setting>
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions/>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
                <ruleResult>
                    <name>routeSelectionStrategy</name>
                    <value>CYCLICAL</value>
                </ruleResult>
                <ruleResult>
                    <name>selectableRouteId</name>
                    <value>omega</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allow anything else to go through route 'omega' -->
    </setting>    
    
```

#### 7. 13. 6. Redirecting the Desired Destination

To redirect the desired destination, you will need either or both of the following rule results:

-   `socks5.desiredDestinationAddressRedirect`: Specifies the desired destination address redirect
-   `socks5.desiredDestinationPortRedirect`: Specifies the desired destination port redirect

These rule results can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.command`
-   `socks5.desiredDestinationAddress`
-   `socks5.desiredDestinationPort`
-   `socks5.method`
-   `socks5.user`
-   `socksServerAddress`

You can also specify the logging action to take if the desired destination is redirected by adding the following rule result:

-   `socks5.desiredDestinationRedirectLogAction`

The rule result `socks5.desiredDestinationRedirectLogAction` is optional.

Partial command line example:

```text
    
    "--setting=rule=socks5.desiredDestinationAddress=discontinuedserver.com firewallAction=ALLOW socks5.desiredDestinationAddressRedirect=newserver.com socks5.desiredDestinationRedirectLogAction=LOG_AS_INFO" \
    --setting=rule=firewallAction=ALLOW
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions>
                <ruleCondition>
                    <name>socks5.desiredDestinationAddress</name>
                    <value>discontinuedserver.com</value>
                </ruleCondition>
            </ruleConditions>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
                <ruleResult>
                    <name>socks5.desiredDestinationAddressRedirect</name>
                    <value>newserver.com</value>
                </ruleResult>
                <ruleResult>
                    <name>socks5.desiredDestinationRedirectLogAction</name>
                    <value>LOG_AS_INFO</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Redirect desired destination 'discontinuedserver.com' to 'newserver.com' and log as an informational message the redirection -->
    </setting>
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions/>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allow anything else -->
    </setting>    
    
```

#### 7. 13. 7. Configuring Sockets

To configure the sockets, you will need any of the following rule results:

-   `bindHost`: Specifies the binding host name or address for all sockets
 
-   `bindTcpPortRange`: Specifies a binding [port range](#7-14-2-port-range) for all TCP sockets (This rule result can be specified multiple times with each rule result specifying another port range)

-   `bindUdpPortRange`: Specifies a binding [port range](#7-14-2-port-range) for all UDP sockets (This rule result can be specified multiple times with each rule result specifying another port range)

-   `clientSocketSetting`: Specifies a socket setting for the client socket (This rule result can be specified multiple times with each rule result specifying another socket setting)

-   `externalFacingBindHost`: Specifies the binding host name or address for all external-facing sockets
 
-   `externalFacingBindTcpPortRange`: Specifies a binding [port range](#7-14-2-port-range) for all external-facing TCP sockets (This rule result can be specified multiple times with each rule result specifying another port range)

-   `externalFacingBindUdpPortRange`: Specifies a binding [port range](#7-14-2-port-range) for all external-facing UDP sockets (This rule result can be specified multiple times with each rule result specifying another port range)

-   `externalFacingSocketSetting`: Specifies a socket setting for all external-facing sockets (This rule result can be specified multiple times with each rule result specifying another socket setting)

-   `internalFacingBindHost`: Specifies the binding host name or address for all internal-facing sockets

-   `internalFacingBindTcpPortRange`: Specifies a binding [port range](#7-14-2-port-range) for all internal-facing TCP sockets (This rule result can be specified multiple times with each rule result specifying another port range)

-   `internalFacingBindUdpPortRange`: Specifies a binding [port range](#7-14-2-port-range) for all internal-facing UDP sockets (This rule result can be specified multiple times with each rule result specifying another port range)

-   `internalFacingSocketSetting`: Specifies a socket setting for all internal-facing sockets (This rule result can be specified multiple times with each rule result specifying another socket setting)

-   `socketSetting`: Specifies a socket setting for all sockets (This rule result can be specified multiple times with each rule result specifying another socket setting)

-   `socks5.onBind.inboundSocketSetting`: Specifies a socket setting for the inbound socket (This rule result can be specified multiple times with each rule result specifying another socket setting)

-   `socks5.onBind.listenBindHost`: Specifies the binding host name or address for the listen socket if the provided host address is all zeros

-   `socks5.onBind.listenBindPortRange`: Specifies a binding [port range](#7-14-2-port-range) for the listen socket if the provided port is zero (This rule result can be specified multiple times with each rule result specifying another port range)

-   `socks5.onBind.listenSocketSetting`: Specifies a socket setting for the listen socket (This rule result can be specified multiple times with each rule result specifying another socket setting)

-   `socks5.onCommand.bindHost`: Specifies the binding host name or address for all sockets
 
-   `socks5.onCommand.bindTcpPortRange`: Specifies a binding [port range](#7-14-2-port-range) for all TCP sockets (This rule result can be specified multiple times with each rule result specifying another port range)

-   `socks5.onCommand.bindUdpPortRange`: Specifies a binding [port range](#7-14-2-port-range) for all UDP sockets (This rule result can be specified multiple times with each rule result specifying another port range)

-   `socks5.onCommand.externalFacingBindHost`: Specifies the binding host name or address for all external-facing sockets
 
-   `socks5.onCommand.externalFacingBindTcpPortRange`: Specifies a binding [port range](#7-14-2-port-range) for all external-facing TCP sockets (This rule result can be specified multiple times with each rule result specifying another port range)

-   `socks5.onCommand.externalFacingBindUdpPortRange`: Specifies a binding [port range](#7-14-2-port-range) for all external-facing UDP sockets (This rule result can be specified multiple times with each rule result specifying another port range)

-   `socks5.onCommand.externalFacingSocketSetting`: Specifies a socket setting for all external-facing sockets (This rule result can be specified multiple times with each rule result specifying another socket setting)

-   `socks5.onCommand.internalFacingBindHost`: Specifies the binding host name or address for all internal-facing sockets

-   `socks5.onCommand.internalFacingBindTcpPortRange`: Specifies a binding [port range](#7-14-2-port-range) for all internal-facing TCP sockets (This rule result can be specified multiple times with each rule result specifying another port range)

-   `socks5.onCommand.internalFacingBindUdpPortRange`: Specifies a binding [port range](#7-14-2-port-range) for all internal-facing UDP sockets (This rule result can be specified multiple times with each rule result specifying another port range)

-   `socks5.onCommand.internalFacingSocketSetting`: Specifies a socket setting for all internal-facing sockets (This rule result can be specified multiple times with each rule result specifying another socket setting)

-   `socks5.onCommand.socketSetting`: Specifies a socket setting for all sockets (This rule result can be specified multiple times with each rule result specifying another socket setting)

-   `socks5.onConnect.prepareServerFacingSocket`: Specifies the boolean value to indicate if the server-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

-   `socks5.onConnect.serverFacingBindHost`: Specifies the binding host name or address for the server-facing socket

-   `socks5.onConnect.serverFacingBindPortRange`: Specifies a binding [port range](#7-14-2-port-range) for the server-facing socket (This rule result can be specified multiple times with each rule result specifying another port range)

-   `socks5.onConnect.serverFacingConnectTimeout`: Specifies the timeout in milliseconds on waiting for the server-facing socket to connect (Value must be an integer between 1 (inclusive) and 2147483647 (inclusive))

-   `socks5.onConnect.serverFacingSocketSetting`: Specifies a socket setting for the server-facing socket (This rule result can be specified multiple times with each rule result specifying another socket setting)

-   `socks5.onUdpAssociate.clientFacingBindHost`: Specifies the binding host name or address for the client-facing UDP socket

-   `socks5.onUdpAssociate.clientFacingBindPortRange`: Specifies a binding [port range](#7-14-2-port-range) for the client-facing UDP socket (This rule result can be specified multiple times with each rule result specifying another port range)

-   `socks5.onUdpAssociate.clientFacingSocketSetting`: Specifies a socket setting for the client-facing UDP socket (This rule result can be specified multiple times with each rule result specifying another socket setting)

-   `socks5.onUdpAssociate.peerFacingBindHost`: Specifies the binding host name or address for the peer-facing UDP socket

-   `socks5.onUdpAssociate.peerFacingBindPortRange`: Specifies a binding [port range](#7-14-2-port-range) for the peer-facing UDP socket (This rule result can be specified multiple times with each rule result specifying another port range)

-   `socks5.onUdpAssociate.peerFacingSocketSetting`: Specifies a socket setting for the peer-facing UDP socket (This rule result can be specified multiple times with each rule result specifying another socket setting)

These rule results can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.command`
-   `socks5.desiredDestinationAddress`
-   `socks5.desiredDestinationPort`
-   `socks5.method`
-   `socks5.user`
-   `socksServerAddress`

The rule result `socks5.onBind.inboundSocketSetting` can also be used with the following rule conditions:

-   `socks5.serverBoundAddress`
-   `socks5.serverBoundPort`

Partial command line example:

```text
    
    "--setting=rule=socks5.command=CONNECT socks5.desiredDestinationAddress=specialserver.com firewallAction=ALLOW socks5.onConnect.prepareServerFacingSocket=true socks5.onConnect.serverFacingSocketSetting=SO_RCVBUF=256 socks5.onConnect.serverFacingSocketSetting=SO_SNDBUF=256" \
    --setting=rule=firewallAction=ALLOW
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions>
                <ruleCondition>
                    <name>socks5.command</name>
                    <value>CONNECT</value>
                </ruleCondition>            
                <ruleCondition>
                    <name>socks5.desiredDestinationAddress</name>
                    <value>specialserver.com</value>
                </ruleCondition>
            </ruleConditions>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
                <ruleResult>
                    <name>socks5.onConnect.prepareServerFacingSocket</name>
                    <value>true</value>
                </ruleResult>
                <ruleResult>
                    <name>socks5.onConnect.serverFacingSocketSetting</name>
                    <socketSetting>
                        <name>SO_RCVBUF</name>
                        <value>256</value>
                    </socketSetting>
                </ruleResult>
                <ruleResult>
                    <name>socks5.onConnect.serverFacingSocketSetting</name>
                    <socketSetting>
                        <name>SO_SNDBUF</name>
                        <value>256</value>
                    </socketSetting>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allow the CONNECT command to connect to 'specialserver.com' and configure the server-facing socket for the CONNECT command -->
    </setting>
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions/>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allow anything else -->
    </setting>
    
```

#### 7. 13. 8. Configuring Relay Settings

To configure the relay settings, you will need any of the following rule results:

-   `socks5.onBind.relayBufferSize`: Specifies the buffer size in bytes for relaying the data (Value must be an integer between 1 (inclusive) and 2147483647  (inclusive))

-   `socks5.onBind.relayIdleTimeout`: Specifies the timeout in milliseconds on relaying no data (Value must be an integer between 1 (inclusive) and 2147483647 (inclusive))

-   `socks5.onCommand.relayBufferSize`: Specifies the buffer size in bytes for relaying the data (Value must be an integer between 1 (inclusive) and 2147483647  (inclusive))

-   `socks5.onCommand.relayIdleTimeout`: Specifies the timeout in milliseconds on relaying no data (Value must be an integer between 1 (inclusive) and 2147483647 (inclusive))

-   `socks5.onConnect.relayBufferSize`: Specifies the buffer size in bytes for relaying the data (Value must be an integer between 1 (inclusive) and 2147483647 (inclusive))

-   `socks5.onConnect.relayIdleTimeout`: Specifies the timeout in milliseconds on relaying no data (Value must be an integer between 1 (inclusive) and 2147483647
(inclusive))

-   `socks5.onUdpAssociate.relayBufferSize`: Specifies the buffer size in bytes for relaying the data (Value must be an integer between 1 (inclusive) and 2147483647 (inclusive))

-   `socks5.onUdpAssociate.relayIdleTimeout`: Specifies the timeout in milliseconds on relaying no data (Value must be an integer between 1 (inclusive) and 2147483647 (inclusive))

These rule results can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.command`
-   `socks5.desiredDestinationAddress`
-   `socks5.desiredDestinationPort`
-   `socks5.method`
-   `socks5.secondServerBoundAddress`
-   `socks5.secondServerBoundPort`
-   `socks5.serverBoundAddress`
-   `socks5.serverBoundPort`
-   `socks5.user`
-   `socksServerAddress`

Partial command line example:

```text
    
    "--setting=rule=socks5.command=CONNECT socks5.desiredDestinationAddress=intermittent-idling-server.com firewallAction=ALLOW socks5.onConnect.relayIdleTimeout=1024000" \
    --setting=rule=firewallAction=ALLOW
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions>
                <ruleCondition>
                    <name>socks5.command</name>
                    <value>CONNECT</value>
                </ruleCondition>            
                <ruleCondition>
                    <name>socks5.desiredDestinationAddress</name>
                    <value>intermittent-idling-server.com</value>
                </ruleCondition>
            </ruleConditions>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
                <ruleResult>
                    <name>socks5.onConnect.relayIdleTimeout</name>
                    <value>1024000</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allow the CONNECT command to connect to 'intermittent-idling-server.com' with a relay idle timeout of 1024000 milliseconds (1024 seconds) -->
    </setting>
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions/>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allow anything else -->
    </setting>    
    
```

#### 7. 13. 9. Limiting Relay Bandwidth

To limit the relay bandwidth, you will need any of the following rule results:

-   `socks5.onBind.relayInboundBandwidthLimit`: Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed
-   `socks5.onBind.relayOutboundBandwidthLimit`: Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed
-   `socks5.onCommand.relayInboundBandwidthLimit`: Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed
-   `socks5.onCommand.relayOutboundBandwidthLimit`: Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed
-   `socks5.onConnect.relayInboundBandwidthLimit`: Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed
-   `socks5.onConnect.relayOutboundBandwidthLimit`: Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed
-   `socks5.onUdpAssociate.relayInboundBandwidthLimit`: Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed
-   `socks5.onUdpAssociate.relayOutboundBandwidthLimit`: Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

The value given to any of the rule results must be an integer between 1 (inclusive) and 2147483647 (inclusive)

These rule results can be used with the following rule conditions:

-   `clientAddress`
-   `socks5.command`
-   `socks5.desiredDestinationAddress`
-   `socks5.desiredDestinationPort`
-   `socks5.method`
-   `socks5.secondServerBoundAddress`
-   `socks5.secondServerBoundPort`
-   `socks5.serverBoundAddress`
-   `socks5.serverBoundPort`
-   `socks5.user`
-   `socksServerAddress`

Partial command line example:

```text
    
    "--setting=rule=socks5.command=CONNECT socks5.desiredDestinationAddress=streamingwebsite.com firewallAction=ALLOW socks5.onConnect.relayInboundBandwidthLimit=1024000 socks5.onConnect.relayOutboundBandwidthLimit=1024000" \
    --setting=rule=firewallAction=ALLOW
    
```

Partial configuration file example:

```xml
    
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions>
                <ruleCondition>
                    <name>socks5.command</name>
                    <value>CONNECT</value>
                </ruleCondition>            
                <ruleCondition>
                    <name>socks5.desiredDestinationAddress</name>
                    <value>streamingwebsite.com</value>
                </ruleCondition>
            </ruleConditions>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
                <ruleResult>
                    <name>socks5.onConnect.relayInboundBandwidthLimit</name>
                    <value>1024000</value>
                </ruleResult>
                <ruleResult>
                    <name>socks5.onConnect.relayOutboundBandwidthLimit</name>
                    <value>1024000</value>
                </ruleResult>                
            </ruleResults>
        </rule>
        <!-- Allow the CONNECT command to connect to 'streamingwebsite.com' with an upper limit on the relay inbound and outbound bandwidth of 1024000 bytes per second -->
    </setting>
    <setting>
        <name>rule</name>
        <rule>
            <ruleConditions/>
            <ruleResults>
                <ruleResult>
                    <name>firewallAction</name>
                    <value>ALLOW</value>
                </ruleResult>
            </ruleResults>
        </rule>
        <!-- Allow anything else -->
    </setting>    
    
```

### 7. 14. Common Value Syntaxes

The following are some common value syntaxes.

#### 7. 14. 1. Address Range

An address range can be specified in the following formats:

-   `ADDRESS` : Range is limited to a single address expressed in `ADDRESS`. Address can be an IPv4 address, an IPv6 address, or a domain name.
-   `ADDRESS1-ADDRESS2` : Range is limited to addresses between the address expressed in `ADDRESS1` (inclusive) and the address expressed in `ADDRESS2` (inclusive). `ADDRESS1` and `ADDRESS2` must be of the same address type (IPv4 or IPv6). `ADDRESS1` and `ADDRESS2` cannot be domain names.
-   `regex:REGULAR_EXPRESSION` : Range is limited to domain names that match the regular expression expressed in `REGULAR_EXPRESSION`

#### 7. 14. 2. Port Range

A port range can be specified in the following formats:

-   `PORT` : Range is limited to a single port number expressed in `PORT`
-   `PORT1-PORT2` : Range is limited to port numbers between the port number expressed in `PORT1` (inclusive) and the port number expressed in `PORT2` (inclusive)

### 7. 15. Miscellaneous Notes

The following are miscellaneous notes regarding Jargyle.

#### 7. 15. 1. Multiple Settings of the Same Name

Unless otherwise stated, if a setting of the same name appears more than once on the command line or in the configuration file, then only the last setting of the same name is recognized. 

#### 7. 15. 2. The SOCKS5 RESOLVE Command

The SOCKS5 RESOLVE command specifies the type of SOCKS5 request sent by the client for the server to perform: to resolve the provided host name and reply with the resolved IPv4 or IPv6 address. At the time of this writing, the SOCKS5 RESOLVE command is an additional SOCKS5 command made for Jargyle. It is not a part of the SOCKS5 protocol specification. 

The following is the specification for defining a SOCKS5 request with the RESOLVE command and the reply to that SOCKS5 request. It is described in expressions, names, and terms that are based off of the SOCKS5 protocol specification described in RFC [1928](https://datatracker.ietf.org/doc/html/rfc1928).

In a SOCKS request, the RESOLVE command is represented as `X'04'` in the `CMD` field.  In the SOCKS request, the `ATYP` field SHOULD be `X'03'` (DOMAINNAME) and the `DST.ADDR` field SHOULD be a fully-qualified domain name with the first octet containing the number of octets of the name that follows. The `DST.PORT` field in the SOCKS request can be of any value in network octet order (`X'0000'` to `X'FFFF'` inclusive).

In reply to a SOCKS request with the RESOLVE command, the `ATYP` field in the reply MUST be of any value other than `X'03'` (DOMAINNAME) and the `BND.ADDR` field in the reply MUST be the resolved address of the `DST.ADDR` field of the SOCKS request. The `BND.PORT` field in the reply can be of any value in network octet order (`X'0000'` to `X'FFFF'` inclusive). If the `ATYP` field and the `DST.ADDR` field of the SOCKS request is not a fully-qualified domain name, the `ATYP` field and the `BND.ADDR` field in the reply MUST be the same as the `ATYP` field and the `DST.ADDR` field of the SOCKS request. After the reply is sent, the connection between the client and the server is then closed.

#### 7. 15. 3. The Doc XML Element

When using an existing configuration file to create a new configuration file, any XML comments from the existing configuration file cannot be transferred to the new configuration file. To preserve XML comments from one configuration file to the next configuration file, the `<doc/>` XML element can be used in the following XML elements:

-   `<setting/>`
-   `<socketSetting/>`

Configuration file example:

```xml
    
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings>
            <setting>
                <name>backlog</name>
                <value>100</value>
                <doc>Allow for 100 backlogged incoming client connections</doc>
            </setting>
            <setting>
                <name>socksServerSocketSettings</name>
                <socketSettings>
                    <socketSetting>
                        <name>SO_TIMEOUT</name>
                        <value>0</value>
                        <doc>No timeout in waiting for a connection from a client</doc>
                    </socketSetting>
                </socketSettings>
            </setting>
        </settings>
    </configuration>
    
```
