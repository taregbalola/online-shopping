package com.tareg.onlineshopping;

import com.tareg.onlineshopping.dao.AddressDAO;
import com.tareg.onlineshopping.dao.CartDAO;
import com.tareg.onlineshopping.dao.OrderDAO;
import com.tareg.onlineshopping.model.Address;
import com.tareg.onlineshopping.model.CartItem;
import com.tareg.onlineshopping.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class CartServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();
    private final AddressDAO addressDAO = new AddressDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = requireUser(request, response);
        if (user == null) return;

        loadViewData(request, user);
        request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = requireUser(request, response);
        if (user == null) return;

        request.setCharacterEncoding("UTF-8");
        String action = clean(request.getParameter("action"));

        switch (action) {
            case "add":
                addToCart(request, response, user);
                return;
            case "updateQty":
                updateQuantity(request, response, user);
                return;
            case "remove":
                removeItem(request, response, user);
                return;
            case "addAddress":
                addAddress(request, response, user);
                return;
            case "setDefaultAddress":
                setDefaultAddress(request, response, user);
                return;
            case "deleteAddress":
                deleteAddress(request, response, user);
                return;
            case "checkout":
                checkout(request, response, user);
                return;
            default:
                request.getSession().setAttribute("flashError", "Unknown cart action.");
                response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        long productId = parseLong(request.getParameter("productId"));
        int quantity = parseInt(request.getParameter("quantity"));
        if (productId <= 0 || quantity <= 0) {
            request.getSession().setAttribute("flashError", "Invalid product or quantity.");
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        cartDAO.addItem(user.getId(), productId, quantity);
        request.getSession().setAttribute("flashSuccess", "Item added to cart.");
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void updateQuantity(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        long itemId = parseLong(request.getParameter("itemId"));
        int quantity = parseInt(request.getParameter("quantity"));
        if (itemId <= 0 || quantity <= 0) {
            request.getSession().setAttribute("flashError", "Quantity must be at least 1.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        cartDAO.updateQuantity(user.getId(), itemId, quantity);
        request.getSession().setAttribute("flashSuccess", "Cart updated.");
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void removeItem(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        long itemId = parseLong(request.getParameter("itemId"));
        if (itemId <= 0) {
            request.getSession().setAttribute("flashError", "Invalid cart item.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        cartDAO.removeItem(user.getId(), itemId);
        request.getSession().setAttribute("flashSuccess", "Item removed from cart.");
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void addAddress(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        String label = clean(request.getParameter("label"));
        String recipient = clean(request.getParameter("recipientName"));
        String phone = clean(request.getParameter("phone"));
        String line1 = clean(request.getParameter("line1"));
        String line2 = clean(request.getParameter("line2"));
        String city = clean(request.getParameter("city"));
        String state = clean(request.getParameter("state"));
        String postal = clean(request.getParameter("postalCode"));
        String country = clean(request.getParameter("country"));
        boolean makeDefault = "on".equalsIgnoreCase(clean(request.getParameter("isDefault")));

        if (recipient.isEmpty() || phone.isEmpty() || line1.isEmpty() || city.isEmpty() || state.isEmpty() || postal.isEmpty() || country.isEmpty()) {
            request.getSession().setAttribute("flashError", "Please fill all required address fields.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        addressDAO.insert(user.getId(), label.isEmpty() ? "Home" : label, recipient, phone, line1, line2, city, state, postal, country, makeDefault);
        request.getSession().setAttribute("flashSuccess", "Address saved.");
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void setDefaultAddress(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        long addressId = parseLong(request.getParameter("addressId"));
        if (addressId <= 0) {
            request.getSession().setAttribute("flashError", "Invalid address.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        addressDAO.setDefault(addressId, user.getId());
        request.getSession().setAttribute("flashSuccess", "Default address updated.");
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void deleteAddress(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        long addressId = parseLong(request.getParameter("addressId"));
        if (addressId <= 0) {
            request.getSession().setAttribute("flashError", "Invalid address.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        addressDAO.delete(addressId, user.getId());
        request.getSession().setAttribute("flashSuccess", "Address deleted.");
        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void checkout(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        long addressId = parseLong(request.getParameter("addressId"));
        String paymentMethod = clean(request.getParameter("paymentMethod"));
        if (addressId <= 0 || paymentMethod.isEmpty()) {
            request.getSession().setAttribute("flashError", "Please select address and payment method.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        try {
            long orderId = orderDAO.checkoutCart(user.getId(), addressId, paymentMethod);
            request.getSession().setAttribute("flashSuccess", "Checkout complete. Order #" + orderId + " created.");
            response.sendRedirect(request.getContextPath() + "/orders");
        } catch (Exception e) {
            request.getSession().setAttribute("flashError", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void loadViewData(HttpServletRequest request, User user) {
        List<CartItem> cartItems = cartDAO.findItemsByUser(user.getId());
        List<Address> addresses = addressDAO.findByUser(user.getId());
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("addresses", addresses);

        pullFlashMessages(request);
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

    private int parseInt(String value) {
        try {
            return Integer.parseInt(clean(value));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

