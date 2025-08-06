//mvn compile exec:java -Dexec.mainClass="com.kount.example.javasdk.Main"
// To run this example, you need to have the Kount Java SDK and its dependencies set up in your project.
// Ensure you have the necessary properties in your config.properties file.
// The properties file should include kount.config.key and kount.api.key.
// The API key can also be provided via a file, as shown in the example.
// Make sure to replace the API key file path with the correct path on your system.
// The example demonstrates how to create a KountRisClient, set up an Inquiry request,
// and process the request to get a response from the Kount Risk API.
// The example uses a test URL and a sample transaction. Adjust the values as needed for your application.
// Ensure you have the necessary permissions and configurations to access the Kount services.
// The example includes error handling for IO exceptions and Kount RIS exceptions.
// You can run this example using Maven with the command: mvn compile exec:java -Dexec.mainClass="com.kount.example.javasdk.Main"
// Make sure to have the Kount Java SDK and its dependencies in your classpath.
// The example is structured to read configuration properties from a file, set system properties,
// and create a request to the Kount Risk API. It demonstrates how to handle the response
// and print the transaction details to the console.
// The example uses a simple console output for demonstration purposes.
// You can modify the output handling as needed for your application.
// The example is designed to be run as a standalone Java application.

package com.kount.example.javasdk;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import com.kount.ris.Inquiry;
import com.kount.ris.KountRisClient;
import com.kount.ris.Response;
import com.kount.ris.util.CartItem;
import com.kount.ris.util.MerchantAcknowledgment;
import com.kount.ris.util.RisException;
import com.kount.ris.util.payment.Payment;
import javax.naming.ConfigurationException;

//import com.kount.ris.util.ConfigurationException;

public class Main {

    private static Properties properties = new Properties();

    public static void ConfigReader() {
        FileInputStream fileInputStream = null;

        try {
            // Load the config.properties file
            fileInputStream = new FileInputStream("src/main/resources/config.properties");
            properties.load(fileInputStream);

            // Retrieve the kount.config.key
            String keyValue = properties.getProperty("kount.config.key");
            System.setProperty("kount.config.key", keyValue);
            System.out.println("kount.config.key = " + keyValue);

            Boolean migrationModeEnabled = Boolean
                    .parseBoolean(properties.getProperty("migration.mode.enabled", "false"));
            System.setProperty("migration.mode.enabled", String.valueOf(migrationModeEnabled));
            System.out.println("migration.mode.enabled = " + migrationModeEnabled);

            String url;
            String apiKey;
            String authEndpoint;
            String clientID;

            if (migrationModeEnabled) {
                clientID = properties.getProperty("payments.fraud.client.id");
                if (clientID != null) {
                    System.setProperty("payments.fraud.client.id", clientID);
                    System.out.println("payments.fraud.client.id = " + clientID);
                } else {
                    System.out.println("payments.fraud.client.id not found in properties file.");
                }
                // Set the properties for migration mode
                authEndpoint = properties.getProperty("payments.fraud.auth.endpoint");
                if (authEndpoint != null) {
                    System.setProperty("payments.fraud.auth.endpoint", authEndpoint);
                    System.out.println("payments.fraud.auth.endpoint = " + authEndpoint);
                } else {
                    System.out.println("payments.fraud.auth.endpoint not found in properties file.");
                }
                url = properties.getProperty("payments.fraud.api.endpoint");
                if (url != null) {
                    System.setProperty("payments.fraud.api.endpoint", url);
                    System.out.println("payments.fraud.api.endpoint = " + url);
                } else {
                    System.out.println("payments.fraud.api.endpoint not found in properties file.");
                }
                apiKey = properties.getProperty("payments.fraud.api.key");
                if (apiKey != null) {
                    System.setProperty("payments.fraud.api.key", apiKey);
                    System.out.println("payments.fraud.api.key = " + apiKey);
                } else {
                    System.out.println("payments.fraud.api.key not found in properties file.");
                }
            } else {
                url = properties.getProperty("kount.api.url");
                if (url != null) {
                    System.setProperty("kount.api.url", url);
                    System.out.println("kount.api.url = " + url);
                } else {
                    System.out.println("kount.api.url not found in properties file.");
                }
                apiKey = properties.getProperty("kount.api.key");
                if (apiKey != null) {
                    System.setProperty("kount.api.key", apiKey);
                    System.out.println("kount.api.key = " + apiKey);
                } else {
                    System.out.println("kount.api.key not found in properties file.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws MalformedURLException, ConfigurationException {

        ConfigReader();

        URL url;
        String apiKey;
        KountRisClient ris;

        if (System.getProperty("migration.mode.enabled") != null
                && Boolean.parseBoolean(System.getProperty("migration.mode.enabled"))) {
            // Use the migration mode endpoint and API key
            url = java.net.URI.create(System.getProperty("payments.fraud.api.endpoint")).toURL();
            apiKey = System.getProperty("payments.fraud.api.key");
        } else {
            // Use the standard Kount API endpoint and API key
            url = java.net.URI.create(System.getProperty("kount.api.url")).toURL();
            apiKey = System.getProperty("kount.api.key");
        }
        // Create a new KountRisClient instance with the URL and API key
        ris = new KountRisClient(url, apiKey);

        // Create and populate a new Request. The Request will be handed
        // off to the RIS client to make the call and provide the response.
        Inquiry req = new Inquiry();
        // req.setMerchantId(900100); // Use the merchant ID from properties if needed
        req.setMerchantId(Integer.parseInt(System.getProperty("payments.fraud.api.merchant.id", "900100")));
        // req.setMerchantId(Long.parseLong(System.getProperty("payments.fraud.api.merchant.id",
        // "900100")));
        req.setSessionId("faa6370074b53928bc51ef913441e0cd");
        Payment payment = new Payment("CARD", "4111111111111111");
        req.setCurrency("USD");
        req.setPayment(payment);
        req.setTotal(125);
        req.setCustomerName("John Doe");
        req.setEmail("johndoe@test.com");
        req.setIpAddress("127.0.0.1");
        req.setMerchantAcknowledgment(MerchantAcknowledgment.YES);
        req.setWebsite("DEFAULT");
        CartItem item0 = new CartItem("SURROUND SOUND", "HTP-2920", "Pioneer High Power 5.1 Surround Sound System", 1,
                49999);
        CartItem item1 = new CartItem("BLURAY PLAYER", "BDP-S500", "Sony 1080p Blu-Ray Disc Player", 1, 69999);
        Collection<CartItem> cart = new ArrayList<>();
        cart.add(item0);
        cart.add(item1);
        req.setCart(cart);
        if (ris != null) {
            try {
                // This is the first point at which the code actually calls the Kount
                // services. Everything prior to this is simply setting up the payload.
                Response response = ris.process(req);
                String responseText = "Transaction ID: " + response.getTransactionId() + "\n\n";
                responseText += response;
                System.out.print(responseText);
            } catch (RisException risException) {
                System.out.print(risException.getMessage());
            }
        }
    }
}