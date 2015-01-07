package com.ustc.mobilemanager.receiver;

import com.ustc.mobilemanager.R;
import com.ustc.mobilemanager.service.GPSService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReceiver";

	private SharedPreferences sp;
	
	//�豸���Է���
	private DevicePolicyManager dpm;

	@Override
	public void onReceive(Context context, Intent intent) {
		// д���ܶ��ŵĴ���

		Object[] objs = (Object[]) intent.getExtras().get("pdus");

		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		dpm = (DevicePolicyManager)context.getSystemService(context.DEVICE_POLICY_SERVICE);

		for (Object b : objs) {
			// �����ĳһ������
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) b);
			// ������
			String sender = sms.getOriginatingAddress();

			String safeNumber = sp.getString("safenumber", "");
			// ��������
			String body = sms.getMessageBody();

			if (sender.contains(safeNumber)) {
				// Log.i(TAG, sender);
				// Toast.makeText(context, sender, 0).show();

				if ("#*location*#".equals(body)) {
					// �õ��ֻ���GPS
					Log.i(TAG, "�õ��ֻ���GPS");
					//��������
					Intent i = new Intent(context,GPSService.class);
					context.startService(i);
					
					SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
					String lastlocation = sp.getString("lastlocation", null);
					if (TextUtils.isEmpty(lastlocation)) {
						//λ��û�еõ�
						
						SmsManager.getDefault().sendTextMessage(sender, null, "���ڻ�ȡλ����...", null, null);
						
					}else {
						//�õ�λ����
						SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
						
					}
					
					// ������㲥��ֹ��
					abortBroadcast();
				} else if ("#*alarm*#".equals(body)) {
					
					MediaPlayer player = MediaPlayer.create(context,R.raw.danxiaogui);
					
					player.setLooping(false);
					player.start();
					
					Log.i(TAG, "���ű�������");
					abortBroadcast();
				} else if ("#*wipedata*#".equals(body)) {
					Log.i(TAG, "Զ����������");
					//Σ�գ�����
					abortBroadcast();
				} else if ("#*lockscreen*#".equals(body)) {
					Log.i(TAG, "Զ������");
					ComponentName who = new ComponentName(context,MyAdmin.class);
			    	if (dpm.isAdminActive(who)) {
						dpm.lockNow();//����
						dpm.resetPassword("123", 0);//������������
						//dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//���sd��������
						//dpm.wipeData(0);//�ָ���������
					}else {
						Toast.makeText(context, "��û�п��������Ȩ��!", 1).show();
						return;
					}
					abortBroadcast();
				}
			}
		}

	}

}
