package com.sd.view;

import com.sd.R;
import com.sd.model.FolderInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PagerItemView extends RelativeLayout {
	
	LinearLayout img;
	TextView name;
	ImageView icon;
	TextView count;
	ImageView lock;
	public PagerItemView(Context context) {
		super(context);
		inflateView(context);
	}

	public PagerItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflateView(context);
	}

	public PagerItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflateView(context);
	}
	
	private void inflateView(Context context) {
		inflate(context, R.layout.item, this);
		img = (LinearLayout) findViewById(R.id.bg);
		name = (TextView) findViewById(R.id.title);
		icon = (ImageView) findViewById(R.id.ic);
		count = (TextView) findViewById(R.id.number);
		lock = (ImageView) findViewById(R.id.lock);
	}
	
	public void setItemInfo(FolderInfo info) {
		img.setBackgroundResource(info.getImg());
		name.setText(info.getName());
		icon.setImageResource(info.getIcon());
		count.setText(String.valueOf(info.getCount()));
		if(info.getPassword().length() > 0) {
			lock.setVisibility(VISIBLE);
			count.setVisibility(INVISIBLE);
		} else {
			count.setVisibility(VISIBLE);
			lock.setVisibility(INVISIBLE);
		}
	}
}
