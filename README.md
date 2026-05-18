# Online Shopping - Servlet Web Application

No Tomcat installation needed! Built-in embedded server with Maven.

---

## How to Run (Step-by-Step)

### Step 1: Open Command Prompt or PowerShell
Press `Win + R`, type `cmd` or `powershell`, press Enter.

### Step 2: Navigate to the project folder
```
D:
cd D:\servlet-app\online-shopping\onlineshopping
```

### Step 3: Build the app
```
mvn clean package
```
Wait for "BUILD SUCCESS" message.

### Step 4: Start the server
```
mvn tomcat7:run
```
Wait for the startup message, then open the browser.

### Step 5: Open the app
- **Home:** http://localhost:8080/onlineshopping/
- **Products page:** http://localhost:8080/onlineshopping/products
- **API test:** http://localhost:8080/onlineshopping/hello

### Step 6: Use the real-time products form
On `/products`, you can add a new product using POST. The catalog refreshes immediately.

### Step 7: Stop the server
In the command prompt window, press **`Ctrl + C`**

---

## Full Command Example (Copy & Paste)
```
D:
cd D:\servlet-app\online-shopping\onlineshopping
mvn clean package
mvn tomcat7:run
```

Then open browser to:
- `http://localhost:8080/onlineshopping/products`
- `http://localhost:8080/onlineshopping/hello`

---

## Run on your local Tomcat 9 service at port 9090

If you want to use the installed Tomcat service (`Tomcat9`) instead of Maven embedded Tomcat:

### Step 1: Build the WAR
```powershell
cd D:\servlet-app\online-shopping\onlineshopping
mvn clean package
```

### Step 2: Copy the WAR to Tomcat 9
```powershell
Copy-Item .\target\onlineshopping.war "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\" -Force
```

### Step 3: Remove old exploded deployment if it exists
```powershell
Remove-Item "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\onlineshopping" -Recurse -Force -ErrorAction SilentlyContinue
```

### Step 4: Restart Tomcat 9
```powershell
Restart-Service Tomcat9
```

### Step 5: Open the app
- Home: `http://localhost:9090/onlineshopping/`
- Products: `http://localhost:9090/onlineshopping/products`

### If the form still does not appear
- Hard refresh the browser with `Ctrl + F5`
- Check Tomcat logs in `C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs`
- Make sure you are opening `/onlineshopping/products`, not just `/products`

---

## New Enterprise Features (Cart + Address Book + Reports)

After updating code, run the DB script again so new tables are created:

```powershell
cd D:\servlet-app\online-shopping\onlineshopping
mvn clean package
```

Then run SQL from `src/main/webapp/WEB-INF/db/schema.sql` in phpMyAdmin (or MySQL CLI).

### Main URLs
- Products: `http://localhost:9090/onlineshopping/products`
- Cart + Address Book + Checkout: `http://localhost:9090/onlineshopping/cart`
- Orders: `http://localhost:9090/onlineshopping/orders`
- Reports (admin only): `http://localhost:9090/onlineshopping/reports`

### Jasper Invoice PDF
- Open `Orders` page, then click `Download Invoice PDF` on any order card.
- Direct URL pattern: `http://localhost:9090/onlineshopping/invoice?orderId=1`

