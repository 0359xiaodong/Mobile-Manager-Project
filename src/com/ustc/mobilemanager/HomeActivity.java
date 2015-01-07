package com.ustc.mobilemanager;

import com.ustc.mobilemanager.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	
	protected static final String TAG = "HomeActivity";


	private GridView list_home;
	
	
	private MyAdapter adapter;
	
	
	private SharedPreferences sp;
	
	private static String[] names = 
		{
		"�ֻ�����","ͨѶ��ʿ","�������",
		"���̹���","����ͳ��","�ֻ�ɱ��",
		"��������","�߼�����","��������"
		};
	
	private static int[] ids = {
		
		R.drawable.shoujifangdao,R.drawable.tongxunweishi,R.drawable.ruanjianguanli,
		R.drawable.jinchengguanli,R.drawable.liuliangtongji,R.drawable.shoujishadu,
		R.drawable.huancunqingli,R.drawable.gaojigongju,R.drawable.shezhizhongxin
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		list_home = (GridView) findViewById(R.id.list_home);
		adapter = new MyAdapter();
		list_home.setAdapter(adapter);
		list_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0://�����ֻ���������
					showLostFindDialog();
					break;
				case 1://������������ؽ���
					Intent intent1 = new Intent(HomeActivity.this, CallSmsSaveActivity.class);
					startActivity(intent1);
					//Ҫ��finish()����startActivity(intent)�������ִ��
					overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
					break;
				case 2://��������������
					Intent intent2 = new Intent(HomeActivity.this, AppManagerActivity.class);
					startActivity(intent2);
					//Ҫ��finish()����startActivity(intent)�������ִ��
					overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
					break;
				case 3://������̹������
					Intent intent3 = new Intent(HomeActivity.this, TaskManagerActivity.class);
					startActivity(intent3);
					//Ҫ��finish()����startActivity(intent)�������ִ��
					overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
					break;
				case 4://���������������
					Intent intent4 = new Intent(HomeActivity.this, TrafficManagerActivity.class);
					startActivity(intent4);
					//Ҫ��finish()����startActivity(intent)�������ִ��
					overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
					break;
				case 5://����ɱ������
					Intent intent5 = new Intent(HomeActivity.this, AntiVirusActivity.class);
					startActivity(intent5);
					//Ҫ��finish()����startActivity(intent)�������ִ��
					overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
					break;
				case 6://���뻺���������
					Intent intent6 = new Intent(HomeActivity.this, CleanCacheActivity.class);
					startActivity(intent6);
					//Ҫ��finish()����startActivity(intent)�������ִ��
					overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
					break;
				case 7://����߼�����
					Intent intent7 = new Intent(HomeActivity.this, AToolsActivity.class);
					startActivity(intent7);
					//Ҫ��finish()����startActivity(intent)�������ִ��
					overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
					
				break;
					
				case 8://������������
					Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
					startActivity(intent);
					//Ҫ��finish()����startActivity(intent)�������ִ��
					overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
					break;

				default:
					break;
				}
			}
		});
	}
	
	protected void showLostFindDialog() {
		//�ж��Ƿ����ù�����
		if (isSetupPwd()) {
			//�Ѿ�����������,������������Ի���
			showEnterDialog();
		}else {
			//û�����ù�����,����������������ĶԻ���
			showSetupPwdDialog();
		}
	}
	private EditText password;
	private EditText confirm_password;
	private Button ok;
	private Button cancel;
	private AlertDialog dialog;
	
	/**
	 * 
	 * ��������Ի���
	 */
	private void showSetupPwdDialog() {
		
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		//�Զ���һ�������ļ�
		View view = View.inflate(this, R.layout.dialog_setup_dialog, null);
		
		password = (EditText) view.findViewById(R.id.password);
		confirm_password = (EditText) view.findViewById(R.id.confirm_password);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ȡ�����Ի���
				dialog.dismiss();
			}
		});
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ȡ������
				String password1 = password.getText().toString().trim();
				String password2 = confirm_password.getText().toString().trim();
				if (TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
					Toast.makeText(HomeActivity.this, "���벻��Ϊ��!", Toast.LENGTH_SHORT).show();
					return;
				}
				//�ж��Ƿ�һ��  
				if (password1.equals(password2)) {
					//һ�µĻ����ͱ������룬�ѶԻ�����������Ҫ�����ֻ�����ҳ��
					Editor editor = sp.edit();
					editor.putString("password", MD5Utils.md5Password(password1));//������ܺ������
					editor.commit();
					dialog.dismiss();
					Log.i(TAG, "һ�µĻ����ͱ������룬�ѶԻ�����������Ҫ�����ֻ�����ҳ��");
					Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
					startActivity(intent);
					//finish();
					//Ҫ��finish()����startActivity(intent)�������ִ��
					overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
				}else {
					Toast.makeText(HomeActivity.this, "������������벻һ��!", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
		
		builder.setView(view);
		dialog = builder.show();
		
	}

	/**
	 * 
	 * ��������Ի���
	 * 
	 */
	private void showEnterDialog() {
		
		AlertDialog.Builder builder = new Builder(HomeActivity.this);
		//�Զ���һ�������ļ�
		View view = View.inflate(this, R.layout.dialog_enter_dialog, null);
		
		password = (EditText) view.findViewById(R.id.password);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.cancel);
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ȡ�����Ի���
				dialog.dismiss();
			}
		});
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String password1 = password.getText().toString().trim();
				String savedPassword = sp.getString("password", null);//ȡ�����ܺ������
				if (TextUtils.isEmpty(password1)) {
					Toast.makeText(HomeActivity.this, "���벻��Ϊ��!", Toast.LENGTH_SHORT).show();
					return;
				}
				if (MD5Utils.md5Password(password1).equals(savedPassword)) {
					//�����������֮ǰ���õ�����,�������ͬ����ʾ�û�������
					//�ѶԻ�������,������ҳ��
					dialog.dismiss();
					Log.i(TAG, "�ѶԻ�������,�����ֻ�����ҳ��");
					
					Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
					startActivity(intent);
					//finish();
					//Ҫ��finish()����startActivity(intent)�������ִ��
					overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
					
				}else {
					Toast.makeText(HomeActivity.this, "�������!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		builder.setView(view);
		dialog = builder.show();
		//dialog.setView(view, 0, 0, 0, 0);
		//dialog.show();
	}

	/**
	 * �ж��Ƿ����ù�����
	 * 
	 * @return
	 */
	private boolean isSetupPwd(){
		String password = sp.getString("password", null);
		
//		if (TextUtils.isEmpty(password)) {
//			return false;
//		}else {
//			return true;
//		}
		return !TextUtils.isEmpty(password);
	}

	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return names.length;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = View.inflate(HomeActivity.this, R.layout.list_item_home, null);
			
			ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
			
			
			tv_item.setText(names[position]);
			
			iv_item.setImageResource(ids[position]);
			
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		
		
	}
	
}
