package com.thoughtworks.rnr.factory;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStreamReader;

public class JSONObjectFactory {

    public JSONObject httpResponseToJSONObject(HttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        return new JSONObject(
                new JSONTokener(new InputStreamReader(
                        entity.getContent()))
        );
    }
}
