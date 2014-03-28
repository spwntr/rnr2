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
}
