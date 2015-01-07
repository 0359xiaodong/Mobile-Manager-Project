package com.ustc.mobilemanager.utils;

import java.security.MessageDigest;

public class MD5Utils {
	
	
	/**
	 * 
	 * md5���ܷ���
	 * 
	 * @param password
	 * @return
	 */

	public static String md5Password(String password) {
		// �õ�һ����ϢժҪ��
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			// ��ÿһ��byte��һ��������oxff(11111111)
			for (byte b : result) {
				// ������
				int number = b & 0xff;
				String str = Integer.toHexString(number);
				if (str.length() == 1) {
					buffer.append("0");
				}
				buffer.append(str);
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
