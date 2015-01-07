package com.ustc.mobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {

	private SharedPreferences sp;
	
	private ImageView iv_protecting;
	
	private TextView tv_safenumber;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// �ж�һ�£��Ƿ����������򵼣����û������������ת��������ҳ��ȥ���ã���������ڵ�ǰ��ҳ��
		boolean configed = sp.getBoolean("configed", false);
		if (configed) {
			// �����ֻ�����ҳ��
			setContentView(R.layout.activity_lost_find);
			
			tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
			iv_protecting  = (ImageView) findViewById(R.id.iv_protecting);
			
			//���ð�ȫ����
			String safenumber = sp.getString("safenumber", "");
			if (safenumber != null) {
				tv_safenumber.setText(safenumber);
			}
			
			//���÷���������״̬
			boolean protecting = sp.getBoolean("protecting", false);
			if (protecting) {
				iv_protecting.setImageResource(R.drawable.strongbox_app_lock_ic_locked);
			}else {
				iv_protecting.setImageResource(R.drawable.strongbox_app_lock_ic_unlock);
			}
		} else {
			// ��û������������
			Intent intent = new Intent(LostFindActivity.this,
					Setup1Activity.class);
			startActivity(intent);
			finish();
			// Ҫ��finish()����startActivity(intent)�������ִ��
			overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		}

	}

	public void back(View view) {
		Intent intent = new Intent(LostFindActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
		//Ҫ��finish()����startActivity(intent)�������ִ��
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

	/**
	 * ���½����������ҳ��(TextView�ĵ���¼�)
	 * 
	 * @param view
	 */
	public void reEnterSetup(View view) {
		Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
		startActivity(intent);
		finish();
		// Ҫ��finish()����startActivity(intent)�������ִ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

	}
}
