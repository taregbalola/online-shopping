<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Online Shopping - Home</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
            color: #333;
            background-color: #f4f4f4;
        }
        header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 1.5rem 0;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        header h1 {
            text-align: center;
            font-size: 2.5rem;
            margin-bottom: 0.5rem;
        }
        header p {
            text-align: center;
            font-size: 1.1rem;
            opacity: 0.9;
        }
        nav {
            background-color: #333;
            padding: 1rem 0;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        nav ul {
            list-style: none;
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
        }
        nav li {
            margin: 0 1.5rem;
        }
        nav a {
            color: white;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.3s;
        }
        nav a:hover {
            color: #667eea;
        }
        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        .content {
            padding: 3rem 2rem;
            text-align: center;
            min-height: 400px;
        }
        .content h2 {
            color: #667eea;
            margin-bottom: 1rem;
            font-size: 2rem;
        }
        .content p {
            font-size: 1.1rem;
            margin-bottom: 1.5rem;
            color: #666;
        }
        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 2rem;
            margin-top: 2rem;
        }
        .product-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 1.5rem;
            border-radius: 8px;
            text-align: center;
            transition: transform 0.3s, box-shadow 0.3s;
        }
        .product-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 20px rgba(0,0,0,0.2);
        }
        .product-card h3 {
            margin-bottom: 0.5rem;
        }
        footer {
            background-color: #333;
            color: white;
            text-align: center;
            padding: 2rem 0;
            margin-top: 3rem;
        }
        footer p {
            margin-bottom: 0.5rem;
        }
        footer a {
            color: #667eea;
            text-decoration: none;
        }
        footer a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <!-- HEADER -->
    <header>
        <h1>🛍️ Online Shopping</h1>
        <p>Welcome to our amazing shopping experience</p>
    </header>

    <!-- NAVIGATION -->
    <nav>
        <ul>
            <li><a href="index.jsp">Home</a></li>
            <li><a href="products">Products</a></li>
            <li><a href="about">About Us</a></li>
            <li><a href="contact">Contact</a></li>
            <li><a href="hello">Hello API</a></li>
        </ul>
    </nav>

    <!-- MAIN CONTENT -->
    <div class="container">
        <div class="content">
            <h2>Welcome to Online Shopping</h2>
            <p>Discover the best products at unbeatable prices!</p>
            <p>Browse our collection and find what you're looking for.</p>

            <div class="product-grid">
                <div class="product-card">
                    <h3>👕 Electronics</h3>
                    <p>Latest gadgets and devices</p>
                </div>
                <div class="product-card">
                    <h3>👔 Fashion</h3>
                    <p>Trendy clothing and accessories</p>
                </div>
                <div class="product-card">
                    <h3>📚 Books</h3>
                    <p>Wide selection of books</p>
                </div>
                <div class="product-card">
                    <h3>🏠 Home</h3>
                    <p>Everything for your home</p>
                </div>
            </div>
        </div>
    </div>

    <!-- FOOTER -->
    <footer>
        <p>&copy; 2024 Online Shopping. All rights reserved.</p>
        <p>
            <a href="#">Privacy Policy</a> |
            <a href="#">Terms of Service</a> |
            <a href="#">Contact Us</a>
        </p>
        <p>Built with Java Servlets | Running on Embedded Tomcat</p>
    </footer>
</body>
</html>
