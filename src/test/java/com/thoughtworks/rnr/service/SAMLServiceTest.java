package com.thoughtworks.rnr.service;

import com.thoughtworks.rnr.saml.Configuration;
import org.junit.Before;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;

public class SAMLServiceTest {

    private SAMLService SAMLService;
    @Mock
    Configuration configuration;

    @Before
    public void setup() {
        initMocks(this);
        SAMLService = new SAMLService();
    }
}
