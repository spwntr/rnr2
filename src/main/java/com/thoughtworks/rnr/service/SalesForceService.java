package com.thoughtworks.rnr.service;

import com.thoughtworks.rnr.factory.JSONObjectFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SalesForceService {

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String INSTANCE_URL = "INSTANCE_URL";
    private static final String CLIENT_ID = "3MVG9Iu66FKeHhINkDZtpSFwPuzIarpL2Rs3AbfckOpkZhCvwKTdcDPUSkZUIESoKIrsbp9ugHPK3KqJXlA_R";
    private static final String CLIENT_SECRET = "4233795443642531062";
    private static final String REDIRECT_URI = "http://localhost:8080/oauth/_callback";
    private static final String ENVIRONMENT = "https://test.salesforce.com";
    private static final String tokenUrl = ENVIRONMENT + "/services/oauth2/token";
    private final JSONObjectFactory factory;
    private String authUrl = null;
    private String email;

    public SalesForceService(String email, JSONObjectFactory factory) throws UnsupportedEncodingException {
        this.email = email;
        this.factory = factory;
        authUrl = ENVIRONMENT
                + "/services/oauth2/authorize?response_type=code&client_id="
                + CLIENT_ID
                + "&redirect_uri="
                + URLEncoder.encode(REDIRECT_URI, "UTF-8");
    }

    public void authenticateWithSalesForce(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessToken = (String) request.getSession().getAttribute(ACCESS_TOKEN);
        if (accessToken == null) {
            response.sendRedirect(authUrl);
        }
    }

    public HttpResponse requestAccessTokenFromSalesForce(HttpServletRequest request, HttpClient httpClient) throws IOException {
        String code = request.getParameter("code");
        HttpPost httpPost = new HttpPost(tokenUrl);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("code", code));
        nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
        nvps.add(new BasicNameValuePair("client_id", CLIENT_ID));
        nvps.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
        nvps.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        return httpClient.execute(httpPost);
    }

    public HttpServletRequest setAccessTokenAndInstanceURL(JSONObject responseJSON, HttpServletRequest request) throws JSONException {
        String accessToken = responseJSON.getString("access_token");
        String instanceURL = responseJSON.getString("instance_url");
        request.getSession().setAttribute(ACCESS_TOKEN, accessToken);
        request.getSession().setAttribute(INSTANCE_URL, instanceURL);
        return request;
    }

    public String queryThoughtWorksStartDate(HttpClient httpClient, String instanceUrl, String accessToken) throws URISyntaxException, IOException, JSONException {
        HttpGet get = new HttpGet(instanceUrl
                + "/services/data/v29.0/query");

        get.setHeader("Authorization", "OAuth " + accessToken);

        URI uri = new URIBuilder(get.getURI()).addParameter("q",
                "SELECT Contact.pse__Start_Date__c, " +
                        "(SELECT pse__Timecard_Header__c.pse__Total_Hours__c " +
                        "FROM Contact.pse__Timecards__r) " +
                        "FROM Contact " +
                        "WHERE Contact.Email = '" + email + "'"
        ).build();
        get.setURI(uri);
        HttpResponse response = httpClient.execute(get);
        JSONObject JSONResponse = factory.httpResponseToJSONObject(response);
        JSONObject result = JSONResponse.getJSONArray("records").getJSONObject(0);
        return (String) result.get("pse__Start_Date__c");
    }
}
