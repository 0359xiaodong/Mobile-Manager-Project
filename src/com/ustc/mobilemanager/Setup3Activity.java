package com.ustc.mobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupActivity {

	private EditText number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setup3);
		number = (EditText) findViewById(R.id.number);
		number.setText(sp.getString("safenumber", null));
	}

	@Override
	public void showBack() {
		Intent intent = new Intent(this, Setup2Activity.class);
		startActivity(intent);
		finish();
		// Ҫ��finish()����startActivity(intent)�������ִ��
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);

	}

	@Override
	public void showNext() {
		//Ӧ�ñ����°�ȫ����
		
		String phonenumber = number.getText().toString().trim();
		if (TextUtils.isEmpty(phonenumber)) {
			Toast.makeText(this, "��ȫ����δ���ã��������ð�ȫ����.", Toast.LENGTH_LONG).show();
			return;
			
		}
		//Ӧ�ñ����°�ȫ����
		Editor editor = sp.edit();
		editor.putString("safenumber", phonenumber);
		editor.commit();
		Intent intent = new Intent(this, Setup4Activity.class);
		startActivity(intent);
		finish();
		// Ҫ��finish()����startActivity(intent)�������ִ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

	}

	/**
	 * ѡ����ϵ�˵İ�ť�ĵ���¼�
	 * 
	 * @param view
	 */
	public void selectContact(View view) {
		Intent intent = new Intent(Setup3Activity.this,
				SelectContactActivity.class);
		// ϣ�����ظ�����һ���绰����,����ʹ������ķ���
		startActivityForResult(intent, 0);
		// Ҫ��finish()����startActivity(intent)�������ִ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data == null) {
			return;
		}
		// �绰����ġ�-��ȥ��
		String phone = data.getStringExtra("phone").replace("-", "");

		number.setText(phone);

	}

}
