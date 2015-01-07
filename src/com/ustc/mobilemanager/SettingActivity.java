package com.ustc.mobilemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.ustc.mobilemanager.service.AddressService;
import com.ustc.mobilemanager.service.CallSmsService;
import com.ustc.mobilemanager.service.WatchDogService;
import com.ustc.mobilemanager.ui.SettingClickView;
import com.ustc.mobilemanager.ui.SettingItemView;
import com.ustc.mobilemanager.utils.ServiceUtils;

public class SettingActivity extends Activity {

	// �����Ƿ����Զ�����
	private SettingItemView siv_update;
	// �����Ƿ�����ʾ���������
	private SettingItemView siv_show_address;
	//���������ص�����
	private SettingItemView siv_callsms_safe;
	//���������Ź�
	private SettingItemView siv_watchdog;
	
	private Intent callSmsSafeService;
	//���������Ź�
	private Intent watchdogIntent;

	private SharedPreferences sp;

	private Intent showAddress;

	// ���ù�������ʾ�򱳾�
	private SettingClickView scv_changebg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
		siv_callsms_safe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
		siv_watchdog = (SettingItemView) findViewById(R.id.siv_watchdog);
		
		boolean update = sp.getBoolean("update", false);
		if (update) {
			// �Զ������Ѿ�����
			siv_update.setChecked(true);
			// siv_update.setDesc("�Զ������Ѿ�����");
		} else {
			// �Զ������Ѿ��ر�
			siv_update.setChecked(false);
			// siv_update.setDesc("�Զ������Ѿ��ر�");
		}
		siv_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();

				// �ж��Ƿ�ѡ��
				// �Ѿ�ѡ����
				if (siv_update.isChecked()) {

					siv_update.setChecked(false);
					// siv_update.setDesc("�Զ������Ѿ��ر�");
					editor.putBoolean("update", false);
				} else {
					// û��ѡ��
					siv_update.setChecked(true);
					// siv_update.setDesc("�Զ������Ѿ�����");
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});
		showAddress = new Intent(this, AddressService.class);

		boolean isServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.ustc.mobilemanager.service.AddressService");

		if (isServiceRunning) {
			// ��Ϊѡ���״̬
			siv_show_address.setChecked(true);
		} else {
			// ��Ϊ��ѡ��״̬
			siv_show_address.setChecked(false);
		}

		siv_show_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (siv_show_address.isChecked()) {
					// ��Ϊ��ѡ��״̬
					siv_show_address.setChecked(false);

					stopService(showAddress);
				} else {
					// ��Ϊѡ���״̬
					siv_show_address.setChecked(true);
					startService(showAddress);
				}

			}
		});
		
		//=========================
		
		callSmsSafeService = new Intent(this, CallSmsService.class);

		siv_callsms_safe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (siv_callsms_safe.isChecked()) {
					// ��Ϊ��ѡ��״̬
					siv_callsms_safe.setChecked(false);

					stopService(callSmsSafeService);
				} else {
					// ��Ϊѡ���״̬
					siv_callsms_safe.setChecked(true);
					startService(callSmsSafeService);
				}

			}
		});
		
		//==========================
		
		//=================================
		
		
		
		watchdogIntent = new Intent(this, WatchDogService.class);

		siv_watchdog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (siv_watchdog.isChecked()) {
					// ��Ϊ��ѡ��״̬
					siv_watchdog.setChecked(false);

					stopService(watchdogIntent);
				} else {
					// ��Ϊѡ���״̬
					siv_watchdog.setChecked(true);
					startService(watchdogIntent);
				}

			}
		});
		
		
		boolean isWatchDogServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.ustc.mobilemanager.service.WatchDogService");

		if (isWatchDogServiceRunning) {
			// ��Ϊѡ���״̬
			siv_watchdog.setChecked(true);
		} else {
			// ��Ϊ��ѡ��״̬
			siv_watchdog.setChecked(false);
		}
		
		//===============================================

		// ���ú�������صı���
		scv_changebg = (SettingClickView) findViewById(R.id.scv_changebg);
		scv_changebg.setTitle("��������ʾ����");
		final String[] items = { "��͸��", "������", "��ʿ��", "������", "ƻ����" };
		int which = sp.getInt("which", 0);
		scv_changebg.setDesc(items[which]);

		scv_changebg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int dd = sp.getInt("which", 0);
				// ����һ���Ի���
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("��������ʾ����");
				builder.setSingleChoiceItems(items, dd,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// ����ѡ�����
								Editor editor = sp.edit();
								editor.putInt("which", which);
								editor.commit();
								scv_changebg.setDesc(items[which]);

								// ȡ���Ի���
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("cancel", null);
				builder.show();

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		showAddress = new Intent(this, AddressService.class);

		boolean isServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.ustc.mobilemanager.service.AddressService");

		if (isServiceRunning) {
			// ��Ϊѡ���״̬
			siv_show_address.setChecked(true);
		} else {
			// ��Ϊ��ѡ��״̬
			siv_show_address.setChecked(false);
		}
		
		//=========================
		
		boolean isCallSmsServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.ustc.mobilemanager.service.CallSmsService");

		if (isCallSmsServiceRunning) {
			// ��Ϊѡ���״̬
			siv_callsms_safe.setChecked(true);
		} else {
			// ��Ϊ��ѡ��״̬
			siv_callsms_safe.setChecked(false);
		}

		//============================

		
		boolean isWatchDogServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.ustc.mobilemanager.service.WatchDogService");

		if (isWatchDogServiceRunning) {
			// ��Ϊѡ���״̬
			siv_watchdog.setChecked(true);
		} else {
			// ��Ϊ��ѡ��״̬
			siv_watchdog.setChecked(false);
		}
		
		//===================================
		
		
	}

}
