package com.ustc.mobilemanager;

import com.ustc.mobilemanager.db.dao.NumberAddressQueryUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberAddressQueryActivity extends Activity {
	
	private static final String TAG = "NumberAddressActivity";

	private EditText ed_phone;
	
	private TextView address_result;
	
	
	private Vibrator vibrator;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_number_address);
		address_result = (TextView) findViewById(R.id.address_result);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		
		ed_phone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s!=null&&s.length() > 3) {
					//��ѯ���ݿ⣬������ʾ���
					
					String address = NumberAddressQueryUtils.queryNumber(s.toString());
					
					address_result.setText(address);
					
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		
		
		
	}
	
	
	
	
	
	
	/**
	 * 
	 * ��ѯ��������صİ�ť�ĵ���¼�
	 * 
	 * 
	 * @param view
	 */
	public void query(View view){
		String phoneNumber = ed_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			//
			Toast.makeText(this, "����Ϊ��!", Toast.LENGTH_LONG).show();
			
		//	vibrator.vibrate(2000);
			
			long[] pattern = {200,200,300,300,1000,2000};
			//-1 :���ظ�   0:ѭ����
			vibrator.vibrate(pattern, -1);
			
			
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
			
			ed_phone.startAnimation(animation);
			
			
			return;
		}else {
			//���ݿ��ѯ���������
			
			//дһ�������࣬ȥ��ѯ���ݿ�
			String address  = NumberAddressQueryUtils.queryNumber(phoneNumber);
			Log.i(TAG, "��ѯ�ĺ���Ϊ:" + phoneNumber);
			address_result.setText(address);
		}
		
	}
	
	
	
}
