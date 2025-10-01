# Ecom Azure Sample (Beginner Friendly)

This is a minimal Spring Boot app that demonstrates:
- Azure App Service (Web App)
- Azure SQL Database (JDBC)
- Azure Blob Storage (images upload)
- Azure Functions (HTTP trigger notified on order placement)
- Logic Apps (send email on order completion - wire from function)
- Azure DevOps YAML pipeline

## Run locally
1. Java 17 + Maven installed.
2. Set environment variables (or edit `src/main/resources/application.properties`):
```
AZURE_SQL_URL=jdbc:sqlserver://<server>.database.windows.net:1433;database=<EcomDB>;encrypt=true;...
AZURE_SQL_USER=<sqladmin>
AZURE_SQL_PASS=<password>
AZURE_STORAGE_CONNECTION_STRING=DefaultEndpointsProtocol=...AccountName=...;AccountKey=...;EndpointSuffix=core.windows.net
AZURE_BLOB_CONTAINER=images
ORDER_FUNCTION_URL=https://<your-function-app>.azurewebsites.net/api/order-placed?code=<function-key>
```
3. Create table in Azure SQL using `db/create.sql`.
4. `mvn spring-boot:run`
5. Open http://localhost:8080

## Deploy to Azure App Service
- Create Web App (Java 17) on Basic/Standard plan.
- In Web App → Configuration → Application settings, add the environment variables above.
- Restart.
- Use DevOps pipeline in `azure-pipelines.yml` to deploy (replace service connection and app name).

## Blob Storage
- Create container `images`.
- The `/api/images/upload/{name}` PUT endpoint uploads raw file bytes.
- Frontend form in `index.html` uses that endpoint.

## SQL Schema
See `db/create.sql`.

## Function (HTTP trigger)
- Create Function App (Java/.NET/Node). Make an HTTP trigger named `order-placed` that accepts JSON with fields:
  `customerName, productId, quantity, totalAmount, createdAt`.
- Return 200 OK.
- Copy function URL (with code) into `ORDER_FUNCTION_URL` setting.

## Logic App
- Trigger: When a HTTP request is received (or from Function step).
- Action: Send an email via Outlook/Gmail connector with the order details.
