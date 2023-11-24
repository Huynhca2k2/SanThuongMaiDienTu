package com.chh.shoponline;

import android.app.Application;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;
public class PayPalConfig extends Application {

    String apiKeyShop = "Af7z4N5q4iwfX3kCXMUOunhSIiUfpai9TwpwGJJrfTN--XoG0RbyhtuC1UXQw7--vTmMbQ6JghK-dn8a";
    @Override
    public void onCreate() {
        super.onCreate();
        PayPalCheckout.setConfig(new CheckoutConfig(
                this,
                apiKeyShop,
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                "com.chh.shoponline://paypalpay"
        ));
    }
}
