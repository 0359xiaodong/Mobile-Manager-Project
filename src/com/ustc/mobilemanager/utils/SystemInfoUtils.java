package com.ustc.mobilemanager.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * ϵͳ��Ϣ�Ĺ�����
 * 
 * @author
 * 
 */
public class SystemInfoUtils {

	/**
	 * ��ȡ�������еĽ��̵�����
	 * 
	 * @param context
	 * @return
	 */
	public static int getRunningProcessCount(Context context) {

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();

		return infos.size();
	}

	/**
	 * ��ȡ�ֻ����õ�ʣ����ڴ�
	 * 
	 * @param context
	 * @return
	 */
	public static long getAvailMem(Context context) {

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

	/**
	 * ��ȡ�ֻ����ڴ�
	 * 
	 * @param context
	 * @return long byte
	 */
	public static long getTotalMem(Context context) {

		// ActivityManager am = (ActivityManager) context
		// .getSystemService(Context.ACTIVITY_SERVICE);
		// MemoryInfo outInfo = new MemoryInfo();
		// am.getMemoryInfo(outInfo);
		//
		// return outInfo.totalMem;

		try {
			File file = new File("/proc/meminfo");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = br.readLine();
			StringBuilder sb = new StringBuilder();
			// MemTotal: 484792 kB
			for (char c : line.toCharArray()) {
				if (c >= '0' && c <= '9') {
					sb.append(c);
				}
			}
			// ��λ��byte ����ת��
			return Long.parseLong(sb.toString()) * 1024;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

}
