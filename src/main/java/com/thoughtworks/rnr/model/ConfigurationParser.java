package com.thoughtworks.rnr.model;

import com.thoughtworks.rnr.saml.Configuration;
import com.thoughtworks.rnr.saml.MetadataNamespaceContext;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

@Component
public class ConfigurationParser {

    //TODO: make this field an autowired bean
    private String configurationFilePath = "/src/main/java/resources/config.xml";
    private XPath xPath;
    private InputSource source;

    public ConfigurationParser(XPathFactory xPathFactory, InputSource source) {
        this.xPath = xPathFactory.newXPath();
        this.source = source;
    }

    public Configuration parse() {
        try {
            xPath.setNamespaceContext(new MetadataNamespaceContext());
            Node root = (Node) xPath.evaluate("configuration", source, XPathConstants.NODE);
            NodeList rootContents = root.getChildNodes();
            Node entityDescriptor = root.getFirstChild();

            for (int i=0; i<rootContents.getLength(); i++) {
                Node node = rootContents.item(i);
                System.out.println(node.getNodeName());
            }
//            NamedNodeMap attributes = entityDescriptor.getAttributes();

            String entityID = xPath.evaluate("md:EntityDescriptor/@entityID", root);
            System.out.println("entityID: " + entityID);
//            String idpDescriptor = xPath.evaluate("md:IDPSSODescriptor", root);
            String sso = xPath.evaluate("md:SingleSignOnService", root);
            String ssoLocation = xPath.evaluate("md:SingleSignOnService/@Location", root);
            System.out.println("ssoLocation: " + ssoLocation);

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return new Configuration("");
    }
}