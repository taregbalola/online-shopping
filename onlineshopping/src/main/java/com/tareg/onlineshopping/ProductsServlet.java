package com.tareg.onlineshopping;

import com.tareg.onlineshopping.dao.ProductDAO;
import com.tareg.onlineshopping.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * ProductsServlet - Enterprise Controller
 * Requires a valid session. Delegates all DB work to ProductDAO.
 */
public class ProductsServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request, response)) return;
        prepareView(request);
        request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request, response)) return;

        String action = clean(request.getParameter("action"));

        if ("delete".equals(action)) {
            long id = parseLong(request.getParameter("id"));
            if (id > 0) productDAO.delete(id);
            request.getSession().setAttribute("flashSuccess", "Product deleted.");
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String name        = clean(request.getParameter("name"));
        String description = clean(request.getParameter("description"));
        String category    = clean(request.getParameter("category"));
        String priceText   = clean(request.getParameter("price"));
        String stockText   = clean(request.getParameter("stock"));

        setFormValues(request, name, description, category, priceText, stockText);

        String error = validate(name, priceText, stockText);
        if (error != null) {
            request.setAttribute("errorMessage", error);
            prepareView(request);
            request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);
            return;
        }

        BigDecimal price = new BigDecimal(priceText);
        int stock = Integer.parseInt(stockText);
        productDAO.insert(name, description, category, price, stock);

        HttpSession session = request.getSession();
        session.setAttribute("flashSuccess", "Product '" + name + "' added successfully.");
        response.sendRedirect(request.getContextPath() + "/products");
    }

    // -------------------------------------------------------------------------

    private boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    private void prepareView(HttpServletRequest request) {
        request.setAttribute("products", productDAO.findAll());
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object success = session.getAttribute("flashSuccess");
            if (success != null) {
                request.setAttribute("successMessage", success);
                session.removeAttribute("flashSuccess");
            }
            Object errorMsg = session.getAttribute("flashError");
            if (errorMsg != null) {
                request.setAttribute("errorMessage", errorMsg);
                session.removeAttribute("flashError");
            }
        }
    }

    private void setFormValues(HttpServletRequest request, String name, String description,
                               String category, String priceText, String stockText) {
        request.setAttribute("formName", name);
        request.setAttribute("formDescription", description);
        request.setAttribute("formCategory", category);
        request.setAttribute("formPrice", priceText);
        request.setAttribute("formStock", stockText);
    }

    private String validate(String name, String priceText, String stockText) {
        if (name.isEmpty())      return "Product name is required.";
        if (priceText.isEmpty()) return "Price is required.";
        if (stockText.isEmpty()) return "Stock quantity is required.";
        try { new BigDecimal(priceText); } catch (NumberFormatException e) { return "Price must be a valid number."; }
        try {
            int s = Integer.parseInt(stockText);
            if (s < 0) return "Stock cannot be negative.";
        } catch (NumberFormatException e) { return "Stock must be a whole number."; }
        return null;
    }

    private String clean(String v) { return v == null ? "" : v.trim(); }

    private long parseLong(String v) {
        try { return v == null ? 0 : Long.parseLong(v.trim()); }
        catch (NumberFormatException e) { return 0; }
    }
}
