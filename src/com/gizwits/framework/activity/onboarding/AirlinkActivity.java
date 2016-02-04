/**
 * Project Name:XPGSdkV4AppBase
 * File Name:AirlinkActivity.java
 * Package Name:com.gizwits.framework.activity.onboarding
 * Date:2015-1-27 14:45:48
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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gizwits.heater.R;
import com.gizwits.framework.activity.BaseActivity;
import com.gizwits.framework.activity.device.DeviceListActivity;
import com.xpg.common.system.IntentUtils;
import com.xpg.common.useful.StringUtils;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiSDK;
import com.xtremeprog.xpgconnect.XPGWifiSDK.XPGWifiGAgentType;

/**
 * ClassName: Class AirlinkActivity. <br/>
 * 配置结果 <br/>
 *
 * @author Lien
 */
public class AirlinkActivity extends BaseActivity implements OnClickListener {

	/**
	 * The btn config.
	 */
	private Button btnConfig;

	/**
	 * The btn retry.
	 */
	private Button btnRetry;

	/**
	 * The btn softap.
	 */
	private Button btnSoftap;

	/**
	 * The iv back.
	 */
	private ImageView ivBack;

	/**
	 * The ll start config.
	 */
	private LinearLayout llStartConfig;

	/**
	 * The ll configing.
	 */
	private LinearLayout llConfiging;

	/**
	 * The ll config failed.
	 */
	private LinearLayout llConfigFailed;

	/** The tv tick. */
	private TextView tvTick;

	/** The secondleft. */
	int secondleft = 60;

	/** The timer. */
	private Timer timer;

	/** The str s sid. */
	private String strSSid;

	/** The str psw. */
	private String strPsw;

	private int mode_temp;
	ArrayList<XPGWifiGAgentType> types;
	ArrayList<XPGWifiSDK.XPGWifiGAgentType> typeList;

	/** The UI_STATE now. */
	private UI_STATE UiStateNow;

	/**
	 * ClassName: Enum handler_key. <br/>
	 * <br/>
	 * date: 2014-11-26 17:51:10 <br/>
	 *
	 * @author Lien
	 */
	private enum handler_key {

		/**
		 * The tick time.
		 */
		TICK_TIME,

		/**
		 * The reg success.
		 */
		CONFIG_SUCCESS,

		/**
		 * The toast.
		 */

		CONFIG_FAILED,

	}

	/**
	 * The handler.
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key) {

			case TICK_TIME:
				secondleft--;
				if (secondleft <= 0) {
					timer.cancel();
					sendEmptyMessage(handler_key.CONFIG_FAILED.ordinal());
				} else {
					tvTick.setText(secondleft + "");
				}
				break;

			case CONFIG_SUCCESS:
				IntentUtils.getInstance().startActivity(AirlinkActivity.this, DeviceListActivity.class);
				finish();
				break;

			case CONFIG_FAILED:
				showLayout(UI_STATE.Result);
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
		setContentView(R.layout.activity_airlink);
		initViews();
		initEvents();
		initData();
	}

	/**
	 * Inits the events.
	 */
	private void initEvents() {
		btnConfig.setOnClickListener(this);
		btnRetry.setOnClickListener(this);
		btnSoftap.setOnClickListener(this);
		ivBack.setOnClickListener(this);
	}

	/**
	 * Inits the views.
	 */
	private void initViews() {
		btnConfig = (Button) findViewById(R.id.btnConfig);
		btnRetry = (Button) findViewById(R.id.btnRetry);
		btnSoftap = (Button) findViewById(R.id.btnSoftap);
		tvTick = (TextView) findViewById(R.id.tvTick);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		llStartConfig = (LinearLayout) findViewById(R.id.llStartConfig);
		llConfiging = (LinearLayout) findViewById(R.id.llConfiging);
		llConfigFailed = (LinearLayout) findViewById(R.id.llConfigFailed);
		showLayout(UI_STATE.Ready);
	}

