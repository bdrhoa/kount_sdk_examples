## .Net Notes
### Copy or rename kountdotnetexample/appsetting.json.example to kountdotnetexample/appsetting.json
### Set the following properties in the src/main/resources/config.properties file:
* Ris.EnableMigrationMode=true|false
* Ris.MerchantId=<your_client_id>
* Ris.Config.Key=<the_kount_config_key>
* Ris.API.Key=<the_kount_command_api_key>
* PaymentsFraud.ClientId=<your_client_id>
* PaymentsFraud.Api.Key=<your_kount_360_api_key>

### To execute issue:
* Make sure you're in the kountdotnetexample directory.
* dotnet run
