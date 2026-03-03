using System.Collections;
using Kount;
using KountRisSdk;
using Kount.Enums;

class Program
{

    static string GetRisField(Kount.Ris.Response response, string key)
    {
        if (response == null) throw new ArgumentNullException(nameof(response));
        if (string.IsNullOrWhiteSpace(key)) throw new ArgumentException("Key is required.", nameof(key));

        var s = response.ToString() ?? "";
        var lines = s.Split(new[] { "\r\n", "\n" }, StringSplitOptions.RemoveEmptyEntries);

        foreach (var line in lines)
        {
            // Expect "KEY=VALUE"
            int eq = line.IndexOf('=');
            if (eq <= 0) continue;

            var k = line.Substring(0, eq).Trim();
            if (string.Equals(k, key, StringComparison.OrdinalIgnoreCase))
                return line.Substring(eq + 1).Trim();
        }

        return "Unknown"; // not found
    }

    static void Main(string[] args)
    {
        Console.WriteLine("Hello, World!");
        Kount.Ris.Inquiry inq = new Kount.Ris.Inquiry();
        Guid myuuid = Guid.NewGuid();
        string sessionID = myuuid.ToString().Replace("-","");   

        // Create masked inquiry with CARD payment
        inq.SetPayment(Kount.Enums.PaymentTypes.Card, "5789372819873789");
        // Merchants acknowledgement to ship/process the order
        inq.SetMack('Y');
        // client email
        inq.SetEmail("joe@domain.com");
        // Set default inquiry mode, internet order type
        
        inq.SetMode(Kount.Enums.InquiryTypes.ModeQ);
        inq.SetSessionId(sessionID);
        // IP address of the customer
        inq.SetIpAddress("165.53.125.33");
        inq.SetWebsite("DEFAULT");
        // total purchase amount
        inq.SetTotal(5000);
        ArrayList cart = new ArrayList();
        cart.Add(new Kount.Ris.CartItem("Electronics", "TV", "Big TV", 1, 24900));
        inq.SetCart(cart);
        Kount.Ris.Response response = inq.GetResponse();
        Console.WriteLine("RESPONSE: " + response.ToString());
        //MODE U
        string transactionId = GetRisField(response, "TRAN");
        Console.WriteLine("TRAN: " + transactionId);
        var update = new Kount.Ris.Update();
        update.SetMode(UpdateTypes.ModeU);
        update.SetAuth('D');
        update.SetSessionId(sessionID);
        update.SetTransactionId(transactionId);
        Kount.Ris.Response updateResponse = update.GetResponse();
        Console.WriteLine("UPDATE RESPONSE: " + updateResponse.ToString());
    }
}