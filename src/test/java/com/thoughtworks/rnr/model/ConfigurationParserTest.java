package com.thoughtworks.rnr.model;

import com.thoughtworks.rnr.saml.Configuration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ConfigurationParserTest {

    private ConfigurationParser parser;

    @Before
    public void setup() {
        parser = new ConfigurationParser();
    }

    @Ignore
    @Test
    public void shouldBuildConfigurationFromFile() {
        Configuration configuration = parser.parse();
        assertThat(configuration.getAuthenticationURL(), is("https://thoughtworks.oktapreview.com/app/template_saml_2_0/k21tpw64VPAMDOMKRXBS/sso/saml"));
    }

}
