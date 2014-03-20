package com.thoughtworks.rnr.interceptor;

import com.thoughtworks.rnr.service.SAMLService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

import static org.mockito.MockitoAnnotations.initMocks;

public class HomeInterceptorTest {

    private final String REDIRECT_URL = "https://thoughtworks.oktapreview.com/app/template_saml_2_0/k21tpw64VPAMDOMKRXBS/sso/saml";
    @Mock
    SAMLService mockSAMLService;
    @Mock
    HttpServletRequest mockHttpServletRequest;
    @Mock
    HttpServletResponse mockHttpServletResponse;
    @Mock
    Object handler;
    private HomeInterceptor homeInterceptor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        homeInterceptor = new HomeInterceptor(mockSAMLService);
    }
    @Ignore
    @Test
    public void interceptRedirectsToOKTA() throws Exception {
        when(mockHttpServletResponse.encodeRedirectURL(REDIRECT_URL)).thenReturn(REDIRECT_URL);

        homeInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, handler);

        verify(mockHttpServletResponse).encodeRedirectURL(REDIRECT_URL);
        verify(mockHttpServletResponse).sendRedirect(REDIRECT_URL);
    }
}
