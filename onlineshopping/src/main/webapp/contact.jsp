<!DOCTYPE html>
<html>
<head>
    <meta charset='UTF-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1.0'>
    <title>Contact Us - Online Shopping</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4; }
        header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 1.5rem 0; text-align: center; }
        header h1 { font-size: 2.5rem; }
        nav { background-color: #333; padding: 1rem; }
        nav a { color: white; text-decoration: none; margin: 0 1rem; }
        nav a:hover { color: #667eea; }
        .container { max-width: 1200px; margin: 2rem auto; background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .contact-info { display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 2rem; margin-top: 2rem; }
        .contact-box { padding: 1.5rem; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border-radius: 8px; transition: all 0.3s; }
        .contact-box:hover { transform: translateY(-5px); box-shadow: 0 5px 20px rgba(0,0,0,0.2); }
        .contact-box h3 { margin-bottom: 0.5rem; font-size: 1.3rem; }
        .contact-box p { margin-bottom: 0.3rem; }
        footer { background-color: #333; color: white; text-align: center; padding: 2rem; margin-top: 3rem; }
        footer a { color: #667eea; text-decoration: none; }
    </style>
</head>
<body>
    <header>
        <h1>📧 Contact Us</h1>
    </header>
    <nav>
        <a href='index.jsp'>Home</a>
        <a href='products'>Products</a>
        <a href='about'>About Us</a>
        <a href='contact'>Contact</a>
        <a href='hello'>API Test</a>
    </nav>
    <div class='container'>
        <h2>Get in Touch</h2>
        <p style='font-size: 1.1rem; margin-bottom: 1.5rem;'>We'd love to hear from you. Feel free to reach out using any of the contact methods below. Our team is here to help!</p>

        <div class='contact-info'>
            <div class='contact-box'>
                <h3>📞 Customer Support</h3>
                <p><strong>Phone:</strong></p>
                <p>1-800-SHOP-NOW</p>
                <p>(1-800-746-7669)</p>
                <p style='margin-top: 0.5rem; font-size: 0.9rem;'>Available 24/7</p>
            </div>

            <div class='contact-box'>
                <h3>✉️ Email Support</h3>
                <p><strong>Email:</strong></p>
                <p>support@onlineshopping.com</p>
                <p>sales@onlineshopping.com</p>
                <p style='margin-top: 0.5rem; font-size: 0.9rem;'>Response within 24 hours</p>
            </div>

            <div class='contact-box'>
                <h3>🏢 Headquarters</h3>
                <p><strong>Address:</strong></p>
                <p>123 Shopping Street</p>
                <p>New York, NY 10001</p>
                <p style='margin-top: 0.5rem; font-size: 0.9rem;'>United States</p>
            </div>

            <div class='contact-box'>
                <h3>🕐 Business Hours</h3>
                <p><strong>Monday - Friday:</strong> 9 AM - 6 PM EST</p>
                <p><strong>Saturday:</strong> 10 AM - 4 PM EST</p>
                <p><strong>Sunday:</strong> Closed</p>
            </div>

            <div class='contact-box'>
                <h3>💬 Live Chat</h3>
                <p><strong>Chat Support:</strong></p>
                <p>Available on website</p>
                <p style='margin-top: 0.5rem; font-size: 0.9rem;'>Monday - Friday, 9 AM - 9 PM</p>
            </div>

            <div class='contact-box'>
                <h3>📱 Follow Us</h3>
                <p><strong>Social Media:</strong></p>
                <p>Facebook | Twitter | Instagram</p>
                <p style='margin-top: 0.5rem; font-size: 0.9rem;'>@OnlineShopping</p>
            </div>
        </div>
    </div>
    <footer>
        <p>&copy; 2024 Online Shopping. All rights reserved.</p>
        <p><a href="#">Privacy Policy</a> | <a href="#">Terms of Service</a></p>
    </footer>
</body>
</html>

