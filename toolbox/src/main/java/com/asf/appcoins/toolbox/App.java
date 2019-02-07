package com.asf.appcoins.toolbox;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import com.appcoins.net.AppcoinsClient;
import com.appcoins.net.AppcoinsClientFactory;
import com.appcoins.net.QueryParams;
import com.asf.appcoins.sdk.ads.AppCoinsAds;

public class App extends Application {

  private final String developerAddress = BuildConfig.IAB_WALLET_ADDR;

  private static AppCoinsAds adsSdk;

  @Override public void onCreate() {
    super.onCreate();

    AdvertisementSdkSingleton.create(this, true);

    try {

      adsSdk = AdvertisementSdkSingleton.getAdsSdk();
      adsSdk.init(this);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
  }
}
