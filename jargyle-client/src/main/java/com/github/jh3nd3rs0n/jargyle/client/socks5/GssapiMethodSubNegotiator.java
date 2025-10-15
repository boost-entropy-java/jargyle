package com.github.jh3nd3rs0n.jargyle.client.socks5;

import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Method;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodEncapsulation;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.MethodSubNegotiationException;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.*;
import org.ietf.jgss.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

final class GssapiMethodSubNegotiator
        extends MethodSubNegotiator {

    public GssapiMethodSubNegotiator() {
        super(Method.GSSAPI);
    }

    private void establishContext(
            final Socket socket, final GSSContext context)
            throws IOException, GSSException {
        InputStream inStream = socket.getInputStream();
        OutputStream outStream = socket.getOutputStream();
        byte[] token = new byte[]{};
        while (!context.isEstablished()) {
            if (token == null) {
                token = new byte[]{};
            }
            token = context.initSecContext(token, 0, token.length);
            if (token != null) {
                outStream.write(AuthenticationMessage.newInstance(
                        Token.newInstance(token)).toByteArray());
                outStream.flush();
            }
            if (!context.isEstablished()) {
                AuthenticationMessage message;
                try {
                    message = AuthenticationMessage.newInstanceFromServer(
                            inStream);
                } catch (AbortMessageException e) {
                    throw new MethodSubNegotiationException(
                            this.getMethod(),
                            "server aborted process of context "
                                    + "establishment");
                }
                token = message.getToken().getBytes();
            }
        }
    }

    private ProtectionLevel negotiateProtectionLevel(
            final Socket socket,
            final GSSContext context,
            final Socks5Client socks5Client)
            throws IOException, GSSException {
        InputStream inStream = socket.getInputStream();
        OutputStream outStream = socket.getOutputStream();
        boolean necReferenceImpl = socks5Client.getProperties().getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL).booleanValue();
        ProtectionLevels protectionLevels =
                socks5Client.getProperties().getValue(
                        Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS);
        byte[] token = new byte[]{protectionLevels.getFirst().byteValue()};
        if (!necReferenceImpl) {
            token = context.wrap(token, 0, token.length,
                    new MessageProp(0, true));
        }
        outStream.write(ProtectionLevelNegotiationMessage.newInstance(
                Token.newInstance(token)).toByteArray());
        outStream.flush();
        ProtectionLevelNegotiationMessage message =
                ProtectionLevelNegotiationMessage.newInstanceFrom(
                        inStream);
        token = message.getToken().getBytes();
        if (!necReferenceImpl) {
            token = context.unwrap(token, 0, token.length,
                    new MessageProp(0, true));
        }
        ProtectionLevel protectionLevelSelection;
        try {
            protectionLevelSelection = ProtectionLevel.valueOfByte(
                    token[0]);
        } catch (IllegalArgumentException e) {
            throw new MethodSubNegotiationException(this.getMethod(), e);
        }
        if (!protectionLevels.contains(protectionLevelSelection)) {
            throw new MethodSubNegotiationException(
                    this.getMethod(),
                    String.format(
                            "server selected %s which is not acceptable "
                                    + "by this socket",
                            protectionLevelSelection));
        }
        return protectionLevelSelection;
    }

    private GSSContext newContext(
            final Socks5Client socks5Client) throws GSSException {
        GSSManager manager = GSSManager.getInstance();
        String server = socks5Client.getProperties().getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SERVICE_NAME);
        GSSName serverName = manager.createName(server, null);
        Oid mechanismOid = socks5Client.getProperties().getValue(
                Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_MECHANISM_OID);
        GSSContext context = manager.createContext(
                serverName,
                mechanismOid,
                null,
                GSSContext.DEFAULT_LIFETIME);
        context.requestMutualAuth(true);
        context.requestConf(true);
        context.requestInteg(true);
        return context;
    }

    @Override
    public MethodEncapsulation subNegotiate(
            final Socket socket,
            final Socks5Client socks5Client) throws IOException {
        GSSContext context = null;
        ProtectionLevel protectionLevelSelection = null;
        try {
            context = this.newContext(socks5Client);
            this.establishContext(socket, context);
            protectionLevelSelection = this.negotiateProtectionLevel(
                    socket, context, socks5Client);
        } catch (IOException e) {
            if (context != null) {
                try {
                    context.dispose();
                } catch (GSSException ex) {
                    throw new MethodSubNegotiationException(
                            this.getMethod(), ex);
                }
            }
            throw e;
        } catch (GSSException e) {
            if (context != null) {
                try {
                    context.dispose();
                } catch (GSSException ex) {
                    throw new MethodSubNegotiationException(
                            this.getMethod(), ex);
                }
            }
            throw new MethodSubNegotiationException(this.getMethod(), e);
        }
        MessageProp msgProp = protectionLevelSelection.newMessageProp(
                socks5Client.getProperties().getValue(
                        Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG),
                socks5Client.getProperties().getValue(
                        Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF));
        return new GssapiMethodEncapsulation(socket, context, msgProp);
    }

}
