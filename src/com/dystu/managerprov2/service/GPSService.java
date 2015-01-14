package com.dystu.managerprov2.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GPSService extends Service {

	// �õ�λ�÷���
	private LocationManager lm;
	private MyLocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// ʵ����
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		// List<String> providers = lm.getAllProviders();
		// for (String l : providers) {
		// System.out.println(l);
		// }

		listener = new MyLocationListener();
		// ע�����λ�÷���
		// �������ṩ����������
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = lm.getBestProvider(criteria, true);
		lm.requestLocationUpdates(provider, 0, 0, listener);
	}

	class MyLocationListener implements LocationListener {

		/**
		 * ��λ�øı��ʱ��ص��������
		 * 
		 */
		@Override
		public void onLocationChanged(Location location) {

			// ����
			String longitude = "jingdu:" + location.getLongitude() + "\n";
			// γ��
			String latitude = "weidu:" + location.getLatitude()+ "\n";
			// ����
			String accuracy = "accuracy:" + location.getAccuracy()+ "\n";
			
			
			//�����Ÿ���ȫ����
			
			
			
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			
			Editor editor = sp.edit();
			
			editor.putString("lastlocation", longitude +latitude + accuracy);
			
			editor.commit();
			
			

			// TextView textView = new TextView(MainActivity.this);
			// textView.setText(longitude + "\n" + latitude + "\n" + accuracy);
			// setContentView(textView);
		}

		/**
		 * 
		 * ��״̬�����ı��ʱ��
		 * 
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		/**
		 * 
		 * 
		 * ĳһ��λ���ṩ�߿���ʹ���˻ص�
		 */
		@Override
		public void onProviderEnabled(String provider) {

		}

		/**
		 * 
		 * 
		 * ĳһ��λ���ṩ�߲�����ʹ���˻ص�
		 */
		@Override
		public void onProviderDisabled(String provider) {

		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// ȡ������λ�÷���
		lm.removeUpdates(listener);
		listener = null;
	}

}
