package com.thoughtworks.rnr.model;

import com.thoughtworks.rnr.saml.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ConfigurationParser {

    //TODO: make this field an autowired bean
    private String configurationFilePath = "/src/main/java/resources/config.xml";

    public Configuration parse() {
        return new Configuration("");
    }
}