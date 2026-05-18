<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.tareg.onlineshopping.model.CartItem" %>
<%@ page import="com.tareg.onlineshopping.model.Address" %>
<%@ page import="com.tareg.onlineshopping.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cart - Online Shopping</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f3f4f6; color: #1f2937; }
        header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 1rem 2rem; display: flex; justify-content: space-between; }
        nav { background: #111827; padding: .8rem 1rem; text-align: center; }
        nav a { color: #f9fafb; margin: 0 .7rem; text-decoration: none; }
        nav a:hover { color: #a5b4fc; }
        .container { max-width: 1280px; margin: 1.5rem auto; padding: 0 1rem; }
        .message { margin-bottom: 1rem; padding: .9rem 1rem; border-radius: 8px; }
        .success { background: #dcfce7; color: #166534; border: 1px solid #86efac; }
        .error { background: #fee2e2; color: #991b1b; border: 1px solid #fca5a5; }
        .grid { display: grid; grid-template-columns: 1.35fr 1fr; gap: 1rem; }
        .panel { background: white; border: 1px solid #e5e7eb; border-radius: 10px; padding: 1rem; }
        .panel h3 { margin-bottom: .8rem; color: #4f46e5; }
        .item { border-bottom: 1px dashed #d1d5db; padding: .75rem 0; }
        .item:last-child { border-bottom: none; }
        .row { display: flex; justify-content: space-between; align-items: center; gap: .8rem; flex-wrap: wrap; }
        .muted { color: #6b7280; font-size: .92rem; }
        input, select, button, textarea { border: 1px solid #d1d5db; border-radius: 6px; padding: .45rem; }
        button { cursor: pointer; }
        .btn { background: #4f46e5; color: white; border: none; }
        .btn-danger { background: #dc2626; color: white; border: none; }
        .address { border: 1px solid #e5e7eb; border-radius: 8px; padding: .65rem; margin-bottom: .5rem; }
        .default { color: #166534; font-size: .85rem; font-weight: 600; }
        .summary { margin-top: .8rem; font-size: 1.05rem; }
        .checkout { margin-top: .8rem; padding-top: .8rem; border-top: 1px dashed #d1d5db; }
        .field { margin-bottom: .5rem; }
        .field input { width: 100%; }
        @media (max-width: 980px) { .grid { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
<%
    User currentUser = (User) session.getAttribute("loggedInUser");
    List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");
    List<Address> addresses = (List<Address>) request.getAttribute("addresses");
    String success = (String) request.getAttribute("successMessage");
    String error = (String) request.getAttribute("errorMessage");
    BigDecimal total = BigDecimal.ZERO;
    if (cartItems != null) {
        for (CartItem i : cartItems) total = total.add(i.getLineTotal());
    }
%>
<%!
    private String esc(String v) {
        if (v == null) return "";
        return v.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
%>
<header>
    <h2>Shopping Cart</h2>
    <div><%= esc(currentUser != null ? currentUser.getFullName() : "") %> | <a href="logout" style="color:white;">Logout</a></div>
</header>
<nav>
    <a href="products">Products</a>
    <a href="cart">Cart</a>
    <a href="orders">Orders</a>
    <% if (currentUser != null && currentUser.isAdmin()) { %><a href="reports">Reports</a><% } %>
</nav>
<div class="container">
    <% if (success != null) { %><div class="message success"><%= esc(success) %></div><% } %>
    <% if (error != null) { %><div class="message error"><%= esc(error) %></div><% } %>

    <div class="grid">
        <div class="panel">
            <h3>Cart Items</h3>
            <% if (cartItems == null || cartItems.isEmpty()) { %>
                <p class="muted">Your cart is empty. Add items from <a href="products">products</a>.</p>
            <% } else { %>
                <% for (CartItem item : cartItems) { %>
                    <div class="item">
                        <div class="row">
                            <div>
                                <strong><%= esc(item.getProductName()) %></strong>
                                <div class="muted">$<%= item.getUnitPrice().toPlainString() %> | Stock: <%= item.getAvailableStock() %></div>
                            </div>
                            <div>$<%= item.getLineTotal().toPlainString() %></div>
                        </div>
                        <div class="row" style="margin-top:.45rem;">
                            <form method="post" action="cart">
                                <input type="hidden" name="action" value="updateQty">
                                <input type="hidden" name="itemId" value="<%= item.getId() %>">
                                <input type="number" name="quantity" min="1" max="999" value="<%= item.getQuantity() %>">
                                <button class="btn" type="submit">Update</button>
                            </form>
                            <form method="post" action="cart" onsubmit="return confirm('Remove this item?')">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="itemId" value="<%= item.getId() %>">
                                <button class="btn-danger" type="submit">Remove</button>
                            </form>
                        </div>
                    </div>
                <% } %>
                <div class="summary"><strong>Total: $<%= total.toPlainString() %></strong></div>
            <% } %>
        </div>

        <div class="panel">
            <h3>Address Book</h3>
            <% if (addresses != null && !addresses.isEmpty()) { %>
                <% for (Address a : addresses) { %>
                    <div class="address">
                        <div class="row">
                            <strong><%= esc(a.getLabel()) %></strong>
                            <% if (a.isDefault()) { %><span class="default">DEFAULT</span><% } %>
                        </div>
                        <div class="muted"><%= esc(a.asShippingText()) %></div>
                        <div class="row" style="margin-top:.4rem;">
                            <% if (!a.isDefault()) { %>
                            <form method="post" action="cart">
                                <input type="hidden" name="action" value="setDefaultAddress">
                                <input type="hidden" name="addressId" value="<%= a.getId() %>">
                                <button class="btn" type="submit">Set Default</button>
                            </form>
                            <% } %>
                            <form method="post" action="cart" onsubmit="return confirm('Delete this address?')">
                                <input type="hidden" name="action" value="deleteAddress">
                                <input type="hidden" name="addressId" value="<%= a.getId() %>">
                                <button class="btn-danger" type="submit">Delete</button>
                            </form>
                        </div>
                    </div>
                <% } %>
            <% } else { %>
                <p class="muted">No addresses yet. Add one below.</p>
            <% } %>

            <div class="checkout">
                <h3 style="margin-top:.3rem;">Add Address</h3>
                <form method="post" action="cart">
                    <input type="hidden" name="action" value="addAddress">
                    <div class="field"><input type="text" name="label" placeholder="Label (Home, Office)"></div>
                    <div class="field"><input type="text" name="recipientName" placeholder="Recipient name" required></div>
                    <div class="field"><input type="text" name="phone" placeholder="Phone" required></div>
                    <div class="field"><input type="text" name="line1" placeholder="Address line 1" required></div>
                    <div class="field"><input type="text" name="line2" placeholder="Address line 2"></div>
                    <div class="field"><input type="text" name="city" placeholder="City" required></div>
                    <div class="field"><input type="text" name="state" placeholder="State" required></div>
                    <div class="field"><input type="text" name="postalCode" placeholder="Postal code" required></div>
                    <div class="field"><input type="text" name="country" placeholder="Country" required></div>
                    <label><input type="checkbox" name="isDefault"> Set as default</label>
                    <div style="margin-top:.5rem;"><button class="btn" type="submit">Save Address</button></div>
                </form>
            </div>

            <div class="checkout">
                <h3>Checkout</h3>
                <form method="post" action="cart">
                    <input type="hidden" name="action" value="checkout">
                    <div class="field">
                        <select name="addressId" required>
                            <option value="">Select address</option>
                            <% if (addresses != null) for (Address a : addresses) { %>
                                <option value="<%= a.getId() %>"><%= esc(a.getLabel()) %> - <%= esc(a.getCity()) %> <% if (a.isDefault()) { %>(Default)<% } %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="field">
                        <select name="paymentMethod" required>
                            <option value="">Select payment method</option>
                            <option value="CASH_ON_DELIVERY">Cash on Delivery</option>
                            <option value="CARD">Card</option>
                        </select>
                    </div>
                    <button class="btn" type="submit">Place Multi-Item Order</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>

