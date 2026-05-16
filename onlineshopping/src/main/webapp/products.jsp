<!DOCTYPE html>
<html>
<head>
    <meta charset='UTF-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1.0'>
    <title>Our Products - Online Shopping</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4; }
        header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 1.5rem 0; text-align: center; }
        header h1 { font-size: 2.5rem; }
        nav { background-color: #333; padding: 1rem; }
        nav a { color: white; text-decoration: none; margin: 0 1rem; }
        nav a:hover { color: #667eea; }
        .container { max-width: 1200px; margin: 2rem auto; background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .product { border: 1px solid #ddd; padding: 1rem; margin: 1rem 0; border-radius: 5px; transition: all 0.3s; }
        .product:hover { box-shadow: 0 3px 10px rgba(0,0,0,0.1); transform: translateY(-2px); }
        .product h3 { color: #667eea; margin-bottom: 0.5rem; }
        footer { background-color: #333; color: white; text-align: center; padding: 2rem; margin-top: 3rem; }
        footer a { color: #667eea; text-decoration: none; }
    </style>
</head>
<body>
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
        <h2>Featured Products</h2>
        <p>Browse our latest collection of premium products</p>
        <div class='product'>
            <h3>📱 Smartphone - Latest Model</h3>
            <p>High-end smartphone with advanced features and 5G support</p>
            <p><strong>Price: $999</strong> | <em>Stock: 45 units</em></p>
        </div>
        <div class='product'>
            <h3>💻 Laptop - Professional Grade</h3>
            <p>High performance computing device for professionals and gamers</p>
            <p><strong>Price: $1,299</strong> | <em>Stock: 28 units</em></p>
        </div>
        <div class='product'>
            <h3>🎧 Wireless Headphones - Premium Sound</h3>
            <p>Premium sound quality with active noise cancellation technology</p>
            <p><strong>Price: $249</strong> | <em>Stock: 156 units</em></p>
        </div>
        <div class='product'>
            <h3>⌚ Smart Watch - Fitness Tracker</h3>
            <p>Track your fitness metrics and stay connected with notifications</p>
            <p><strong>Price: $399</strong> | <em>Stock: 92 units</em></p>
        </div>
        <div class='product'>
            <h3>📷 Digital Camera - 4K Video</h3>
            <p>Professional-grade camera with 4K video recording capability</p>
            <p><strong>Price: $799</strong> | <em>Stock: 35 units</em></p>
        </div>
        <div class='product'>
            <h3>🎮 Gaming Console - Next Gen</h3>
            <p>Latest generation gaming console with exclusive titles</p>
            <p><strong>Price: $499</strong> | <em>Stock: 67 units</em></p>
        </div>
    </div>
    <footer>
        <p>&copy; 2024 Online Shopping. All rights reserved.</p>
        <p><a href="#">Privacy Policy</a> | <a href="#">Terms of Service</a></p>
    </footer>
</body>
</html>

