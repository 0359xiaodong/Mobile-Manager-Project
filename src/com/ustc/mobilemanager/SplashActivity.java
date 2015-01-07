package com.ustc.mobilemanager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ustc.mobilemanager.utils.StringUtils;

public class SplashActivity extends Activity {

	protected static final String TAG = "SplashActivity";
	protected static final int SHOW_UPDATE_DIALOG = 0;
	protected static final int ENTER_HOME = 1;
	protected static final int ERROR = 2;
	private TextView tv_splash_version;
	private TextView tv_update_info;

	private String description;
	private String apkurl;

	private RelativeLayout rootLayout;

	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		rootLayout = (RelativeLayout) findViewById(R.id.rl_root);
		tv_update_info = (TextView) findViewById(R.id.tv_update_info);
		// ���ð汾��
		tv_splash_version.setText("�汾��:" + getVersion());
		boolean update = sp.getBoolean("update", false);

		// �������ݿ�
		copyDB("antivirus.db");
		copyDB("address.db");

		installShortcut();

		if (update) {
			// �������
			checkUpdate();
		} else {
			// �Զ������Ѿ��ر�
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					enterHome();
				}
			}, 2000);
		}

		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(3000);
		rootLayout.startAnimation(alphaAnimation);
	}

	/**
	 * ������ݷ�ʽ
	 * 
	 */
	private void installShortcut() {

		boolean shortcut = sp.getBoolean("shortcut", false);

		if (shortcut) {
			return;
		}
		Editor editor = sp.edit();

		// ���͹㲥��Intent
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		// ��ݷ�ʽ��Ҫ����������Ҫ����Ϣ��1.���ƣ�2.ͼ�ꣻ3.��ʲô����
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "�ֻ��ܼ�");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
				BitmapFactory.decodeResource(getResources(), R.drawable.icon));

		Intent shortcutIntent = new Intent();
		shortcutIntent.setAction(Intent.ACTION_MAIN);
		shortcutIntent.addCategory(Intent.CATEGORY_DEFAULT);
		shortcutIntent.setClassName(getPackageName(),
				"com.ustc.mobilemanager.SplashActivity");
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		sendBroadcast(intent);
		Toast.makeText(this, "�Ѵ����ֻ��ܼҿ�ݷ�ʽ", 0).show();
		editor.putBoolean("shortcut", true);
		editor.commit();

	}

	/**
	 * ����������������ݿ� �����ݿ⿽����data/data/����/files/address.db
	 */
	private void copyDB(String fileName) {
		// ��һ�ο���֮�� ��������������Ҫ����
		try {

			File file = new File(getFilesDir(), fileName);

			if (file.exists() && file.length() > 0) {
				// ����Ҫ������
				Log.i(TAG, "����Ҫ������");
			} else {
				InputStream is = getAssets().open(fileName);

				FileOutputStream fos = new FileOutputStream(file);

				byte[] buffer = new byte[1024];

				int len = 0;

				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			// ��ʾ�����ĶԻ���
			case SHOW_UPDATE_DIALOG:
				Log.i(TAG, "��ʾ�����ĶԻ���");
				// ����F3 ���뷽��
				showUpdateDialog();
				break;
			// ����������
			case ENTER_HOME:
				enterHome();
				break;
			// ������,����������
			case ERROR:
				enterHome();
				Toast.makeText(SplashActivity.this, "���󣬽���������",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 
	 * ����Ƿ����°汾 ����о�����
	 * 
	 */
	private void checkUpdate() {
		new Thread() {
			public void run() {

				// Return a new Message instance from the global pool. Allows us
				// to avoid allocating new objects in many cases.
				Message msg = Message.obtain();

				long start = System.currentTimeMillis();

				// URL http://192.168.1.31:8080/updateinfo.html
				try {

					URL url = new URL(getString(R.string.serverurl));
					// ����
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();

					conn.setRequestMethod("GET");

					conn.setConnectTimeout(4000);

					conn.setReadTimeout(4000);

					int responseCode = conn.getResponseCode();

					if (responseCode == 200) {
						// �����ɹ�
						InputStream is = conn.getInputStream();
						// ����ת��ΪString
						String result = StringUtils.readFromStream(is);
						Log.i(TAG, "�����ɹ���:" + result);

						// json����
						JSONObject obj = new JSONObject(result);
						// �õ��������İ汾��Ϣ
						String version = (String) obj.get("version");
						description = (String) obj.get("description");
						apkurl = (String) obj.get("apkurl");

						// У���Ƿ����°汾
						if (getVersion().equals(version)) {
							// �汾һ�£�û���°汾�����뵽������

							msg.what = ENTER_HOME;

						} else {
							// ���°汾�����������Ի���
							msg.what = SHOW_UPDATE_DIALOG;
						}

					}

				} catch (Exception e) {
					msg.what = ERROR;
					e.printStackTrace();
				} finally {

					long end = System.currentTimeMillis();
					// ���˶���ʱ��
					long dTime = end - start;
					// 2000
					if (dTime < 2000) {
						try {
							Thread.sleep(2000 - dTime);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					handler.sendMessage(msg);
				}

			};
		}.start();

	}

	private void enterHome() {

		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * ���������Ի���
	 * 
	 */
	public void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(SplashActivity.this);
		// builder.setCancelable(false);
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// ����������,
				enterHome();
				dialog.dismiss();
			}
		});
		builder.setTitle("��������");
		builder.setMessage(description);
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ����apk �����滻��װ
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// sd����
					// ����������
					FinalHttp finalHttp = new FinalHttp();
					finalHttp.download(apkurl, Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/mobilemanager2.0.apk",
							new AjaxCallBack<File>() {

								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									t.printStackTrace();
									Toast.makeText(getApplicationContext(),
											"����ʧ��", Toast.LENGTH_SHORT).show();
									super.onFailure(t, errorNo, strMsg);
								}

								@Override
								public void onLoading(long count, long current) {
									super.onLoading(count, current);
									tv_update_info.setVisibility(View.VISIBLE);
									// ��ǰ���صİٷֱ�
									int progress = (int) (current * 100 / count);
									tv_update_info.setText("���ؽ���:" + progress
											+ "%");
								}

								@Override
								public void onSuccess(File t) {
									super.onSuccess(t);
									installAPK(t);
								}

								/**
								 * ��װAPK
								 * 
								 * @param t
								 */
								private void installAPK(File t) {
									Intent intent = new Intent();
									// action category data���Զ�λ����
									// extra��data���Դ�������
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");
									intent.setDataAndType(Uri.fromFile(t),
											"application/vnd.android.package-archive");
									startActivity(intent);
								}

							});

				} else {
					Toast.makeText(getApplicationContext(), "SD��������",
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHome();// ����������
			}
		});
		builder.show();
	}

	/**
	 * ����Ӧ�ó���İ汾��
	 * 
	 * @return
	 */
	private String getVersion() {
		// ���������ֻ���APK(��������)
		PackageManager pm = getPackageManager();
		try {
			// �õ�ָ��APK�Ĺ����嵥�ļ�
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
