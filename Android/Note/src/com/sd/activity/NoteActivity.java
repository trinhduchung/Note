package com.sd.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sd.R;
import com.sd.database.DatabaseHelper;
import com.sd.model.FolderInfo;
import com.sd.view.HorizontalPager;
import com.sd.view.PagerItemView;

public class NoteActivity extends Activity {
	HorizontalPager pager;
	HorizontalPager.OnScreenSwitchListener listener = new HorizontalPager.OnScreenSwitchListener() {
		
		@Override
		public void onScreenSwitched(int screen) {
			System.out.println("screen = " + screen);
			flipImage(screen);
		}
	};
	LinearLayout paperCount;
	int totalPage = 3;
	int itemPerPage = 5;
	int totalItem = 0;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 0) {
				hideLoading();
				inflateView();
			}
		}
		
	};
	ProgressDialog loading;
	DatabaseHelper databaseHelper;
	ArrayList<FolderInfo> list = new ArrayList<FolderInfo>();
	
	private static final int MENU_CREATE_FOLDER = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paper);
		databaseHelper = new DatabaseHelper(this);
		Thread thread = new Thread(runnable);
		thread.start();
		showDialog();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_CREATE_FOLDER, 0, "New Folder")
		.setIcon(R.drawable.menu_create_folder);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_CREATE_FOLDER:
			FolderInfo info = new FolderInfo(1, "Idea", R.drawable.bg4, R.drawable.i_03, "22", 5);
			long id = databaseHelper.insertFolder(info);
			System.out.println("id = " + id);
			showDialog();
			Thread thread = new Thread(runnable);
			thread.start();
			break;

		default:
			break;
		}
		return false;
	}

	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			loadData();
			Message message = handler.obtainMessage(0);
			handler.sendMessage(message);
		}
	};
	
	private void showDialog() {
		loading = ProgressDialog.show(this, "", "loading ...", true , true);
	}
	
	private void hideLoading() {
		if(null != loading && loading.isShowing()) {
			loading.dismiss();
		}
	}
	
	private void loadData() {
		list = databaseHelper.getAllFolder();
		totalItem = list.size();
		if(0 == (totalPage % itemPerPage)) {
			totalPage = totalItem / itemPerPage;
		} else {
			totalPage = totalItem / itemPerPage + 1;
		}
		if(totalPage == 0) {
			totalPage = 1;
		}
	}
	
	private void inflateView() {
		paperCount = (LinearLayout) findViewById(R.id.pager_count);
		paperCount.removeAllViews();
		pager = (HorizontalPager) findViewById(R.id.horizontal_pager);
		pager.setOnScreenSwitchListener(listener);
		pager.removeAllViews();
		for(int i = 0;i < totalPage;i++) {
			LinearLayout paperItem = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.pager_item, null);
			for(int j = i * itemPerPage;j < i * itemPerPage + itemPerPage;j ++) {
				if(j >= totalItem) {
					break;
				}
				PagerItemView item = new PagerItemView(this);
				item.setItemInfo(list.get(j));
				paperItem.addView(item);
			}
			pager.addView(paperItem);
			
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(R.drawable.dark);
			paperCount.addView(imageView,i);
		}
		((ImageView)paperCount.getChildAt(0)).setImageResource(R.drawable.light);
	}
	
	private void flipImage(int screen) {
		for(int i = 0;i < totalPage;i++) {
			if (i == screen) {
				((ImageView)paperCount.getChildAt(i)).setImageResource(R.drawable.light);
			} else {
				((ImageView)paperCount.getChildAt(i)).setImageResource(R.drawable.dark);
			}
		}
	}
}
