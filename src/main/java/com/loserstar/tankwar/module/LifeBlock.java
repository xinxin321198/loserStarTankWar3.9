package com.loserstar.tankwar.module;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

//血块
public class LifeBlock {
	public static final int LIFEBLOCKWIDTH=25;
	public static final int LIFEBLOCKHEIGHT=25;
	public static final Color LIFEBLOCKCOLOR=Color.white;
	private int lifeBlockX,lifeBlockY;
	private boolean isLive = true;
	private int step = 0;
	private int[][] pos={
						{130,800},{130,850},{140,823},{140,420},{133,444},{130,475}
					};
	
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image images = null;
	
	static{
		images = tk.getImage(Explode.class.getClassLoader().getResource("images/life.gif"));
	}
	
	public boolean isLive() {
		return isLive;
	}


	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}


	//构造
	public LifeBlock(int lifeBlockX, int lifeBlockY) {
		this.lifeBlockX = pos[0][0];
		this.lifeBlockY = pos[0][1];
	}


	public void draw(Graphics g){
		Color c= g.getColor();
		if(!this.isLive()){
			return ;
		}
		if(step==pos.length){
			step = 0;
		}
		g.setColor(LIFEBLOCKCOLOR);
		
//		g.fillRect(pos[step][0], pos[step][1], LIFEBLOCKWIDTH, LIFEBLOCKHEIGHT);
		g.drawImage(images, pos[step][0], pos[step][1], null);
		g.setColor(c);
		
		step++;
	}
	
	public Rectangle getRectangle(){
		return new Rectangle(lifeBlockX, lifeBlockY, LIFEBLOCKWIDTH, LIFEBLOCKHEIGHT);
	}
}
