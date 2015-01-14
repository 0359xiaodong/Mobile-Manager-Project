package com.dystu.managerprov2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dystu.managerprov2.utils.MD5Utils;

public class EnterPwdActivity extends Activity {

	protected static final String TAG = "EnterPwdActivity";
	private SharedPreferences sp;
	private EditText password;
	private EditText confirm_password;
	private Button ok;
	private Button cancel;

	private ImageView iv_icon;
	private TextView tv_name;

	// ��ǰҪ������Ӧ�ó���İ���

	private String packageName;
	private PackageManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		pm = getPackageManager();
		Intent intent = getIntent();
		packageName = intent.getStringExtra("packname");
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		Log.i(TAG,"isSetupPwd" +  isSetupPwd());
		
		if (isSetupPwd()) {
			hassetup();
		} else {
			
			not();
		}

	}

	private void hassetup() {
		setContentView(R.layout.activity_enter_pwd);// ��������
		password = (EditText) findViewById(R.id.password);
		ok = (Button) findViewById(R.id.ok);
		cancel = (Button) findViewById(R.id.cancel);

		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		tv_name = (TextView) findViewById(R.id.tv_name);

		try {
			ApplicationInfo info = pm.getApplicationInfo(packageName, 0);

			String name = info.loadLabel(pm).toString();

			Drawable icon = info.loadIcon(pm);

			iv_icon.setImageDrawable(icon);
			tv_name.setText(name);

		} catch (Exception e) {
			e.printStackTrace();
		}

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.MAIN");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addCategory("android.intent.category.HOME");
				intent.addCategory("android.intent.category.MONKEY");

				startActivity(intent);
			}
		});

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password1 = password.getText().toString().trim();
				String savedPassword = sp.getString("passwordt", null);// ȡ�����ܺ������
				if (TextUtils.isEmpty(password1)) {
					Toast.makeText(EnterPwdActivity.this, "���벻��Ϊ��!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (MD5Utils.md5Password(password1).equals(savedPassword)) {
					// ���߿��Ź�������������������ȷ�ˣ�������ʱ��ֹͣ����
					// �Զ���㲥ʵ�����֮���ͨ�ţ�Activity��Service��,��ʱֹͣ����
					Intent intent = new Intent();

					intent.setAction("com.dystu.managerprov2.TEMPSTOP");

					intent.putExtra("packname", packageName);

					sendBroadcast(intent);

					finish();
				} else {
					Toast.makeText(EnterPwdActivity.this, "�������!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void not() {
		setContentView(R.layout.activity_setup_pwd);// ��������
		
		password = (EditText) findViewById(R.id.password);
		confirm_password = (EditText) findViewById(R.id.confirm_password);
		ok = (Button) findViewById(R.id.ok);
		cancel = (Button) findViewById(R.id.cancel);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		tv_name = (TextView) findViewById(R.id.tv_name);

		try {
			ApplicationInfo info = pm.getApplicationInfo(packageName, 0);

			String name = info.loadLabel(pm).toString();

			Drawable icon = info.loadIcon(pm);

			iv_icon.setImageDrawable(icon);
			tv_name.setText(name);

		} catch (Exception e) {
			e.printStackTrace();
		}

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.MAIN");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addCategory("android.intent.category.HOME");
				intent.addCategory("android.intent.category.MONKEY");

				startActivity(intent);
			}
		});

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ȡ������
				String password1 = password.getText().toString().trim();
				String password2 = confirm_password.getText().toString()
						.trim();
				if (TextUtils.isEmpty(password1)
						|| TextUtils.isEmpty(password2)) {
					Toast.makeText(EnterPwdActivity.this, "���벻��Ϊ��!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				// �ж��Ƿ�һ��
				if (password1.equals(password2)) {
					Editor editor = sp.edit();
					editor.putString("passwordt",
							MD5Utils.md5Password(password1));// ������ܺ������
					editor.commit();

					Intent intent = new Intent();

					intent.setAction("com.dystu.managerprov2.TEMPSTOP");

					intent.putExtra("packname", packageName);

					sendBroadcast(intent);

					finish();

				} else {
					Toast.makeText(EnterPwdActivity.this, "������������벻һ��!",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
	}

	// =======================================================

	/**
	 * �ж��Ƿ����ù�����
	 * 
	 * @return
	 */
	private boolean isSetupPwd() {

		String password = sp.getString("passwordt", null);
		return !TextUtils.isEmpty(password);

	}

	/**
	 * 
	 * 
	 * ������
	 * 
	 */

	@Override
	public void onBackPressed() {

		// <action android:name="android.intent.action.MAIN" />
		// <category android:name="android.intent.category.HOME" />
		// <category android:name="android.intent.category.DEFAULT" />
		// <category android:name="android.intent.category.MONKEY"/>
		//

		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.MONKEY");

		startActivity(intent);
		// ���е�Activity��С��������ִ��onDestroy����������ִֻ��onStop����������

	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

}
