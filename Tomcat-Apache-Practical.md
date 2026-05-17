# Tomcat / Apache Practical Notes

This practical note is based on the current project setup:
- App: `onlineshopping` (WAR packaging)
- Context path: `/onlineshopping`
- Embedded run command: `mvn tomcat7:run`
- Servlet mappings in `onlineshopping/src/main/webapp/WEB-INF/web.xml`

## 1) What is Tomcat?
Tomcat is a Java Servlet/JSP container.
It runs Java web applications (`.war`) and handles servlet lifecycle, sessions, and request routing.

Practical in this project:
```powershell
D:
cd D:\servlet-app\online-shopping\onlineshopping
mvn tomcat7:run
```
Then open:
- `http://localhost:8080/onlineshopping/`
- `http://localhost:8080/onlineshopping/products`

## 2) Difference between Apache and Tomcat?
- Apache HTTP Server: general web server (static files, reverse proxy, SSL termination).
- Tomcat: Java app server/servlet container (runs servlet/JSP code).

Typical enterprise flow:
- Client -> Apache HTTP Server -> Tomcat

Apache is front-door web server, Tomcat runs Java backend app.

## 3) What is WAR deployment?
WAR = Web ARchive package of a Java web app.
It contains compiled classes, JSP files, web.xml, dependencies, and static assets.

Practical build in this project:
```powershell
D:
cd D:\servlet-app\online-shopping\onlineshopping
mvn clean package
```
WAR output:
- `D:\servlet-app\online-shopping\onlineshopping\target\onlineshopping.war`

Manual deployment idea (external Tomcat):
- Copy WAR into `<TOMCAT_HOME>\webapps\`

 Tomcat 9 service on `9090`:
```powershell
Copy-Item D:\servlet-app\online-shopping\onlineshopping\target\onlineshopping.war "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\" -Force
Remove-Item "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\onlineshopping" -Recurse -Force -ErrorAction SilentlyContinue
Restart-Service Tomcat9
```

## 4) How do you check Tomcat logs?
For embedded plugin here, logs are under target:
- `onlineshopping\target\tomcat\logs\`

Practical commands:
```powershell
cd D:\servlet-app\online-shopping\onlineshopping
Get-ChildItem .\target\tomcat\logs
Get-Content .\target\tomcat\logs\access_log*.txt -Tail 50
```

For external Tomcat, common logs:
- `<TOMCAT_HOME>\logs\catalina*.log`
- `<TOMCAT_HOME>\logs\localhost*.log`
- `<TOMCAT_HOME>\logs\localhost_access_log*.txt`

## 5) What is reverse proxy?
A reverse proxy sits in front of application servers.
It receives client requests and forwards them to backend services (Tomcat), then returns responses.

Why used:
- SSL termination
- Load balancing
- Security isolation
- Centralized routing

Conceptual Apache HTTPD example:
```apache
ProxyPass /shop http://localhost:8080/onlineshopping
ProxyPassReverse /shop http://localhost:8080/onlineshopping
```

## 6) How do you restart Tomcat service?
If using embedded Maven Tomcat (your current setup):
- Stop with `Ctrl + C`
- Start again with `mvn tomcat7:run`

If using Windows service (external Tomcat):
```powershell
# run in elevated PowerShell
Restart-Service Tomcat9
# or
Stop-Service Tomcat9
Start-Service Tomcat9
```
(Service name can differ: `Tomcat10`, `Apache Tomcat`, etc.)

## 7) What causes 404 in Tomcat?
Most common causes:
- Wrong context path (missing `/onlineshopping`)
- Wrong servlet mapping in `web.xml`
- Wrong URL pattern in browser
- WAR not deployed or failed startup
- Reverse proxy route mismatch

Practical checks for this project:
1. Confirm context path in `onlineshopping/pom.xml` (`/onlineshopping`).
2. Confirm servlet mappings in `onlineshopping/src/main/webapp/WEB-INF/web.xml`.
3. Test valid URLs:
   - `/onlineshopping/hello`
   - `/onlineshopping/products`
   - `/onlineshopping/about`
   - `/onlineshopping/contact`
4. Inspect logs in `target/tomcat/logs`.

## 8) What is connection pooling?
Connection pooling keeps a reusable set of DB connections.
Instead of opening/closing DB connection per request, app borrows and returns from pool.

Benefits:
- Faster response
- Lower DB overhead
- Better scalability

In enterprise Tomcat, usually configured as JNDI DataSource (for example in `context.xml`) and injected/lookup in app.
Common tuning fields:
- `maxTotal`
- `maxIdle`
- `minIdle`
- `maxWaitMillis`

## Quick interview one-liners
- Tomcat: Java servlet container.
- Apache vs Tomcat: Apache serves/proxies, Tomcat executes Java web apps.
- WAR: packaged Java web app deployment unit.
- Logs: check `catalina` and access logs.
- Reverse proxy: front server forwarding to backend apps.
- Restart: service restart or stop/start embedded run.
- 404: usually wrong context path or mapping.
- Connection pooling: reuse DB connections for performance.

