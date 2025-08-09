using System.Collections;
using Kount;
using KountRisSdk;

class Program
{
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
    }
}