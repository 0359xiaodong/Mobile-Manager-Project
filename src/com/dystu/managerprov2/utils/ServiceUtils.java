package com.dystu.managerprov2.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 
 * У��ĳ�������Ƿ񻹻���
 * 
 * @author
 * 
 */

public class ServiceUtils {
	/**
	 * 
	 * У��ĳ�������Ƿ񻹻���
	 * 
	 * @param service
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String service) {
		// У������Ƿ񻹻���

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		// ������Ŀ
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);

		for (RunningServiceInfo runningServiceInfo : runningServices) {
			// �������еķ��������
			String name = runningServiceInfo.service.getClassName();
			if (service.equals(name)) {
				return true;
			}
		}

		return false;

	}

}
