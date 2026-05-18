package com.tareg.onlineshopping;

import com.tareg.onlineshopping.dao.OrderDAO;
import com.tareg.onlineshopping.model.Order;
import com.tareg.onlineshopping.model.User;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class InvoicePdfServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = requireUser(request, response);
        if (user == null) return;

        long orderId = parseLong(request.getParameter("orderId"));
        if (orderId <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order id.");
            return;
        }

        Order order = orderDAO.findByIdAccessible(orderId, user.getId(), user.isAdmin());
        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found or access denied.");
            return;
        }

        try (InputStream template = getServletContext().getResourceAsStream("/WEB-INF/reports/invoice.jrxml")) {
            if (template == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invoice template not found.");
                return;
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(template);
            Map<String, Object> params = new HashMap<>();
            params.put("invoiceTitle", "Online Shopping Invoice");
            params.put("orderId", String.valueOf(order.getId()));
            params.put("customerName", order.getCustomerName());
            params.put("orderDate", String.valueOf(order.getCreatedAt()));
            params.put("shippingAddress", order.getShippingAddress());
            params.put("paymentMethod", order.getPaymentMethod());
            params.put("paymentStatus", order.getPaymentStatus());
            params.put("orderStatus", order.getStatus());
            params.put("totalAmount", order.getTotalAmount().toPlainString());

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    params,
                    new JRBeanCollectionDataSource(order.getItems())
            );

            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=invoice-order-" + order.getId() + ".pdf");
            response.setContentLength(pdf.length);
            response.getOutputStream().write(pdf);
            response.getOutputStream().flush();
        } catch (JRException e) {
            throw new ServletException("Failed to generate invoice PDF", e);
        }
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

    private long parseLong(String value) {
        if (value == null) return 0;
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

