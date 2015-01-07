package com.ustc.mobilemanager.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.ustc.mobilemanager.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallSmsService extends Service {

	public static final String TAG = "CallSmsService";
	private InnerSmsReceiver receiver;

	private BlackNumberDao dao;

	private TelephonyManager tm;

	private MyListener listener;

	@Override
	public void onCreate() {

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		receiver = new InnerSmsReceiver();

		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");

		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

		registerReceiver(receiver, filter);

		dao = new BlackNumberDao(this);

		super.onCreate();
	}

	private class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String mode = dao.findMode(incomingNumber);
				if ("1".equals(mode) || "3".equals(mode)) {
					// �Ҷϵ绰
					Log.i(TAG, "�Ҷϵ绰");

					// �۲���м�¼���ݿ����ݵı仯
					// ���м�¼��uri·��
					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true,
							new CallLogObserver(incomingNumber, new Handler()));

					endCall();// ������һ�������������е�Զ�̷���ķ������������ú󣬺��м�¼���ܻ�û�����ɣ���������������м�¼
					// ɾ�����м�¼
					// ����һ��Ӧ�ó����˽�е���ϵ�����ݿ�

					// deleteCallLog(incomingNumber);//��������endCall()����һ������������

				}
				break;
			case TelephonyManager.CALL_STATE_IDLE:

				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:

				break;

			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}

	}

	private class CallLogObserver extends ContentObserver {

		private String incomingNumber;

		public CallLogObserver(String incomingNumber, Handler handler) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}

		@Override
		public void onChange(boolean selfChange) {
			Log.i(TAG, "���ݿ�����ݱ仯�ˣ������˺��м�¼");
			// ȡ��ע��
			getContentResolver().unregisterContentObserver(this);
			deleteCallLog(incomingNumber);
			super.onChange(selfChange);
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * ���������ṩ��ȥɾ��ͨ����¼
	 * 
	 * @param incomingNumber
	 */
	public void deleteCallLog(String incomingNumber) {
		// �����ṩ�߽�����
		ContentResolver resolver = getContentResolver();
		// ���м�¼��uri·��
		Uri uri = Uri.parse("content://call_log/calls");

		// CallLog.CONTENT_URI ֱ��ʹ�ó���Ҳ����

		resolver.delete(uri, "number=?", new String[] { incomingNumber });
		
		//��������query��ͨ����¼�ĺ��뵼�뵽��������

	}

	public void endCall() {

		// IBinder b = ServiceManager.getService(TELEPHONY_SERVICE);
		// ---->ServiceManager�ò�������ô�죿��������

		// ����ServiceManager���ֽ���
		try {
			Class clazz = CallSmsService.class.getClassLoader().loadClass(
					"android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);

			ITelephony.Stub.asInterface(iBinder).endCall();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private class InnerSmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "����������");
			// ��鷢�����Ƿ��Ǻ��������룬���������˶������ػ���ȫ������

			Object[] objs = (Object[]) intent.getExtras().get("pdus");

			for (Object object : objs) {
				SmsMessage smsMessage = SmsMessage
						.createFromPdu((byte[]) object);
				// �õ����ŷ�����
				String sender = smsMessage.getOriginatingAddress();
				String mode = dao.findMode(sender);
				if ("2".equals(mode) || "3".equals(sender)) {
					// ���ض���
					Log.i(TAG, "���ض���");
					abortBroadcast();
				}
			}

		}

	}

	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}

}
