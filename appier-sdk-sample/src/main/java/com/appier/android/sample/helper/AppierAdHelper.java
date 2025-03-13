package com.appier.android.sample.helper;

import com.appier.ads.Appier;
import com.appier.ads.AppierBannerView;
import com.appier.ads.AppierBaseAd;
import com.appier.ads.common.AppierTargeting;
import com.appier.ads.common.ConsentStatus;

import java.util.HashMap;
import java.util.Map;

public class AppierAdHelper {

    public static void setAppierGlobal() {

        /*
         * (Optional) Enable test mode for Ad responses
         */
        Appier.setTestMode(Appier.TestMode.BID);

        /*
         * (Optional) Set GDPR and consent status explicitly
         */
        Appier.setGDPRApplies(true);
        Appier.setConsentStatus(ConsentStatus.EXPLICIT_YES);

        /*
         * (Optional) Set COPPA explicitly to follow the regulations
         */
        Appier.setCoppaApplies(true);

        /*
         * (Optional) Set browser agent explicitly
         */
        Appier.setBrowserAgent(Appier.BrowserAgent.NATIVE);

        /*
         * (Optional) Set Appier custom parameters
         */
        Appier.setCustomParameters(new HashMap<String, String>() {
            {
                // Set the host to production or staging
                 put("host", "production");
//                put("host", "staging");
                put("path", "v1");
            }
        });
    }

    public static void setTargeting(AppierBaseAd appierAd) {

        /*
         * (Optional) Set Appier Targeting
         * Set targeting could bring more precise ads, which may increase revenue for App developers
         */
        appierAd.setYob(2001);
        appierAd.setGender(AppierTargeting.Gender.FEMALE);
        appierAd.addKeyword("interest", "sports");
    }

    public static void setTargeting(AppierBannerView bannerView) {
        /*
         * (Optional) Set Appier Targeting
         * Set targeting could bring more precise ads, which may increase revenue for App developers
         */
        bannerView.setYob(1985);
        bannerView.setGender(AppierTargeting.Gender.MALE);
        bannerView.addKeyword("favorite", "cars");

    }
}
