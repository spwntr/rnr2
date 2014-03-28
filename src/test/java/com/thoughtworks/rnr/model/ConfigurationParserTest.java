package com.thoughtworks.rnr.model;

import com.thoughtworks.rnr.saml.Configuration;
import org.apache.xpath.jaxp.XPathFactoryImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import java.io.*;
import java.util.Scanner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConfigurationParserTest {

    @Mock
    private XPath xPathMock;
    @Mock
    private XPathFactory xPathFactoryMock;
    @Mock
    private XPathExpression configXPathExpressionMock;
    @Mock
    private XPathExpression entityXPathExpressionMock;
    @Mock
    private XPathExpression ssoLocationXPathExpressionMock;
    @Mock
    private InputSource inputSourceMock;
    @Mock
    private Node nodeMock;
    @Mock
    private NodeList nodeListMock;
    private ConfigurationParser parser;


    @Before
    public void setup() throws XPathExpressionException {
        initMocks(this);
        when(xPathFactoryMock.newXPath()).thenReturn(xPathMock);
        when(xPathMock.compile("configuration")).thenReturn(configXPathExpressionMock);
        when(xPathMock.evaluate("configuration", inputSourceMock, XPathConstants.NODE)).thenReturn(nodeMock);
        when(nodeMock.getChildNodes()).thenReturn(nodeListMock);

        parser = new ConfigurationParser(xPathFactoryMock, inputSourceMock);
    }

    @Ignore
    @Test
    public void shouldBuildConfigurationFromFile() throws IOException, ParserConfigurationException, SAXException {
        InputSource source = makeInputSource();
        parser = new ConfigurationParser(new XPathFactoryImpl(), source);

        Configuration configuration = parser.parse();

        assertThat(configuration.getAuthenticationURL(), is("https://thoughtworks.oktapreview.com/app/template_saml_2_0/k21tpw64VPAMDOMKRXBS/sso/saml"));
        assertThat(configuration.getOktaId(), is("http://www.okta.com/k21tpw64VPAMDOMKRXBS"));
    }

    private InputSource makeInputSource() throws ParserConfigurationException, IOException, SAXException {
        Scanner scanner = new Scanner(new File("src/main/java/resources/config.xml"), "UTF-8").useDelimiter("\\A");
        String fileAsString = scanner.hasNext() ? scanner.next() : "";
        fileAsString = fileAsString.replaceAll("\\n", "");
        fileAsString = fileAsString.replaceAll("  ", "");
        System.out.println(fileAsString);

        return new InputSource(new StringReader(fileAsString));
    }

    @Test
    public void shouldCompileConfigurationAndEntityExpressions() throws XPathExpressionException {
        parser.parse();
        verify(xPathMock).evaluate("configuration", inputSourceMock, XPathConstants.NODE);
        verify(xPathMock).evaluate("md:EntityDescriptor/@entityID", nodeMock);
        verify(xPathMock).evaluate("md:SingleSignOnService/@Location", nodeMock);
    }

    @Test
    public void shouldThrowXPathException() throws XPathExpressionException {
        when(xPathMock.compile("configuration")).thenThrow(new XPathExpressionException("Error!"));
        ExpectedException thrown = ExpectedException.none();
        thrown.expect(XPathExpressionException.class);

        parser.parse();
    }

}
