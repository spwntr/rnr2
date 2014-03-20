package com.thoughtworks.rnr.service;

import com.thoughtworks.rnr.saml.Configuration;
import com.thoughtworks.rnr.saml.SAMLValidator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SAMLServiceTest {

    @Mock
    private Configuration mockConfiguration;
    private com.thoughtworks.rnr.service.SAMLService SAMLService;
    private SAMLValidator mockSAMLValidator;

    @Before
    public void setup() {
        initMocks(this);
        SAMLService = new SAMLService(mockSAMLValidator,mockConfiguration);
    }
    @Ignore
    @Test
    public void testRedirectToIDPWithSAMLRequest() throws Exception {
        verify(mockSAMLValidator).getConfigurationFrom("/src/main/java/resources");
    }
}
