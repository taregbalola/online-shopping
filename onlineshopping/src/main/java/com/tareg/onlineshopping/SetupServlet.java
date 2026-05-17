package com.tareg.onlineshopping;

import com.tareg.onlineshopping.dao.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * SetupServlet - ONE-TIME setup endpoint.
 * Visit /setup once to create the default admin account.
 * Username: admin  /  Password: admin123
 */
public class SetupServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        out.println("<title>Setup - Online Shopping</title>");
        out.println("<style>body{font-family:Segoe UI,sans-serif;background:#f4f4f4;display:flex;align-items:center;justify-content:center;min-height:100vh;margin:0}");
        out.println(".card{background:white;border-radius:12px;padding:2.5rem;max-width:450px;box-shadow:0 4px 20px rgba(0,0,0,.15);text-align:center}");
        out.println("h2{color:#667eea;margin-bottom:1rem}.msg{padding:1rem;border-radius:6px;margin:1rem 0}");
        out.println(".success{background:#e8f8ee;color:#1f7a3f;border:1px solid #a7e1bc}");
        out.println(".error{background:#fdecea;color:#b42318;border:1px solid #f5b7b1}");
        out.println(".info{background:#eef2ff;color:#3730a3;border:1px solid #c7d2fe}");
        out.println("a{display:inline-block;margin-top:1rem;background:linear-gradient(135deg,#667eea,#764ba2);color:white;padding:.7rem 1.5rem;border-radius:7px;text-decoration:none;font-weight:600}</style></head><body>");
        out.println("<div class='card'><h2>🛠️ App Setup</h2>");

        boolean created = userDAO.register("admin", "admin123", "Admin User", "ADMIN");
        if (created) {
            out.println("<div class='msg success'>✅ Admin account created successfully!</div>");
            out.println("<div class='msg info'><strong>Username:</strong> admin<br><strong>Password:</strong> admin123</div>");
            out.println("<p style='color:#666;font-size:.9rem;margin-top:1rem'>⚠️ Please change the password after first login.</p>");
        } else {
            out.println("<div class='msg error'>⚠️ Admin user already exists or setup was already run.</div>");
        }

        out.println("<a href='" + request.getContextPath() + "/login'>Go to Login →</a>");
        out.println("</div></body></html>");
    }
}

