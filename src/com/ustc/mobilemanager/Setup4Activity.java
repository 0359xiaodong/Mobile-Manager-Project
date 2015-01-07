package com.ustc.mobilemanager;

import com.ustc.mobilemanager.receiver.MyAdmin;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupActivity {

	private SharedPreferences sp;
	
	private CheckBox cb_protecting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setup4);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		cb_protecting = (CheckBox) findViewById(R.id.cb_protecting);
		
		boolean protecting = sp.getBoolean("protecting", false);
		
		if (protecting) {
			//�ֻ������Ѿ�����
			cb_protecting.setText("�ֻ������Ѿ�����");
			cb_protecting.setChecked(true);
		}else {
			//�ֻ�����û�п���
			cb_protecting.setText("�ֻ�����û�п���");
			cb_protecting.setChecked(false);
		}
		cb_protecting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					cb_protecting.setText("�ֻ������Ѿ�����");
				}else {
					cb_protecting.setText("�ֻ�����û�п���");
				}
				
				//����ѡ���״̬
				Editor editor = sp.edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();
			}
		});
		
		
	}

	@Override
	public void showBack() {
		Intent intent = new Intent(this, Setup3Activity.class);
		startActivity(intent);
		finish();
		// Ҫ��finish()����startActivity(intent)�������ִ��
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);

	}

	
	public void ok(View view) {
		Editor edit = sp.edit();
		edit.putBoolean("configed", true);
		edit.commit();
		Intent intent = new Intent(this, LostFindActivity.class);
		startActivity(intent);
		finish();
		// Ҫ��finish()����startActivity(intent)�������ִ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

	}
	public void lock(View view){
		
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
    	//��Ҫ����˭
    	ComponentName mDeviceAdminSample = new ComponentName(this, MyAdmin.class);
    	
    	intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
    	
    	//��ʾ�û���������Ա��Ȩ��
    	intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"��������һ������!");
    	startActivity(intent);
	}

	@Override
	public void showNext() {
		
		
	}
}
