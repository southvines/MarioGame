package com.itheima.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.sound.sampled.*;

import com.itheima.mario.Mario;
import com.itheima.role.*;
import com.itheima.util.Map;
import com.itheima.util.MusicUtil;

/**
   主体窗口界面：展示角色。

 */
public class GameFrame extends JFrame{
	
	public Mario mario;
	
	public Enemy pipe ,cion , brick, hole, bali;
	
	public BackgroundImage bg ;
	
	public ArrayList<Enemy> eneryList = new ArrayList<Enemy>();
	
	public ArrayList<Boom> boomList = new ArrayList<Boom>();
	
	public int bspeed=0;

	
	public int[][] map = null;
	{
	
		Map mp = new Map();
		map = mp.readMap();
	}

	
	public GameFrame() throws Exception {
		
		this.setSize(800,450);
		this.setTitle("超级玛丽");
		this.setResizable(false);
		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

		
		mario = Mario.getInstance(this);

		
		bg = new BackgroundImage();

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				
				if(map[i][j]==1){			
					brick = new Brick(j*30,i*30,30,30,new ImageIcon("image/brick.png").getImage());
					eneryList.add(brick);
				}
			
				if(map[i][j]==2){
					cion = new Coin(j*30,i*30,30,30,new ImageIcon("image/coin_brick.png").getImage());
					eneryList.add(cion);
				}
				
				if(map[i][j]==3){
					pipe = new Pipe(j*30,i*30,60,120,new ImageIcon("image/pipe.png").getImage());
					eneryList.add(pipe);
				}
				if(map[i][j]==4){
					pipe = new Pipe(j*30,i*30+60,60,60,new ImageIcon("image/pipe1.png").getImage());
					eneryList.add(pipe);
				}
				
				if(map[i][j]==5){
					hole = new Hole(j*30,i*30+90,60,60,new ImageIcon("image/pipe1.png").getImage());
					eneryList.add(hole);
				}
				
				if(map[i][j]==6){
					bali = new Bali(j*30,i*30,20,20,new ImageIcon("image/bali.gif").getImage());
					eneryList.add(bali);
				}
				
			}
		}

		mario.start();
		
		new Thread(){
			public void run(){
				while(true){
					AudioInputStream as;
					try {
						as = AudioSystem.getAudioInputStream(new File("music/bg1.wav"));
						AudioFormat format = as.getFormat();
						SourceDataLine sdl = null;
						DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
						sdl = (SourceDataLine) AudioSystem.getLine(info);
						sdl.open(format);
						sdl.start();
						int nBytesRead = 0;
						byte[] abData = new byte[512];
						while (nBytesRead != -1) {
							nBytesRead = as.read(abData, 0, abData.length);
							if (nBytesRead >= 0)
								sdl.write(abData, 0, nBytesRead);
						}
					
						sdl.drain();
						sdl.close();
						}catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
						}
						}
			}
				
			}.start();

		new Thread(){
			public void run(){
				while(true){
					if(mario.getHealth()<0) {
						mario.setScore(0);
						mario.setHealth(3);
						mario.setIshole(false);
						bg.x=0;
						mario.setX(100);
						mario.setY(358);
						int count=0; 
						for (int i = 0; i < map.length; i++) {
							for (int j = 0; j < map[0].length; j++) {
								
								if(map[i][j]!=0){
									Enemy enery = eneryList.get(count);
									count++;
									enery.x=j*30;
								}

							}
						}
					}
					else if(mario.getY()>400){
						mario.reduceHealth();
						mario.setIshole(false);
						mario.setX(mario.getX()-100);
						mario.setY(0);
					
					}
					repaint(); 
			
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		
		new Thread(){
			public void run(){
				while(true){
					for(int i = 0; i < eneryList.size(); i++) {
						Enemy enery1 = eneryList.get(i);
						if(enery1 instanceof Bali) {
							int xspeed=1;
							Bali enery=(Bali) enery1;
							enery.x+=xspeed*enery.direction;
							enery.yspeed=1;
							System.out.println(enery.y);
							Rectangle myrect = new Rectangle(enery.x,enery.y,enery.width,enery.height);
							Rectangle rect =null;
							Rectangle rect2 =null;
							for (int j = 0; j < eneryList.size(); j++) {
								Enemy rectal = eneryList.get(j);
								if(rectal!=enery) {
									if(enery.direction==-1){
										rect = new Rectangle(rectal.x+2,rectal.y,rectal.width,rectal.height);
									}else if(enery.direction==1){
									
										rect = new Rectangle(rectal.x-2,rectal.y,rectal.width,rectal.height);
									}
									rect2 = new Rectangle(rectal.x,rectal.y-2,rectal.width,rectal.height);
									if(myrect.intersects(rect2)) {
										enery.yspeed=0;
										
									}
									
									if(myrect.intersects(rect)){
										enery.direction=-enery.direction;
										
									}
									else if(enery.x<=bg.x) {
										System.out.println(enery.direction);
										enery.x+=10;
										enery.direction=-enery.direction;
									}
								}
							}
							if(enery.y<370) {
								enery.y+=enery.yspeed;
							}
								
						}
					}
						
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	
	

	public void paint(Graphics g) {
		
		BufferedImage bi =(BufferedImage)this.createImage(this.getSize().width,this.getSize().height);
		Graphics big = bi.getGraphics();
		big.drawImage(bg.img, bg.x, bg.y, null);
		for (int i = 0; i < eneryList.size(); i++) {
			Enemy e = eneryList.get(i);
			big.drawImage(e.img, e.x, e.y, e.width, e.height,null);
		}


		//画人物 玛丽自己
		big.drawImage(mario.getImage(), mario.getX(), mario.getY(), mario.getWidth(), mario.getHeight(),null);
		big.drawString("your Health:  "+Integer.toString(mario.getHealth()), 50, 50);
		big.drawString("your Score:  "+Integer.toString(mario.getScore()), 50, 70);
		g.drawImage(bi,0,0,null);

	}
	


}