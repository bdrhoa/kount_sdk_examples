require 'json' 
require 'kount'

# 1. Configure the SDK to point to Kount 360
# Keep your existing Merchant ID, Key, and KSALT, but override the endpoint.
options = {
  merchant_id: 'your_client_id',              # Required: Your 6-digit Kount Merchant ID
  ksalt:       '',          # Required: Provided by Kount
  is_test:     true,                 # Optional: Set to true if hitting a sandbox/test environment
  version:     '0720',
  migration_mode_enabled: true,
  pf_client_id:'your_client_id',
  pf_auth_endpoint: 'https://login.kount.com/oauth2/ausdppkujzCPQuIrY357/v1/token',
  pf_api_endpoint: 'https://api-sandbox.kount.com/commerce/ris',
  pf_api_key: 'your_API_key',
}

client = Kount.new(options)

# 2. Build the Inquiry (Mode Q)
inquiry = Kount::Inquiry.new(
  SITE: "DEFAULT",
  MACK: "Y",
  AUTH: "A",
  ORDR: "ORDER_3458",
  TOTL: "5000",
  CURR: "USD",
  EMAL: "customer@example.com",
  IPAD: "127.0.0.1",
  SESS: "QWER2314567" # Must match your Device Data Collector session ID
)

# 3. Add Payment Information (KHASH handled automatically)
inquiry.add_payment(Kount::PaymentTypes::CREDIT_CARD, "4111111111111111")

# 4. Add Cart Items
cart = Kount::Cart.new()
cart.add_item('64 inch LCD TV', 'Electronics', 'Television', '1', '4500')
inquiry.add_cart(cart)

# 5. Execute Call & Read the Raw Hash

  # client.get_response returns a Ruby Hash, no wrapper class needed!
  response = client.get_response(inquiry)
  
  puts "=== Kount 360 Response ==="
  #puts response
  puts "Transaction ID : #{response['TRAN']}"
  puts "Decision (AUTO): #{response['AUTO']}"
  puts "Score          : #{response['SCORE']}"
  puts "Geo Location   : #{response['GEOX']}"
  
  # To see everything Kount 360 returned:
  puts response.inspect

