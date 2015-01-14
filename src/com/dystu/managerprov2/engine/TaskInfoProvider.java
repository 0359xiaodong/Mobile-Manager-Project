package com.dystu.managerprov2.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.dystu.managerprov2.R;
import com.dystu.managerprov2.domain.TaskInfo;

/**
 * 
 * �ṩ�ֻ�����Ľ�����Ϣ
 * 
 * @author
 * 
 */
public class TaskInfoProvider {

	/**
	 * ��ȡ���еĽ�����Ϣ
	 * 
	 * @param context
	 * @return
	 */
	public static List<TaskInfo> getTaskInfo(Context context) {

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();

		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		for (RunningAppProcessInfo processInfo : processInfos) {

			TaskInfo taskInfo = new TaskInfo();

			// ���������ǰ���
			String packname = processInfo.processName;

			taskInfo.setPackname(packname);

			// import android.os.Debug.MemoryInfo�������ܵ�����
			MemoryInfo[] memoryInfos = am
					.getProcessMemoryInfo(new int[] { processInfo.pid });
			// ת��byte������ת��
			long memsize = memoryInfos[0].getTotalPrivateDirty() * 1024l;
			taskInfo.setMemSize(memsize);

			try {
				// PackageInfo packageInfo = pm.getPackageInfo(packname, 0);
				// packageInfo.applicationInfo

				ApplicationInfo applicationInfo = pm.getApplicationInfo(
						packname, 0);
				Drawable icon = applicationInfo.loadIcon(pm);
				taskInfo.setIcon(icon);

				String name = applicationInfo.loadLabel(pm).toString();

				taskInfo.setName(name);

				if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					// �û�����
					taskInfo.setUserTask(true);

				} else {
					// ϵͳ����

					taskInfo.setUserTask(false);
				}

			} catch (Exception e) {
				e.printStackTrace();
				//ϵͳ�ں˽���   û������
				taskInfo.setName(packname);
				taskInfo.setIcon(context.getResources().getDrawable(R.drawable.icon));
			}
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}

}
