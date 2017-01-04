package com.loserstar.tankwar.module;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.loserstar.tankwar.client.GameWindow;

public class CPUTank {
	public static final int CPUTANKXSPEED = 5;//玩家坦克X移动速度
	public static final int CPUTANKYSPEED = 5;//玩家坦克Y移动速度
	public static final int CPUTANKWIDTH = 40 ;//玩家坦克宽度
	public static final int CPUTANKHEIGHT = 40 ;//玩家坦克高度
	public static final Color TANKCPUCOLOR = Color.red;//玩家坦克颜色
	public static final Color GUNBARRELCOLOR = Color.blue;//坦克炮筒颜色
	public static Random random = new Random();;//随机数
	static private int step = random.nextInt(12)+3;;//坦克移动的步数
	private GameWindow gameWindow = null;//窗口引用
	private int CPUTankX , CPUTankY;//坦克坐标
	private int OldX,OldY;//上一个坐标
	private boolean CPUTankLive = true;//生或死
	private Direction direction = Direction.STOP;//当前的坦克方向
	private Direction gunbarrelDirection = Direction.R;//炮筒方向
	private Direction[] directions = Direction.values();//坦克方向数组形式
	private int good = Ball.CPUTANK;
	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image[] images = null;
	
	private static Map<String, Image> cputankMap = new HashMap<String, Image>();
	
	static{
		images = new Image[]{
		tk.getImage(Explode.class.getClassLoader().getResource("images/CPUtankL.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/CPUtankLU.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/CPUtankU.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/CPUtankRU.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/CPUtankR.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/CPUtankRD.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/CPUtankD.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/CPUtankLD.gif")),
		};
		
		cputankMap.put("L", images[0]);
		cputankMap.put("LU", images[1]);
		cputankMap.put("U", images[2]);
		cputankMap.put("RU", images[3]);
		cputankMap.put("R", images[4]);
		cputankMap.put("RD", images[5]);
		cputankMap.put("D", images[6]);
		cputankMap.put("LD", images[7]);
	}
	
	
	public int getCPUTankX() {
		return CPUTankX;
	}


	public void setCPUTankX(int cPUTankX) {
		CPUTankX = cPUTankX;
	}


	public int getCPUTankY() {
		return CPUTankY;
	}


	public void setCPUTankY(int cPUTankY) {
		CPUTankY = cPUTankY;
	}

	//坦克是否存活
	public boolean isCPUTankLive() {
		return CPUTankLive;
	}

	//设置坦克生或死
	public void setCPUTankLive(boolean cPUTankLive) {
		CPUTankLive = cPUTankLive;
	}

	//构造
	public CPUTank(int CPUTankX, int CPUTankY,GameWindow gameWindow) {
		this.CPUTankX = CPUTankX;
		this.CPUTankY = CPUTankY;
		this.gameWindow=gameWindow;
	}

	
	//绘制坦克
	public void draw(Graphics graphics){
		if(!this.isCPUTankLive()){//如果坦克死亡就不画
			gameWindow.getCpuTanks().remove(this);
			return;
		}
		this.locateDirection();
		this.move();
		this.collidesWithCPUTank(gameWindow.getCpuTanks());
		for(int i=0;i<gameWindow.getWalls().size();i++){
			Wall w = gameWindow.getWalls().get(i);
			collidesWithWall(w);
		}
		Color thisColor = graphics.getColor();
		graphics.setColor(TANKCPUCOLOR);
//		graphics.fillOval(CPUTankX, CPUTankY, CPUTANKWIDTH, CPUTANKHEIGHT);
		graphics.setColor(CPUTank.GUNBARRELCOLOR);
		
		switch (this.gunbarrelDirection) {//根据当前坦克方向画出炮筒方向
		case L:
			graphics.drawImage(cputankMap.get("L"), this.CPUTankX, this.CPUTankY, null);
			break;
		case LU:{
			graphics.drawImage(cputankMap.get("LU"),this.CPUTankX, this.CPUTankY, null);
		}break;
		case U:{
			graphics.drawImage(cputankMap.get("U"),this.CPUTankX, this.CPUTankY, null);
		}break;
		case RU:{
			graphics.drawImage(cputankMap.get("RU"),this.CPUTankX, this.CPUTankY, null);
		}break;
		case R:{
			graphics.drawImage(cputankMap.get("R"),this.CPUTankX, this.CPUTankY, null);
		}break;
		case RD:{
			graphics.drawImage(cputankMap.get("RD"),this.CPUTankX, this.CPUTankY, null);
		}break;
		case D:{
			graphics.drawImage(cputankMap.get("D"),this.CPUTankX, this.CPUTankY, null);
		}break;
		case LD:{
			graphics.drawImage(cputankMap.get("LD"),this.CPUTankX, this.CPUTankY, null);

		}break;
		}
		graphics.setColor(thisColor);
	}

	
	//设定移动的方向,移动不熟，开炮概率
	private void locateDirection(){
		if(step==0){
			int randomNumber = random.nextInt(directions.length);
			direction = directions[randomNumber];
			gunbarrelLocateDirection();
			step= random.nextInt(12)+3;//随机产生移动的步数，最少为3
			if(step==3){//随机开炮
				gameWindow.getBalls().add(fire());
			}
		}
		 step--;
	}
	
