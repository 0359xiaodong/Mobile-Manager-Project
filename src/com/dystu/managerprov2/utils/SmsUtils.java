package com.dystu.managerprov2.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

/**
 * 
 * ���ŵĹ�����
 * @author 
 *
 */
public class SmsUtils {
	/**
	 * 
	 * ���ݶ��ŵĻص��ӿ�
	 * 
	 * @author 
	 *
	 */
	public interface BackUpCallBack{
		/**
		 * ��ʼ���ݵ�ʱ����Ҫ�������Ľ���
		 * @param max
		 */
		public void beforeBackUp(int max);
		/**
		 * ���ݵĹ����У����ӽ���
		 * @param progress ����
		 */
		public void onBackUp(int progress);
		
	}
	
	
	/**
	 * 
	 * ��ԭ���ŵĽӿ�
	 * 
	 */
	public interface RestoreSms{
		public void beforeRestore(String max);
		public void onRestore(int progress);
	}
	
	
	
	/**
	 * �����û��Ķ���
	 * 
	 * @param context
	 * @param ProgressDialog ������
	 * @throws Exception
	 */
	public static void backupSms(Context context,BackUpCallBack callBack) throws Exception{
		ContentResolver resolver = context.getContentResolver();
		File file = new File(context.getFilesDir(),"smsbackup.xml");
		FileOutputStream fos = new FileOutputStream(file);
		//���û�����Ϣһ��һ���Ķ�����������һ���ĸ�ʽд���ļ���
//		<smss>
//			<sms>
//				<body>��ð�</body>
//				<date>2014��12��18��</date>
//				<type>1</type>�����ͻ��ǽ��ܣ�
//				<address>����</address>
//			</sms>
//		</smss>
		//��ȡxml�ļ������л���
		XmlSerializer serializer = Xml.newSerializer();
		//��ʼ�����л���
		serializer.setOutput(fos, "utf-8");
		serializer.startDocument("utf-8", true);//ͷ
		serializer.startTag(null, "smss");//��
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = resolver.query(uri, new String[]{"body","address","type","date"}, null, null, null);
		//��ʼ���ݵ�ʱ�����ý����������ֵ
		int max = cursor.getCount();
		
		callBack.beforeBackUp(max);
		serializer.attribute(null, "max", max+"");
		
		int process = 0;
		
		while (cursor.moveToNext()) {
			Thread.sleep(500);
			String body = cursor.getString(0);
			String address = cursor.getString(1);
			String type = cursor.getString(2);
			String date = cursor.getString(3);
			
			
			
			serializer.startTag(null, "sms");
			serializer.startTag(null, "date");
			serializer.text(date);
			serializer.endTag(null, "date");
			serializer.startTag(null, "type");
			serializer.text(type);
			serializer.endTag(null, "type");
			serializer.startTag(null, "address");
			serializer.text(address);
			serializer.endTag(null, "address");
			serializer.startTag(null, "body");
			serializer.text(body);
			serializer.endTag(null, "body");
			serializer.endTag(null, "sms");
			//���ݹ����У����ӽ���
			process++;
			callBack.onBackUp(process);
		}
		cursor.close();
		serializer.endTag(null, "smss");
		serializer.endDocument();
		fos.close();
	}
	/**
	 * ��ԭ����
	 * @param context
	 * @param flag �Ƿ�����ԭ���Ķ���
	 * @throws Exception һ�����׳��쳣�����ǲ����쳣
	 */
	public static void restoreSms(Context context,boolean flag,RestoreSms restoreSms) throws Exception{
		Uri uri = Uri.parse("content://sms/");
		if (flag) {
			//����ɵĶ���
			context.getContentResolver().delete(uri, null, null);
		}
		XmlPullParser parser = Xml.newPullParser();
		
		
		//1.��ȡsd���ϵ�xml�ļ�
		
		File file = new File(context.getFilesDir(),"smsbackup.xml");
		FileInputStream fis = new FileInputStream(file);
		parser.setInput(fis, "utf-8");
		int eventType = parser.getEventType();
		
		//2.��ȡmax(����)
		String attributeValue = parser.getAttributeValue(null, "max");
		restoreSms.beforeRestore(attributeValue);
		
		//3.��ȡÿһ���Ķ�����Ϣbody date type address
		
		String body = null;
		String date = null;
		String type = null;
		String address = null;
		
		
		//4.�Ѷ��Ų��뵽���ŵ�ϵͳӦ��
		ContentValues values = new ContentValues();
		int progress = 0;
		while (eventType!=XmlPullParser.END_DOCUMENT) {
			String tagName = parser.getName();
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if ("body".equals(tagName)) {
					body = parser.nextText();
				}else if ("date".equals(tagName)) {
					date = parser.nextText();
				}else if ("type".equals(tagName)) {
					type = parser.nextText();
				}else if ("address".equals(tagName)) {
					address = parser.nextText();
				}
				break;
			case XmlPullParser.END_TAG:
				
				values.put("body", body);
				values.put("date", date);
				values.put("type", type);
				values.put("address", address);
				
				context.getContentResolver().insert(uri, values);
				
				
				break;
				
				
				
			default:
				break;
			}
			eventType = parser.next();
			progress++;
			restoreSms.onRestore(progress);
		}
		
		fis.close();
		
		
		
		
	}

}
