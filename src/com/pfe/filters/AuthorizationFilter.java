package com.pfe.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pfe.controllers.Connexion;

@WebFilter( "/faces/restreint/*" )
public class AuthorizationFilter implements Filter {
	
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException,
            ServletException {
     
     	
     	
        HttpServletRequest req = (HttpServletRequest) request;
        Connexion cnx = (Connexion) req.getSession().getAttribute( "connexion" );

        if ( cnx != null && cnx.isLoggedIn() ) {
            // User is logged in, so just continue request.
           
        	
        	
        	chain.doFilter( request, response );
      
        
        
        } else {
            // User is not logged in, so redirect to index.
            HttpServletResponse res = (HttpServletResponse) response;
            res.sendRedirect( req.getContextPath() + "/faces/login.xhtml" );
        }
    }

    @Override
    public void init( FilterConfig arg0 ) throws ServletException {
        // TODO Auto-generated method stub

    }
}
