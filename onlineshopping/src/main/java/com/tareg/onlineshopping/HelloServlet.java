package com.tareg.onlineshopping;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HelloServlet - Simple API endpoint test
 * Returns JSON response for testing servlet functionality
 */
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println("{");
        out.println("  \"message\": \"Hello from Online Shopping API\",");
        out.println("  \"status\": \"success\",");
        out.println("  \"timestamp\": \"" + new java.util.Date() + "\",");
        out.println("  \"version\": \"1.0\"");
        out.println("}");
    }
}

