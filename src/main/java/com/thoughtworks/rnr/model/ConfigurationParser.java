package com.thoughtworks.rnr.model;

import com.thoughtworks.rnr.saml.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ConfigurationParser {

    @Autowired
    private String configPath;

    public Configuration parse() {
        System.out.println(configPath);
        return new Configuration("");
    }
}
