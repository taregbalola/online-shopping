<%@ page import="java.util.List" %>
<%@ page import="com.tareg.onlineshopping.Product" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset='UTF-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1.0'>
    <title>Our Products - Online Shopping</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4; color: #333; }
        header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 1.5rem 0; text-align: center; }
        header h1 { font-size: 2.5rem; }
        nav { background-color: #333; padding: 1rem; text-align: center; }
        nav a { color: white; text-decoration: none; margin: 0 1rem; }
        nav a:hover { color: #667eea; }
        .container { max-width: 1200px; margin: 2rem auto; background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .hero { margin-bottom: 1.5rem; }
        .hero p { color: #666; margin-top: 0.5rem; }
        .message { padding: 1rem; border-radius: 6px; margin: 1rem 0; }
        .message.success { background: #e8f8ee; color: #1f7a3f; border: 1px solid #a7e1bc; }
        .message.error { background: #fdecea; color: #b42318; border: 1px solid #f5b7b1; }
        .layout { display: grid; grid-template-columns: 1fr 1.2fr; gap: 2rem; margin-top: 1.5rem; }
        .panel { border: 1px solid #e5e7eb; border-radius: 8px; padding: 1.25rem; background: #fafafa; }
        .panel h3 { color: #667eea; margin-bottom: 1rem; }
        .form-row { margin-bottom: 0.9rem; }
        .form-row label { display: block; margin-bottom: 0.35rem; font-weight: 600; }
        .form-row input, .form-row textarea, .form-row select {
            width: 100%; padding: 0.85rem; border: 1px solid #d1d5db; border-radius: 6px; font-size: 0.98rem; background: white;
        }
        .form-row textarea { min-height: 110px; resize: vertical; }
        .btn {
            display: inline-block; border: none; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white; padding: 0.85rem 1.2rem; border-radius: 6px; cursor: pointer; font-weight: 600;
        }
        .btn:hover { opacity: 0.95; }
        .count { color: #666; margin-bottom: 1rem; }
        .catalog { display: grid; gap: 1rem; }
        .product {
            border: 1px solid #ddd; padding: 1rem; border-radius: 8px; background: white; transition: all 0.3s;
        }
        .product:hover { box-shadow: 0 3px 10px rgba(0,0,0,0.08); transform: translateY(-2px); }
        .product h4 { color: #667eea; margin-bottom: 0.4rem; }
        .meta { margin-top: 0.5rem; color: #555; display: flex; gap: 1rem; flex-wrap: wrap; }
        .pill { background: #eef2ff; color: #4338ca; padding: 0.2rem 0.6rem; border-radius: 999px; font-size: 0.85rem; }
        footer { background-color: #333; color: white; text-align: center; padding: 2rem; margin-top: 3rem; }
        footer a { color: #667eea; text-decoration: none; }
        .small { font-size: 0.92rem; color: #666; }
        @media (max-width: 900px) { .layout { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
<%
    List<Product> products = (List<Product>) request.getAttribute("products");
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");

    String formName = (String) request.getAttribute("formName");
    String formDescription = (String) request.getAttribute("formDescription");
    String formCategory = (String) request.getAttribute("formCategory");
    String formPrice = (String) request.getAttribute("formPrice");
    String formStock = (String) request.getAttribute("formStock");

    formName = formName == null ? "" : formName;
    formDescription = formDescription == null ? "" : formDescription;
    formCategory = formCategory == null ? "Electronics" : formCategory;
    formPrice = formPrice == null ? "" : formPrice;
    formStock = formStock == null ? "" : formStock;
    int productCount = products == null ? 0 : products.size();
%>

<%!
    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
%>

    <header>
        <h1>🛍️ Our Products</h1>
    </header>
    <nav>
        <a href='index.jsp'>Home</a>
        <a href='products'>Products</a>
        <a href='about'>About Us</a>
        <a href='contact'>Contact</a>
        <a href='hello'>API Test</a>
    </nav>

    <div class='container'>
        <div class='hero'>
            <h2>Product Catalog</h2>
            <p>Manage products in real time using a servlet controller and JSP view. Add a new product, then refresh the catalog instantly.</p>
        </div>

        <% if (successMessage != null) { %>
            <div class='message success'><%= escapeHtml(successMessage) %></div>
        <% } %>
        <% if (errorMessage != null) { %>
            <div class='message error'><%= escapeHtml(errorMessage) %></div>
        <% } %>

        <div class='layout'>
            <div class='panel'>
                <h3>Add New Product</h3>
                <form method='post' action='products'>
                    <div class='form-row'>
                        <label for='name'>Product Name *</label>
                        <input type='text' id='name' name='name' value='<%= escapeHtml(formName) %>' placeholder='e.g. Bluetooth Speaker' required>
                    </div>
                    <div class='form-row'>
                        <label for='description'>Description</label>
                        <textarea id='description' name='description' placeholder='Short product description'><%= escapeHtml(formDescription) %></textarea>
                    </div>
                    <div class='form-row'>
                        <label for='category'>Category</label>
                        <select id='category' name='category'>
                            <option value='Electronics' <%= "Electronics".equals(formCategory) ? "selected" : "" %>>Electronics</option>
                            <option value='Accessories' <%= "Accessories".equals(formCategory) ? "selected" : "" %>>Accessories</option>
                            <option value='Wearables' <%= "Wearables".equals(formCategory) ? "selected" : "" %>>Wearables</option>
                            <option value='Home' <%= "Home".equals(formCategory) ? "selected" : "" %>>Home</option>
                            <option value='Fashion' <%= "Fashion".equals(formCategory) ? "selected" : "" %>>Fashion</option>
                            <option value='Other' <%= "Other".equals(formCategory) ? "selected" : "" %>>Other</option>
                        </select>
                    </div>
                    <div class='form-row'>
                        <label for='price'>Price *</label>
                        <input type='number' step='0.01' min='0' id='price' name='price' value='<%= escapeHtml(formPrice) %>' placeholder='0.00' required>
                    </div>
                    <div class='form-row'>
                        <label for='stock'>Stock *</label>
                        <input type='number' min='0' id='stock' name='stock' value='<%= escapeHtml(formStock) %>' placeholder='0' required>
                    </div>
                    <button type='submit' class='btn'>Add Product</button>
                    <p class='small' style='margin-top: 0.75rem;'>Fields marked with * are required.</p>
                </form>
            </div>

            <div class='panel'>
                <h3>Live Catalog</h3>
                <div class='count'>Total products: <strong><%= productCount %></strong></div>
                <div class='catalog'>
                    <% if (products != null && !products.isEmpty()) {
                        for (Product product : products) { %>
                            <div class='product'>
                                <h4><%= escapeHtml(product.getName()) %></h4>
                                <p><%= escapeHtml(product.getDescription()) %></p>
                                <div class='meta'>
                                    <span class='pill'>#<%= product.getId() %></span>
                                    <span class='pill'><%= escapeHtml(product.getCategory()) %></span>
                                    <span class='pill'>$<%= product.getPrice().toPlainString() %></span>
                                    <span class='pill'>Stock: <%= product.getStock() %></span>
                                </div>
                                <p class='small' style='margin-top: 0.5rem;'>Created: <%= product.getCreatedAt() %></p>
                            </div>
                    <%  }
                       } else { %>
                            <p>No products available yet.</p>
                    <% } %>
                </div>
            </div>
        </div>
    </div>

    <footer>
        <p>&copy; 2024 Online Shopping. All rights reserved.</p>
        <p><a href="#">Privacy Policy</a> | <a href="#">Terms of Service</a></p>
    </footer>
</body>
</html>

