  <?php

   // RIS request --- requires curl support enabled in PHP
   // php_mbstring support enabled in your php.ini file
  
  // If using Direct Download
  //require __DIR__ . 'path-to-sdk/src/autoload.php';

  //  OR

  // If using Composer installation
  require __DIR__ . '/vendor/autoload.php';

  // Minimal RIS inquiry example:
  try {
    $inquiry = new Kount_Ris_Request_Inquiry();

    $inquiry->setSessionId('CAF15D3831DB4F7CBFCEA22F8723B169');
    $inquiry->setPayment('CARD', '4111111111111111');
   
    $inquiry->setTotal(500);
    $inquiry->setEmail('test@kount.com');
    $inquiry->setIpAddress('192.168.0.21');
    $inquiry->setMack('Y');
    $inquiry->setWebsite("DEFAULT");
    $cart = array();
    $cart[] = new Kount_Ris_Data_CartItem("TV", "LZG-123", "32-inch LCD", 1, 129999);
    $inquiry->setCart($cart);
    $inquiry->setAuth('A');
   
    // additional optional setters..
    // setGender value can be either "M" or "F"

    $inquiry->setGender("M");
    $inquiry->setDateOfBirth("2017-03-12");

    $response = $inquiry->getResponse();
    // optional getter
    $warnings = $response->getWarnings();

    print_r("resonse\n");
    print_r($response);
    print_r("warnings\n");
    print_r($warnings);

    $score = $response->getScore();
    $auto = $response->getAuto();

  } catch (Exception $e) {
    print_r($e);
    // handle exception
  }
