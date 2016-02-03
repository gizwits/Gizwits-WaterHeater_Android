/**
 * Project Name:XPGSdkV4AppBase
 * File Name:ForgetPswActivity.java
 * Package Name:com.gizwits.framework.activity.account
 * Date:2015-1-27 14:44:57
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
package com.gizwits.framework.activity.account;

import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gizwits.heater.R;
import com.gizwits.framework.activity.BaseActivity;
import com.gizwits.framework.config.Configs;
import com.gizwits.framework.widget.MyInputFilter;
import com.xpg.common.useful.StringUtils;
import com.xpg.ui.utils.ToastUtils;
import com.xtremeprog.xpgconnect.XPGWifiSDK;

/**
 * ClassName: Class ForgetPswActivity. <br/>
 * 忘记密码，该类主要包含了两个修改密码的方法，手机号注册的用户通过获取验证码修改密码，邮箱注册的用户需要通过注册邮箱的重置邮件进行重置。<br/>
 * date: 2014-12-09 17:27:10 <br/>
 * 
 * @author StephenC
 */
public class ForgetPswActivity extends BaseActivity implements OnClickListener {

	/**
	 * The ll CaptchaCode
	 */
	private LinearLayout llCaptchaCode;

	/**
	 * The pb CaptchaCode_loading
	 */
	private ProgressBar CaptchaCode_loading;

	/**
	 * The et InputCaptchaCode_farget
	 */
	private EditText etInputCaptchaCode_farget;

	/**
	 * The iv GetCaptchaCode_farget
	 */
	private ImageView ivGetCaptchaCode_farget;

	/**
	 * The btn ReGetCaptchaCode_farget
	 */
	private Button btnReGetCaptchaCode_farget;

	/**
	 * The ll InputCode
	 */
	private LinearLayout llInputCode;

	/**
	 * The ll InputPsw
	 */
	private LinearLayout llInputPsw;

	/**
	 * The tv dialog.
	 */
	private TextView tvDialog;

	/**
	 * The et name.
	 */
	private EditText etName;

	/**
	 * The et input code.
	 */
	private EditText etInputCode;

	/**
	 * The et input psw.
	 */
	private EditText etInputPsw;

	/**
	 * The et input email.
	 */
	private EditText etInputEmail;

	/**
	 * The btn get code.
	 */
	private Button btnGetCode;

	/**
	 * The btn re get code.
	 */
	private Button btnReGetCode;

	/**
	 * The btn sureEmail.
	 */
	private Button btnSureEmail;

	/**
	 * The btn phoneReset.
	 */
	private Button btnPhoneReset;

	/**
	 * The btn emailReset.
	 */
	private Button btnEmailReset;

	/**
	 * The ll input menu.
	 */
	private LinearLayout llInputMenu;

	/**
	 * The ll input phone.
	 */
	private LinearLayout llInputPhone;

	/**
	 * The rl input email.
	 */
	private RelativeLayout rlInputEmail;

	/**
	 * The rl dialog.
	 */
	private RelativeLayout rlDialog;

	/**
	 * The btn sure.
	 */
	private Button btnSure;

	/**
	 * The iv back.
	 */
	private ImageView ivBack;

	/**
	 * The tb psw flag.
	 */
	private ToggleButton tbPswFlag;

	/**
	 * The ui_statu statuNow.
	 */
	private ui_statu statuNow;

	/**
	 * The secondleft.
	 */
	int secondleft = 60;

	/**
	 * The timer.
	 */
	Timer timer;

	/**
	 * The dialog.
	 */
	ProgressDialog dialog;

	/**
	 * ClassName: Enum handler_key. <br/>
	 * <br/>
	 * date: 2014-11-26 17:51:10 <br/>
	 * 
	 * @author Lien
	 */
	private enum handler_key {

		/**
		 * 倒计时通知
		 */
		TICK_TIME,

		/**
		 * 修改成功
		 */
		CHANGE_SUCCESS,

		/**
		 * Toast弹出通知
		 */
		TOAST,
		/**
		 * 获取图片验证码
		 */
		CaptchaCode,

		/**
		 * 修改界面
		 */
		CHANGE

	}

	/**
	 * ClassName: Enum ui_statu. <br/>
	 * UI状态枚举类<br/>
	 * date: 2014-12-3 10:52:52 <br/>
	 * 
	 * @author Lien
	 */
	private enum ui_statu {

		/**
		 * 主菜单
		 */
		DEFAULT,

		/**
		 * 通过手机重置密码
		 */
		PHONE,

		/**
		 * 通过邮箱重置密码
		 */
		EMAIL,

