# Jargyle 

[![Build Status](https://travis-ci.com/jh3nd3rs0n/jargyle.svg?branch=master)](https://travis-ci.com/jh3nd3rs0n/jargyle) [![Total alerts](https://img.shields.io/lgtm/alerts/g/jh3nd3rs0n/jargyle.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/jargyle/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/jh3nd3rs0n/jargyle.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/jargyle/context:java)

Jargyle is a Java SOCKS5 server. It has the following features:

**It is compliant with the [SOCKS5 specification](https://tools.ietf.org/html/rfc1928)**

**It can have one or more of the following SOCKS5 authentication methods:**

- No authentication
  
- [Username password authentication](https://tools.ietf.org/html/rfc1929)
  
- [GSS-API authentication](https://tools.ietf.org/html/rfc1961)
  
**It can have its external connections be set through another SOCKS5 server**

**Disclaimer:**

Jargyle is a hobby project and is currently subject to breaking changes. Jargyle is currently not production ready but it aims to be.

## Contents

- [1. Requirements](#1-requirements)

- [2. Building](#2-building)

- [3. Running Jargyle](#3-running-jargyle)

- [3. 1. Usage](#3-1-usage)
  
- [3. 2. Creating a Configuration File](#3-2-creating-a-configuration-file)
  
- [3. 3. Supplementing a Configuration File with Command Line Options](#3-3-supplementing-a-configuration-file-with-command-line-options)
  
- [3. 4. Combining Configuration Files](#3-4-combining-configuration-files)
  
- [3. 5. Running Jargyle with a Configuration File](#3-5-running-jargyle-with-a-configuration-file)
  
- [3. 6. Managing SOCKS5 Users (for Username Password Authentication)](#3-6-managing-socks5-users-for-username-password-authentication)
  
- [3. 6. 1. Creating a Users File](#3-6-1-creating-a-users-file)
  	
- [3. 6. 2. Adding Users to an Existing Users File](#3-6-2-adding-users-to-an-existing-users-file)
  	
- [3. 6. 3. Removing a User from an Existing Users File](#3-6-3-removing-a-user-from-an-existing-users-file)
  	
- [3. 7. Using SOCKS5 Authentication](#3-7-using-socks5-authentication)
  
- [3. 7. 1. Using No Authentication](#3-7-1-using-no-authentication)
    
- [3. 7. 2. Using Username Password Authentication](#3-7-2-using-username-password-authentication)
    
- [3. 7. 3. Using GSS-API Authentication](#3-7-3-using-gss-api-authentication)

- [3. 8. With External Connections Set to Another SOCKS Server](#3-8-with-external-connections-set-to-another-socks-server)

- [3. 8. 1. Using SOCKS5 Authentication](#3-8-1-using-socks5-authentication)

- [3. 8. 1. 1. Using No Authentication](#3-8-1-1-using-no-authentication)

- [3. 8. 1. 2. Using Username Password Authentication](#3-8-1-2-using-username-password-authentication)

- [3. 8. 1. 3. Using GSS-API Authentication](#3-8-1-3-using-gss-api-authentication)

- [4. Integration Testing](#4-integration-testing)

- [5. TODO](#5-todo)

- [6. Contact](#6-contact)

## 1. Requirements

- Apache Maven&#8482; 3.3.9 or higher 

- Java&#8482; SDK 1.8 or higher

## 2. Building

To build and package Jargyle as an executable jar file, run the following commands:

```

    $ cd jargyle
    $ mvn package

```

## 3. Running Jargyle 

To run Jargyle, you can run the following command:

```

    $ mvn exec:java

```

If you have Jargyle packaged as an executable jar file, you can run the following command:

```

    $ java -jar target/jargyle-${VERSION}.jar

```

Be sure to replace `${VERSION}` with the actual version shown within the name of the executable jar file.

### 3. 1. Usage

The following is the command line help for Jargyle (displayed when using the command line option `--help`):

```

    Usage: jargyle.server.SocksServer [OPTIONS]
           jargyle.server.SocksServer --config-file-xsd
           jargyle.server.SocksServer --help
           jargyle.server.SocksServer [OPTIONS] --new-config-file=FILE
           jargyle.server.SocksServer --settings-help
           jargyle.server.SocksServer --socks5-users ARGS
    
    OPTIONS:
      --config-file=FILE, -f FILE
          The configuration file
      --config-file-xsd, -x
          Print the configuration file XSD and exit
      --enter-external-client-socks5-user-pass
          Enter through an interactive prompt the username password for the external SOCKS5 server for external connections
      --external-client-socks5-user-pass=USERNAME:PASSWORD
          The username password for the external SOCKS5 server for external connections
      --help, -h
          Print this help and exit
      --new-config-file=FILE, -n FILE
          Create a new configuration file based on the preceding options and exit
      --settings-help, -H
          Print the list of available settings for the SOCKS server and exit
      --settings=[NAME1=VALUE1[,NAME2=VALUE2[...]]], -s [NAME1=VALUE1[,NAME2=VALUE2[...]]]
          The comma-separated list of settings for the SOCKS server
      --socks5-user-pass-authenticator=CLASSNAME[:PARAMETER_STRING]
          The SOCKS5 username password authenticator for the SOCKS server
      --socks5-users
          Mode for managing SOCKS5 users (add --help for more information)
    
```

The following is a list of available settings for the SOCKS server (displayed when using the command line option `--settings-help`):

```

    SETTINGS:
    
      backlog=INTEGER_BETWEEN_0_AND_2147483647
          The maximum length of the queue of incoming connections (default is 50)
    
      clientSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the client socket
    
      externalClient.connectTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on waiting to TCP connect to the external SOCKS server for external connections (default is 60000)
    
      externalClient.externalServerUri=SCHEME://HOST[:PORT]
          The URI of the external SOCKS server for external connections.
    
      externalClient.socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings to TCP connect to the external SOCKS server for external connections
    
      externalClient.socks5.authMethods=SOCKS5_AUTH_METHOD1[ SOCKS5_AUTH_METHOD2[...]]
          The space separated list of acceptable authentication methods to the external SOCKS5 server for external connections (default is NO_AUTHENTICATION_REQUIRED)
    
      externalClient.socks5.gssapiMechanismOid=GSSAPI_MECHANISM_OID
          The object ID for the GSS-API authentication mechanism to the external SOCKS5 server for external connections (default is 1.2.840.113554.1.2.2)
    
      externalClient.socks5.gssapiNecReferenceImpl=true|false
          The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected should the external SOCKS5 server for external connections use the NEC reference implementation (default is false)
    
      externalClient.socks5.gssapiProtectionLevels=SOCKS5_GSSAPI_PROTECTION_LEVEL1[ SOCKS5_GSSAPI_PROTECTION_LEVEL2[...]]
          The space separated list of acceptable protection levels after GSS-API authentication with the external SOCKS5 server for external connections (The first is preferred. The remaining are acceptable if the server does not accept the first.) (default is REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)
    
      externalClient.socks5.gssapiServiceName=GSSAPI_SERVICE_NAME
          The GSS-API service name for the external SOCKS5 server for external connections
    
      port=INTEGER_BETWEEN_1_AND_65535
          The port for the SOCKS server (default is 1080)
    
      socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the SOCKS server
    
      socks5.authMethods=SOCKS5_AUTH_METHOD1[ SOCKS5_AUTH_METHOD2[...]]
          The space separated list of acceptable authentication methods in order of preference (default is NO_AUTHENTICATION_REQUIRED)
    
      socks5.gssapiNecReferenceImpl=true|false
          The boolean value to indicate if the exchange of the GSS-API protection level negotiation must be unprotected according to the NEC reference implementation (default is false)
    
      socks5.gssapiProtectionLevels=SOCKS5_GSSAPI_PROTECTION_LEVEL1[ SOCKS5_GSSAPI_PROTECTION_LEVEL2[...]]
          The space separated list of acceptable protection levels after GSS-API authentication (The first is preferred if the client does not provide a protection level that is acceptable.) (default is REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)
    
      socks5.onBind.incomingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the incoming socket
    
      socks5.onBind.listenPortRanges=PORT_RANGE1[ PORT_RANGE2[...]]
          The space separated list of acceptable port ranges for the listen socket (default is 1-65535)
    
      socks5.onBind.listenSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the listen socket
    
      socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
          The buffer size in bytes for relaying the data (default is 1024)
    
      socks5.onBind.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on relaying no data (default is 60000)
    
      socks5.onConnect.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
          The buffer size in bytes for relaying the data (default is 1024)
    
      socks5.onConnect.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on relaying no data (default is 60000)
    
      socks5.onConnect.serverConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on waiting the server-facing socket to connect (default is 60000)
    
      socks5.onConnect.serverSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the server-facing socket
    
      socks5.onUdpAssociate.clientPortRanges=PORT_RANGE1[ PORT_RANGE2[...]]
          The space separated list of acceptable port ranges for the client-facing UDP socket (default is 1-65535)
    
      socks5.onUdpAssociate.clientSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the client-facing UDP socket
    
      socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647
          The buffer size in bytes for relaying the data (default is 32768)
    
      socks5.onUdpAssociate.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647
          The timeout in milliseconds on relaying no data (default is 60000)
    
      socks5.onUdpAssociate.serverPortRanges=PORT_RANGE1[ PORT_RANGE2[...]]
          The space separated list of acceptable port ranges for the server-facing UDP socket (default is 1-65535)
    
      socks5.onUdpAssociate.serverSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]
          The space separated list of socket settings for the server-facing UDP socket
    
    SCHEMES:
    
      socks5
          SOCKS protocol version 5
    
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
    
    SOCKS5_AUTH_METHODS:
    
      NO_AUTHENTICATION_REQUIRED
          No authentication required
    
      GSSAPI
          GSS-API authentication
    
      USERNAME_PASSWORD
          Username password authentication
    
    SOCKS5_GSSAPI_PROTECTION_LEVELS:
    
      NONE
          No protection
    
      REQUIRED_INTEG
          Required per-message integrity
    
      REQUIRED_INTEG_AND_CONF
          Required per-message integrity and confidentiality
    
```

The following is the command line help for managing SOCKS5 users for username password authentication (displayed when using the command line options `--socks5-users --help`):

```

    Usage: jargyle.server.SocksServer --socks5-users COMMAND
           jargyle.server.SocksServer --socks5-users --help
           jargyle.server.SocksServer --socks5-users --xsd
    
    COMMANDS:
      add-users-to-file FILE
          Add users to an existing file through an interactive prompt
      create-new-file FILE
          Create a new file of zero or more users through an interactive prompt
      remove-user NAME FILE
          Remove user by name from an existing file
    
    OPTIONS:
      --help, -h
          Print this help and exit
      --xsd, -x
          Print the XSD and exit
    
```

### 3. 2. Creating a Configuration File

You can create a configuration file by using the command line option `--new-config-file`

The following command creates an empty configuration file:

```

    $ java -jar target/jargyle-${VERSION}.jar --new-config-file=configuration.xml

```

`configuration.xml`:

```xml

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration/>

```

Any preceding command line options that do not terminate before the command line option `--new-config-file` will be set in the new configuration file.

The following command creates a configuration file with the port number, the number of allowed backlogged connections, and no authentication required:

```

    $ java -jar target/jargyle-${VERSION}.jar --settings=port=1234,backlog=100,socks5.authMethods=NO_AUTHENTICATION_REQUIRED --new-config-file=configuration.xml

```

`configuration.xml`:

```xml

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings>
            <setting name="port" value="1234"/>
            <setting name="backlog" value="100"/>
            <setting name="socks5.authMethods" value="NO_AUTHENTICATION_REQUIRED"/>
        </settings>
    </configuration>

```
  
### 3. 3. Supplementing a Configuration File with Command Line Options

You can supplement an existing configuration file with command line options.

The following command adds one command line options before the existing configuration file and another command line option after the existing configuration file:

```

    $ java -jar target/jargyle-${VERSION}.jar --settings=clientSocketSettings=SO_TIMEOUT=500 --config-file=configuration.xml --settings=socketSettings=SO_TIMEOUT=0 --new-config-file=new_configuration.xml

```

`new_configuration.xml`:

```xml

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings>
            <setting name="clientSocketSettings" value="SO_TIMEOUT=500"/>
            <setting name="port" value="1234"/>
            <setting name="backlog" value="100"/>
            <setting name="socks5.authMethods" value="NO_AUTHENTICATION_REQUIRED"/>
            <setting name="socketSettings" value="SO_TIMEOUT=0"/>
        </settings>
    </configuration>

```

### 3. 4. Combining Configuration Files

You can combine multiple configuration files into one configuration file.

The following command combines the two earlier configuration files into one:

```

    $ java -jar target/jargyle-${VERSION}.jar --config-file=configuration.xml --config-file=new_configuration.xml --new-config-file=combined_configuration.xml

```

`combined_configuration.xml`:

```xml

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <configuration>
        <settings>
            <setting name="port" value="1234"/>
            <setting name="backlog" value="100"/>
            <setting name="socks5.authMethods" value="NO_AUTHENTICATION_REQUIRED"/>
            <setting name="clientSocketSettings" value="SO_TIMEOUT=500"/>
            <setting name="port" value="1234"/>
            <setting name="backlog" value="100"/>
            <setting name="socks5.authMethods" value="NO_AUTHENTICATION_REQUIRED"/>
            <setting name="socketSettings" value="SO_TIMEOUT=0"/>
        </settings>
    </configuration>

```

Although the redundant settings in the combined configuration file is unnecessary, the result configuration file is for demonstration purposes only.

Also, if a setting of the same name appears more than once in the configuration file, then only the last setting of the same name is recognized. 

### 3. 5. Running Jargyle with a Configuration File

To run Jargyle with a configuration file, you can use the command line option `--config-file`

```

    $ java -jar target/jargyle-${VERSION}.jar --config-file=configuration.xml

```

Also the configuration file can be supplemented with command line options and/or combined with multiple configuration files.

### 3. 6. Managing SOCKS5 Users (for Username Password Authentication)

You can manage SOCKS5 users stored in an XML file called a users file. A users file can be used for [username password authentication](#3-7-2-using-username-password-authentication).

#### 3. 6. 1. Creating a Users File

To create a users file, you would run the following command:

```

    $ java -jar target/jargyle-${VERSION}.jar --socks5-users create-new-file FILE

```

Where `FILE` would be the name for the new users file.

Once you have run the command, an interactive prompt will ask you if you want to enter a user.

```

    $ java -jar target/jargyle-${VERSION}.jar --socks5-users create-new-file users.xml
    Would you like to enter a user? ('Y' for yes): 

```

If you do not want to enter a user, a new empty users file will be created. 

```

    Would you like to enter a user? ('Y' for yes): n
    Writing to 'users.xml'...

```

`users.xml`:

```xml

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <users/>

```

If you want to enter a user, the prompt will ask you for the user's name, password, and re-typed password. It will repeat the process to add another user if you want to continue to enter another user. If you wish not to enter any more users, the new users file will be created.

```

    Would you like to enter a user? ('Y' for yes): Y
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
    Would you like to enter another user? ('Y' for yes): n
    Writing to 'users.xml'...

```

`users.xml`:

```xml

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <users>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=</hash>
                <salt>mGvQZmPl/q4=</salt>
            </hashedPassword>
            <name>Aladdin</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=</hash>
                <salt>K+aacLMX4TQ=</salt>
            </hashedPassword>
            <name>Jasmine</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=</hash>
                <salt>SaTI6PwS6WE=</salt>
            </hashedPassword>
            <name>Abu</name>
        </user>
    </users>


```

#### 3. 6. 2. Adding Users to an Existing Users File

To add users to an existing users file, you would run the following command:

```

    $ java -jar target/jargyle-${VERSION}.jar --socks5-users add-users-to-file FILE

```

Where `FILE` would be the name for the existing users file.

Once you have run the command, an interactive prompt will ask you for the new user's name, password, and re-typed password. It will repeat the process to add another user if you want to continue to enter another user. If you wish not to enter any more users, the updated users file will be saved.

```

    $ java -jar target/jargyle-${VERSION}.jar --socks5-users add-users-to-file users.xml
    User
    Name: Jafar
    Password: 
    Re-type password:
    User 'Jafar' added.
    Would you like to enter another user? ('Y' for yes): n
    Writing to 'users.xml'...

```

`users.xml`:

```xml

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <users>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=</hash>
                <salt>mGvQZmPl/q4=</salt>
            </hashedPassword>
            <name>Aladdin</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=</hash>
                <salt>K+aacLMX4TQ=</salt>
            </hashedPassword>
            <name>Jasmine</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=</hash>
                <salt>SaTI6PwS6WE=</salt>
            </hashedPassword>
            <name>Abu</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>Qaht9FcEqjEtwbBADurB5Swt5eKg6LNQ9Hl9FnUT4kw=</hash>
                <salt>jIBPXJxqlMk=</salt>
            </hashedPassword>
            <name>Jafar</name>
        </user>
    </users>

```

#### 3. 6. 3. Removing a User from an Existing Users File

To remove a user from an existing users file, you would run the following command:

```

    $ java -jar target/jargyle-${VERSION}.jar --socks5-users remove-user NAME FILE

```

Where `NAME` would be the name of the user and `FILE` would be the name for the existing users file.

Once you have run the command, the user of the specified name will be removed from the existing users file.

```

    $ java -jar target/jargyle-${VERSION}.jar --socks5-users remove-user Jafar users.xml
    User 'Jafar' removed
    Writing to 'users.xml'...

```

`users.xml`:

```xml

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <users>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>bTORKdLBo2nUSOSaQXi5tIKFvxeurm+Bzm6F/VwQERo=</hash>
                <salt>mGvQZmPl/q4=</salt>
            </hashedPassword>
            <name>Aladdin</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>c5/RXb2EC0eqVWP5kAIuS0d78Z7O3K49OfxcerMupuo=</hash>
                <salt>K+aacLMX4TQ=</salt>
            </hashedPassword>
            <name>Jasmine</name>
        </user>
        <user>
            <hashedPassword xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="pbkdf2WithHmacSha256HashedPassword">
                <hash>ycdKuXCehif76Kv4a5TC9tYun5DdibqTOjKmqNv7bJU=</hash>
                <salt>SaTI6PwS6WE=</salt>
            </hashedPassword>
            <name>Abu</name>
        </user>
    </users>

```

### 3. 7. Using SOCKS5 Authentication

Jargyle has the following SOCKS5 authentication methods to choose from:

- `NO_AUTHENTICATION_REQUIRED`: No authentication required

- `GSSAPI`: GSS-API authentication

- `USERNAME_PASSWORD`: Username password authentication

You can have one or more of the aforementioned authentication methods set in the setting `socks5.authMethods` as a space separated list.

Partial command line example:

```

    "--settings=socks5.authMethods=NO_AUTHENTICATION_REQUIRED GSSAPI"

```

Partial configuration file example:

```xml

    <setting name="socks5.authMethods" value="GSSAPI USERNAME_PASSWORD"/>

```

If not set, the default value for the setting `socks5.authMethods` is set to `NO_AUTHENTICATION_REQUIRED`

#### 3. 7. 1. Using No Authentication

Because the default value for the setting `socks5.authMethods` is set to `NO_AUTHENTICATION_REQUIRED`, it is not required for `NO_AUTHENTICATION_REQUIRED` to be included in the setting `socks5.authMethods`.

However, if other authentication methods are to be used in addition to `NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be included in the setting `socks5.authMethods`

Partial command line example:

```

    "--settings=socks5.authMethods=NO_AUTHENTICATION_REQUIRED GSS-API USERNAME_PASSWORD"

```

Partial configuration file example:

```xml

    <setting name="socks5.authMethods" value="NO_AUTHENTICATION_REQUIRED GSS-API USERNAME_PASSWORD"/>

```

#### 3. 7. 2. Using Username Password Authentication

To use username password authentication, you will need to have the setting `socks5.authMethods` to have `USERNAME_PASSWORD` included.

Partial command line example:

```

    --settings=socks5.authMethods=USERNAME_PASSWORD

```

Partial configuration file example:

```xml

    <setting name="socks5.authMethods" value="USERNAME_PASSWORD"/>

```

Also, you will need to specify the name of the class that extends `jargyle.server.socks5.UsernamePasswordAuthenticator` along with a parameter string

The following are two provided classes you can use:

- `jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator`
- `jargyle.server.socks5.XmlFileSourceUsernamePasswordAuthenticator`

`jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator`: This class authenticates the username and password based on the parameter string of a comma separated list of USERNAME:PASSWORD pairs

Partial command line example:

```

    --socks5-user-pass-authenticator=jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator:Aladdin:opensesame,Jasmine:mission%3Aimpossible

```

Partial configuration file example:

```xml

    <socks5UsernamePasswordAuthenticator>
	    <className>jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator</className>	
	    <parameterString>Aladdin:opensesame,Jasmine:mission%3Aimpossible</parameterString>
    </socks5UsernamePasswordAuthenticator>

```

If any of the usernames or any of the passwords contain a colon character (`:`), then each colon character must be replaced with the URL encoding character `%3A`.

Also, if any of the usernames or any of the passwords contain a percent sign character (`%`) not used for URL encoding, then each percent sign character not used for URL encoding must be replaced with the URL encoding character `%25`.


`jargyle.server.socks5.XmlFileSourceUsernamePasswordAuthenticator`: This class authenticates the username and password based on the [XML file of users](#3-6-managing-socks5-users-for-username-password-authentication) whose file name is provided as a parameter string

Partial command line example:

```

    --socks5-user-pass-authenticator=jargyle.server.socks5.XmlFileSourceUsernamePasswordAuthenticator:users.xml

```

Partial configuration file example:

```xml

    <socks5UsernamePasswordAuthenticator>
	    <className>jargyle.server.socks5.StringSourceUsernamePasswordAuthenticator</className>	
	    <parameterString>users.xml</parameterString>
    </socks5UsernamePasswordAuthenticator>

```

#### 3. 7. 3. Using GSS-API Authentication

To use GSS-API authentication, you will need to have the setting `socks5.authMethods` to have `GSSAPI` included.

Partial command line example:

```

    --settings=socks5.authMethods=GSSAPI

```

Partial configuration file example:

```xml

    <setting name="socks5.authMethods" value="GSSAPI"/>

```

Also, you will need to specify Java system properties to use a security mechanism that implements the GSS-API (for example, Kerberos is a security mechanism that implements the GSS-API).

The following is a sufficient example of using the Kerberos security mechanism:

```

    $ java -Djavax.security.auth.useSubjectCredsOnly=false \
	    -Djava.security.auth.login.config=login.conf \
	    -Djava.security.krb5.conf=krb5.conf \
	    -jar target/jargyle-${VERSION}.jar \
	    --settings=socks5.authMethods=GSSAPI 

```

The Java system property `-Djavax.security.auth.useSubjectCredsOnly=false` disables JAAS-based authentication to obtain the credentials directly and lets the underlying security mechanism obtain them instead.

The Java system property `-Djava.security.auth.login.config=login.conf` provides a JAAS configuration file to the underlying security mechanism.

`login.conf`:

```

    com.sun.security.jgss.accept  {
      com.sun.security.auth.module.Krb5LoginModule required
      useKeyTab=true
      keyTab="rcmd.keytab"
      storeKey=true
      principal="rcmd/127.0.0.1";
    };

``` 

In `login.conf`, `rcmd/127.0.0.1` is a service principal that is created by a Kerberos administrator specifically for a SOCKS5 server with the service name `rcmd` residing at the address `127.0.0.1`. (In a production environment, the address `127.0.0.1` should be replaced by the fully qualified domain name of where the SOCKS5 server resides.) Also in `login.conf`, `rcmd.keytab` is a keytab file also created by a Kerberos administrator that contains the aforementioned service principal and its respective encrypted key.  

The Java system property `-Djava.security.krb5.conf=krb5.conf` provides the Kerberos configuration file that points to the Kerberos Key Distribution Center (KDC) for authentication.   

`krb5.conf`:

```

    [libdefaults]
        kdc_realm = EXAMPLE.COM
        default_realm = EXAMPLE.COM
        kdc_udp_port = 12345
        kdc_tcp_port = 12345
    
    [realms]
        EXAMPLE.COM = {
            kdc = 127.0.0.1:12345
        }
    
```

In `krb5.conf`, a KDC is defined as running at the address `127.0.0.1` on port `12345` with its realm as `EXAMPLE.COM`. (In a production environment, the address `127.0.0.1` should be replaced by the fully qualified domain name of where the KDC resides. Also, in a production environment, the realm `EXAMPLE.COM` should be replaced by an actual realm provided by the Kerberos administrator.)  

### 3. 8. With External Connections Set to Another SOCKS Server

You can have Jargyle's external connections set to another SOCKS server. To have its external connections set to another SOCKS server, you will need to specify the other SOCKS server as a URI in the setting `externalClient.externalServerUri`


Partial command line example:

```

    --settings=externalClient.externalServerUri=socks5://127.0.0.1:23456

```

Partial configuration file example:

```xml

    <setting name="externalClient.externalServerUri" value="socks5://127.0.0.1:23456"/>

```

Please note that the scheme in the URI specifies the SOCKS protocol to be used when accessing the other SOCKS server (`socks5`), the host or address of the other SOCKS server (`127.0.0.1`), and the port number of the other SOCKS server (`23456`). In the aforementioned examples, the SOCKS protocol version 5 is used. At this time, the only supported scheme for the URI format is `socks5`

#### 3. 8. 1. Using SOCKS5 Authentication

You have the following SOCKS5 authentication methods to choose from for accessing the other SOCKS5 server:

- `NO_AUTHENTICATION_REQUIRED`: No authentication required

- `GSSAPI`: GSS-API authentication

- `USERNAME_PASSWORD`: Username password authentication

You can have one or more of the aforementioned authentication methods set in the setting `externalClient.socks5.authMethods` as a space separated list.

Partial command line example:

```

    "--settings=externalClient.socks5.authMethods=NO_AUTHENTICATION_REQUIRED GSSAPI"

```

Partial configuration file example:

```xml

    <setting name="externalClient.socks5.authMethods" value="GSSAPI USERNAME_PASSWORD"/>

```

If not set, the default value for the setting `externalClient.socks5.authMethods` is set to `NO_AUTHENTICATION_REQUIRED`

##### 3. 8. 1. 1. Using No Authentication

Because the default value for the setting `externalClient.socks5.authMethods` is set to `NO_AUTHENTICATION_REQUIRED`, it is not required for `NO_AUTHENTICATION_REQUIRED` to be included in the setting `externalClient.socks5.authMethods`.

However, if other authentication methods are to be used in addition to `NO_AUTHENTICATION_REQUIRED`, `NO_AUTHENTICATION_REQUIRED` must be included in the setting `externalClient.socks5.authMethods`

Partial command line example:

```

    "--settings=externalClient.socks5.authMethods=NO_AUTHENTICATION_REQUIRED GSS-API USERNAME_PASSWORD"

```

Partial configuration file example:

```xml

    <setting name="externalClient.socks5.authMethods" value="NO_AUTHENTICATION_REQUIRED GSS-API USERNAME_PASSWORD"/>

```

##### 3. 8. 1. 2. Using Username Password Authentication

To use username password authentication, you will need to have the setting `externalClient.socks5.authMethods` to have `USERNAME_PASSWORD` included.

Partial command line example:

```

    --settings=externalClient.socks5.authMethods=USERNAME_PASSWORD

```

Partial configuration file example:

```xml

    <setting name="externalClient.socks5.authMethods" value="USERNAME_PASSWORD"/>

```

To provide a username and password for the other SOCKS5 server, you can use either of the following command line options:

- `--external-client-socks5-user-pass=USERNAME:PASSWORD`

- `--enter-external-client-socks5-user-pass`

The command line option `--external-client-socks5-user-pass` requires that the actual username be followed by a colon character (`:`) followed by the the actual password.

Partial command line example:

```

    --external-client-socks5-user-pass=Aladdin:opensesame

```

If the username or the password contains a colon character (`:`), then each colon character must be replaced with the URL encoding character `%3A`.

Also, if the username or the password contains a percent sign character (`%`) not used for URL encoding, then each percent sign character not used for URL encoding must be replaced with the URL encoding character `%25`.

The command line option `--enter-external-client-socks5-user-pass` provides an interactive prompt for you to enter the username and password. This command line option is best when you do not wish to have the username and password appear in any script or in the command line history for security reasons.

##### 3. 8. 1. 3. Using GSS-API Authentication

To use GSS-API authentication, you will need to have the setting `externalClient.socks5.authMethods` to have `GSSAPI` included.

Partial command line example:

```

    --settings=externalClient.socks5.authMethods=GSSAPI

```

Partial configuration file example:

```xml

    <setting name="externalClient.socks5.authMethods" value="GSSAPI"/>

```

Also, you will need to specify Java system properties to use a security mechanism that implements the GSS-API (for example, Kerberos is a security mechanism that implements the GSS-API), and you will also need to specify the GSS-API service name of the other SOCKS5 server.

The following is a sufficient example of using the Kerberos security mechanism:

```

    $ java -Djavax.security.auth.useSubjectCredsOnly=false \
	    -Djava.security.auth.login.config=login.conf \
	    -Djava.security.krb5.conf=krb5.conf \
	    -jar target/jargyle-${VERSION}.jar \
	    --settings=externalClient.externalServerUri=socks5://127.0.0.1:23456 \
	    --settings=externalClient.socks5.authMethods=GSSAPI \
	    --settings=externalClient.socks5.gssapiServiceName=rcmd/127.0.0.1 

```

The Java system property `-Djavax.security.auth.useSubjectCredsOnly=false` disables JAAS-based authentication to obtain the credentials directly and lets the underlying security mechanism obtain them instead.

The Java system property `-Djava.security.auth.login.config=login.conf` provides a JAAS configuration file to the underlying security mechanism.

`login.conf`:

```

    com.sun.security.jgss.initiate  {
      com.sun.security.auth.module.Krb5LoginModule required
      useKeyTab=true
      keyTab="alice.keytab"
      storeKey=true
      principal="alice";
    };

``` 

In `login.conf`, `alice` is a principal that is created by a Kerberos administrator. Also in `login.conf`, `alice.keytab` is a keytab file also created by a Kerberos administrator that contains the aforementioned principal and its respective encrypted key.  

The Java system property `-Djava.security.krb5.conf=krb5.conf` provides the Kerberos configuration file that points to the Kerberos Key Distribution Center (KDC) for authentication.   

`krb5.conf`:

```

    [libdefaults]
        kdc_realm = EXAMPLE.COM
        default_realm = EXAMPLE.COM
        kdc_udp_port = 12345
        kdc_tcp_port = 12345
    
    [realms]
        EXAMPLE.COM = {
            kdc = 127.0.0.1:12345
        }
    
```

In `krb5.conf`, a KDC is defined as running at the address `127.0.0.1` on port `12345` with its realm as `EXAMPLE.COM`. (In a production environment, the address `127.0.0.1` should be replaced by the fully qualified domain name of where the KDC resides. Also, in a production environment, the realm `EXAMPLE.COM` should be replaced by an actual realm provided by the Kerberos administrator.)

The command line option `--settings=externalClient.socks5.gssapiServiceName=rcmd/127.0.0.1` is the GSS-API service name (or the Kerberos service principal) of the other SOCKS5 server residing at the address `127.0.0.1`. (In a production environment, the address `127.0.0.1` should be replaced by the fully qualified domain name of where the other SOCKS5 server resides.)

## 4. Integration Testing

To run integration testing, you would run the following command:

```

    $ mvn integration-test

```

## 5. TODO

**Javadoc documentation on all types**

**Unit testing on other types**

**Further documentation**

- Command line reference

- Configuration file reference
  
- Users file reference
  
## 6. Contact

If you have any questions or comments, you can e-mail me at `j0n4th4n.h3nd3rs0n@gmail.com`