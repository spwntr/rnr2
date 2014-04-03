package com.thoughtworks.rnr.service;

import com.thoughtworks.rnr.factory.JSONObjectFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static lib.RegexMatcher.matches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class SalesForceServiceTest {


    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String INSTANCE_URL = "INSTANCE_URL";
    private static final String CLIENT_ID = "3MVG9Iu66FKeHhINkDZtpSFwPuzIarpL2Rs3AbfckOpkZhCvwKTdcDPUSkZUIESoKIrsbp9ugHPK3KqJXlA_R";
    private static final String CLIENT_SECRET = "4233795443642531062";
    private static final String REDIRECT_URI = "http://localhost:8080/oauth/_callback";
    private static final String ENVIRONMENT = "https://test.salesforce.com";
    private static final String tokenUrl = ENVIRONMENT + "/services/oauth2/token";
    private SalesForceService salesForceService;
    private String authUrl;

    @Mock
    HttpServletRequest mockHttpServletRequest;
    @Mock
    HttpServletResponse mockHttpServletResponse;
    @Mock
    HttpSession mockHttpSession;
    @Mock
    HttpClient mockHttpClient;
    @Mock
    HttpPost mockHttpPost;
    @Mock
    HttpResponse mockHttpResponse;
    @Mock
    HttpEntity mockHttpEntity;
    @Mock
    JSONObject mockJSONObject;
    @Mock
    JSONObjectFactory mockJSONObjectFactory;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        authUrl = ENVIRONMENT
                + "/services/oauth2/authorize?response_type=code&client_id="
                + CLIENT_ID
                + "&redirect_uri="
                + URLEncoder.encode(REDIRECT_URI, "UTF-8");
        salesForceService = new SalesForceService("bsiebert@thoughtworks.com", mockJSONObjectFactory);
    }

    @Test
    public void authenticateWithSalesForce_shouldRedirectToSalesForceAuthenticationURLIfNoAccessToken() throws Exception {
        when(mockHttpServletRequest.getSession()).thenReturn(mockHttpSession);
        when(mockHttpSession.getAttribute(ACCESS_TOKEN)).thenReturn(null);
        salesForceService.authenticateWithSalesForce(mockHttpServletRequest, mockHttpServletResponse);
        verify(mockHttpServletResponse).sendRedirect(authUrl);
    }

    @Test
    public void requestAccessTokenFromSalesForce_shouldPOSTWithNameValuePairsAndReturnHttpResponse() throws Exception {
        when(mockHttpServletRequest.getParameter("code")).thenReturn("fake code");
        when(mockHttpClient.execute(any(HttpPost.class))).thenReturn(mock(HttpResponse.class));
        HttpResponse response = salesForceService.requestAccessTokenFromSalesForce(mockHttpServletRequest, mockHttpClient);
        ArgumentCaptor<HttpPost> httpPostCaptor = ArgumentCaptor.forClass(HttpPost.class);
        verify(mockHttpClient).execute(httpPostCaptor.capture());
        String capturedEncodedURL = IOUtils.toString(httpPostCaptor.getValue().getEntity().getContent());
        assertEquals(IOUtils.toString(createHttpPost().getEntity().getContent()), capturedEncodedURL);
        assertThat(response, instanceOf(HttpResponse.class));
    }

    @Test
    public void setAccessTokenAndInstanceURL_shouldSetSessionAccessTokenAndInstanceUrlAndReturnHTTPServletRequest() throws Exception {
        when(mockJSONObject.getString("access_token")).thenReturn("fake token");
        when(mockJSONObject.getString("instance_url")).thenReturn("fake instance url");
        when(mockHttpServletRequest.getSession()).thenReturn(mockHttpSession);
        HttpServletRequest request = salesForceService.setAccessTokenAndInstanceURL(mockJSONObject,
                mockHttpServletRequest);
        verify(mockHttpSession).setAttribute(ACCESS_TOKEN, "fake token");
        verify(mockHttpSession).setAttribute(INSTANCE_URL, "fake instance url");
        assertThat(request, instanceOf(HttpServletRequest.class));
    }

    @Test
    public void queryThoughtWorksStartDate_shouldReturnDateString() throws IOException, URISyntaxException, JSONException {
        JSONObject stubJSONObject = new JSONObject("{records: [{pse__Start_Date__c: 2007-01-10}]}");
        when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockHttpResponse);
        when(mockJSONObjectFactory.httpResponseToJSONObject(any(HttpResponse.class))).thenReturn(stubJSONObject);
        String date = salesForceService.queryThoughtWorksStartDate(mockHttpClient, "instance_url", "access_token");
        verify(mockHttpClient).execute(any(HttpGet.class));
        assertThat(date, matches("^\\d{4}-\\d{2}-\\d{2}$"));
    }

    private HttpPost createHttpPost() throws IOException {
        HttpPost httpPost = new HttpPost(tokenUrl);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("code", "fake code"));
        nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
        nvps.add(new BasicNameValuePair("client_id", CLIENT_ID));
        nvps.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
        nvps.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        return httpPost;
    }
}
