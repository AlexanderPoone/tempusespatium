package hk.edu.cuhk.cse.tempusespatium;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by softfeta on 3/15/18.
 */

public class PrefsActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prefs_activity);

        MobileAds.initialize(this, "ca-app-pub-9627209153774793~3950569449");
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        AdView adView = (AdView) findViewById(R.id.ad);
        AdView adView2 = (AdView) findViewById(R.id.ad2);

        AdRequest request = new AdRequest.Builder().addTestDevice("210A8F39D562F35F67912205BF9A0FBD")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
//        Log.i("Hihi", Boolean.toString(request.isTestDevice(this)));
        adView.loadAd(request);

        AdRequest request2 = new AdRequest.Builder().addTestDevice("210A8F39D562F35F67912205BF9A0FBD")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView2.loadAd(request2);
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }
}
