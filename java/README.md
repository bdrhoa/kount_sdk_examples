## Java Notes
### Copy or rename src/main/resources/config.properties.example to src/main/resources/config.properties
### Set the following properties in the src/main/resources/config.properties file:
* migration.mode.enabled=true|false
* kount.config.key=<the_kount_config_key>
* kount.api.key=<the_kount_command_api_key>
* payments.fraud.client.id=<your_client_id>
* payments.fraud.api.key=<your_kount_360_api_key>

### To execute issue:
* Make sure you're in the javasdkmigration directory.
* mvn compile exec:java -Dexec.mainClass="com.kount.example.javasdk.Main"
* If you've run this before, to make sure you're using the latest SDK, run mvn -U compile exec:java -Dexec.mainClass="com.kount.example.javasdk.Main"