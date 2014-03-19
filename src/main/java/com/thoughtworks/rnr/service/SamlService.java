package com.thoughtworks.rnr.service;

import com.thoughtworks.rnr.saml.Application;
import com.thoughtworks.rnr.saml.Configuration;
import com.thoughtworks.rnr.saml.SAMLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SAMLService {


    private SAMLValidator validator;
    private Configuration configuration;

    @Autowired
    public SAMLService(SAMLValidator validator, Configuration configuration) {
        this.validator = validator;
        this.configuration = configuration;
    }


    public String redirectToIDPWithSAMLRequest() {
        return null;
    }

    public boolean sessionToken() {
        return true;
    }
}

