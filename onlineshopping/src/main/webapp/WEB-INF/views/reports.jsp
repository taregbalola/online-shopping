<%@ page import="java.util.List" %>
<%@ page import="com.tareg.onlineshopping.model.ReportSummary" %>
<%@ page import="com.tareg.onlineshopping.model.DailySalesStat" %>
<%@ page import="com.tareg.onlineshopping.model.TopProductStat" %>
<%@ page import="com.tareg.onlineshopping.model.OrderStatusStat" %>
<%@ page import="com.tareg.onlineshopping.model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reports - Online Shopping</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f3f4f6; color: #1f2937; }
        header { background: linear-gradient(135deg, #312e81 0%, #4c1d95 100%); color: white; padding: 1rem 2rem; }
        nav { background: #111827; padding: .8rem 1rem; text-align: center; }
        nav a { color: #f9fafb; margin: 0 .7rem; text-decoration: none; }
        .container { max-width: 1200px; margin: 1.5rem auto; padding: 0 1rem; }
        .kpis { display: grid; grid-template-columns: repeat(3, 1fr); gap: .8rem; margin-bottom: 1rem; }
        .kpi { background: white; border: 1px solid #e5e7eb; border-radius: 10px; padding: 1rem; }
        .kpi h4 { color: #6366f1; margin-bottom: .45rem; }
        .panel { background: white; border: 1px solid #e5e7eb; border-radius: 10px; padding: 1rem; margin-bottom: 1rem; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: .55rem; border-bottom: 1px solid #e5e7eb; text-align: left; }
        th { background: #eef2ff; color: #3730a3; }
        @media (max-width: 900px) { .kpis { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
<%
    User currentUser = (User) session.getAttribute("loggedInUser");
    ReportSummary summary = (ReportSummary) request.getAttribute("summary");
    List<DailySalesStat> dailySales = (List<DailySalesStat>) request.getAttribute("dailySales");
    List<TopProductStat> topProducts = (List<TopProductStat>) request.getAttribute("topProducts");
    List<OrderStatusStat> statusStats = (List<OrderStatusStat>) request.getAttribute("statusStats");
%>
<header>
    <h2>Admin Invoice & Reports Dashboard</h2>
    <p>Daily sales, top products, and order status analytics</p>
</header>
<nav>
    <a href="products">Products</a>
    <a href="cart">Cart</a>
    <a href="orders?scope=all">All Orders</a>
    <a href="reports">Reports</a>
    <a href="logout">Logout</a>
</nav>
<div class="container">
    <div class="kpis">
        <div class="kpi"><h4>Total Orders</h4><div><strong><%= summary != null ? summary.getTotalOrders() : 0 %></strong></div></div>
        <div class="kpi"><h4>Total Revenue</h4><div><strong>$<%= summary != null ? summary.getTotalRevenue().toPlainString() : "0.00" %></strong></div></div>
        <div class="kpi"><h4>Pending Orders</h4><div><strong><%= summary != null ? summary.getPendingOrders() : 0 %></strong></div></div>
    </div>

    <div class="panel">
        <h3 style="margin-bottom:.6rem;">Daily Sales (Last 14 Days)</h3>
        <table>
            <tr><th>Date</th><th>Orders</th><th>Revenue</th></tr>
            <% if (dailySales != null) for (DailySalesStat d : dailySales) { %>
                <tr><td><%= d.getSalesDate() %></td><td><%= d.getOrdersCount() %></td><td>$<%= d.getRevenue().toPlainString() %></td></tr>
            <% } %>
        </table>
    </div>

    <div class="panel">
        <h3 style="margin-bottom:.6rem;">Top Products</h3>
        <table>
            <tr><th>Product</th><th>Quantity Sold</th><th>Revenue</th></tr>
            <% if (topProducts != null) for (TopProductStat t : topProducts) { %>
                <tr><td><%= t.getProductName() %></td><td><%= t.getQuantitySold() %></td><td>$<%= t.getRevenue().toPlainString() %></td></tr>
            <% } %>
        </table>
    </div>

    <div class="panel">
        <h3 style="margin-bottom:.6rem;">Order Status Breakdown</h3>
        <table>
            <tr><th>Status</th><th>Total</th></tr>
            <% if (statusStats != null) for (OrderStatusStat s : statusStats) { %>
                <tr><td><%= s.getStatus() %></td><td><%= s.getTotal() %></td></tr>
            <% } %>
        </table>
    </div>
</div>
</body>
</html>

