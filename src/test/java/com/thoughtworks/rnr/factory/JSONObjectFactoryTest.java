package com.thoughtworks.rnr.factory;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class JSONObjectFactoryTest {

    private JSONObjectFactory jsonObjectFactory;

    @Mock
    HttpResponse mockHttpResponse;
    @Mock
    HttpEntity mockHttpEntity;

    @Before
    public void setUp() throws Exception {
        jsonObjectFactory = new JSONObjectFactory();
        initMocks(this);
    }

    @Test
    public void httpResponseToJSONObject_shouldReturnJSONObject() throws Exception {
        InputStream stubInputStream = IOUtils.toInputStream("{data: some test data for my input stream}");
        when(mockHttpResponse.getEntity()).thenReturn(mockHttpEntity);
        when(mockHttpEntity.getContent()).thenReturn((stubInputStream));
        JSONObject object = jsonObjectFactory.httpResponseToJSONObject(mockHttpResponse);
        assertThat(object, instanceOf(JSONObject.class));
    }
}
