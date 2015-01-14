package com.dystu.managerprov2.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dystu.managerprov2.R;
import com.dystu.managerprov2.TaskSettingActivity;
import com.dystu.managerprov2.domain.TaskInfo;
import com.dystu.managerprov2.engine.TaskInfoProvider;
import com.dystu.managerprov2.utils.SystemInfoUtils;
import com.dystu.managerprov2.widget.CircleImageView;

public class Anquanfanghu extends Fragment implements OnClickListener {

	/*
	 * ���еĽ��̵�������
	 */
	private TextView tv_process_count;
	/*
	 * ʣ��/���ڴ�
	 */
	private TextView tv_mem_info;

	private TextView tv_status;

	private LinearLayout ll_loading;
	private ListView lv_task_manager;
	// ȫ���Ľ�����Ϣ
	private List<TaskInfo> allTaskInfos;
	// �û����̵ļ���
	private List<TaskInfo> userTaskInfos;
	// ϵͳ���̵ļ���
	private List<TaskInfo> systemTaskInfos;

	private TaskManagerAdapter adapter;
	private int processCount;
	private long availMem;
	private long totalMem;
	private Button selectAll;
	private Button selectOppo;
	private Button killAll;
	private Button enterSetting;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_aqfh, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tv_status = (TextView) getView().findViewById(R.id.tv_status);
		lv_task_manager = (ListView) getView().findViewById(
				R.id.lv_task_manager);
		ll_loading = (LinearLayout) getView().findViewById(R.id.ll_loading);
		tv_mem_info = (TextView) getView().findViewById(R.id.tv_mem_info);
		tv_process_count = (TextView) getView().findViewById(
				R.id.tv_process_count);

		selectAll = (Button) getView().findViewById(R.id.selectAll);
		selectOppo = (Button) getView().findViewById(R.id.selectOppo);
		killAll = (Button) getView().findViewById(R.id.killAll);
		enterSetting = (Button) getView().findViewById(R.id.enterSetting);
		selectAll.setOnClickListener(this);
		selectOppo.setOnClickListener(this);
		killAll.setOnClickListener(this);
		enterSetting.setOnClickListener(this);

		setTitle();
		lv_task_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TaskInfo taskInfo;
				if (position == 0) {
					// ����Ӧ����¼�
					return;
				} else if (position == (userTaskInfos.size() + 1)) {
					// ����Ӧ����¼�
					return;
				} else if (position <= userTaskInfos.size()) {
					taskInfo = userTaskInfos.get(position - 1);
				} else {
					taskInfo = systemTaskInfos.get(position - 1
							- userTaskInfos.size() - 1);
				}

				if (getActivity().getPackageName().equals(
						taskInfo.getPackname())) {
					return;
				}

