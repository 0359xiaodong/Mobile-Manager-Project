package com.dystu.managerprov2.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * �������ݿ��ѯ��ҵ����
 * 
 * @author 
 *
 */

public class AntiVirusDao {

	/**
	 * ��ѯһ��md5�Ƿ������ݿ��д���
	 * @param md5
	 * @return
	 */
	public static  boolean isVirus(String md5){
		
		String path = "/data/data/com.dystu.managerprov2/files/antivirus.db";
		
		boolean result = false;
		
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		
		Cursor cursor = db.rawQuery("select * from datable where md5=?", new String[]{md5});
		
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
	
	
}
