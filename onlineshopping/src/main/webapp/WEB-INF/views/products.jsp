<%@ page import="java.util.List" %>
<%@ page import="com.tareg.onlineshopping.model.Product" %>
<%@ page import="com.tareg.onlineshopping.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset='UTF-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1.0'>
    <title>Products - Online Shopping</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4; color: #333; }
        header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 1rem 2rem; display: flex; align-items: center; justify-content: space-between; }
        header h1 { font-size: 1.8rem; }
        .user-info { display: flex; align-items: center; gap: 1rem; font-size: 0.95rem; }
        .user-info a { color: white; text-decoration: none; background: rgba(255,255,255,0.2); padding: 0.4rem 0.85rem; border-radius: 20px; }
        .user-info a:hover { background: rgba(255,255,255,0.35); }
        nav { background-color: #333; padding: 0.8rem; text-align: center; }
        nav a { color: white; text-decoration: none; margin: 0 1rem; }
        nav a:hover { color: #667eea; }
        .container { max-width: 1300px; margin: 2rem auto; padding: 0 1rem; }
        .page-title { margin-bottom: 1.5rem; }
        .page-title p { color: #666; margin-top: 0.4rem; }
        .message { padding: 1rem 1.2rem; border-radius: 6px; margin-bottom: 1.2rem; }
        .message.success { background: #e8f8ee; color: #1f7a3f; border: 1px solid #a7e1bc; }
        .message.error   { background: #fdecea; color: #b42318; border: 1px solid #f5b7b1; }
        .layout { display: grid; grid-template-columns: 380px 1fr; gap: 2rem; }
        .panel { border: 1px solid #e5e7eb; border-radius: 10px; padding: 1.5rem; background: white; align-self: start; }
        .panel h3 { color: #667eea; margin-bottom: 1.1rem; font-size: 1.15rem; border-bottom: 2px solid #eef2ff; padding-bottom: 0.5rem; }
        .form-row { margin-bottom: 1rem; }
        .form-row label { display: block; font-weight: 600; margin-bottom: 0.35rem; color: #555; }
        .form-row input, .form-row textarea, .form-row select {
            width: 100%; padding: 0.8rem; border: 1.5px solid #d1d5db; border-radius: 6px; font-size: 0.97rem;
        }
        .form-row input:focus, .form-row textarea:focus, .form-row select:focus { outline: none; border-color: #667eea; }
        .form-row textarea { min-height: 90px; resize: vertical; }
        .btn { border: none; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
               color: white; padding: 0.85rem 1.5rem; border-radius: 7px; cursor: pointer; font-weight: 600; font-size: 1rem; }
        .btn:hover { opacity: 0.92; }
        .btn-danger { border: none; background: #ef4444; color: white; padding: 0.4rem 0.9rem; border-radius: 5px; cursor: pointer; font-size: 0.88rem; }
        .btn-danger:hover { background: #dc2626; }
        .count-bar { display: flex; align-items: center; justify-content: space-between; margin-bottom: 1rem; }
        .count { color: #666; }
        .catalog { display: grid; gap: 1rem; }
        .product { border: 1px solid #e5e7eb; padding: 1.1rem; border-radius: 9px; background: white; transition: all 0.2s; display: flex; flex-direction: column; gap: 0.4rem; }
        .product:hover { box-shadow: 0 4px 14px rgba(0,0,0,0.08); transform: translateY(-1px); }
        .product-header { display: flex; justify-content: space-between; align-items: flex-start; gap: 1rem; }
        .product h4 { color: #3730a3; font-size: 1.05rem; }
        .product p { color: #555; font-size: 0.95rem; }
        .meta { display: flex; flex-wrap: wrap; gap: 0.5rem; margin-top: 0.4rem; }
        .pill { background: #eef2ff; color: #4338ca; padding: 0.2rem 0.65rem; border-radius: 999px; font-size: 0.82rem; font-weight: 500; }
        .pill.price { background: #f0fdf4; color: #15803d; }
        .pill.stock-low { background: #fef9c3; color: #854d0e; }
        .small { font-size: 0.87rem; color: #9ca3af; }
        .buy-form { margin-top: 0.6rem; padding-top: 0.6rem; border-top: 1px dashed #d1d5db; display: flex; gap: 0.45rem; align-items: center; }
        .buy-form input { padding: 0.5rem; border: 1px solid #d1d5db; border-radius: 6px; width: 90px; }
        .btn-buy { border: none; background: #059669; color: white; padding: 0.55rem 0.9rem; border-radius: 6px; cursor: pointer; }
        .btn-buy:hover { background: #047857; }
        footer { background-color: #333; color: white; text-align: center; padding: 1.5rem; margin-top: 3rem; }
        footer a { color: #667eea; text-decoration: none; }
        @media (max-width: 900px) { .layout { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
<%
    List<Product> products   = (List<Product>) request.getAttribute("products");
    String successMessage    = (String) request.getAttribute("successMessage");
    String errorMessage      = (String) request.getAttribute("errorMessage");
    User   currentUser       = (User) session.getAttribute("loggedInUser");

    String formName        = (String) request.getAttribute("formName");
    String formDescription = (String) request.getAttribute("formDescription");
    String formCategory    = (String) request.getAttribute("formCategory");
    String formPrice       = (String) request.getAttribute("formPrice");
    String formStock       = (String) request.getAttribute("formStock");

    formName        = formName == null ? "" : formName;
    formDescription = formDescription == null ? "" : formDescription;
    formCategory    = formCategory == null ? "Electronics" : formCategory;
    formPrice       = formPrice == null ? "" : formPrice;
    formStock       = formStock == null ? "" : formStock;
    int productCount = products == null ? 0 : products.size();
%>

<%!
    private String esc(String v) {
        if (v == null) return "";
        return v.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
                .replace("\"","&quot;").replace("'","&#39;");
    }
    private boolean sel(String current, String option) { return option.equals(current); }
%>

<header>
    <h1>🛍️ Online Shopping</h1>
    <div class="user-info">
        <span>👤 <%= esc(currentUser != null ? currentUser.getFullName() : "") %></span>
        <a href="logout">Logout</a>
    </div>
</header>
<nav>
    <a href="index.jsp">Home</a>
    <a href="products">Products</a>
    <a href="cart">Cart</a>
    <a href="orders">Orders</a>
    <% if (currentUser != null && currentUser.isAdmin()) { %><a href="reports">Reports</a><% } %>
    <a href="about">About Us</a>
    <a href="contact">Contact</a>
    <a href="hello">API</a>
</nav>

<div class="container">
    <div class="page-title">
        <h2>Product Catalog</h2>
        <p>Add, view, and delete products. All data is stored in MySQL via XAMPP.</p>
    </div>

    <% if (successMessage != null) { %><div class="message success"><%= esc(successMessage) %></div><% } %>
    <% if (errorMessage   != null) { %><div class="message error">  <%= esc(errorMessage)   %></div><% } %>

    <div class="layout">

        <%-- ADD PRODUCT FORM --%>
        <div class="panel">
            <h3>➕ Add New Product</h3>
            <form method="post" action="products">
                <div class="form-row">
                    <label>Product Name *</label>
                    <input type="text" name="name" value="<%= esc(formName) %>" placeholder="e.g. Bluetooth Speaker" required>
                </div>
                <div class="form-row">
                    <label>Description</label>
                    <textarea name="description" placeholder="Short product description"><%= esc(formDescription) %></textarea>
                </div>
                <div class="form-row">
                    <label>Category</label>
                    <select name="category">
                        <option value="Electronics" <%= sel(formCategory,"Electronics") ? "selected" : "" %>>Electronics</option>
                        <option value="Accessories" <%= sel(formCategory,"Accessories") ? "selected" : "" %>>Accessories</option>
                        <option value="Wearables"   <%= sel(formCategory,"Wearables")   ? "selected" : "" %>>Wearables</option>
                        <option value="Home"        <%= sel(formCategory,"Home")        ? "selected" : "" %>>Home</option>
                        <option value="Fashion"     <%= sel(formCategory,"Fashion")     ? "selected" : "" %>>Fashion</option>
                        <option value="Other"       <%= sel(formCategory,"Other")       ? "selected" : "" %>>Other</option>
                    </select>
                </div>
                <div class="form-row">
                    <label>Price (USD) *</label>
                    <input type="number" step="0.01" min="0" name="price" value="<%= esc(formPrice) %>" placeholder="0.00" required>
                </div>
                <div class="form-row">
                    <label>Stock *</label>
                    <input type="number" min="0" name="stock" value="<%= esc(formStock) %>" placeholder="0" required>
                </div>
                <button type="submit" class="btn">Add Product</button>
            </form>
        </div>

        <%-- LIVE CATALOG --%>
        <div class="panel">
            <div class="count-bar">
                <h3>📋 Live Catalog</h3>
                <span class="count"><strong><%= productCount %></strong> product(s)</span>
            </div>
            <div class="catalog">
                <% if (products != null && !products.isEmpty()) {
                     for (Product p : products) {
                       boolean lowStock = p.getStock() <= 10;
                %>
                    <div class="product">
                        <div class="product-header">
                            <h4><%= esc(p.getName()) %></h4>
                            <form method="post" action="products" onsubmit="return confirm('Delete this product?')">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%= p.getId() %>">
                                <button type="submit" class="btn-danger">Delete</button>
                            </form>
                        </div>
                        <p><%= esc(p.getDescription()) %></p>
                        <div class="meta">
                            <span class="pill">#<%= p.getId() %></span>
                            <span class="pill"><%= esc(p.getCategory()) %></span>
                            <span class="pill price">$<%= p.getPrice().toPlainString() %></span>
                            <span class="pill <%= lowStock ? "stock-low" : "" %>">
                                Stock: <%= p.getStock() %><%= lowStock ? " ⚠" : "" %>
                            </span>
                        </div>
                        <p class="small">Created: <%= p.getCreatedAt() %></p>
                        <form method="post" action="cart" class="buy-form">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="<%= p.getId() %>">
                            <input type="number" name="quantity" min="1" max="50" value="1" required>
                            <button type="submit" class="btn-buy">Add to Cart</button>
                        </form>
                    </div>
                <% } } else { %>
                    <p style="color:#888;">No products found. Add one using the form.</p>
                <% } %>
            </div>
        </div>

    </div>
</div>

<footer>
    <p>&copy; 2024 Online Shopping. All rights reserved. | <a href="#">Privacy Policy</a></p>
</footer>
</body>
</html>

