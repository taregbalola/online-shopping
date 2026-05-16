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
Wait for message: `INFO [main] org.apache.catalina.startup.Catalina.start Server startup`

### Step 5: Open your browser and visit
- **Home page:** http://localhost:8080/onlineshopping/
- **Servlet endpoint:** http://localhost:8080/onlineshopping/hello

You should see: `Hello from servlet without installing Tomcat!`

### Step 6: Stop the server
In the command prompt window, press **`Ctrl + C`**

---

## Full Command Example (Copy & Paste)
```
D:
cd D:\servlet-app\online-shopping\onlineshopping
mvn clean package
mvn tomcat7:run
```

Then open browser to: `http://localhost:8080/onlineshopping/hello`