				System.out.println("--------------" + taskInfo.toString());
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				if (taskInfo.isChecked()) {
					taskInfo.setChecked(false);
					viewHolder.cb_status.setChecked(false);
				} else {
					taskInfo.setChecked(true);
					viewHolder.cb_status.setChecked(true);
				}

			}
		});

		lv_task_manager.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (userTaskInfos != null && systemTaskInfos != null) {
					if (firstVisibleItem > userTaskInfos.size()) {
						tv_status.setText("ϵͳ����:" + systemTaskInfos.size()
								+ "��");
					} else {
						tv_status.setText("�û�����:" + userTaskInfos.size() + "��");
					}
				}
			}
		});

		fillData();

	}

	private void setTitle() {
		processCount = SystemInfoUtils.getRunningProcessCount(getActivity());
		tv_process_count.setText("�����н���:" + processCount + "��");

		availMem = SystemInfoUtils.getAvailMem(getActivity());
		totalMem = SystemInfoUtils.getTotalMem(getActivity());

		tv_mem_info.setText("ʣ��/���ڴ�:"
				+ Formatter.formatFileSize(getActivity(), availMem) + "/"
				+ Formatter.formatFileSize(getActivity(), totalMem));
	}

	/**
	 * ��������
	 */
	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				allTaskInfos = TaskInfoProvider.getTaskInfo(getActivity());
				userTaskInfos = new ArrayList<TaskInfo>();
				systemTaskInfos = new ArrayList<TaskInfo>();
				for (TaskInfo taskInfo : allTaskInfos) {
					if (taskInfo.isUserTask()) {
						userTaskInfos.add(taskInfo);
					} else {
						systemTaskInfos.add(taskInfo);
					}
				}
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						if (adapter == null) {
							adapter = new TaskManagerAdapter();
							lv_task_manager.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						setTitle();
					}
				});
			};
		}.start();
	}

	private class TaskManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			SharedPreferences sp = getActivity().getSharedPreferences("config",
					Context.MODE_PRIVATE);
			if (sp.getBoolean("showsystem", false)) {
				return userTaskInfos.size() + 1 + systemTaskInfos.size() + 1;

			} else {
				return userTaskInfos.size() + 1;
			}

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TaskInfo taskInfo;
			if (position == 0) {
				TextView tv = new TextView(getActivity());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("�û�����:" + userTaskInfos.size() + "��");
				return tv;
			} else if (position == (userTaskInfos.size() + 1)) {
				TextView tv = new TextView(getActivity());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("ϵͳ����:" + systemTaskInfos.size() + "��");
				return tv;
			} else if (position <= userTaskInfos.size()) {
				taskInfo = userTaskInfos.get(position - 1);
			} else {
				taskInfo = systemTaskInfos.get(position - 1
						- userTaskInfos.size() - 1);
			}

			View view;
			ViewHolder viewHolder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;

				viewHolder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getActivity(), R.layout.list_item_taskinfo,
						null);

				viewHolder = new ViewHolder();

				viewHolder.iv_task_icon = (CircleImageView) view
						.findViewById(R.id.iv_task_icon);
				viewHolder.tv_task_name = (TextView) view
						.findViewById(R.id.tv_task_name);
				viewHolder.tv_task_memsize = (TextView) view
						.findViewById(R.id.tv_task_memsize);
				viewHolder.cb_status = (CheckBox) view
						.findViewById(R.id.cb_status);
				view.setTag(viewHolder);

			}

			viewHolder.iv_task_icon.setImageDrawable(taskInfo.getIcon());
			viewHolder.tv_task_name.setText(taskInfo.getName());
			viewHolder.tv_task_memsize.setText("�ڴ�ռ��:"
					+ Formatter.formatFileSize(getActivity(),
							taskInfo.getMemSize()));
			viewHolder.cb_status.setChecked(taskInfo.isChecked());

			if (getActivity().getPackageName().equals(taskInfo.getPackname())) {
				viewHolder.cb_status.setVisibility(View.INVISIBLE);
			} else {
				viewHolder.cb_status.setVisibility(View.VISIBLE);
			}

			return view;
		}

		@Override
		public Object getItem(int position) {
			TaskInfo taskInfo;
			if (position == 0) {
				return null;
			} else if (position == (userTaskInfos.size() + 1)) {
				return null;
			} else if (position <= userTaskInfos.size()) {
				taskInfo = userTaskInfos.get(position - 1);
			} else {
				taskInfo = systemTaskInfos.get(position - 1
						- userTaskInfos.size() - 1);
			}
			return taskInfo;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	static class ViewHolder {
		CircleImageView iv_task_icon;
		TextView tv_task_name;
		TextView tv_task_memsize;
		CheckBox cb_status;
	}

	/**
	 * ȫѡ
	 * 
	 * @param view
	 */
	/*public void selectAll(View view) {
		for (TaskInfo info : allTaskInfos) {
			if (getActivity().getPackageName().equals(info.getPackname())) {
				continue;
			}
			info.setChecked(true);
		}
		// ����֪ͨ���ݸ�����
		adapter.notifyDataSetChanged();
	}*/

	/**
	 * ��ѡ
	 * 
	 * @param view
	 *//*
	public void selectOppo(View view) {
		for (TaskInfo info : allTaskInfos) {
			if (getActivity().getPackageName().equals(info.getPackname())) {
				continue;
			}
			info.setChecked(!info.isChecked());
		}
		adapter.notifyDataSetChanged();
	}*/

	/**
	 * ����
	 * 
	 * �������һ���ܴ��BUG
	 * 
	 * @param view
	 */
	/*public void killAll(View view) {

		// ActivityManager am = (ActivityManager)
		// getSystemService(ACTIVITY_SERVICE);
		//
		// int count = 0;
		//
		// int savedMem = 0;
		//
		// // ��¼��ɱ������Ŀ
		// List<TaskInfo> killedTaskInfos = new ArrayList<TaskInfo>();
		//
		// for (TaskInfo info : allTaskInfos) {
		// if (info.isChecked()) {// ����ѡ�ˣ�ɱ���������
		// // ��Ҫ���Ȩ�ޣ� <uses-permission
		// // android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>
		// am.killBackgroundProcesses(info.getPackname());
		//
		// if (info.isUserTask()) {// �û�����
		// userTaskInfos.remove(info);
		// } else {
		// systemTaskInfos.remove(info);
		// }
		//
		// killedTaskInfos.add(info);
		//
		// count++;
		// savedMem += info.getMemSize();
		// // �������ڵ�����ʱ���޸ļ��ϣ��мǣ���������
		// // allTaskInfos.remove(info);
		// }
		//
		// }
		// allTaskInfos.removeAll(killedTaskInfos);
		// adapter.notifyDataSetChanged();
		// Toast.makeText(
		// this,
		// "ɱ����" + count + "������," + "�ͷ���"
		// + Formatter.formatFileSize(this, savedMem) + "���ڴ�", 1)
		// .show();
		// processCount -= count;
		// availMem += savedMem;
		// tv_process_count.setText("�����н���:" + processCount + "��");
		// tv_mem_info.setText("ʣ��/���ڴ�:"
		// + Formatter.formatFileSize(this, availMem) + "/"
		// + Formatter.formatFileSize(this, totalMem));

		// =================��һ���汾==============================

		ActivityManager am = (ActivityManager) getActivity().getSystemService(
				Context.ACTIVITY_SERVICE);
		int total = 0;
		long savedMem = 0;
		List<TaskInfo> killedTaskInfos = new ArrayList<TaskInfo>();
		for (TaskInfo info : userTaskInfos) {
			if (info.isChecked()) {
				am.killBackgroundProcesses(info.getPackname());
				total++;
				savedMem += info.getMemSize();
				// userTaskInfos.remove(info);
				killedTaskInfos.add(info);
			}
		}
		for (TaskInfo info : systemTaskInfos) {
			if (info.isChecked()) {
				am.killBackgroundProcesses(info.getPackname());
				total++;
				savedMem += info.getMemSize();
				// systemTaskInfos.remove(info);
				killedTaskInfos.add(info);
			}
		}

		for (TaskInfo info : killedTaskInfos) {
			if (info.isUserTask()) {
				userTaskInfos.remove(info);
			} else {
				systemTaskInfos.remove(info);
			}
		}

		// ���û�һ����˾���� �����û������ʲô���顣
		Toast.makeText(
				getActivity(),
				"ɱ����" + total + "������,�ͷ���"
						+ Formatter.formatFileSize(getActivity(), savedMem)
						+ "���ڴ�", 1).show();
		processCount -= total;
		availMem += savedMem;
		tv_process_count.setText("�����н���:" + processCount + "��");
		tv_mem_info.setText("ʣ��/���ڴ棺"
				+ Formatter.formatFileSize(getActivity(), availMem) + "/"
				+ Formatter.formatFileSize(getActivity(), totalMem));
		adapter.notifyDataSetChanged();

	}*/

	/**
	 * ����
	 * 
	 * @param view
	 */
	/*
	public void enterSetting(View view) {
		TaskSettingActivity.actionStart(getActivity());
		startActivityForResult(getActivity().getIntent(), 0);
	}*/

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		adapter.notifyDataSetChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectAll:

			for (TaskInfo info : allTaskInfos) {
				if (getActivity().getPackageName().equals(info.getPackname())) {
					continue;
				}
				info.setChecked(true);
			}
			// ����֪ͨ���ݸ�����
			adapter.notifyDataSetChanged();
			
			break;
		case R.id.selectOppo:

			for (TaskInfo info : allTaskInfos) {
				if (getActivity().getPackageName().equals(info.getPackname())) {
					continue;
				}
				info.setChecked(!info.isChecked());
			}
			adapter.notifyDataSetChanged();
			
			break;
		case R.id.killAll:
			
			ActivityManager am = (ActivityManager) getActivity().getSystemService(
					Context.ACTIVITY_SERVICE);
			int total = 0;
			long savedMem = 0;
			List<TaskInfo> killedTaskInfos = new ArrayList<TaskInfo>();
			for (TaskInfo info : userTaskInfos) {
				if (info.isChecked()) {
					am.killBackgroundProcesses(info.getPackname());
					total++;
					savedMem += info.getMemSize();
					// userTaskInfos.remove(info);
					killedTaskInfos.add(info);
				}
			}
			for (TaskInfo info : systemTaskInfos) {
				if (info.isChecked()) {
					am.killBackgroundProcesses(info.getPackname());
					total++;
					savedMem += info.getMemSize();
					// systemTaskInfos.remove(info);
					killedTaskInfos.add(info);
				}
			}

			for (TaskInfo info : killedTaskInfos) {
				if (info.isUserTask()) {
					userTaskInfos.remove(info);
				} else {
					systemTaskInfos.remove(info);
				}
			}

			// ���û�һ����˾���� �����û������ʲô���顣
			Toast.makeText(
					getActivity(),
					"ɱ����" + total + "������,�ͷ���"
							+ Formatter.formatFileSize(getActivity(), savedMem)
							+ "���ڴ�", 1).show();
			processCount -= total;
			availMem += savedMem;
			tv_process_count.setText("�����н���:" + processCount + "��");
			tv_mem_info.setText("ʣ��/���ڴ棺"
					+ Formatter.formatFileSize(getActivity(), availMem) + "/"
					+ Formatter.formatFileSize(getActivity(), totalMem));
			adapter.notifyDataSetChanged();

			break;
		case R.id.enterSetting:
			TaskSettingActivity.actionStart(getActivity());
			
			/**
			 * 
			 * ����������⣬��ʱ�Ľ�������ǽ�launchmode�����£���ϸ���嵥�ļ�
			 * 
			 */
			startActivityForResult(getActivity().getIntent(), 0);
			break;

		default:
			break;
		}

	}

}