	//计算坦克坐标
	private void move(){
		OldX = CPUTankX;
		OldY = CPUTankY;
		switch (direction) {
		case L:
			CPUTankX-=CPUTANKXSPEED;
			break;
		case LU:
			CPUTankX-=CPUTANKXSPEED;
			CPUTankY-=CPUTANKYSPEED;
			break;
		case U:
			CPUTankY-=CPUTANKYSPEED;
			break;
		case RU:
			CPUTankX+=CPUTANKXSPEED;
			CPUTankY-=CPUTANKYSPEED;
			break;
		case R:
			CPUTankX+=CPUTANKXSPEED;
			break;
		case RD:
			CPUTankX+=CPUTANKXSPEED;
			CPUTankY+=CPUTANKYSPEED;
			break;
		case D:
			CPUTankY+=CPUTANKYSPEED;
			break;
		case LD:
			CPUTankX-=CPUTANKXSPEED;
			CPUTankY+=CPUTANKYSPEED;
			break;
		case STOP:
			break;
		}
		
		//边界
		if(CPUTankX<0){
			CPUTankX = 0;
		}
		if(CPUTankY<gameWindow.getMenuHeight()){
			CPUTankY = gameWindow.getMenuHeight();
		}
		if(CPUTankX+CPUTANKWIDTH>gameWindow.getGameWidth()){
			CPUTankX = gameWindow.getGameWidth() - CPUTANKWIDTH;
		}
		if(CPUTankY+CPUTANKHEIGHT>gameWindow.getGameHeight()){
			CPUTankY = gameWindow.getGameHeight() - CPUTANKHEIGHT;
		}

	}
	
	

	//设定炮筒方向，确定子弹方向
	private void gunbarrelLocateDirection(){
		if(direction != Direction.STOP){
			gunbarrelDirection = direction;
		}
		else{
			gunbarrelDirection = gunbarrelDirection;
		}
	}
 	
	//打出子弹
	private Ball fire(){
		int x = this.CPUTankX+this.CPUTANKWIDTH/2 - Ball.BALLWIDTH/2;
		int y = this.CPUTankY+this.CPUTANKHEIGHT/2 - Ball.BALLHEIGHT/2;
		return new Ball(0, gunbarrelDirection,good, x, y, gameWindow);
	}

	//得到坦克的矩形
	public Rectangle getRectangle(){
		return new Rectangle(CPUTankX, CPUTankY,  images[0].getWidth(null),  images[0].getWidth(null));
	}
	
	//设置上一次的坐标
	private void stay(){
		this.CPUTankX=OldX;
		this.CPUTankY=OldY;
	}
	
	//与墙得碰撞检测
	private boolean collidesWithWall(Wall w){
		if(this.isCPUTankLive()&&this.getRectangle().intersects(w.getRectangle())){
			this.stay();
			return true;
		}
		return false;
	}

	//与坦克的碰碰撞检测
	private boolean collidesWithCPUTank(List<CPUTank> cpus){
		for(int i = 0;i<cpus.size();i++){
			if(this!=cpus.get(i)){
				CPUTank tmp  =cpus.get(i);
				if(this.getRectangle().intersects(tmp.getRectangle())&&tmp.isCPUTankLive()&&tmp.isCPUTankLive()){
					this.stay();
					return true;
				}
			}
		}
		return false;
	}
}
