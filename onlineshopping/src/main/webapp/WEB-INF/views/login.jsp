<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Online Shopping</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .card {
            background: white;
            border-radius: 12px;
            padding: 2.5rem;
            width: 100%;
            max-width: 420px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
        }
        .logo { text-align: center; margin-bottom: 1.5rem; }
        .logo h1 { font-size: 2rem; color: #667eea; }
        .logo p { color: #888; font-size: 0.95rem; margin-top: 0.3rem; }
        h2 { text-align: center; color: #333; margin-bottom: 1.5rem; }
        .form-row { margin-bottom: 1.1rem; }
        .form-row label { display: block; font-weight: 600; margin-bottom: 0.4rem; color: #444; }
        .form-row input {
            width: 100%; padding: 0.85rem 1rem;
            border: 1.5px solid #d1d5db; border-radius: 7px;
            font-size: 1rem; transition: border-color 0.2s;
        }
        .form-row input:focus { outline: none; border-color: #667eea; }
        .btn {
            width: 100%; padding: 0.9rem;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white; border: none; border-radius: 7px;
            font-size: 1.05rem; font-weight: 600; cursor: pointer;
            margin-top: 0.5rem;
        }
        .btn:hover { opacity: 0.92; }
        .message { padding: 0.85rem 1rem; border-radius: 6px; margin-bottom: 1rem; font-size: 0.95rem; }
        .message.error { background: #fdecea; color: #b42318; border: 1px solid #f5b7b1; }
        .message.success { background: #e8f8ee; color: #1f7a3f; border: 1px solid #a7e1bc; }
        .footer-text { text-align: center; margin-top: 1.2rem; color: #888; font-size: 0.9rem; }
    </style>
</head>
<body>
<%
    String errorMessage = (String) request.getAttribute("errorMessage");
    String formUsername = (String) request.getAttribute("formUsername");
    if (formUsername == null) formUsername = "";
%>

    <div class="card">
        <div class="logo">
            <h1>🛍️ OnlineShop</h1>
            <p>Enterprise Management Portal</p>
        </div>
        <h2>Sign In</h2>

        <% if (errorMessage != null) { %>
            <div class="message error"><%= errorMessage %></div>
        <% } %>

        <form method="post" action="login">
            <div class="form-row">
                <label for="username">Username</label>
                <input type="text" id="username" name="username"
                       value="<%= formUsername.replace("\"", "&quot;") %>"
                       placeholder="Enter your username" autofocus>
            </div>
            <div class="form-row">
                <label for="password">Password</label>
                <input type="password" id="password" name="password"
                       placeholder="Enter your password">
            </div>
            <button type="submit" class="btn">Login</button>
        </form>

        <p class="footer-text">
            Online Shopping &copy; 2024 &mdash; All rights reserved
        </p>
    </div>
</body>
</html>