		/**
		 * 修改UI
		 */
		CHANGE_UI
	}

	/**
	 * The handler.
	 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key) {

			case TICK_TIME:
				secondleft--;
				if (secondleft <= 0) {
					timer.cancel();
					btnReGetCode.setEnabled(true);
					btnReGetCode.setText("重新获取验证码");
					btnReGetCode.setBackgroundResource(R.drawable.button_blue_short);
				} else {
					btnReGetCode.setText(
							secondleft + "" + getResources().getText(R.string.forget_password_get_verifycode3));

				}
				break;

			case CHANGE_SUCCESS:
				finish();
				break;

			case TOAST:
				tvDialog.setText((String) msg.obj);
				rlDialog.setVisibility(View.VISIBLE);
				dialog.cancel();
				break;
			case CaptchaCode:
				XPGWifiSDK.sharedInstance().getCaptchaCode(Configs.APP_SECRET);
				break;
			case CHANGE:
				toogleUI(ui_statu.CHANGE_UI);
				isStartTimer();
				break;
			}
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gizwits.framework.activity.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_reset);
		initViews();
		initEvents();
	}

	/**
	 * Inits the views.
	 */
	private void initViews() {
		etInputCaptchaCode_farget = (EditText) findViewById(R.id.etInputCaptchaCode_farget);
		btnReGetCaptchaCode_farget = (Button) findViewById(R.id.btnReGetCaptchaCode_farget);
		llInputCode = (LinearLayout) findViewById(R.id.llInputCode);
		llInputPsw = (LinearLayout) findViewById(R.id.llInputPsw);
		ivGetCaptchaCode_farget = (ImageView) findViewById(R.id.ivGetCaptchaCode_farget);
		CaptchaCode_loading = (ProgressBar) findViewById(R.id.CaptchaCode_loading);
		llCaptchaCode=(LinearLayout) findViewById(R.id.llCaptchaCode);
		//
		tvDialog = (TextView) findViewById(R.id.tvDialog);
		etName = (EditText) findViewById(R.id.etName);
		etInputCode = (EditText) findViewById(R.id.etInputCode);
		etInputPsw = (EditText) findViewById(R.id.etInputPsw);
		etInputEmail = (EditText) findViewById(R.id.etInputEmail);
		btnGetCode = (Button) findViewById(R.id.btnGetCode);
		btnReGetCode = (Button) findViewById(R.id.btnReGetCode);
		btnSure = (Button) findViewById(R.id.btnSure);
		btnSureEmail = (Button) findViewById(R.id.btnSureEmail);
		btnPhoneReset = (Button) findViewById(R.id.btnPhoneReset);
		btnEmailReset = (Button) findViewById(R.id.btnEmailReset);
		llInputMenu = (LinearLayout) findViewById(R.id.llInputMenu);
		llInputPhone = (LinearLayout) findViewById(R.id.llInputPhone);
		rlInputEmail = (RelativeLayout) findViewById(R.id.rlInputEmail);
		rlDialog = (RelativeLayout) findViewById(R.id.rlDialog);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tbPswFlag = (ToggleButton) findViewById(R.id.tbPswFlag);
		toogleUI(ui_statu.DEFAULT);
		dialog = new ProgressDialog(this);
		dialog.setMessage("处理中，请稍候...");

		MyInputFilter filter = new MyInputFilter();
		etInputPsw.setFilters(new InputFilter[] { filter });
	}

