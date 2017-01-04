package com.loserstar.tankwar.module;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import com.loserstar.tankwar.client.GameWindow;

//爆炸类
public class Explode {
	private int explodeX,explodeY;//爆炸的坐标
	private boolean explodeLive = true;//爆炸是否存活
	private boolean init = false;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] images = {
							tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
							tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
							tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
							tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
							tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
							tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
							tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
							tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
							tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
							tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif")),

						
								};
	
	private int step = 0;
	private GameWindow gameWindow = null;
	
	public Explode(int x,int y,GameWindow gameWindow){
		this.explodeX=x;
		this.explodeY=y;
		this.gameWindow=gameWindow;
	}
	
	//爆炸是否存活
	public boolean isExplodeLive() {
		return explodeLive;
	}

	//设置爆炸存活
	public void setExplodeLive(boolean explodeLive) {
		this.explodeLive = explodeLive;
	}
	
	//绘制
	public void draw(Graphics g){
		if(!explodeLive){//如果不存活，从window里删除此爆炸
			gameWindow.getExplodes().remove(this);
			return;
		}
		if(!init){
			for(int i =0;i<images.length;i++){
				g.drawImage(images[i], -100, -100,null);
				init = true;
			}
			
		}
		if(step == images.length){//if画完10次设置爆炸死亡
			this.setExplodeLive(false);//设置爆炸死亡
			this.step = 0;//次数变为0
			return;
		}
		Color c=g.getColor();
		
		g.setColor(Color.orange);
		g.drawImage(images[step], explodeX, explodeY,null);
		step++;
		g.setColor(c);
	}
}
