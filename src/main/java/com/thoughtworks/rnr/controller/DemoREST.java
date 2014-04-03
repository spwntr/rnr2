package com.thoughtworks.rnr.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

@WebServlet(urlPatterns = {"/DemoREST/*", "/DemoREST"})
public class DemoREST extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String INSTANCE_URL = "INSTANCE_URL";

    private void showAccounts(String instanceUrl, String accessToken,
                              PrintWriter writer) throws ServletException, IOException, URISyntaxException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(instanceUrl
                + "/services/data/v29.0/query");

        // set the token in the header
        get.setHeader("Authorization", "OAuth " + accessToken);

        // set the SOQL as a query param
        URI uri = new URIBuilder(get.getURI()).addParameter("q",
                "SELECT Contact.pse__Start_Date__c, " +
                        "Contact.Email, " +
                        "(SELECT pse__Timecard_Header__c.pse__Total_Hours__c " +
                        "FROM Contact.pse__Timecards__r) " +
                        "FROM Contact " +
                        "WHERE Contact.Email = 'bsiebert@thoughtworks.com'").build();

        get.setURI(uri);

        try {
            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // Now lets use the standard java json classes to work with the
                // results
                try {
                    JSONObject response2 = new JSONObject(
                            new JSONTokener(new InputStreamReader(
                                    entity.getContent())));
                    writer.write("Query response: "
                            + response2.toString(2));

                    writer.write(response2.getString("totalSize")
                            + " record(s) returned\n\n");

                    JSONObject result = response2.getJSONArray("records").getJSONObject(0);

                    writer.write(result.getString("Email")
                            + "\n"
                            +result.get("pse__Start_Date__c")
                            +"\n");
                    JSONArray timeCards = result.getJSONObject("pse__Timecards__r").getJSONArray("records");
                    for (int j = 0; j < timeCards.length(); j++) {
                        writer.write(timeCards.getJSONObject(j).getString("pse__Total_Hours__c") + "\n");
                    }

                    writer.write("\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new ServletException(e);
                }
            }
        } finally {
            get.releaseConnection();
        }
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();

        String accessToken = (String) request.getSession().getAttribute(
                ACCESS_TOKEN);

        String instanceUrl = (String) request.getSession().getAttribute(
                INSTANCE_URL);

        if (accessToken == null) {
            writer.write("Error - no access token");
            return;
        }

        writer.write("We have an access token: " + accessToken + "\n"
                + "Using instance " + instanceUrl + "\n\n");

        try {
            showAccounts(instanceUrl, accessToken, writer);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