	/**
	 * Inits the events.
	 */
	private void initEvents() {
		btnReGetCaptchaCode_farget.setOnClickListener(this);
		//
		rlDialog.setOnClickListener(this);
		btnGetCode.setOnClickListener(this);
		btnReGetCode.setOnClickListener(this);
		btnSureEmail.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnPhoneReset.setOnClickListener(this);
		btnEmailReset.setOnClickListener(this);
		// tvPhoneSwitch.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		tbPswFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					etInputPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					etInputPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
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
		case R.id.btnGetCode:
			String phone2 = etName.getText().toString().trim();
			if (StringUtils.isEmpty(phone2) || phone2.length() != 11) {
				ToastUtils.showShort(this, "请输入正确的手机号码。");
				return;
			}
			if (etInputCaptchaCode_farget.getText().toString().isEmpty()) {
				ToastUtils.showShort(this, "请输入图片验证码。");
				return;
			}
			sendVerifyCode(phone2);
			break;
		case R.id.btnReGetCode:
			toogleUI(ui_statu.PHONE);
			break;
		case R.id.btnSure:
			doChangePsw();
			break;
		case R.id.ivBack:
			onBackPressed();
			break;
		case R.id.btnSureEmail:
			String email = etInputEmail.getText().toString().trim();
			if (StringUtils.isEmpty(email) || !email.contains("@")) {
				ToastUtils.showShort(this, "请输入正确的账号。");
				return;
			}

			getEmail(email);
			break;
		case R.id.btnPhoneReset:
			toogleUI(ui_statu.PHONE);
			break;
		case R.id.btnEmailReset:
			toogleUI(ui_statu.EMAIL);
			break;
		case R.id.rlDialog:
			rlDialog.setVisibility(View.GONE);
			break;
		case R.id.btnReGetCaptchaCode_farget:
			ivGetCaptchaCode_farget.setVisibility(View.GONE);
			CaptchaCode_loading.setVisibility(View.VISIBLE);
			handler.sendEmptyMessage(handler_key.CaptchaCode.ordinal());
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (rlDialog.getVisibility() == View.VISIBLE) {
			rlDialog.setVisibility(View.GONE);
			return;
		}
		switch (statuNow) {
		case DEFAULT:
			finish();
			break;
		case PHONE:
			toogleUI(ui_statu.DEFAULT);
			break;
		case EMAIL:
			toogleUI(ui_statu.DEFAULT);
			break;
		case CHANGE_UI:
			toogleUI(ui_statu.PHONE);
			break;
		}
	}

	/**
	 * Toogle ui.
	 * 
	 * @param statu
	 *            the statu
	 */
	private void toogleUI(ui_statu statu) {
		statuNow = statu;
		switch (statu) {
		case DEFAULT:
			llInputMenu.setVisibility(View.VISIBLE);
			llInputPhone.setVisibility(View.GONE);
			rlInputEmail.setVisibility(View.GONE);
			etInputCode.setText("");
			etInputPsw.setText("");
			etInputEmail.setText("");		
			etName.setText("");
			break;
		case PHONE:
			llInputMenu.setVisibility(View.GONE);
			llInputPhone.setVisibility(View.VISIBLE);
			rlInputEmail.setVisibility(View.GONE);
			CaptchaCode_loading.setVisibility(View.VISIBLE);
			ivGetCaptchaCode_farget.setVisibility(View.GONE);
			llCaptchaCode.setVisibility(View.VISIBLE);
			etName.setEnabled(true);
			etInputCaptchaCode_farget.setText("");
			btnGetCode.setVisibility(View.VISIBLE);
			//
			llInputCode.setVisibility(View.GONE);
			llInputPsw.setVisibility(View.GONE);
			btnSure.setVisibility(View.GONE);
			handler.sendEmptyMessage(handler_key.CaptchaCode.ordinal());
			break;
		case EMAIL:
			llInputMenu.setVisibility(View.GONE);
			llInputPhone.setVisibility(View.GONE);
			rlInputEmail.setVisibility(View.VISIBLE);
			break;

		case CHANGE_UI:
			llInputMenu.setVisibility(View.GONE);
			llInputPhone.setVisibility(View.VISIBLE);
			rlInputEmail.setVisibility(View.GONE);
			llCaptchaCode.setVisibility(View.GONE);
			btnGetCode.setVisibility(View.GONE);
			llInputCode.setVisibility(View.VISIBLE);
			llInputPsw.setVisibility(View.VISIBLE);
			btnSure.setVisibility(View.VISIBLE);
			etInputCode.setText("");
			etInputPsw.setText("");
			etName.setEnabled(false);
			break;
		}
	}

	/**
	 * Gets the email.
	 * 
	 * @param email
	 *            the email
	 * @return the email
	 */
	private void getEmail(String email) {
		mCenter.cChangePassworfByEmail(email);
		dialog.show();
	}

