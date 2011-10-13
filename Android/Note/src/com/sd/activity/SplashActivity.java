package com.sd.activity;

import com.sd.Config;
import com.sd.NoteApplication;
import com.sd.R;
import com.sd.Config.activity;
import com.sd.R.id;
import com.sd.R.layout;
import com.sd.R.string;
import com.sd.model.UserData;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class SplashActivity extends Activity implements AnimationListener{
	
	interface LoadingListener {
		public void onCompleted();
	}
	
	private final String TAG = getClass().getSimpleName();
	private String mTargetActivity;
	private TextView mSologan;
	private TextView mLogo;
	private float scale;// = getResources().getDisplayMetrics().density;
	private float xscale;// = (int)(12 * scale);
	private Display display;
	private Paint paint;
	private int WidthText;
	private int MaxWidth;// = (int) (280 * scale);
	private int Width;
	private int Height;
	private CharSequence mText;
	private int mIndex;
	private long mDelay = 50;
	private Handler mHandler= new Handler();
	private ApplicationDataLoadingTask loadingTask;
	
	public LoadingListener listener = new LoadingListener() {
		
		@Override
		public void onCompleted() {
			loadingTask = new ApplicationDataLoadingTask();
			loadingTask.execute();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.splash);
		
		inflateView();
		
		checkLogin();
	}
	
	private void checkLogin() {
		UserData userData = NoteApplication.Instance().getUserdata();
		if(userData.isAuthen) {
			mTargetActivity = Config.activity.NoteActivity.toString();
		} else {
			mTargetActivity = Config.activity.LoginActivity.toString();
		}
		
		//check if a target activity was specified
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            if (extras.containsKey("targetActivity")) {
                mTargetActivity = extras.getString("targetActivity");
            }
        }
	}
	
	private void inflateView() {
		scale = getResources().getDisplayMetrics().density;
		xscale = (int)(18 * scale);
		MaxWidth = (int) (300 * scale);
		
        mSologan = (TextView) findViewById(R.id.sologan);
        mLogo = (TextView) findViewById(R.id.logo_text);
        
        display = getWindowManager().getDefaultDisplay();
		Width = display.getWidth();
		Height = display.getHeight();
        
        paint = new Paint();
		paint.setTextSize(xscale);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		WidthText = (int)paint.measureText(mSologan.getText().toString());
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		startLogoTranslateAnimation();
	}


	private void startLogoTranslateAnimation() {
		mSologan.setVisibility(View.INVISIBLE);
		//mLogo.setVisibility(View.INVISIBLE);
		
		Animation logoTranslateAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f);
		logoTranslateAnim.setDuration(2000);
		logoTranslateAnim.setInterpolator(new LinearInterpolator());
		logoTranslateAnim.setAnimationListener(this);
		
		mLogo.startAnimation(logoTranslateAnim);
		
	}
	
	public void startNextActivity() {
		Intent intent;
		if(mTargetActivity.equals(Config.activity.NoteActivity.toString())) {
			intent = new Intent(this, MainActivity.class);
		} else {
			intent = new Intent(this, LoginActivity.class);
		}
		
		 startActivity(intent);
		 finish();
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		mSologan.setVisibility(View.VISIBLE);
		mSologan.setPadding((int)((Width - WidthText) / 2 - 5 * scale),(int)(20 * scale), 0,0);
		animateText(getString(R.string.splash_text));
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}
	
	private Runnable characterAdder = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			mSologan.setText(mText.subSequence(0, mIndex++));
            if(mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            } else{
            	listener.onCompleted();
            }
		}
	};
	
	public void animateText(CharSequence text) {
        mText = text;
        mIndex = 0;

        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }
	
	public class ApplicationDataLoadingTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            Log.d(TAG, "ApplicationDataLoadingTask::onPreExecute");
        }
    
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "ApplicationDataLoadingTask::doInBackground");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    
        protected void onPostExecute(Void v) {
            Log.d(TAG, "ApplicationDataLoadingTask::onPostExecute");
            startNextActivity();
        }
    }
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if(null != loadingTask) {
			loadingTask.cancel(true);
		}
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
	}
	
	
}
