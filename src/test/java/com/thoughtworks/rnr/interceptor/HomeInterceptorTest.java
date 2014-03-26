package com.thoughtworks.rnr.interceptor;

import com.thoughtworks.rnr.service.SAMLService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HomeInterceptorTest {
    @Mock
    SAMLService mockSAMLService;
    @Mock
    HttpServletRequest mockRequest;
    @Mock
    HttpServletResponse mockResponse;
    @Mock
    HttpSession mockHttpSession;
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
    public void shouldRedirectToOKTALoginWhenNoSessionExists() throws Exception {
        when(mockRequest.getSession(false)).thenReturn(null);
        shouldRedirectToSAMLRequest();
    }

    @Ignore
    @Test
    public void shouldRedirectToOKTALoginWhenNoPrincipalIsAttachedToTheSession() throws Exception {
        when(mockRequest.getSession(false)).thenReturn(mockHttpSession);
        when(mockHttpSession.getAttribute("user")).thenReturn(null);
        shouldRedirectToSAMLRequest();
    }

    @Test
    public void shouldPassThroughToHomeControllerWhenAPrincipalIsAttachedToTheSession() throws Exception {
        when(mockRequest.getSession(false)).thenReturn(mockHttpSession);
        when(mockHttpSession.getAttribute("user")).thenReturn("a string");

        boolean interceptorReturn = homeInterceptor.preHandle(mockRequest, mockResponse, handler);

        assertTrue(interceptorReturn);
    }

    private void shouldRedirectToSAMLRequest() throws Exception {
        String mockSAMLRequest = "SAMLRequest";
        when(mockSAMLService.createSAMLRequest()).thenReturn(mockSAMLRequest);

        boolean interceptorReturn = homeInterceptor.preHandle(mockRequest, mockResponse, handler);

        assertFalse(interceptorReturn);
        verify(mockResponse).sendRedirect(mockSAMLRequest);
    }
}
