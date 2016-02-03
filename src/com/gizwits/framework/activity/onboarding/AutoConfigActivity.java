/**
 * Project Name:XPGSdkV4AppBase
 * File Name:AutoConfigActivity.java
 * Package Name:com.gizwits.framework.activity.onboarding
 * Date:2015-1-27 14:45:54
 * Copyright (c) 2014~2015 Xtreme Programming Group, Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gizwits.framework.activity.onboarding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemSelectedListener;

import com.gizwits.heater.R;
import com.gizwits.framework.activity.BaseActivity;
import com.xpg.common.useful.NetworkUtils;
import com.xpg.common.useful.StringUtils;
import com.xpg.ui.utils.ToastUtils;

// TODO: Auto-generated Javadoc
/**
 * 
 * ClassName: Class AutoConfigActivity. <br/>
 * 自动配置界面 <br/>
 * date: 2014-12-9 17:30:20 <br/>
 * 
 * @author Lien
 */
public class AutoConfigActivity extends BaseActivity implements OnClickListener {

	/** The tv ssid. */
	private TextView tvSsid;

	/** The et input psw. */
	private EditText etInputPsw;

	/** The tb psw flag. */
	private ToggleButton tbPswFlag;

	/** The btn next. */
	private Button btnNext;
	/** The sp mode */
	private Spinner sp_mode;
	private static int mode_temp;
	/**
	 * The iv back.
	 */
	private ImageView ivBack;

	/** 网络状态广播接受器. */
	ConnecteChangeBroadcast mChangeBroadcast = new ConnecteChangeBroadcast();

	/** The str ssid. */
	private String strSsid;

	/** The str psw. */
	private String strPsw;

	/**
	 * 
	 * ClassName: Enum handler_key. <br/>
	 * <br/>
	 * date: 2014-11-26 17:51:10 <br/>
	 * 
	 * @author Lien
	 */
	private enum handler_key {

		/** The change wifi. */
		CHANGE_WIFI,

	}

	/** The handler. */
	Handler handler = new Handler() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key) {
			case CHANGE_WIFI:
				strSsid = NetworkUtils.getCurentWifiSSID(AutoConfigActivity.this);
				tvSsid.setText(getString(R.string.wifi_name) + strSsid);
				break;

			}
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gizwits.aircondition.activity.BaseActivity#onCreate(android.os.
	 * Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_autoconfig);
		initViews();
		initEvents();
	}

	/**
	 * Inits the views.
	 */
	private void initViews() {
		sp_mode = (Spinner) findViewById(R.id.sp_mode);
		//
		tvSsid = (TextView) findViewById(R.id.tvSsid);
		etInputPsw = (EditText) findViewById(R.id.etInputPsw);
		tbPswFlag = (ToggleButton) findViewById(R.id.tbPswFlag);
		btnNext = (Button) findViewById(R.id.btnNext);
		ivBack = (ImageView) findViewById(R.id.ivBack);
	}

	/**
	 * Inits the events.
	 */
	private void initEvents() {
		btnNext.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		tbPswFlag.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					etInputPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					etInputPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}

			}
		});
		sp_mode.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mode_temp = position;

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNext:
			if (!NetworkUtils.isWifiConnected(this)) {
				ToastUtils.showShort(this, getString(R.string.wifi_first));
				break;
			}

			if (StringUtils.isEmpty(strSsid)) {
				ToastUtils.showShort(this, getString(R.string.wifi_first));
				break;
			}

			Intent intent = new Intent(AutoConfigActivity.this, AirlinkActivity.class);
			intent.putExtra("ssid", strSsid);
			intent.putExtra("Temp", mode_temp);
			strPsw = etInputPsw.getText().toString().trim();
			if (!StringUtils.isEmpty(strPsw)) {
				intent.putExtra("psw", strPsw);
			} else {
				intent.putExtra("psw", "");
			}
			startActivity(intent);
			finish();
			break;
		case R.id.ivBack:
			onBackPressed();
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gizwits.framework.activity.BaseActivity#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mChangeBroadcast, filter);
		if (NetworkUtils.isWifiConnected(this)) {
			handler.sendEmptyMessage(handler_key.CHANGE_WIFI.ordinal());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	public void onPause() {
		super.onPause();
		unregisterReceiver(mChangeBroadcast);

	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(AutoConfigActivity.this, SearchDeviceActivity.class));
		finish();
	}

	/**
	 * 广播监听器，监听wifi连上的广播.
	 *
	 * @author Lien
	 */
	public class ConnecteChangeBroadcast extends BroadcastReceiver {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.BroadcastReceiver#onReceive(android.content.Context,
		 * android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {

			boolean iswifi = NetworkUtils.isWifiConnected(context);
			Log.i("networkchange", "change" + iswifi);
			if (iswifi) {
				handler.sendEmptyMessage(handler_key.CHANGE_WIFI.ordinal());
			}
		}
	}
}
