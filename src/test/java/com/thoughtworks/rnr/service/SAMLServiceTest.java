package com.thoughtworks.rnr.service;

import com.thoughtworks.rnr.model.ConfigurationParser;
import com.thoughtworks.rnr.saml.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SAMLServiceTest {

    private final String oktaRedirectURL = "https://thoughtworks.oktapreview.com/app/template_saml_2_0/k21tpw64VPAMDOMKRXBS/sso/saml?SAMLRequest=";
    private final String testRequest = "testRequest";
    private SAMLService SAMLService;
    @Mock
    Configuration configuration;
    @Mock
    ConfigurationParser configurationParserMock;

    @Before
    public void setup() {
        initMocks(this);
        when(configurationParserMock.parse()).thenReturn(configuration);
        when(configuration.getRequest()).thenReturn(testRequest);

        SAMLService = new SAMLService(configurationParserMock);
    }

    @Test
    public void testCreateSAMLRequest() throws Exception {
        String samlRequest = SAMLService.createSAMLRequest();
        assertTrue(samlRequest.startsWith(oktaRedirectURL));
        verify(configurationParserMock).parse();
    }

    @Test
    public void createSAMLRequestShouldGetRequestInfoFromConfiguration() {
        String samlRequest = SAMLService.createSAMLRequest();
        assertTrue(samlRequest.endsWith(testRequest));
    }
}
