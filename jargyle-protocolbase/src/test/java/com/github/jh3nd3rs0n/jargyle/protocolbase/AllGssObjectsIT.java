package com.github.jh3nd3rs0n.jargyle.protocolbase;

import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.GssDatagramSocketIT;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.GssSocketIT;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.GssapiMethodEncapsulationIT;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GssapiMethodEncapsulationIT.class,
        GssDatagramSocketIT.class,
        GssSocketIT.class,
})
public class AllGssObjectsIT {

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        GssEnvironment.setUpBeforeClass(AllGssObjectsIT.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws IOException {
        GssEnvironment.tearDownAfterClass(AllGssObjectsIT.class);
    }

}
