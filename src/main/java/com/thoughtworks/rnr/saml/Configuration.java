package com.thoughtworks.rnr.saml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class Configuration {

    private String authenticationURL;

    public Configuration(String configurationFilePath) {
//        Scanner scanner = new Scanner(configurationFilePath);
//        InputStream inputStream = getClass().getResourceAsStream(configurationFilePath);
//        String fileAsString = streamToString(inputStream);
    }

    private String streamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scanner.hasNext() ? scanner.next() : "";
    }

    public String getRequest() {
        return "";
    }

    public String getAuthenticationURL() {
        return authenticationURL;
    }
}
