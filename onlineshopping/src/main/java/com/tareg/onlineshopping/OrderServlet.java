package com.tareg.onlineshopping;

import com.tareg.onlineshopping.dao.OrderDAO;
import com.tareg.onlineshopping.model.Order;
import com.tareg.onlineshopping.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class OrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = requireUser(request, response);
        if (user == null) return;

        boolean adminAll = user.isAdmin() && "all".equalsIgnoreCase(clean(request.getParameter("scope")));
        List<Order> orders = adminAll ? orderDAO.findAll() : orderDAO.findByUser(user.getId());

        request.setAttribute("orders", orders);
        request.setAttribute("showAll", adminAll);
        pullFlashMessages(request);
        request.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = requireUser(request, response);
        if (user == null) return;

        request.setCharacterEncoding("UTF-8");
        String action = clean(request.getParameter("action"));

        if ("updateStatus".equals(action) && user.isAdmin()) {
            handleStatusUpdate(request, response);
            return;
        }

        request.getSession().setAttribute("flashError", "Invalid order action.");
        response.sendRedirect(request.getContextPath() + "/orders");
    }

    private void handleStatusUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long orderId = parseLong(request.getParameter("orderId"));
        String status = clean(request.getParameter("status")).toUpperCase();

        if (orderId <= 0 || status.isEmpty()) {
            request.getSession().setAttribute("flashError", "Invalid order status update request.");
            response.sendRedirect(request.getContextPath() + "/orders?scope=all");
            return;
        }

        boolean updated = orderDAO.updateOrderStatus(orderId, status);
        if (updated) {
            request.getSession().setAttribute("flashSuccess", "Order #" + orderId + " updated to " + status + ".");
        } else {
            request.getSession().setAttribute("flashError", "Order status update failed.");
        }
        response.sendRedirect(request.getContextPath() + "/orders?scope=all");
    }

    private User requireUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        Object userObj = session.getAttribute("loggedInUser");
        if (!(userObj instanceof User)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return (User) userObj;
    }

    private void pullFlashMessages(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return;

        Object success = session.getAttribute("flashSuccess");
        if (success != null) {
            request.setAttribute("successMessage", success);
            session.removeAttribute("flashSuccess");
        }

        Object error = session.getAttribute("flashError");
        if (error != null) {
            request.setAttribute("errorMessage", error);
            session.removeAttribute("flashError");
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }

    private long parseLong(String value) {
        try {
            return Long.parseLong(clean(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}

