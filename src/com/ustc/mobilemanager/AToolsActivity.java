package com.ustc.mobilemanager;

import com.ustc.mobilemanager.utils.SmsUtils;
import com.ustc.mobilemanager.utils.SmsUtils.BackUpCallBack;
import com.ustc.mobilemanager.utils.SmsUtils.RestoreSms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AToolsActivity extends Activity {
	
	private ProgressDialog pd;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_atools);
	}

	/**
	 * 
	 * ����¼�
	 * 
	 * �����������ز�ѯ��ҳ��
	 * 
	 * @param view
	 */
	public void numberQuery(View view) {
		Intent intent = new Intent(this, NumberAddressQueryActivity.class);
		startActivity(intent);
		// Ҫ��finish()����startActivity(intent)�������ִ��
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	/**
	 * 
	 * ���ű���
	 * 
	 * @param view
	 */
	public void smsBackup(View view) {
		
		pd = new ProgressDialog(AToolsActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		pd.setMessage("���ڱ��ݶ���");
		pd.show();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SmsUtils.backupSms(AToolsActivity.this,new BackUpCallBack() {
						
						@Override
						public void onBackUp(int progress) {
							pd.setProgress(progress);
						}
						
						@Override
						public void beforeBackUp(int max) {
							pd.setMax(max);
						}
					});
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(AToolsActivity.this, "���ݳɹ�", 0)
									.show();

						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(AToolsActivity.this, "����ʧ��", 0)
									.show();

						}
					});
					
				}finally{
					pd.dismiss();
				}

			}
		}).start();

	}

	/**
	 * 
	 * ���Ż�ԭ
	 * 
	 * @param view
	 */
	public void smsRestore(View view) {
		
		pd = new ProgressDialog(AToolsActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		pd.setMessage("���ڻ�ԭ����");
		pd.show();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					SmsUtils.restoreSms(getApplicationContext(), false, new RestoreSms() {
						
						@Override
						public void onRestore(int progress) {
							pd.setProgress(progress);
						}
						
						@Override
						public void beforeRestore(String max) {
							pd.setMax(Integer.parseInt(max));
						}
					});
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(AToolsActivity.this, "��ԭ�ɹ�", 0)
									.show();

						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(AToolsActivity.this, "��ԭʧ��", 0)
									.show();

						}
					});
					
				}finally{
					pd.dismiss();
				}

			}
		}).start();

	}

}
