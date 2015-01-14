package com.dystu.managerprov2.domain;

import android.graphics.drawable.Drawable;

/**
 * ������Ϣ��ҵ��bean
 * 
 * @author
 * 
 */
public class TaskInfo {

	private Drawable icon;
	private String name;
	private String packname;
	private long memSize;// �ڴ�ռ�ô�С
	
	private boolean checked;
	
	
	private boolean userTask;// �û�����(true)orϵͳ����(false)

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackname() {
		return packname;
	}

	public void setPackname(String packname) {
		this.packname = packname;
	}

	public long getMemSize() {
		return memSize;
	}

	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}

	public boolean isUserTask() {
		return userTask;
	}

	public void setUserTask(boolean userTask) {
		this.userTask = userTask;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public String toString() {
		return "TaskInfo [icon=" + icon + ", name=" + name + ", packname="
				+ packname + ", memSize=" + memSize + ", checked=" + checked
				+ ", userTask=" + userTask + "]";
	}

	

}
