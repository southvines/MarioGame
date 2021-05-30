package com.itheima.role;

import java.awt.Image;

public class Bali extends Enemy{
	public int direction;
	public boolean isharmful=true;
	public int yspeed=1;
	public Bali(int x, int y, int width, int height, Image img) {
		super(x, y, width, height, img);
		direction=1;
	}
}