	/**
	 * 执行手机号重置密码操作
	 */
	private void doChangePsw() {

		String phone = etName.getText().toString().trim();
		String code = etInputCode.getText().toString().trim();
		String password = etInputPsw.getText().toString();
		if (phone.length() != 11) {
			Toast.makeText(this, "电话号码格式不正确", Toast.LENGTH_SHORT).show();
			return;
		}
		if (code.length() == 0) {
			Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		if (password.contains(" ")) {
			Toast.makeText(this, "密码不能有空格", Toast.LENGTH_SHORT).show();
			return;
		}
		if (password.length() < 6 || password.length() > 16) {
			Toast.makeText(this, "密码长度应为6~16", Toast.LENGTH_SHORT).show();
			return;
		}
		mCenter.cChangeUserPasswordWithCode(phone, code, password);
		dialog.show();
	}

	/**
	 * 发送验证码
	 * 
	 * @param phone
	 *            the phone
	 */
	private void sendVerifyCode(final String phone) {
		String CaptchaCode = etInputCaptchaCode_farget.getText().toString();
		dialog.show();

		// 发送请求验证码指令
		// mCenter.cRequestSendVerifyCode(phone);
		// 发送请求验证码指令
		Log.i("AppTest", tokenString + ", " + captchaidString + ", " + CaptchaCode + ", " + phone);
		mCenter.cRequestSendVerifyCode(tokenString, captchaidString, CaptchaCode, phone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gizwits.framework.activity.BaseActivity#didRequestSendVerifyCode(int,
	 * java.lang.String)
	 */
	/*
	 * @Override protected void didRequestSendVerifyCode(int error, String
	 * errorMessage) { Log.i("error message ", error + " " + errorMessage); if
	 * (error == 0) {// 发送成功 Message msg = new Message(); msg.what =
	 * handler_key.TOAST.ordinal(); msg.obj = "发送成功"; handler.sendMessage(msg);
	 * } else {// 发送失败 Message msg = new Message(); msg.what =
	 * handler_key.TOAST.ordinal(); msg.obj = errorMessage;
	 * handler.sendMessage(msg); } }
	 */
	/**
	 * 图片验证码回调
	 */
	private String tokenString, captchaidString, captcthishaURL_String;

	protected void didGetCaptchaCode(int result, java.lang.String errorMessage, java.lang.String token,
			java.lang.String captchaId, java.lang.String captcthishaURL) {
		Log.e("AppTest",
				"图片验证码回调" + result + ", " + errorMessage + ", " + token + ", " + captchaId + ", " + captcthishaURL);
		tokenString = token;
		captchaidString = captchaId;
		captcthishaURL_String = captcthishaURL;
		new load_image().execute(captcthishaURL_String);
	}

	class load_image extends AsyncTask<String, Void, Drawable> {

		/**
		 * 加载网络图片
		 * 
		 * @param url
		 * @return
		 */
		private Drawable LoadImageFromWebOperations(String url) {
			InputStream is = null;
			Drawable d = null;
			try {
				is = (InputStream) new URL(url).getContent();
				d = Drawable.createFromStream(is, "src name");
				return d;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected Drawable doInBackground(String... params) {
			Drawable drawable = LoadImageFromWebOperations(params[0]);
			return drawable;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			super.onPostExecute(result);
			ivGetCaptchaCode_farget.setImageDrawable(result);
			CaptchaCode_loading.setVisibility(View.GONE);
			ivGetCaptchaCode_farget.setVisibility(View.VISIBLE);
		}

	}

	protected void didRequestSendPhoneSMSCode(int result, java.lang.String errorMessage) {
		Log.e("AppTest", result + ", " + errorMessage);
		if (result == 0) {// 发送成功
			Message msg = new Message();
			msg.what = handler_key.TOAST.ordinal();
			msg.obj = "Send Succeessful";
			handler.sendMessage(msg);
			handler.sendEmptyMessage(handler_key.CHANGE.ordinal());
		} else {// 发送失败
			Message msg = new Message();
			msg.what = handler_key.TOAST.ordinal();
			msg.obj = errorMessage;
			handler.sendMessage(msg);
			handler.sendEmptyMessage(handler_key.CaptchaCode.ordinal());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gizwits.framework.activity.BaseActivity#didChangeUserPassword(int,
	 * java.lang.String)
	 */
	@Override
	protected void didChangeUserPassword(int error, String errorMessage) {
		if (error == 0) {// 修改成功
			Message msg = new Message();
			msg.what = handler_key.TOAST.ordinal();
			if (statuNow == ui_statu.EMAIL) {
				Drawable img = getResources().getDrawable(R.drawable.slib_tick);
				img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
				tvDialog.setCompoundDrawables(img, null, null, null);
				msg.obj = "已发送至您的邮箱，\n请登录邮箱查收！";
			} else {
				msg.obj = "设置成功";
			}
			handler.sendMessage(msg);
			handler.sendEmptyMessageDelayed(handler_key.CHANGE_SUCCESS.ordinal(), 2000);

		} else {// 修改失败
			Message msg = new Message();
			msg.what = handler_key.TOAST.ordinal();
			msg.obj = errorMessage;
			handler.sendMessage(msg);
		}
		super.didChangeUserPassword(error, errorMessage);
	}

	public void isStartTimer() {
		btnReGetCode.setEnabled(false);
		btnReGetCode.setBackgroundResource(R.drawable.button_gray_short);
		secondleft = 60;
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(handler_key.TICK_TIME.ordinal());
			}
		}, 1000, 1000);
	}
}
