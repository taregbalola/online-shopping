package com.tareg.onlineshopping;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ProductsServlet - Enterprise Controller
 * Handles product listing and product creation using a JSP view.
 */
public class ProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        prepareView(request);
        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = clean(request.getParameter("name"));
        String description = clean(request.getParameter("description"));
        String category = clean(request.getParameter("category"));
        String priceText = clean(request.getParameter("price"));
        String stockText = clean(request.getParameter("stock"));

        setFormValues(request, name, description, category, priceText, stockText);

        String validationError = validate(name, priceText, stockText);
        if (validationError != null) {
            request.setAttribute("errorMessage", validationError);
            prepareView(request);
            request.getRequestDispatcher("/products.jsp").forward(request, response);
            return;
        }

        BigDecimal price = new BigDecimal(priceText);
        int stock = Integer.parseInt(stockText);
        ProductCatalog.addProduct(name, description, category, price, stock);

        HttpSession session = request.getSession();
        session.setAttribute("flashSuccess", "Product '" + name + "' added successfully.");
        response.sendRedirect(request.getContextPath() + "/products");
    }

    private void prepareView(HttpServletRequest request) {
        request.setAttribute("products", ProductCatalog.getProducts());
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object success = session.getAttribute("flashSuccess");
            Object error = session.getAttribute("flashError");
            if (success != null) {
                request.setAttribute("successMessage", success);
                session.removeAttribute("flashSuccess");
            }
            if (error != null) {
                request.setAttribute("errorMessage", error);
                session.removeAttribute("flashError");
            }
        }
    }

    private void setFormValues(HttpServletRequest request, String name, String description, String category, String priceText, String stockText) {
        request.setAttribute("formName", name);
        request.setAttribute("formDescription", description);
        request.setAttribute("formCategory", category);
        request.setAttribute("formPrice", priceText);
        request.setAttribute("formStock", stockText);
    }

    private String validate(String name, String priceText, String stockText) {
        if (name.length() == 0) {
            return "Product name is required.";
        }
        if (priceText.length() == 0) {
            return "Price is required.";
        }
        if (stockText.length() == 0) {
            return "Stock quantity is required.";
        }
        try {
            new BigDecimal(priceText);
        } catch (NumberFormatException ex) {
            return "Price must be a valid number.";
        }
        try {
            int stock = Integer.parseInt(stockText);
            if (stock < 0) {
                return "Stock cannot be negative.";
            }
        } catch (NumberFormatException ex) {
            return "Stock must be a valid whole number.";
        }
        return null;
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
