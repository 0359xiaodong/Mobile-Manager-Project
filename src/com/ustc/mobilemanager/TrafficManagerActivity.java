package com.ustc.mobilemanager;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.view.Window;
import android.widget.SlidingDrawer;


/**
 * 
 *proc/uid_stat/uid/tcp_snd
 * 
 * @author Administrator
 *
 */

public class TrafficManagerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		
		PackageManager pm = getPackageManager();
		
		List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(0);
		
		for (ApplicationInfo applicationInfo : applicationInfos) {
			int uid = applicationInfo.uid;
			
			long tx = TrafficStats.getUidTxBytes(uid);//�ϴ�
			
			long rx = TrafficStats.getUidRxBytes(uid);//����
			//��������ֵΪ-1 �������Ӧ�ó���û�в����������߲���ϵͳ��֧������ͳ��
			
		}
		
		TrafficStats.getMobileTxBytes();//��ȡ�ֻ�3g/2g�����ϴ���������
		TrafficStats.getMobileTxBytes();//�ֻ�2g/3g���ص�������
		
		
		TrafficStats.getTotalTxBytes();//��ȡ�ϴ���������
		TrafficStats.getTotalRxBytes();//��ȡ���ص�������
		
		
		setContentView(R.layout.activity_traffic_manager);
		
		
	}
}
