package com.thoughtworks.rnr.saml;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConfigurationTest {
    private Configuration configuration;

    @Before
    public void setUp(){
        configuration = new Configuration("/saml-config.xml");
    }

    @Ignore
    @Test
    public void getApplication_shouldReturnTheApplicationFromTheLocalConfigurationFile() {
        Application application = configuration.getApplication();

        assertThat(application.getAuthenticationURL(), is("https://thoughtworks.oktapreview.com/app/template_saml_2_0/k21tpw64VPAMDOMKRXBS/sso/saml"));
        assertThat(application.getCertificate(), is("MIICozCCAgygAwIBAgIGAT+fauIOMA0GCSqGSIb3DQEBBQUAMIGUMQswCQYDVQQGEwJVUzETMBEG\n" +
                "A1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2FuIEZyYW5jaXNjbzENMAsGA1UECgwET2t0YTEU\n" +
                "MBIGA1UECwwLU1NPUHJvdmlkZXIxFTATBgNVBAMMDHRob3VnaHR3b3JrczEcMBoGCSqGSIb3DQEJ\n" +
                "ARYNaW5mb0Bva3RhLmNvbTAeFw0xMzA3MDIxMjQ0NDlaFw00MzA3MDIxMjQ1NDlaMIGUMQswCQYD\n" +
                "VQQGEwJVUzETMBEGA1UECAwKQ2FsaWZvcm5pYTEWMBQGA1UEBwwNU2FuIEZyYW5jaXNjbzENMAsG\n" +
                "A1UECgwET2t0YTEUMBIGA1UECwwLU1NPUHJvdmlkZXIxFTATBgNVBAMMDHRob3VnaHR3b3JrczEc\n" +
                "MBoGCSqGSIb3DQEJARYNaW5mb0Bva3RhLmNvbTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEA\n" +
                "kAYqmCS63DBsav/zhCMNW38JQxW4hNgO15DCo2RvnkEN1jDK+pnAlRu6pGKJmpvVVx3K0zgSxNBM\n" +
                "XFvWCPfWdk1RaRoo+P/4pcMBAry/ysbVAJ3r1tpUuP9nMt4zuGkwL+TpnFUKVsS690fwID+mRydx\n" +
                "Ab1hTa3EcG5gXdu7pD0CAwEAATANBgkqhkiG9w0BAQUFAAOBgQAXWO+wxM6WSZ6MTZvdh2g1wF0d\n" +
                "GvZhS5LO3q2PUvq4qHx1SchiKbxje+CUHCqOOODOjQeD+SVcUBUPJ8I9OWi9aDiQjKnmpr87h8PH\n" +
                "+Ni1yB2C2KRHdxxSR6SfRjkyNeVEwzTyh2Y2zu+hghddKvllWQoSfwXhIcSrLKtsL71NrQ=="));
        assertThat(application.getIssuer(), is("http://www.okta.com/k21tpw64VPAMDOMKRXBS"));
    }
}
