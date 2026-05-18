package com.tareg.onlineshopping;

import com.tareg.onlineshopping.dao.ReportDAO;
import com.tareg.onlineshopping.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminReportServlet extends HttpServlet {

    private final ReportDAO reportDAO = new ReportDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = requireAdmin(request, response);
        if (user == null) return;

        request.setAttribute("summary", reportDAO.getSummary());
        request.setAttribute("dailySales", reportDAO.getDailySales(14));
        request.setAttribute("topProducts", reportDAO.getTopProducts(5));
        request.setAttribute("statusStats", reportDAO.getOrderStatusBreakdown());
        request.getRequestDispatcher("/WEB-INF/views/reports.jsp").forward(request, response);
    }

    private User requireAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        User user = (User) userObj;
        if (!user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return null;
        }
        return user;
    }
}

