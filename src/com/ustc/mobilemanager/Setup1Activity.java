package com.ustc.mobilemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Setup1Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setup1);
	}
	@Override
	public void showBack() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
		// Ҫ��finish()����startActivity(intent)�������ִ��
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
	@Override
	public void showNext() {
		Intent intent = new Intent(this, Setup2Activity.class);
		startActivity(intent);
		finish();
		// Ҫ��finish()����startActivity(intent)�������ִ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

}
