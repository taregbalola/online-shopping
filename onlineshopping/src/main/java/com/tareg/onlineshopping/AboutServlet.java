package com.tareg.onlineshopping;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AboutServlet - Enterprise Controller
 * Handles about page requests and forwards to JSP view layer
 */
public class AboutServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Business logic can go here
        // Example: fetch company information from database
        // For now, we just forward to the view
        
        request.getRequestDispatcher("/about.jsp").forward(request, response);
    }
}