	/**
	 * Inits the data.
	 */
	private void initData() {
		if (getIntent() != null) {
			if (!StringUtils.isEmpty(getIntent().getStringExtra("ssid"))) {
				strSSid = getIntent().getStringExtra("ssid");
			}
			if (!StringUtils.isEmpty(getIntent().getStringExtra("psw"))) {
				strPsw = getIntent().getStringExtra("psw");
			} else {
				strPsw = "";
			}
			mode_temp = getIntent().getIntExtra("Temp", 0);
		}
		typeList = new ArrayList<XPGWifiSDK.XPGWifiGAgentType>();
		typeList.add(XPGWifiSDK.XPGWifiGAgentType.XPGWifiGAgentTypeMXCHIP);// 庆科
		typeList.add(XPGWifiSDK.XPGWifiGAgentType.XPGWifiGAgentTypeHF);// 汉枫
		typeList.add(XPGWifiSDK.XPGWifiGAgentType.XPGWifiGAgentTypeRTK);// 瑞昱
		typeList.add(XPGWifiSDK.XPGWifiGAgentType.XPGWifiGAgentTypeWM);// 联盛德
		typeList.add(XPGWifiSDK.XPGWifiGAgentType.XPGWifiGAgentTypeESP);// 乐鑫
		typeList.add(XPGWifiSDK.XPGWifiGAgentType.XPGWifiGAgentTypeQCA);// 高通
		typeList.add(XPGWifiSDK.XPGWifiGAgentType.XPGWifiGAgentTypeTI);// TI
	}

	private enum UI_STATE {
		Ready, Setting, Result;
	}

	private void showLayout(UI_STATE ui) {
		UiStateNow = ui;
		switch (ui) {
		case Ready:
			llStartConfig.setVisibility(View.VISIBLE);
			llConfiging.setVisibility(View.GONE);
			llConfigFailed.setVisibility(View.GONE);
			ivBack.setVisibility(View.VISIBLE);
			break;
		case Setting:
			llStartConfig.setVisibility(View.GONE);
			llConfiging.setVisibility(View.VISIBLE);
			llConfigFailed.setVisibility(View.GONE);
			ivBack.setVisibility(View.GONE);
			break;
		case Result:
			llConfigFailed.setVisibility(View.VISIBLE);
			llConfiging.setVisibility(View.GONE);
			llStartConfig.setVisibility(View.GONE);
			ivBack.setVisibility(View.VISIBLE);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnConfig:
			// airlink配置
			startAirlink();
			break;
		case R.id.btnRetry:
			onBackPressed();
			break;
		case R.id.btnSoftap:
			// spftap配置
			Intent intent = new Intent(AirlinkActivity.this, SoftApConfigActivity.class);
			intent.putExtra("ssid", strSSid);
			intent.putExtra("psw", strPsw);
			startActivity(intent);
			finish();
			break;
		case R.id.ivBack:
			onBackPressed();
			break;
		}

	}

	/**
	 * Start airlink.
	 */
	private void startAirlink() {
		secondleft = 60;
		tvTick.setText(secondleft + "");
		showLayout(UI_STATE.Setting);
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(handler_key.TICK_TIME.ordinal());
			}
		}, 1000, 1000);
		types = new ArrayList<XPGWifiSDK.XPGWifiGAgentType>();
		types.add(typeList.get(mode_temp));
		mCenter.cSetAirLink(strSSid, strPsw, types);
	}

	@Override
	public void onBackPressed() {
		switch (UiStateNow) {
		case Ready:
			startActivity(new Intent(AirlinkActivity.this, AutoConfigActivity.class));
			finish();
			break;
		case Setting:
			break;
		case Result:
			startActivity(new Intent(AirlinkActivity.this, SearchDeviceActivity.class));
			finish();
			break;

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gizwits.framework.activity.BaseActivity#didSetDeviceWifi(int,
	 * com.xtremeprog.xpgconnect.XPGWifiDevice)
	 */
	@Override
	protected void didSetDeviceWifi(int error, XPGWifiDevice device) {
		if (error == 0) {
			handler.sendEmptyMessage(handler_key.CONFIG_SUCCESS.ordinal());
		} else {
			handler.sendEmptyMessage(handler_key.CONFIG_FAILED.ordinal());
		}
	}
}
