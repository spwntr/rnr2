package com.thoughtworks.rnr.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet parameters
 */
@WebServlet(name = "oauth", urlPatterns = {"/oauth/_callback", "/oauth"}, initParams = {
        // clientId is 'Consumer Key' in the Remote Access UI
        @WebInitParam(name = "clientId", value = "3MVG9Iu66FKeHhINkDZtpSFwPuzIarpL2Rs3AbfckOpkZhCvwKTdcDPUSkZUIESoKIrsbp9ugHPK3KqJXlA_R"),
        // clientSecret is 'Consumer Secret' in the Remote Access UI
        @WebInitParam(name = "clientSecret", value = "4233795443642531062"),
        // This must be identical to 'Callback URL' in the Remote Access UI
        @WebInitParam(name = "redirectUri", value = "http://localhost:8080/oauth/_callback"),
        @WebInitParam(name = "environment", value = "https://test.salesforce.com"),})
public class OAuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String INSTANCE_URL = "INSTANCE_URL";

    private String clientId = null;
    private String clientSecret = null;
    private String redirectUri = null;
    private String environment = null;
    private String authUrl = null;
    private String tokenUrl = null;

    public void init() throws ServletException {
        clientId = this.getInitParameter("clientId");
        clientSecret = this.getInitParameter("clientSecret");
        redirectUri = this.getInitParameter("redirectUri");
        environment = this.getInitParameter("environment");

        try {
            authUrl = environment
                    + "/services/oauth2/authorize?response_type=code&client_id="
                    + clientId + "&redirect_uri="
                    + URLEncoder.encode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ServletException(e);
        }

        tokenUrl = environment + "/services/oauth2/token";
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String accessToken = (String) request.getSession().getAttribute(
                ACCESS_TOKEN);

        if (accessToken == null) {
            String instanceUrl = null;

            if (request.getRequestURI().endsWith("oauth")) {
                // we need to send the user to authorize
                response.sendRedirect(authUrl);
                return;
            } else {
                System.out.println("Auth successful - got callback");
                HttpClient httpClient = HttpClients.createDefault();
                String code = request.getParameter("code");

                HttpPost httpPost = new HttpPost(tokenUrl);
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("code", code));
                nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
                nvps.add(new BasicNameValuePair("client_id", clientId));
                nvps.add(new BasicNameValuePair("client_secret", clientSecret));
                nvps.add(new BasicNameValuePair("redirect_uri", redirectUri));
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                try {
                    HttpResponse response2 = httpClient.execute(httpPost);
                    HttpEntity entity = response2.getEntity();
                    try {
                        JSONObject authResponse = new JSONObject(
                                new JSONTokener(new InputStreamReader(
                                        entity.getContent()))
                        );

                        System.out.println("Auth response: "
                                + authResponse.toString(2));

                        accessToken = authResponse.getString("access_token");
                        instanceUrl = authResponse.getString("instance_url");

                        System.out.println("Got access token: " + accessToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        throw new ServletException(e);
                    }
                } finally {
                    httpPost.releaseConnection();
                }
            }

            // Set a session attribute so that other servlets can get the access
            // token
            request.getSession().setAttribute(ACCESS_TOKEN, accessToken);

            // We also get the instance URL from the OAuth response, so set it
            // in the session too
            request.getSession().setAttribute(INSTANCE_URL, instanceUrl);
        }

        response.sendRedirect(request.getContextPath() + "/DemoREST");
    }
}

