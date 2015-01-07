package com.ustc.mobilemanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	// 1.��������ʶ����
	private GestureDetector detector;
	
	protected SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		
		// setContentView(R.layout.activity_base_setup);
		// 2.ʵ��������ʶ����

		detector = new GestureDetector(this, new SimpleOnGestureListener() {

			/**
			 * 
			 * ���ƻ����ļ�����
			 * 
			 * ��ָ�����滬����ʱ��ص�
			 * 
			 */

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				//������x�Ử������������
				if (Math.abs(velocityX) < 200) {
					Toast.makeText(getApplicationContext(), "������̫����!", Toast.LENGTH_SHORT).show();
					return true;
				}
				
				
				//����б�Ż��������
				if (Math.abs((e2.getRawY() - e1.getRawY())) > 100) {
					Toast.makeText(getApplicationContext(), "����������!", Toast.LENGTH_SHORT).show();
					return true;
				}
				if ((e2.getRawX() - e1.getRawX()) > 200) {
					// ��ʾ��һ��ҳ��:�������һ���
					showBack();
					return true;
				}
				if ((e1.getRawX() - e2.getRawX()) > 200) {
					// ��ʾ��һ��ҳ��:�������󻬶�
					showNext();
					return true;
				}

				return super.onFling(e1, e2, velocityX, velocityY);
			}

		});
	}

	public abstract void showBack();

	public abstract void showNext();

	// ��һ���ĵ���¼�
	public void next(View view) {

		showNext();

	}

	// ��һ���ĵ���¼�
	public void back(View view) {
		showBack();
	}

	// 3.ʹ������ʶ����
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		detector.onTouchEvent(event);

		return super.onTouchEvent(event);
	}

}
