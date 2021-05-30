package com.itheima.util;

import com.itheima.role.Boom;
import com.itheima.ui.GameFrame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;


public class KeyListener extends KeyAdapter{


	public GameFrame gf;

	public KeyListener(GameFrame gf) {
		this.gf = gf;
	}
	
	//���̼���

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch(code){
			//������
			case 39:
				gf.mario.setRight(true); 
				break;
			//������
			case 37:
				gf.mario.setLeft(true);
				break;
			case 66:
				
				break;
			//������
			case 38:
				gf.mario.setUp(true);
				break;
		}
	}
	
	
	
	public void keyReleased(KeyEvent e) {
		int code=e.getKeyCode();
		if(code==39){
			gf.mario.setRight(false);
			gf.mario.setImage(new ImageIcon("image/mari1.png").getImage());
		}
		if(code==37){
			gf.mario.setLeft(false);
			gf.mario.setImage(new ImageIcon("image/mari_left1.png").getImage());
		}
		if(code==38){
			gf.mario.setUp(false);
			
		}
	}

}
