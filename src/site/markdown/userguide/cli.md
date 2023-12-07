# Command Line Interface

The following are topics for using the command line interface.

## Page Contents

-   [Environment Variables](#environment-variables)
-   [Usage](#usage)
    -   [manage-socks5-users Usage](#manage-socks5-users-usage)
    -   [new-server-config-file Usage](#new-server-config-file-usage)
    -   [start-server Usage](#start-server-usage)
    -   [Settings Help](#settings-help)
-   [Managing SOCKS5 Users](#managing-socks5-users)
    -   [Adding SOCKS5 Users](#adding-socks5-users)
    -   [List All SOCKS5 Users](#list-all-socks5-users)
    -   [Removing a SOCKS5 User](#removing-a-socks5-user)
-   [Creating a Server Configuration File](#creating-a-server-configuration-file)
    -   [Creating a Server Configuration File Supplemented With Command Line Options](#creating-a-server-configuration-file-supplemented-with-command-line-options)
    -   [Creating a Server Configuration File Combined From Server Configuration Files](#creating-a-server-configuration-file-combined-from-server-configuration-files)
    -   [The Doc Setting and the Doc XML Element](#the-doc-setting-and-the-doc-xml-element)
-   [Starting the Server](#starting-the-server)
    -   [Starting the Server With a Monitored Server Configuration File](#starting-the-server-with-a-monitored-server-configuration-file)

## Environment Variables

The following are required environment variables Jargyle uses:

`JAVA_HOME`: Java home directory

The following are optional environment variables Jargyle uses:

`JARGYLE_HOME`: Jargyle home directory

`JARGYLE_OPTS`: Command line options used to start up the JVM running 
Jargyle and can be used to supply additional options to it. For example, JVM 
memory settings could be defined with the value `-Xms256m -Xmx512m`

## Usage

The following is the command line help for Jargyle (displayed when using the 
command line option `--help`):

```text
Usage: jargyle COMMAND
       jargyle --help
       jargyle --version

COMMANDS:
  manage-socks5-users USER_REPOSITORY COMMAND
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

### manage-socks5-users Usage

The following is the command line help for the command `manage-socks5-users` 
(displayed when using the command `manage-socks5-users --help`):

```text
Usage: jargyle manage-socks5-users USER_REPOSITORY COMMAND
       jargyle manage-socks5-users --help

USER_REPOSITORIES:

  FileSourceUserRepository:FILE
      User repository that handles the storage of the users from a provided file of a list of URL encoded username and hashed password pairs (If the file does not exist, it will be created and used.)

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

### new-server-config-file Usage

The following is the command line help for the command 
`new-server-config-file` (displayed when using the command 
`new-server-config-file --help`):

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
  --enter-chaining-socks5-userpassmethod-pass
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

### start-server Usage

The following is the command line help for the command `start-server` 
(displayed when using the command `start-server --help`):

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
  --enter-chaining-socks5-userpassmethod-pass
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

### Settings Help

The following is a list of available settings for the SOCKS server (displayed 
when using either the commands `new-server-config-file --settings-help` 
or `start-server --settings-help`):

```text
SETTINGS:

  GENERAL SETTINGS:

    backlog=NONNEGATIVE_INTEGER
        The maximum length of the queue of incoming client connections to the SOCKS server (default is 50)

    bindHost=HOST
        The default binding host name or address for all sockets (default is 0.0.0.0)

    bindTcpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all TCP sockets (default is 0)

    bindUdpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all UDP sockets (default is 0)

    clientSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the client socket

    doc=TEXT
        A documentation setting

    externalFacingBindHost=HOST
        The default binding host name or address for all external-facing sockets

    externalFacingBindTcpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all external-facing TCP sockets

    externalFacingBindUdpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all external-facing UDP sockets

    externalFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of default socket settings for all external-facing sockets

    internalFacingBindHost=HOST
        The default binding host name or address for all internal-facing sockets

    internalFacingBindTcpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all internal-facing TCP sockets

    internalFacingBindUdpPortRanges=PORT_RANGES
        The comma separated list of default binding port ranges for all internal-facing UDP sockets

    internalFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of default socket settings for all internal-facing sockets

    lastRouteId=ROUTE_ID
        The ID for the last and unassigned route (default is lastRoute)

    port=PORT
        The port for the SOCKS server

    routeSelectionLogAction=LOG_ACTION
        The logging action to take if a route is selected

    routeSelectionStrategy=SELECTION_STRATEGY
        The selection strategy for the next route (default is CYCLICAL)

    rule=RULE
        A rule for the SOCKS server (default is firewallAction=ALLOW)

    socketSettings=SOCKET_SETTINGS
        The comma separated list of default socket settings for all sockets

    socksServerBindHost=HOST
        The binding host name or address for the SOCKS server socket

    socksServerBindPortRangesPORT_RANGES
        The comma separated list of binding port ranges for the SOCKS server socket

    socksServerSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the SOCKS server socket

  CHAINING GENERAL SETTINGS:

    chaining.clientBindHost=HOST
        The binding host name or address for the client socket that is used to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 0.0.0.0)

    chaining.clientBindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the client socket that is used to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 0)

    chaining.clientConnectTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on waiting for the client socket to connect to the other SOCKS server (used for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) (default is 60000)

    chaining.clientSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the client socket that is used to connect to the other SOCKS server (used for the SOCKS5 command RESOLVE and UDP ASSOCIATE)

    chaining.routeId=ROUTE_ID
        The ID for a route through a chain of other SOCKS servers. This setting also marks the current other SOCKS server as the last SOCKS server in the chain of other SOCKS servers

    chaining.socksServerUri=SOCKS_SERVER_URI
        The URI of the other SOCKS server

  CHAINING DTLS SETTINGS:

    chaining.dtls.enabled=true|false
        The boolean value to indicate if DTLS connections to the other SOCKS server are enabled (default is false)

    chaining.dtls.enabledCipherSuites=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable cipher suites enabled for DTLS connections to the other SOCKS server

    chaining.dtls.enabledProtocols=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable protocol versions enabled for DTLS connections to the other SOCKS server

    chaining.dtls.keyStoreFile=FILE
        The key store file for the DTLS connections to the other SOCKS server

    chaining.dtls.keyStorePassword=PASSWORD
        The password for the key store for the DTLS connections to the other SOCKS server

    chaining.dtls.keyStoreType=TYPE
        The type of key store file for the DTLS connections to the other SOCKS server (default is PKCS12)

    chaining.dtls.maxPacketSize=POSITIVE_INTEGER
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

    chaining.socks5.gssapimethod.mechanismOid=OID
        The object ID for the GSS-API authentication mechanism to the other SOCKS5 server (default is 1.2.840.113554.1.2.2)

    chaining.socks5.gssapimethod.necReferenceImpl=true|false
        The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the other SOCKS5 server use the NEC reference implementation (default is false)

    chaining.socks5.gssapimethod.protectionLevels=SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS
        The comma separated list of acceptable protection levels after GSS-API authentication with the other SOCKS5 server (The first is preferred. The remaining are acceptable if the server does not accept the first.) (default is REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE)

    chaining.socks5.gssapimethod.serviceName=SERVICE_NAME
        The GSS-API service name for the other SOCKS5 server

    chaining.socks5.methods=SOCKS5_METHODS
        The comma separated list of acceptable authentication methods to the other SOCKS5 server (default is NO_AUTHENTICATION_REQUIRED)

    chaining.socks5.useResolveCommand=true|false
        The boolean value to indicate that the RESOLVE command is to be used on the other SOCKS5 server for resolving host names (default is false)

    chaining.socks5.userpassmethod.password=PASSWORD
        The password to be used to access the other SOCKS5 server

    chaining.socks5.userpassmethod.username=USERNAME
        The username to be used to access the other SOCKS5 server

  CHAINING SSL/TLS SETTINGS:

    chaining.ssl.enabled=true|false
        The boolean value to indicate if SSL/TLS connections to the other SOCKS server are enabled (default is false)

    chaining.ssl.enabledCipherSuites=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the other SOCKS server

    chaining.ssl.enabledProtocols=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the other SOCKS server

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

    dtls.enabledCipherSuites=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable cipher suites enabled for DTLS connections to the SOCKS server

    dtls.enabledProtocols=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable protocol versions enabled for DTLS connections to the SOCKS server

    dtls.keyStoreFile=FILE
        The key store file for the DTLS connections to the SOCKS server

    dtls.keyStorePassword=PASSWORD
        The password for the key store for the DTLS connections to the SOCKS server

    dtls.keyStoreType=TYPE
        The type of key store file for the DTLS connections to the SOCKS server (default is PKCS12)

    dtls.maxPacketSize=POSITIVE_INTEGER
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

    socks5.gssapimethod.necReferenceImpl=true|false
        The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation (default is false)

    socks5.gssapimethod.protectionLevels=SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS
        The comma separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.) (default is REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE)

    socks5.methods=SOCKS5_METHODS
        The comma separated list of acceptable authentication methods in order of preference (default is NO_AUTHENTICATION_REQUIRED)

    socks5.onBind.inboundSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the inbound socket

    socks5.onBind.listenBindHost=HOST
        The binding host name or address for the listen socket if the provided host address is all zeros

    socks5.onBind.listenBindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the listen socket if the provided port is zero

    socks5.onBind.listenSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the listen socket

    socks5.onBind.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data

    socks5.onBind.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data

    socks5.onBind.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks5.onBind.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks5.onCommand.bindHost=HOST
        The binding host name or address for all sockets

    socks5.onCommand.bindTcpPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for all TCP sockets

    socks5.onCommand.bindUdpPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for all UDP sockets

    socks5.onCommand.externalFacingBindHost=HOST
        The binding host name or address for all external-facing sockets

    socks5.onCommand.externalFacingBindTcpPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for all external-facing TCP sockets

    socks5.onCommand.externalFacingBindUdpPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for all external-facing UDP sockets

    socks5.onCommand.externalFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for all external-facing sockets

    socks5.onCommand.internalFacingBindHost=HOST
        The binding host name or address for all internal-facing sockets

    socks5.onCommand.internalFacingBindTcpPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for all internal-facing TCP sockets

    socks5.onCommand.internalFacingBindUdpPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for all internal-facing UDP sockets

    socks5.onCommand.internalFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for all internal-facing sockets

    socks5.onCommand.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data (default is 1024)

    socks5.onCommand.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data (default is 60000)

    socks5.onCommand.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks5.onCommand.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks5.onCommand.socketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for all sockets

    socks5.onConnect.prepareServerFacingSocket=true|false
        The boolean value to indicate if the server-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect) (default is false)

    socks5.onConnect.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data

    socks5.onConnect.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data

    socks5.onConnect.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks5.onConnect.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks5.onConnect.serverFacingBindHost=HOST
        The binding host name or address for the server-facing socket

    socks5.onConnect.serverFacingBindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the server-facing socket

    socks5.onConnect.serverFacingConnectTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on waiting for the server-facing socket to connect (default is 60000)

    socks5.onConnect.serverFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the server-facing socket

    socks5.onUdpAssociate.clientFacingBindHost=HOST
        The binding host name or address for the client-facing UDP socket

    socks5.onUdpAssociate.clientFacingBindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the client-facing UDP socket

    socks5.onUdpAssociate.clientFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the client-facing UDP socket

    socks5.onUdpAssociate.peerFacingBindHost=HOST
        The binding host name or address for the peer-facing UDP socket

    socks5.onUdpAssociate.peerFacingBindPortRanges=PORT_RANGES
        The comma separated list of binding port ranges for the peer-facing UDP socket

    socks5.onUdpAssociate.peerFacingSocketSettings=SOCKET_SETTINGS
        The comma separated list of socket settings for the peer-facing UDP socket

    socks5.onUdpAssociate.relayBufferSize=POSITIVE_INTEGER
        The buffer size in bytes for relaying the data

    socks5.onUdpAssociate.relayIdleTimeout=POSITIVE_INTEGER
        The timeout in milliseconds on relaying no data

    socks5.onUdpAssociate.relayInboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

    socks5.onUdpAssociate.relayOutboundBandwidthLimit=POSITIVE_INTEGER
        The upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

    socks5.userpassmethod.userRepository=SOCKS5_USERPASSMETHOD_USER_REPOSITORY
        The user repository used for username password authentication (default is StringSourceUserRepository:)

  SSL/TLS SETTINGS:

    ssl.enabled=true|false
        The boolean value to indicate if SSL/TLS connections to the SOCKS server are enabled (default is false)

    ssl.enabledCipherSuites=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable cipher suites enabled for SSL/TLS connections to the SOCKS server

    ssl.enabledProtocols=COMMA_SEPARATED_VALUES
        The comma separated list of acceptable protocol versions enabled for SSL/TLS connections to the SOCKS server

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

  ADDRESS_RANGE: ADDRESS|IP_ADDRESS1-IP_ADDRESS2|regex:REGULAR_EXPRESSION

  COMMA_SEPARATED_VALUES: [VALUE1[,VALUE2[...]]]

  DIGIT: 0-9

  FIREWALL_ACTION: ALLOW|DENY

    ALLOW

    DENY

  HOST: HOST_NAME|HOST_ADDRESS

  LOG_ACTION: LOG_AS_WARNING|LOG_AS_INFO

    LOG_AS_WARNING
        Log message as a warning message

    LOG_AS_INFO
        Log message as an informational message

  NONNEGATIVE_INTEGER: 0-2147483647

  PERFORMANCE_PREFERENCES: DIGITDIGITDIGIT

  PORT: 0-65535

  PORT_RANGE: PORT|PORT1-PORT2

  PORT_RANGES: [PORT_RANGE1[,PORT_RANGE2[...]]]

  POSITIVE_INTEGER: 1-2147483647

  RULE: [RULE_CONDITION1,[RULE_CONDITION2,[...]]]RULE_RESULT1[,RULE_RESULT2[...]]

  RULE_CONDITION: NAME=VALUE

    GENERAL RULE CONDITIONS:

        clientAddress=ADDRESS_RANGE
            Specifies the client address

        socksServerAddress=ADDRESS_RANGE
            Specifies the SOCKS server address the client connected to

    SOCKS5 RULE CONDITIONS:

        socks5.command=SOCKS5_COMMAND
            Specifies the SOCKS5 command

        socks5.desiredDestinationAddress=ADDRESS_RANGE
            Specifies the desired destination address

        socks5.desiredDestinationPort=PORT_RANGE
            Specifies the desired destination port

        socks5.method=SOCKS5_METHOD
            Specifies the negotiated SOCKS5 method

        socks5.secondServerBoundAddress=ADDRESS_RANGE
            Specifies the second server bound address

        socks5.secondServerBoundPort=PORT_RANGE
            Specifies the second server bound port

        socks5.serverBoundAddress=ADDRESS_RANGE
            Specifies the server bound address

        socks5.serverBoundPort=PORT_RANGE
            Specifies the server bound port

        socks5.udp.inbound.desiredDestinationAddress=ADDRESS_RANGE
            Specifies the UDP inbound desired destination address

        socks5.udp.inbound.desiredDestinationPort=PORT_RANGE
            Specifies the UDP inbound desired destination port

        socks5.udp.inbound.sourceAddress=ADDRESS_RANGE
            Specifies the UDP inbound source address

        socks5.udp.inbound.sourcePort=PORT_RANGE
            Specifies the UDP inbound source port

        socks5.udp.outbound.desiredDestinationAddress=ADDRESS_RANGE
            Specifies the UDP outbound desired destination address

        socks5.udp.outbound.desiredDestinationPort=PORT_RANGE
            Specifies the UDP outbound desired destination port

        socks5.udp.outbound.sourceAddress=ADDRESS_RANGE
            Specifies the UDP outbound source address

        socks5.udp.outbound.sourcePort=PORT_RANGE
            Specifies the UDP outbound source port

        socks5.user=USER
            Specifies the user if any after the negotiated SOCKS5 method

  RULE_RESULT: NAME=VALUE

    GENERAL RULE RESULTS:

        bindHost=HOST
            Specifies the binding host name or address for all sockets

        bindTcpPortRange=PORT_RANGE
            Specifies a binding port range for all TCP sockets (can be specified multiple times with each rule result specifying another port range)

        bindUdpPortRange=PORT_RANGE
            Specifies a binding port range for all UDP sockets (can be specified multiple times with each rule result specifying another port ranges)

        clientSocketSetting=SOCKET_SETTING
            Specifies a socket setting for the client socket (can be specified multiple times with each rule result specifying another socket setting)

        externalFacingBindHost=HOST
            Specifies the binding host name or address for all external-facing sockets

        externalFacingBindTcpPortRange=PORT_RANGE
            Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule result specifying another port range)

        externalFacingBindUdpPortRange=PORT_RANGE
            Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)

        externalFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule result specifying another socket setting)

        firewallAction=FIREWALL_ACTION
            Specifies the firewall action to take

        firewallActionAllowLimit=NONNEGATIVE_INTEGER
            Specifies the limit on the number of simultaneous instances of the rule's firewall action ALLOW

        firewallActionAllowLimitReachedLogAction=LOG_ACTION
            Specifies the logging action to take if the limit on the number of simultaneous instances of the rule's firewall action ALLOW has been reached

        firewallActionLogAction=LOG_ACTION
            Specifies the logging action to take if the firewall action is applied

        internalFacingBindHost=HOST
            Specifies the binding host name or address for all internal-facing sockets

        internalFacingBindTcpPortRange=PORT_RANGE
            Specifies a binding port range for all internal-facing TCP sockets (can be specified multiple times with each rule result specifying another port range)

        internalFacingBindUdpPortRange=PORT_RANGE
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

    SOCKS5 RULE RESULTS:

        socks5.desiredDestinationAddressRedirect=SOCKS5_ADDRESS
            Specifies the desired destination address redirect

        socks5.desiredDestinationPortRedirect=PORT
            Specifies the desired destination port redirect

        socks5.desiredDestinationRedirectLogAction=LOG_ACTION
            Specifies the logging action to take if the desired destination is redirected

        socks5.onBind.inboundSocketSetting=SOCKET_SETTING
            Specifies a socket setting for the inbound socket (can be specified multiple times with each rule result specifying another socket setting)

        socks5.onBind.listenBindHost=HOST
            Specifies the binding host name or address for the listen socket if the provided host address is all zeros

        socks5.onBind.listenBindPortRange=PORT_RANGE
            Specifies a binding port range for the listen socket if the provided port is zero (can be specified multiple times with each rule result specifying another port range)

        socks5.onBind.listenSocketSetting=SOCKET_SETTING
            Specifies a socket setting for the listen socket (can be specified multiple times with each rule result specifying another socket setting)

        socks5.onBind.relayBufferSize=POSITIVE_INTEGER
            Specifies the buffer size in bytes for relaying the data

        socks5.onBind.relayIdleTimeout=POSITIVE_INTEGER
            Specifies the timeout in milliseconds on relaying no data

        socks5.onBind.relayInboundBandwidthLimit=POSITIVE_INTEGER
            Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

        socks5.onBind.relayOutboundBandwidthLimit=POSITIVE_INTEGER
            Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

        socks5.onCommand.bindHost=HOST
            Specifies the binding host name or address for all sockets

        socks5.onCommand.bindTcpPortRange=PORT_RANGE
            Specifies a binding port range for all TCP sockets (can be specified multiple times with each rule result specifying another port range)

        socks5.onCommand.bindUdpPortRange=PORT_RANGE
            Specifies a binding port range for all UDP sockets (can be specified multiple times with each rule result specifying another port range)

        socks5.onCommand.externalFacingBindHost=HOST
            Specifies the binding host name or address for all external-facing sockets

        socks5.onCommand.externalFacingBindTcpPortRange=PORT_RANGE
            Specifies a binding port range for all external-facing TCP sockets (can be specified multiple times with each rule result specifying another port range)

        socks5.onCommand.externalFacingBindUdpPortRange=PORT_RANGE
            Specifies a binding port range for all external-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)

        socks5.onCommand.externalFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for all external-facing sockets (can be specified multiple times with each rule result specifying another socket setting)

        socks5.onCommand.internalFacingBindHost=HOST
            Specifies the binding host name or address for all internal-facing sockets

        socks5.onCommand.internalFacingBindTcpPortRange=PORT_RANGE
            Specifies a binding port range for all internal-facing TCP sockets (can be specified multiple times with each rule result specifying another port range)

        socks5.onCommand.internalFacingBindUdpPortRange=PORT_RANGE
            Specifies a binding port range for all internal-facing UDP sockets (can be specified multiple times with each rule result specifying another port range)

        socks5.onCommand.internalFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for all internal-facing sockets (can be specified multiple times with each rule result specifying another socket setting)

        socks5.onCommand.relayBufferSize=POSITIVE_INTEGER
            Specifies the buffer size in bytes for relaying the data

        socks5.onCommand.relayIdleTimeout=POSITIVE_INTEGER
            Specifies the timeout in milliseconds on relaying no data

        socks5.onCommand.relayInboundBandwidthLimit=POSITIVE_INTEGER
            Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

        socks5.onCommand.relayOutboundBandwidthLimit=POSITIVE_INTEGER
            Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

        socks5.onCommand.socketSetting=SOCKET_SETTING
            Specifies a socket setting for all sockets (can be specified multiple times with each rule result specifying another socket setting)

        socks5.onConnect.prepareServerFacingSocket=true|false
            Specifies the boolean value to indicate if the server-facing socket is to be prepared before connecting (involves applying the specified socket settings, resolving the target host name, and setting the specified timeout on waiting to connect)

        socks5.onConnect.relayBufferSize=POSITIVE_INTEGER
            Specifies the buffer size in bytes for relaying the data

        socks5.onConnect.relayIdleTimeout=POSITIVE_INTEGER
            Specifies the timeout in milliseconds on relaying no data

        socks5.onConnect.relayInboundBandwidthLimit=POSITIVE_INTEGER
            Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

        socks5.onConnect.relayOutboundBandwidthLimit=POSITIVE_INTEGER
            Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

        socks5.onConnect.serverFacingBindHost=HOST
            Specifies the binding host name or address for the server-facing socket

        socks5.onConnect.serverFacingBindPortRange=PORT_RANGE
            Specifies a binding port range for the server-facing socket (can be specified multiple times with each rule result specifying another port range)

        socks5.onConnect.serverFacingConnectTimeout=POSITIVE_INTEGER
            Specifies the timeout in milliseconds on waiting for the server-facing socket to connect

        socks5.onConnect.serverFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for the server-facing socket (can be specified multiple times with each rule result specifying another socket setting)

        socks5.onUdpAssociate.clientFacingBindHost=HOST
            Specifies the binding host name or address for the client-facing UDP socket

        socks5.onUdpAssociate.clientFacingBindPortRange=PORT_RANGE
            Specifies a binding port range for the client-facing UDP socket (can be specified multiple times with each rule result specifying another port range)

        socks5.onUdpAssociate.clientFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for the client-facing UDP socket (can be specified multiple times with each rule result specifying another socket setting)

        socks5.onUdpAssociate.peerFacingBindHost=HOST
            Specifies the binding host name or address for the peer-facing UDP socket

        socks5.onUdpAssociate.peerFacingBindPortRange=PORT_RANGE
            Specifies a binding port range for the peer-facing UDP socket (can be specified multiple times with each rule result specifying another port range)

        socks5.onUdpAssociate.peerFacingSocketSetting=SOCKET_SETTING
            Specifies a socket setting for the peer-facing UDP socket (can be specified multiple times with each rule result specifying another socket setting)

        socks5.onUdpAssociate.relayBufferSize=POSITIVE_INTEGER
            Specifies the buffer size in bytes for relaying the data

        socks5.onUdpAssociate.relayIdleTimeout=POSITIVE_INTEGER
            Specifies the timeout in milliseconds on relaying no data

        socks5.onUdpAssociate.relayInboundBandwidthLimit=POSITIVE_INTEGER
            Specifies the upper limit on bandwidth in bytes per second of receiving inbound data to be relayed

        socks5.onUdpAssociate.relayOutboundBandwidthLimit=POSITIVE_INTEGER
            Specifies the upper limit on bandwidth in bytes per second of receiving outbound data to be relayed

  SCHEME: socks5

    socks5
        SOCKS5 protocol version 5

  SELECTION_STRATEGY: CYCLICAL|RANDOM

    SELECTION STRATEGIES:

        CYCLICAL
            Select the next in the cycle

        RANDOM
            Select the next at random

  SOCKET_SETTING: NAME=VALUE

    STANDARD SOCKET SETTINGS:

        IP_TOS=UNSIGNED_BYTE
            The type-of-service or traffic class field in the IP header for a TCP or UDP socket

        PERF_PREFS=PERFORMANCE_PREFERENCES
            Performance preferences for a TCP socket described by three digits whose values indicate the relative importance of short connection time, low latency, and high bandwidth

        SO_BROADCAST=true|false
            Can send broadcast datagrams

        SO_KEEPALIVE=true|false
            Keeps a TCP socket alive when no data has been exchanged in either direction

        SO_LINGER=NONNEGATIVE_INTEGER
            Linger on closing the TCP socket in seconds

        SO_OOBINLINE=true|false
            Can receive TCP urgent data

        SO_RCVBUF=POSITIVE_INTEGER
            The receive buffer size

        SO_REUSEADDR=true|false
            Can reuse socket address and port

        SO_SNDBUF=POSITIVE_INTEGER
            The send buffer size

        SO_TIMEOUT=NONNEGATIVE_INTEGER
            The timeout in milliseconds on waiting for an idle socket

        TCP_NODELAY=true|false
            Disables Nagle's algorithm

  SOCKET_SETTINGS: [SOCKET_SETTING1[,SOCKET_SETTING2[...]]]

  SOCKS5_ADDRESS: DOMAINNAME|IPV4_ADDRESS|IPV6_ADDRESS

  SOCKS5_COMMAND: CONNECT|BIND|UDP_ASSOCIATE|RESOLVE

    CONNECT
        A request to the SOCKS server to connect to another server

    BIND
        A request to the SOCKS server to bind to another address and port in order to receive an inbound connection

    UDP_ASSOCIATE
        A request to the SOCKS server to associate a UDP socket for sending and receiving datagrams

    RESOLVE
        A request to the SOCKS server to resolve a host name

  SOCKS5_GSSAPIMETHOD_PROTECTION_LEVEL: NONE|REQUIRED_INTEG|REQUIRED_INTEG_AND_CONF

    NONE
        No protection

    REQUIRED_INTEG
        Required per-message integrity

    REQUIRED_INTEG_AND_CONF
        Required per-message integrity and confidentiality

  SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS: SOCKS5_GSSAPIMETHOD_PROTECTION_LEVEL1[,SOCKS5_GSSAPIMETHOD_PROTECTION_LEVEL2[...]]

  SOCKS5_METHOD: NO_AUTHENTICATION_REQUIRED|GSSAPI|USERNAME_PASSWORD

    NO_AUTHENTICATION_REQUIRED
        No authentication required

    GSSAPI
        GSS-API authentication

    USERNAME_PASSWORD
        Username password authentication

  SOCKS5_METHODS: [SOCKS5_METHOD1[,SOCKS5_METHOD2[...]]]

  SOCKS5_USERPASSMETHOD_USER_REPOSITORY: TYPE_NAME:INITIALIZATION_STRING

    SOCKS5 USERNAME PASSWORD METHOD USER REPOSITORIES:

        FileSourceUserRepository:FILE
            User repository that handles the storage of the users from a provided file of a list of URL encoded username and hashed password pairs (If the file does not exist, it will be created and used.)

        StringSourceUserRepository:[USERNAME1:PASSWORD1[,USERNAME2:PASSWORD2[...]]]
            User repository that handles the storage of the users from a provided string of a comma separated list of URL encoded username and password pairs

  SOCKS_SERVER_URI: SCHEME://HOST[:PORT]

  UNSIGNED_BYTE: 0-255

```

## Managing SOCKS5 Users

To manage SOCKS5 users, you would run the following command:

```text
jargyle manage-socks5-users USER_REPOSITORY COMMAND
```

Where `USER_REPOSITORY` is the user repository and `COMMAND` is the name of 
the command. Both are described in the 
[command line help](#manage-socks5-users-usage) for the command 
`manage-socks5-users`.

The following is one provided user repository you can use for 
`USER_REPOSITORY`:

-   `FileSourceUserRepository:FILE`: This user repository handles the 
storage of the SOCKS5 users from `FILE`: a provided file of a list of URL 
encoded username and hashed password pairs. The SOCKS5 users from the file are 
loaded onto memory. Because of this, you will need at least as much memory as 
the size of the file. If the file does not exist, it will be created and used. 
If the file does exist, the existing file will be used. 

### Adding SOCKS5 Users

To add SOCKS5 users to a user repository, you would run the following command:

```text
jargyle manage-socks5-users USER_REPOSITORY add
```

Where `USER_REPOSITORY` is the the user repository.

Once you have run the command, an interactive prompt will ask you for the new 
SOCKS5 user's name, password, and re-typed password. It will repeat the 
process to add another SOCKS5 user if you want to continue to enter another 
SOCKS5 user. If you do not want to enter any more SOCKS5 users, the new SOCKS5 
users will be saved.

Command line example:

```bash
jargyle manage-socks5-users FileSourceUserRepository:users add
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

### List All SOCKS5 Users

To list all SOCKS5 users from a user repository, you would run the following 
command:

```text
jargyle manage-socks5-users USER_REPOSITORY list
```

Where `USER_REPOSITORY` is the user repository.

Once you have run the command, it will list all the SOCKS5 users from the user 
repository.

Command line example:

```bash
jargyle manage-socks5-users FileSourceUserRepository:users list
Aladdin
Jasmine
Abu
Jafar
```

### Removing a SOCKS5 User

To remove a SOCKS5 user from a user repository, you would run the following 
command:

```text
jargyle manage-socks5-users USER_REPOSITORY remove NAME
```

Where `USER_REPOSITORY` is the user repository and `NAME` is the specified 
name of the SOCKS5 user to be removed from the user repository.

Once you have run the command, the SOCKS5 user of the specified name will be 
removed from the user repository.

Command line example:

```bash
jargyle manage-socks5-users FileSourceUserRepository:users remove Jafar
User 'Jafar' removed
```

## Creating a Server Configuration File

To create a server configuration file, you would run the following command:

```text
jargyle new-server-config-file [OPTIONS] FILE
```

Where `[OPTIONS]` are optional command line options described in the 
[command line help](#new-server-config-file-usage) for the command 
`new-server-config-file` and `FILE` is the new server configuration file.

As an example, the following command creates an empty server configuration file:

```bash
jargyle new-server-config-file empty_configuration.xml
```

`empty_configuration.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings/>
</configuration>
```

As an example, the following command creates a server configuration file with 
the port number and the number of allowed backlogged incoming client connections:

```bash
jargyle new-server-config-file \
    --setting=port=1234 \
    --setting=backlog=100 \
    general_configuration.xml
```

`general_configuration.xml`:

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
    </settings>
</configuration>
```

### Creating a Server Configuration File Supplemented With Command Line Options

You can supplement an existing server configuration file with command line 
options.

As an example, the following command creates another server configuration file 
by adding one command line option after an earlier server configuration file:

```bash
jargyle new-server-config-file \
    --config-file=general_configuration.xml \
    --setting=socksServerSocketSettings=SO_TIMEOUT=0 \
    supplemented_general_configuration.xml
```

`supplemented_general_configuration.xml`:

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

### Creating a Server Configuration File Combined From Server Configuration Files

You can combine multiple server configuration files into one server 
configuration file.

As an example, the following commands create another server configuration file 
and then combine an earlier server configuration file with the new server 
configuration file into one:

```bash
jargyle new-server-config-file \
    --setting=socks5.methods=NO_AUTHENTICATION_REQUIRED \
    socks5_configuration.xml
jargyle new-server-config-file \
    --config-file=supplemented_general_configuration.xml \
    --config-file=socks5_configuration.xml \
    combined_configuration.xml
```

`socks5_configuration.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
            </socks5.methods>
        </setting>
    </settings>
</configuration>
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
            <name>socksServerSocketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_TIMEOUT</name>
                    <value>0</value>
                </socketSetting>
            </socketSettings>
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

### The Doc Setting and the Doc XML Element

When using an existing server configuration file to create a new server 
configuration file, any XML comments from the existing server configuration 
file cannot be transferred to the new server configuration file. To preserve 
XML comments from one server configuration file to the next server 
configuration file, you can use either or both of the following: the setting 
`doc` and the `<doc/>` XML element.

The setting `doc` can be used for documentation purposes. It can be specified 
multiple times.

As an example, the following command creates a new server configuration file by 
combining earlier server configuration files each supplemented by command line 
options that document the start and end of a configuration:

```bash
jargyle new-server-config-file \
    "--setting=doc=Start of general settings" \
    --config-file=supplemented_general_configuration.xml \
    "--setting=doc=End of general settings" \
    "--setting=doc=Start of SOCKS5 settings" \
    --config-file=socks5_configuration.xml \
    "--setting=doc=End of SOCKS5 settings" \
    documented_combined_configuration.xml
```

`documented_combined_configuration.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>doc</name>
            <value>Start of general settings</value>
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
            <name>socksServerSocketSettings</name>
            <socketSettings>
                <socketSetting>
                    <name>SO_TIMEOUT</name>
                    <value>0</value>
                </socketSetting>
            </socketSettings>
        </setting>
        <setting>
            <name>doc</name>
            <value>End of general settings</value>
        </setting>
        <setting>
            <name>doc</name>
            <value>Start of SOCKS5 settings</value>
        </setting>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>doc</name>
            <value>End of SOCKS5 settings</value>
        </setting>
    </settings>
</configuration>
```

The `<doc/>` XML element can also be used for documentation purposes. It can be 
used in the following XML elements:

-   `<setting/>`
-   `<socketSetting/>`

The `<doc/>` XML element can only be added in the aforementioned XML elements 
by editing the server configuration file to include it.

`documented_combined_configuration.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings>
        <setting>
            <name>doc</name>
            <value>Start of general settings</value>
        </setting>
        <setting>
            <name>port</name>
            <value>1234</value>
        </setting>
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
        <setting>
            <name>doc</name>
            <value>End of general settings</value>
        </setting>
        <setting>
            <name>doc</name>
            <value>Start of SOCKS5 settings</value>
        </setting>
        <setting>
            <name>socks5.methods</name>
            <socks5.methods>
                <socks5.method>NO_AUTHENTICATION_REQUIRED</socks5.method>
            </socks5.methods>
        </setting>
        <setting>
            <name>doc</name>
            <value>End of SOCKS5 settings</value>
        </setting>
    </settings>
</configuration>
```

## Starting the Server

To start the server without any command line arguments, you would run the 
following command:

```bash
jargyle start-server
```

The aforementioned command will start the server on port 1080 at address 
0.0.0.0.

Supplemental command line options including multiple server configuration files 
provided by the command line options `--config-file` can be included.

As an example, the following command starts the server with earlier server 
configuration files:

```bash
jargyle start-server \
    --config-file=supplemented_general_configuration.xml \
    --config-file=socks5_configuration.xml
```

### Starting the Server With a Monitored Server Configuration File

You can start the server with a server configuration file to be monitored for 
any changes to be applied to the running configuration.

To start the server with a monitored server configuration file, you would run 
the following command:

```text
jargyle start-server [MONITORED_CONFIG_FILE]
```

Where `[MONITORED_CONFIG_FILE]` is the optional server configuration file 
to be monitored for any changes to be applied to the running configuration.

As an example, the following command starts the server with an earlier server 
configuration file as the monitored server configuration file:

```bash
jargyle start-server combined_configuration.xml
```

When a monitored server configuration file is provided, any supplemental command 
line options including multiple server configuration files provided by the 
command line options `--config-file` will be ignored.

The following are the settings in the monitored server configuration file that 
will have no effect if changed during the running configuration:

-   `backlog`
-   `port`
-   `socksServerBindHost`
-   `socksServerBindPortRanges`
-   `socksServerSocketSettings`

A restart of the server would be required if you want any of the changed 
aforementioned settings to be applied to the running configuration.