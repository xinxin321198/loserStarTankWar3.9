package com.loserstar.tankwar.module;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import com.loserstar.tankwar.client.GameWindow;

//墙类
public class Wall {
	public static final int HORIZONTAL = 1;
	public static final int VERTICAL = 2;
	private int wallx,wally,wallwidth,wallheight;
	private GameWindow gameWindow;
	public static final Color WALLCOLOR = Color.MAGENTA;
	private int type;
	private static Map<String, Image> wallImageMap = new HashMap<String, Image>();

	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image[] images = null;

	static {
		images = new Image[] {
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/horizontal.jpg")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/vertical.jpg")),};

		wallImageMap.put("horizontal", images[0]);
		wallImageMap.put("vertical", images[1]);
	}

	//构造方法
	public Wall(int type,int wallx, int wally, int wallwidth, int wallheight,
			GameWindow gameWindow) {
		this.type=type;
		this.wallx = wallx;
		this.wally = wally;
		this.wallwidth = wallwidth;
		this.wallheight = wallheight;
		this.gameWindow = gameWindow;
	}


	//绘画的方法
	public void draw(Graphics g){
		Color c= g.getColor();
		g.setColor(WALLCOLOR);
//		g.fillRect(wallx, wally, wallwidth, wallheight);
		if(this.type==Wall.HORIZONTAL){
			g.drawImage(wallImageMap.get("horizontal"), wallx, wally,null);
		}else if(this.type==Wall.VERTICAL){
			g.drawImage(wallImageMap.get("vertical"), wallx, wally,null);
		}
		
		g.setColor(c);
	}
	
	//得到墙得矩形
	public Rectangle getRectangle(){
		return new Rectangle(wallx,wally,wallwidth,wallheight);
	}
}
