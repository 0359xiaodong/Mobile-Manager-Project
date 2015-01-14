package com.dystu.managerprov2.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.dystu.managerprov2.CleanCacheActivity;
import com.dystu.managerprov2.R;
import com.dystu.managerprov2.utils.NumberAddressQueryUtils;

public class AddressService extends Service {

	protected static final String TAG = "AddressService";

	private TelephonyManager tm;

	private MyListenerPhone listenerPhone;

	private OutCallReceiver outCallReceiver;

	// ���������
	private WindowManager wm;

	private View view;

	private WindowManager.LayoutParams params;

	private SharedPreferences sp;

	long[] mHits = new long[2];

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// ����������ڲ���
	// �㲥�����ߵ��������ںͷ���һ��

	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// ����������õ��Ĳ���ȥ�ĵ绰����
			String phone = getResultData();
			// ��ѯ���ݿ�
			String address = NumberAddressQueryUtils.queryNumber(phone);
			// Toast.makeText(context, address, 1).show();
			MyToast(address);
		}
	}

	private class MyListenerPhone extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// ������������
				// ��ѯ���ݿ�Ĳ���
				String address = NumberAddressQueryUtils
						.queryNumber(incomingNumber);
				// Toast.makeText(getApplicationContext(), address, 1).show();
				MyToast(address);

				break;

			case TelephonyManager.CALL_STATE_OFFHOOK:
				if (view != null) {
					wm.removeView(view);
				}

				break;

			case TelephonyManager.CALL_STATE_IDLE:// �绰�Ŀ���״̬
				if (view != null) {
					wm.removeView(view);
				}

				break;

			default:
				break;
			}

		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		listenerPhone = new MyListenerPhone();

		tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);

		// ����ȥע��һ���㲥������
		outCallReceiver = new OutCallReceiver();

		IntentFilter filter = new IntentFilter();

		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");

		registerReceiver(outCallReceiver, filter);

		// ʵ��������

		wm = (WindowManager) getSystemService(WINDOW_SERVICE);

	}

	/**
	 * 
	 * �Զ�����˾
	 * 
	 * @param address
	 */
	public void MyToast(String address) {
		view = View.inflate(this, R.layout.address_show, null);
		TextView textview = (TextView) view.findViewById(R.id.tv_address);
		// ����¼�
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "onClick");
				// src ������Դ����
				// srcPos ��Դ������Ǹ�λ�ÿ�ʼ����.
				// dst Ŀ������
				// dstPos ��Ŀ��������Ǹ�λ�ӿ�ʼд����
				// length ������Ԫ�صĸ���
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					/*
					 * Toast.makeText(AddressService.this, "��ϲ�㣬3�ε���ˡ�",
					 * 0).show();
					 */
					// ˫������

					params.x = wm.getDefaultDisplay().getWidth() / 2
							- view.getWidth() / 2;

					wm.updateViewLayout(view, params);

					// ��¼
					Editor editor = sp.edit();
					editor.putInt("lastX", params.x);
					editor.commit();

				}
			}
		});

		// ��view����һ�������ļ�����
		view.setOnTouchListener(new OnTouchListener() {

			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Log.i(TAG, "ACTION_DOWN");
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					// Log.i(TAG, "(" + startX + "," + startY + ")");
					break;
				case MotionEvent.ACTION_MOVE:
					Log.i(TAG, "ACTION_MOVE");
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					// Log.i(TAG, "(" + newX + "," + newY + ")");
					int dx = newX - startX;
					int dy = newY - startY;
					// Log.i(TAG, "ƫ����" + "(" + dx + "," + dy + ")");

					params.x += dx;
					params.y += dy;

					// ���ǵ��߽�����
					if (params.x < 0) {
						params.x = 0;
					}
					if (params.y < 0) {
						params.y = 0;
					}
					if (params.x > (wm.getDefaultDisplay().getWidth() - view
							.getWidth())) {
						params.x = (wm.getDefaultDisplay().getWidth() - view
								.getWidth());
					}
					if (params.y > (wm.getDefaultDisplay().getHeight() - view
							.getHeight())) {
						params.y = (wm.getDefaultDisplay().getHeight() - view
								.getHeight());
					}

					wm.updateViewLayout(view, params);

					// ���³�ʼ����ָ��λ��
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;

				case MotionEvent.ACTION_UP:
					Log.i(TAG, "ACTION_UP");
					// ��¼�ؼ��������Ͻǵ�����
					Editor editor = sp.edit();
					editor.putInt("lastX", params.x);
					editor.putInt("lastY", params.y);
					editor.commit();

					break;

				default:
					break;
				}
				// false��ʾ�¼�������û�д����꣬Ϊtrue���ʾ�������,��Ҫ�ø��ؼ� ��������Ӧ�����¼�
				return false;
			}
		});

		// "��͸��","������","��ʿ��","������","ƻ����"
		int[] ids = { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		sp = getSharedPreferences("config", MODE_PRIVATE);
		view.setBackgroundResource(ids[sp.getInt("which", 0)]);
		textview.setText(address);

		params = new WindowManager.LayoutParams();

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;

		params.gravity = Gravity.TOP + Gravity.LEFT;

		params.x = sp.getInt("lastX", 0);
		params.y = sp.getInt("lastY", 0);

		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		// ��׿ϵͳ���е绰���ȼ��Ĵ������ͣ��ǵ����Ȩ�� <uses-permission
		// android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		wm.addView(view, params);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
		listenerPhone = null;

		// ȡ��ע��
		unregisterReceiver(outCallReceiver);
		outCallReceiver = null;

	}
	
	public static void actionStart(Context context){
		Intent intent = new Intent(context, AddressService.class);
		
		
		
		
	}

}
