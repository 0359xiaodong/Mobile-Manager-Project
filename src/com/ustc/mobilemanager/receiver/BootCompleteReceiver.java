package com.ustc.mobilemanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	private static final String TAG = "BootCompleteReceiver";

	private SharedPreferences sp;
	
	private TelephonyManager tm;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//��ȡ֮ǰ�����sim����Ϣ
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String saveSim = sp.getString("sim", "");
		
		//��ȡ��ǰ��sim����Ϣ
		String realSim = tm.getSimSerialNumber();
		
		//�Ƚ��Ƿ�һ��
		if (saveSim.equals(realSim)) {
			//simû�б��������ͬһ��sim��
			Toast.makeText(context, "simû�з������", Toast.LENGTH_LONG).show();
		}else {
			//sim�������,��һ�����Ÿ���ȫ����
			Log.i(TAG, "sim�������");
			Toast.makeText(context, "sim�������", Toast.LENGTH_LONG).show();
		}
		
		
	}

}
