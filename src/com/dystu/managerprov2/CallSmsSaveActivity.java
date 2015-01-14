package com.dystu.managerprov2;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dystu.managerprov2.dao.BlackNumberDao;
import com.dystu.managerprov2.domain.BlackNumberInfo;

public class CallSmsSaveActivity extends Activity {

	private ListView listView;

	private List<BlackNumberInfo> infos;

	private BlackNumberDao dao;

	private CallSmsSafeAdapter safeAdapter;

	private LinearLayout ll_loading;

	private int offset = 0;
	private int maxnumber = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_call_sms_save);
		listView = (ListView) findViewById(R.id.list);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		dao = new BlackNumberDao(this);
		
		final List<BlackNumberInfo> findAll = dao.findAll();
		//final int size = findAll.size();
		//System.out.println("size:" + size);

		fillData();

		// listviewע��һ�������¼��ļ�����
		listView.setOnScrollListener(new OnScrollListener() {
			/**
			 * ��������״̬�����仯��ʱ��
			 * 
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				// ����״̬
				case OnScrollListener.SCROLL_STATE_IDLE:
					System.out.println("SCROLL_STATE_IDLE");

					// �жϵ�ǰlistview������λ��
					// ��ȡListView���һ���ɼ���Ŀ�ڼ��������λ��
					int lastVisiblePosition = listView.getLastVisiblePosition();

					System.out.println(lastVisiblePosition);

					if (lastVisiblePosition == (infos.size() - 1)) {
						System.out.println("�б��ƶ��������һ��λ�ã����ظ������ݡ�����");
						offset += maxnumber;
						
						if (offset >= findAll.size()) {
							Toast.makeText(getApplicationContext(), "�ף������Ѿ����������!", 0).show();
							ll_loading.setVisibility(View.INVISIBLE);
						}else {
							fillData();
						}
					//	System.out.println("offset:" + offset);
					//	System.out.println("�б�ĳ��ȣ�"+ infos.size());
						
						
						
						
						
					}

					break;
				// ��ָ��������
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					System.out.println("SCROLL_STATE_TOUCH_SCROLL");
					break;
				// ���Ի���״̬
				case OnScrollListener.SCROLL_STATE_FLING:
					System.out.println("SCROLL_STATE_FLING");
					break;
				default:
					break;
				}
			}

			/**
			 * 
			 * ������ʱ����õķ���
			 * 
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

		// ���µķ�����û�����ã�
		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// final int position, long id) {
		// AlertDialog.Builder builder = new Builder(
		// CallSmsSaveActivity.this);
		// final AlertDialog dialog = builder.create();
		//
		// view = View.inflate(CallSmsSaveActivity.this,
		// R.layout.dialog_add_blacknumber, null);
		//
		// cancel = (Button) view.findViewById(R.id.cancel);
		// ok = (Button) view.findViewById(R.id.ok);
		// cb_phone = (CheckBox) view.findViewById(R.id.cb_phone);
		// cb_sms = (CheckBox) view.findViewById(R.id.cb_sms);
		// et_blacknumber = (EditText) view
		// .findViewById(R.id.et_blacknumber);
		//
		// dialog.setView(view, 0, 0, 0, 0);
		//
		// dialog.show();
		//
		// cancel.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// dialog.dismiss();
		// }
		// });
		// ok.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// String blacknumber = infos.get(position).getNumber();
		// et_blacknumber.setText(blacknumber);
		// if (TextUtils.isEmpty(blacknumber)) {
		// Toast.makeText(getApplicationContext(),
		// "���������벻��Ϊ��!", 0).show();
		// return;
		// }
		// String mode;
		// if (cb_phone.isChecked() && cb_sms.isChecked()) {
		// // ȫ������
		// mode = "3";
		//
		// } else if (cb_phone.isChecked()) {
		// // �绰����
		// mode = "1";
		//
		// } else if (cb_sms.isChecked()) {
		// // ��������
		// mode = "2";
		// } else {
		//
		// Toast.makeText(getApplicationContext(), "��ѡ������ģʽ!",
		// 0).show();
		// return;
		// }
		// // ���ݱ�����
		// dao.update(blacknumber, mode);
		// // ����ListView������
		//
		// BlackNumberInfo info = new BlackNumberInfo();
		//
		// info.setMode(mode);
		//
		// info.setNumber(blacknumber);
		//
		// infos.add(0, info);
		//
		// // ֪ͨ���������ݸ�����
		//
		// safeAdapter.notifyDataSetChanged();
		//
		// dialog.dismiss();
		// }
		// });
		//
		// }
		// });
	}

	private void fillData() {
		List<BlackNumberInfo> all = dao.findAll();
		final int size = all.size();
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (infos == null) {
					infos = dao.findPart(offset, maxnumber);
				}else {
					infos.addAll(dao.findPart(offset, maxnumber));
				}
				//����д�����⣿�ɵ����ݻ�������𣿡��������Լ���������ж�
				//infos = dao.findPart(offset, maxnumber);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						if (safeAdapter == null) {
							safeAdapter = new CallSmsSafeAdapter();
							listView.setAdapter(safeAdapter);
							
						}else {
							safeAdapter.notifyDataSetChanged();
						}
						
						
						//ÿ�μ����µ����ݣ��ɵ����ݾͻ��ܵ���һҳ�ĵ�һ��item������
						//safeAdapter = new CallSmsSafeAdapter();
						//listView.setAdapter(safeAdapter);

					}
				});
			};
		}.start();
	}

	private class CallSmsSafeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;

			ViewHolder viewHolder;

			if (convertView == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.list_item_callsms, null);

				viewHolder = new ViewHolder();

				viewHolder.tv_black_number = (TextView) view
						.findViewById(R.id.tv_black_number);
				viewHolder.tv_black_mode = (TextView) view
						.findViewById(R.id.tv_black_mode);

				viewHolder.iv_delete = (ImageView) view
						.findViewById(R.id.iv_delete);

				view.setTag(viewHolder);

			} else {
				view = convertView;

				viewHolder = (ViewHolder) view.getTag();

			}

			viewHolder.tv_black_number.setText(infos.get(position).getNumber());

			String mode = infos.get(position).getMode();

			if ("1".equals(mode)) {
				viewHolder.tv_black_mode.setText("�绰����");

			} else if ("2".equals(mode)) {
				viewHolder.tv_black_mode.setText("��������");
			} else {
				viewHolder.tv_black_mode.setText("ȫ������");
			}
			// viewHolder.iv_delete.setTag(position);
			viewHolder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(
							CallSmsSaveActivity.this);
					builder.setTitle("��ʾ");
					builder.setMessage("ȷ��ɾ��������¼��?");
					builder.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									System.out.println("ɾ��");

									// ɾ�����ݿ������
									dao.delete(infos.get(position).getNumber());

									// ���½���
									infos.remove(position);

									safeAdapter.notifyDataSetChanged();

								}
							});
					builder.setNegativeButton("ȡ��", null);
					builder.show();
				}
			});
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

	static class ViewHolder {
		TextView tv_black_number;
		TextView tv_black_mode;
		ImageView iv_delete;
	}

	private EditText et_blacknumber;

	private CheckBox cb_phone, cb_sms;

	private Button cancel, ok;

	public void addBlackNumber(View view) {

		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog dialog = builder.create();

		view = View.inflate(this, R.layout.dialog_add_blacknumber, null);

		cancel = (Button) view.findViewById(R.id.cancel);
		ok = (Button) view.findViewById(R.id.ok);
		cb_phone = (CheckBox) view.findViewById(R.id.cb_phone);
		cb_sms = (CheckBox) view.findViewById(R.id.cb_sms);
		et_blacknumber = (EditText) view.findViewById(R.id.et_blacknumber);

		dialog.setView(view, 0, 0, 0, 0);

		dialog.show();

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String blacknumber = et_blacknumber.getText().toString().trim();
				if (TextUtils.isEmpty(blacknumber)) {
					Toast.makeText(getApplicationContext(), "���������벻��Ϊ��!", 0)
							.show();
					return;
				}
				String mode;
				if (cb_phone.isChecked() && cb_sms.isChecked()) {
					// ȫ������
					mode = "3";

				} else if (cb_phone.isChecked()) {
					// �绰����
					mode = "1";

				} else if (cb_sms.isChecked()) {
					// ��������
					mode = "2";
				} else {

					Toast.makeText(getApplicationContext(), "��ѡ������ģʽ!", 0)
							.show();
					return;
				}
				// ���ݱ����
				dao.add(blacknumber, mode);
				// ����ListView������

				BlackNumberInfo info = new BlackNumberInfo();

				info.setMode(mode);

				info.setNumber(blacknumber);

				infos.add(0, info);

				// ֪ͨ���������ݸ�����

				safeAdapter.notifyDataSetChanged();

				dialog.dismiss();
			}
		});

	}
	
	public static void actionStart(Context context){
		Intent intent = new Intent(context, CallSmsSaveActivity.class);
		
		context.startActivity(intent);
		
		
	}

}
