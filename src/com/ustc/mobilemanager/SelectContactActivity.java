package com.ustc.mobilemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectContactActivity extends Activity {
	private ListView list_select_contact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_contact);
		list_select_contact = (ListView) findViewById(R.id.list_select_contact);
		
		final List<Map<String, String>> data = getContactInfo();

		list_select_contact.setAdapter(new SimpleAdapter(this, data, R.layout.list_item, new String[]{"name","phone"},
		 new int[]{R.id.name,R.id.phone}));
		
		list_select_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phone = data.get(position).get("phone");
				Intent data = new Intent();
				data.putExtra("phone", phone);
				setResult(0, data);
				finish();
			}
		});
	}

	
	/**
	 * 
	 * ��ȡ�ֻ�����ϵ����Ϣ
	 * 
	 * @return
	 */
	private List<Map<String, String>> getContactInfo() {
		//�����е���ϵ��
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		
		// �õ�һ�����ݽ�����
		ContentResolver resolver = getContentResolver();
		// raw_contacts���ű��·��
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		// data���·��
		Uri uriData = Uri.parse("content://com.android.contacts/data");
		
		Uri uri_mimetype = Uri.parse("content://com.android.contacts/mimetype");
		
		

		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);
		while (cursor.moveToNext()) {
			String contact_id = cursor.getString(0);
			if (contact_id != null) {
				//�����ĳһ����ϵ��
				Map<String, String> map = new HashMap<String, String>();
				
				Cursor dataCursor = resolver.query(uriData,
						new String[] { "data1","mimetype" }, "contact_id=?",
						new String[] { contact_id }, null);
				while (dataCursor.moveToNext()) {
					String data1 = dataCursor.getString(0);
					//System.out.println(data1);
					String mimetype = dataCursor.getString(1);
					System.out.println(mimetype + ":" + data1);
					if ("vnd.android.cursor.item/name".equals(mimetype)) {
						//��ϵ�˵�����
						map.put("name", data1);
					}else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
						//��ϵ�˵ĵ绰����
						map.put("phone", data1);
					}
				}
				list.add(map);
				dataCursor.close();
			}
		}
		cursor.close();
		return list;
	}
	
	public void back(View view) {

		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// Ҫ��finish()����startActivity(intent)�������ִ��
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
}
