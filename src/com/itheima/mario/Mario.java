package com.itheima.mario;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.*;
import com.itheima.ui.GameFrame;
import java.util.Timer;

import com.itheima.role.*;


public class Mario extends Thread {
	
	private GameFrame gf;
	
	public boolean jumpFlag=true;
	
	//马里奥的坐标
	private int x=100,y=358;
	//马里奥的速度
	private int xspeed=5 , yspeed=1;
	//马里奥的宽高
	private int width=30,height=32;
	//马里奥的图片
	private Image img = new ImageIcon("image/mari1.png").getImage();
	
	private boolean left=false,right=false,down=false,up=false;
	
	private String Dir_Up="Up",Dir_Left="Left",Dir_Right="Right",Dir_Down="Down";
	
	private boolean ishole=false;
	
	private int health=3;
	
	private int score=0;
	
	private Mario (GameFrame gf) {
		this.gf = gf;
		this.Gravity();
	}
	
	private boolean canHurt=true;
	
	private final int DELAY=3000;
	
	Timer timer=new Timer();
	
	
	private static Mario instance;
	
	public static Mario getInstance(GameFrame gf) {
		if(instance==null) {
			instance=new Mario(gf);
			return instance;
		}
		return instance;
	}

	
	public void run(){
		while(true){
//			System.out.println(gf.bg.x);
//			System.out.println(this.x);
			System.out.println(this.y);
//			System.out.println(isGravity);
			//向左走
			if(left){
				//碰撞到了
			
				if(hit(Dir_Left)||(gf.bg.x>=0&&this.x<=0)){
					this.xspeed=0;
				}
				
				if(this.x>=100){
					this.x-=this.xspeed;
					this.img=new ImageIcon("image/mari_left.gif").getImage();
				}
				
				if(gf.bg.x<0&&this.x<100) {
					
					gf.bg.x+=this.xspeed;
					for (int i = 0; i <gf.eneryList.size(); i++) {
						Enemy enery = gf.eneryList.get(i);
						enery.x+=this.xspeed;
					}
					this.img= new ImageIcon("image/mari_left.gif").getImage();
				}
				
				this.xspeed=5;
			}
			
			//向右走
			if(right){
				
				if(hit(Dir_Right)){
					this.xspeed=0;
				}
				
				if(this.x<400){
					this.x += this.xspeed;
					this.img=new ImageIcon("image/mari_right.gif").getImage();
				}
				
				if(this.x>=400){
					if(gf.bg.x>=-6525) {
						gf.bg.x-=this.xspeed;
				
						for (int i = 0; i <gf.eneryList.size(); i++) {
							Enemy enery = gf.eneryList.get(i);
							enery.x-=this.xspeed;
						}
						this.img= new ImageIcon("image/mari_right.gif").getImage();
					}
					else if(this.x<760){
						this.x += this.xspeed;
						this.img=new ImageIcon("image/mari_right.gif").getImage();
					}
					else {
						this.xspeed=0;
					}
				}
				this.xspeed=5;
			}
			
		
			if(up){

				if(jumpFlag && !isGravity){
					jumpFlag=false;
					new Thread(){
						public void run(){
							jump();
							jumpFlag=true;
						}
					}.start();
				}
			}
			
			try {
				sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	//向上跳的函数
	public void jump(){
		int jumpHeigh=0;
		for (int i = 0; i < 150; i++) {
			gf.mario.y-=this.yspeed;
			jumpHeigh++;
			if(hit(Dir_Up)){
				break;
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i <jumpHeigh; i++) {
			gf.mario.y+=this.yspeed;
			if(hit(Dir_Down)){
				this.yspeed=0;
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
		}
		this.yspeed=1;
	}
	

	public boolean hit(String dir){
		Rectangle myrect = new Rectangle(this.x,this.y,this.width,this.height);

		Rectangle rect =null;
		
		for (int i = 0; i < gf.eneryList.size(); i++) {
			Enemy enery = gf.eneryList.get(i);
			
			if(dir.equals("Left")){
				rect = new Rectangle(enery.x+2,enery.y,enery.width,enery.height);
			}else if(dir.equals("Right")){
			
				rect = new Rectangle(enery.x-2,enery.y,enery.width,enery.height);
			}
			
			else if(dir.equals("Up")){
				rect = new Rectangle(enery.x,enery.y+1,enery.width,enery.height);
			}else if(dir.equals("Down")){
				rect = new Rectangle(enery.x,enery.y-2,enery.width,enery.height);
			}
		
			if(myrect.intersects(rect)&&enery instanceof Coin&&dir.equals("Up")){
					
					enery.img=(new ImageIcon("image/coin_brick_null.png").getImage());
					this.score+=50;
					
				return true;
			}
			else if(myrect.intersects(rect)&&enery instanceof Hole) {
				ishole=true;
				return false;
					
			}
			else if(myrect.intersects(rect)&&enery instanceof Bali){
				
				Bali enery1=(Bali)enery;
				if(enery1.isharmful&&this.y<enery1.y-30) {
					enery1.isharmful=false;
					score+=50;
					enery1.img=null;
					jump();
					
					return true;
				}
				else if(enery1.isharmful&&canHurt) {
					reduceHealth();
					canHurt=false;
					timer.schedule(new Task(),DELAY);
					return false;
				}
				else {
					return false;
				}
				
			}
			
			else if(myrect.intersects(rect)) {
				return true;
			}
			

	}

			
		
		
		return false;
	}

	public boolean isGravity=false;


	public void Gravity(){
			new Thread(){
				public void run(){
					
					while(true){
						try {
							sleep(10);
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						

						while(true){
							
							if(!jumpFlag){
								break;
							}
							
							if(hit(Dir_Down)){
								isGravity=false;
								break;
							}
							if(ishole) {
								isGravity=true;
								y+=yspeed*3;
							}
							else if(y>=358){
								isGravity=false;
							}else{
								isGravity=true;
								y+=yspeed*3;
							}
							
							try {
								sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}.start();
}


	public int getX() {
		return this.x;
	}
	public void setX(int x) {
		this.x=x;
	}
	public int getY() {
		return this.y;
	}
	public void setY(int y) {
		this.y=y;
	}
	public boolean getIshole() {
		return this.ishole;
	}
	
	public void setIshole(boolean flag) {
		this.ishole=flag;
	}
	
	public void setLeft(boolean flag) {
		this.left=flag;
	}
	public void setRight(boolean flag) {
		this.right=flag;
	}
	public void setUp(boolean flag) {
		this.up=flag;
	}
	public void setDown(boolean flag) {
		this.down=flag;
	}
	public Image getImage() {
		return this.img;
	}
	
	public void setImage(Image image) {
		this.img=image;
	}
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void reduceHealth() {
		this.health--;
	}
	
	public void setHealth(int health) {
		this.health=health;
	}
	
	public void addScore(int score) {
		this.score+=score;
	}
	
	public void setScore(int score) {
		this.score=score;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setcanHurt(boolean canHurt) {
		this.canHurt=canHurt;
	}
	
	public boolean getcanHurt() {
		return this.canHurt;
	}
	
}