<%@ page import="java.util.List" %>
<%@ page import="com.tareg.onlineshopping.model.Order" %>
<%@ page import="com.tareg.onlineshopping.model.OrderItem" %>
<%@ page import="com.tareg.onlineshopping.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Orders - Online Shopping</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f3f4f6; color: #1f2937; }
        header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; }
        .user-info a { color: white; text-decoration: none; background: rgba(255,255,255,.25); padding: .45rem .9rem; border-radius: 18px; }
        nav { background: #111827; padding: .8rem 1rem; text-align: center; }
        nav a { color: #f9fafb; margin: 0 .7rem; text-decoration: none; }
        nav a:hover { color: #a5b4fc; }
        .container { max-width: 1200px; margin: 1.5rem auto; padding: 0 1rem; }
        .message { margin-bottom: 1rem; padding: .9rem 1rem; border-radius: 8px; }
        .success { background: #dcfce7; color: #166534; border: 1px solid #86efac; }
        .error { background: #fee2e2; color: #991b1b; border: 1px solid #fca5a5; }
        .card { background: white; border: 1px solid #e5e7eb; border-radius: 10px; margin-bottom: 1rem; padding: 1rem; }
        .row { display: flex; justify-content: space-between; gap: 1rem; flex-wrap: wrap; }
        .meta { color: #6b7280; font-size: .93rem; }
        .pill { display: inline-block; padding: .2rem .6rem; border-radius: 999px; font-size: .83rem; font-weight: 600; background: #e0e7ff; color: #3730a3; }
        .items { margin-top: .8rem; border-top: 1px dashed #d1d5db; padding-top: .8rem; }
        .item { display: flex; justify-content: space-between; padding: .35rem 0; }
        .admin-actions { margin-top: .8rem; }
        .admin-actions select, .admin-actions button { padding: .45rem; border-radius: 6px; border: 1px solid #d1d5db; }
        .admin-actions button { background: #4f46e5; color: white; border: none; cursor: pointer; }
    </style>
</head>
<body>
<%
    User currentUser = (User) session.getAttribute("loggedInUser");
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    String success = (String) request.getAttribute("successMessage");
    String error = (String) request.getAttribute("errorMessage");
    Boolean showAll = (Boolean) request.getAttribute("showAll");
    if (showAll == null) showAll = false;
%>
<%!
    private String esc(String v) {
        if (v == null) return "";
        return v.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
%>
<header>
    <h2>Order Center</h2>
    <div class="user-info">
        <span><%= esc(currentUser != null ? currentUser.getFullName() : "") %></span>
        <a href="logout">Logout</a>
    </div>
</header>
<nav>
    <a href="products">Products</a>
    <a href="cart">Cart</a>
    <a href="orders">My Orders</a>
    <% if (currentUser != null && currentUser.isAdmin()) { %>
        <a href="orders?scope=all">All Orders</a>
        <a href="reports">Reports</a>
    <% } %>
</nav>
<div class="container">
    <% if (success != null) { %><div class="message success"><%= esc(success) %></div><% } %>
    <% if (error != null) { %><div class="message error"><%= esc(error) %></div><% } %>

    <h3 style="margin-bottom:1rem;"><%= showAll ? "All Customer Orders" : "My Orders" %></h3>

    <% if (orders == null || orders.isEmpty()) { %>
        <div class="card">No orders yet. Go to <a href="products">products</a> and place your first order.</div>
    <% } else { %>
        <% for (Order o : orders) { %>
            <div class="card">
                <div class="row">
                    <div>
                        <strong>Order #<%= o.getId() %></strong>
                        <div class="meta">Customer: <%= esc(o.getCustomerName()) %> | Created: <%= o.getCreatedAt() %></div>
                        <div class="meta">Ship to: <%= esc(o.getShippingAddress()) %></div>
                    </div>
                    <div style="text-align:right;">
                        <div><span class="pill"><%= esc(o.getStatus()) %></span></div>
                        <div class="meta">Payment: <%= esc(o.getPaymentMethod()) %> / <%= esc(o.getPaymentStatus()) %></div>
                        <div style="margin-top:.4rem;"><strong>$<%= o.getTotalAmount().toPlainString() %></strong></div>
                        <div style="margin-top:.45rem;">
                            <a href="invoice?orderId=<%= o.getId() %>" style="font-size:.9rem;">Download Invoice PDF</a>
                        </div>
                    </div>
                </div>

                <div class="items">
                    <% for (OrderItem item : o.getItems()) { %>
                        <div class="item">
                            <span><%= esc(item.getProductName()) %> x <%= item.getQuantity() %></span>
                            <span>$<%= item.getLineTotal().toPlainString() %></span>
                        </div>
                    <% } %>
                </div>

                <% if (currentUser != null && currentUser.isAdmin() && showAll) { %>
                    <div class="admin-actions">
                        <form method="post" action="orders">
                            <input type="hidden" name="action" value="updateStatus">
                            <input type="hidden" name="orderId" value="<%= o.getId() %>">
                            <select name="status">
                                <option value="PENDING">PENDING</option>
                                <option value="PROCESSING">PROCESSING</option>
                                <option value="SHIPPED">SHIPPED</option>
                                <option value="DELIVERED">DELIVERED</option>
                                <option value="CANCELLED">CANCELLED</option>
                            </select>
                            <button type="submit">Update Status</button>
                        </form>
                    </div>
                <% } %>
            </div>
        <% } %>
    <% } %>
</div>
</body>
</html>

