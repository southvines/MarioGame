package com.itheima.role;

import java.awt.Image;

//�ϰ���ĳ�����
public abstract class Enemy {
	public int x,y;
	public int width,height;
	public Image img;
	public Enemy(int x, int y, int width, int height, Image img) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.img=img;
	}
}
