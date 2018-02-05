package hk.edu.cuhk.cse.tempusespatium;

/**
 * Created by Alex Poon on 2/6/2018.
 */

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class CountDownTimer {

    private final long mMillisInFuture;
    private final long mCountdownInterval;
    private long mStopTimeInFuture;
    private long mPauseTime;
    private boolean mCancelled = false;
    private boolean mPaused = false;

    public CountDownTimer(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    public final void cancel() {
        mHandler.removeMessages(MSG);
        mCancelled = true;
    }

    public synchronized final CountDownTimer start() {
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        mCancelled = false;
        mPaused = false;
        return this;
    }

    public long pause() {
        mPauseTime = mStopTimeInFuture - SystemClock.elapsedRealtime();
        mPaused = true;
        return mPauseTime;
    }

    public long resume() {
        mStopTimeInFuture = mPauseTime + SystemClock.elapsedRealtime();
        mPaused = false;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return mPauseTime;
    }

    public abstract void onTick(long millisUntilFinished);

    public abstract void onFinish();

    private static final int MSG = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            synchronized (CountDownTimer.this) {
                if (!mPaused) {
                    final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                    if (millisLeft <= 0) {
                        onFinish();
                    } else if (millisLeft < mCountdownInterval) {
                        sendMessageDelayed(obtainMessage(MSG), millisLeft);
                    } else {
                        long lastTickStart = SystemClock.elapsedRealtime();
                        onTick(millisLeft);
                        long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();
                        while (delay < 0) delay += mCountdownInterval;
                        if (!mCancelled) {
                            sendMessageDelayed(obtainMessage(MSG), delay);
                        }
                    }
                }
            }
        }
    };
}
