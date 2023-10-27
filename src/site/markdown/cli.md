# Command Line Interface

## Page Contents

-   [Environment Variables](cli.html#Environment_Variables)
-   [Usage](cli.html#Usage)
    -   [manage-socks5-users Usage](cli.html#manage-socks5-users_Usage)
    -   [new-server-config-file Usage](cli.html#new-server-config-file_Usage)
    -   [start-server Usage](cli.html#start-server_Usage)
    -   [Settings Help](cli.html#Settings_Help)
-   [Managing SOCKS5 Users](cli.html#Managing_SOCKS5_Users)
    -   [Adding SOCKS5 Users](cli.html#Adding_SOCKS5_Users)
    -   [List All SOCKS5 Users](cli.html#List_All_SOCKS5_Users)
    -   [Removing a SOCKS5 User](cli.html#Removing_a_SOCKS5_User)
-   [Creating a Server Configuration File](cli.html#Creating_a_Server_Configuration_File)
    -   [Creating a Server Configuration File Supplemented With Command Line Options](cli.html#Creating_a_Server_Configuration_File_Supplemented_With_Command_Line_Options)
    -   [Creating a Server Configuration File Combined From Server Configuration Files](cli.html#Creating_a_Server_Configuration_File_Combined_From_Server_Configuration_Files)
    -   [The Doc XML Element](cli.html#The_Doc_XML_Element)
-   [Starting the Server](cli.html#Starting_the_Server)
    -   [Starting the Server With a Monitored Server Configuration File](cli.html#Starting_the_Server_With_a_Monitored_Server_Configuration_File)

## Environment Variables

The following are required environment variables Jargyle uses:

`JAVA_HOME`: Java&#8482; home directory

The following are optional environment variables Jargyle uses:

`JARGYLE_HOME`: Jargyle home directory

`JARGYLE_OPTS`: Command line options used to start up the JVM running 
Jargyle and can be used to supply additional options to it. For example, JVM 
memory settings could be defined with the value `-Xms256m -Xmx512m`

## Usage

The following is the command line help for Jargyle (displayed when using the 
command line option `--help`):

```
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

### manage-socks5-users Usage

The following is the command line help for the command `manage-socks5-users` 
(displayed when using the command `manage-socks5-users --help`):

```
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

### new-server-config-file Usage

The following is the command line help for the command 
`new-server-config-file` (displayed when using the command 
`new-server-config-file --help`):

```
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

### start-server Usage

The following is the command line help for the command `start-server` 
(displayed when using the command `start-server --help`):

```
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

### Settings Help

The following is a list of available settings for the SOCKS server (displayed 
when using either the commands `new-server-config-file --settings-help` 
or `start-server --settings-help`):

```
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

## Managing SOCKS5 Users

To manage SOCKS5 users, you would run the following command:

```
./bin/jargyle manage-socks5-users USER_REPOSITORY_CLASS_NAME:INITIALIZATION_STRING COMMAND
```

Where `USER_REPOSITORY_CLASS_NAME` is the name of the class that extends 
`com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository`,
 `INITIALIZATION_STRING` is the initialization string value, and `COMMAND` 
 is the name of the command described in the 
[command line help](cli.html#manage-socks5-users_Usage) for the command 
`manage-socks5-users`.

The following is one provided class you can use for 
`USER_REPOSITORY_CLASS_NAME`:

-   `com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.CsvFileSourceUserRepository`: 
This class handles the storage of the SOCKS5 users from a CSV file whose name is 
provided as an initialization string value. The SOCKS5 users from the CSV file 
are loaded onto memory. Because of this, you will need at least as much memory 
as the size of the CSV file. If the CSV file does not exist, it will be created 
and used. If the CSV file does exist, the existing CSV file will be used. 

### Adding SOCKS5 Users

To add SOCKS5 users to a user repository, you would run the following command:

```
./bin/jargyle manage-socks5-users USER_REPOSITORY_CLASS_NAME:INITIALIZATION_STRING add
```

Where `USER_REPOSITORY_CLASS_NAME` is the name of the class that extends 
`com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository` 
and `INITIALIZATION_STRING` is the initialization string value.

Once you have run the command, an interactive prompt will ask you for the new 
SOCKS5 user's name, password, and re-typed password. It will repeat the 
process to add another SOCKS5 user if you want to continue to enter another 
SOCKS5 user. If you do not want to enter any more SOCKS5 users, the new SOCKS5 
users will be saved.

Command line example:

```
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

### List All SOCKS5 Users

To list all SOCKS5 users from a user repository, you would run the following 
command:

```
./bin/jargyle manage-socks5-users USER_REPOSITORY_CLASS_NAME:INITIALIZATION_STRING list
```

Where `USER_REPOSITORY_CLASS_NAME` is the name of the class that extends 
`com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository` 
and `INITIALIZATION_STRING` is the initialization string value.

Once you have run the command, it will list all the SOCKS5 users from the user 
repository.

Command line example:

```
./bin/jargyle manage-socks5-users com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.CsvFileSourceUserRepository:users.csv list
Aladdin
Jasmine
Abu
Jafar
```

### Removing a SOCKS5 User

To remove a SOCKS5 user from a user repository, you would run the following 
command:

```
./bin/jargyle manage-socks5-users USER_REPOSITORY_CLASS_NAME:INITIALIZATION_STRING remove NAME
```

Where `USER_REPOSITORY_CLASS_NAME` is the name of the class that extends 
`com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.UserRepository`,
 `INITIALIZATION_STRING` is the initialization string value, and `NAME` 
 is the specified name of the SOCKS5 user to be removed from the user repository.

Once you have run the command, the SOCKS5 user of the specified name will be 
removed from the user repository.

Command line example:

```
./bin/jargyle manage-socks5-users com.github.jh3nd3rs0n.jargyle.server.socks5.userpassauth.userrepo.impl.CsvFileSourceUserRepository:users.csv remove Jafar
User 'Jafar' removed
```

## Creating a Server Configuration File

To create a server configuration file, you would run the following command:

```
./bin/jargyle new-server-config-file [OPTIONS] FILE
```

Where `[OPTIONS]` are optional command line options described in the 
[command line help](cli.html#new-server-config-file_Usage) for the command 
`new-server-config-file` and `FILE` is the new server configuration file.

As an example, the following command creates an empty configuration file:

```
./bin/jargyle new-server-config-file empty_configuration.xml
```

`empty_configuration.xml`:

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <settings/>
</configuration>
```

As an example, the following command creates a server configuration file with 
the port number, the number of allowed backlogged incoming client connections, 
and no SOCKS5 authentication required:

```
./bin/jargyle new-server-config-file \
    --setting=port=1234 \
    --setting=backlog=100 \
    --setting=socks5.methods=NO_AUTHENTICATION_REQUIRED \
    configuration.xml
```

`configuration.xml`:

```
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

### Creating a Server Configuration File Supplemented With Command Line Options

You can supplement an existing server configuration file with command line 
options.

As an example, the following command adds one command line option after the 
earlier server configuration file:

```
./bin/jargyle new-server-config-file \
    --config-file=configuration.xml \
    --setting=socksServerSocketSettings=SO_TIMEOUT=0 \
    supplemented_configuration.xml
```

`supplemented_configuration.xml`:

```
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

### Creating a Server Configuration File Combined From Server Configuration Files

You can combine multiple server configuration files into one server 
configuration file.

As an example, the following command combines the two earlier server 
configuration files into one:

```
./bin/jargyle new-server-config-file \
    --config-file=configuration.xml \
    --config-file=supplemented_configuration.xml \
    combined_configuration.xml
```

`combined_configuration.xml`:

```
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

Although the redundant settings in the combined server configuration file are 
unnecessary, the result server configuration file is for demonstration purposes 
only.

### The Doc XML Element

When using an existing server configuration file to create a new server 
configuration file, any XML comments from the existing server configuration 
file cannot be transferred to the new server configuration file. To preserve 
XML comments from one server configuration file to the next server 
configuration file, the `<doc/>` XML element can be used in the following XML 
elements:

-   `<setting/>`
-   `<socketSetting/>`

Server configuration file example:

```
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

## Starting the Server

To start the server without any command line arguments, you would run the 
following command:

```
./bin/jargyle start-server
```

The aforementioned command will start the server on port 1080 at address 
0.0.0.0.

Supplemental command line options including multiple server configuration files 
provided by the command line options `--config-file` can be included.

### Starting the Server With a Monitored Server Configuration File

You can start the server with a server configuration file to be monitored for 
any changes to be applied to the running configuration.

To start the server with a monitored server configuration file, you would run 
the following command:

```
./bin/jargyle start-server [MONITORED_CONFIG_FILE]
```

Where `[MONITORED_CONFIG_FILE]` is the optional server configuration file 
to be monitored for any changes to be applied to the running configuration.

When a monitored server configuration file is provided, supplemental command line 
options including multiple server configuration files provided by the command 
line options `--config-file` will be ignored.

The following are the settings in the monitored server configuration file that 
will have no effect if changed during the running configuration:

-   `backlog`
-   `port`
-   `socksServerBindHost`
-   `socksServerBindPortRanges`
-   `socksServerSocketSettings`

A restart of the server would be required if you want any of the changed 
aforementioned settings to be applied to the running configuration.