package com.thoughtworks.rnr.interceptor;

import com.thoughtworks.rnr.service.SAMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HomeInterceptor extends HandlerInterceptorAdapter{
    private static final String OKTA_REDIRECT = "https://thoughtworks.oktapreview.com/app/template_saml_2_0/k21tpw64VPAMDOMKRXBS/sso/saml";
    private SAMLService samlService;

    @Autowired
    public HomeInterceptor(SAMLService samlService) {
        this.samlService = samlService;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        String encodedURL = httpServletResponse.encodeRedirectURL("/redirect");
//        httpServletResponse.sendRedirect(encodedURL);
        System.out.println("got here");
        return true;
    }
}
