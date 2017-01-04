package com.loserstar.tankwar.module;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import com.loserstar.tankwar.client.GameWindow;

/* 
 * 客户坦克类
 */

public class TankClient {
	public static final int CLIENTTANKXSPEED = 5;// 玩家坦克X移动速度
	public static final int CLIENTTANKYSPEED = 5;// 玩家坦克Y移动速度
	public static final Color TANKCLIENTCOLOR = Color.cyan;// 玩家坦克颜色
	public static final Color GUNBARRELCOLOR = Color.YELLOW;// 坦克炮筒颜色
	public static final int MAXLIFE = 100;// 最大血量
	private boolean isLive = false;// 主机坦克生死量
	private boolean bL = false, bU = false, bR = false, bD = false;// 坦克方向
	private Direction direction = Direction.STOP;// 当前的坦克方向
	private Direction gunbarrelDirection = Direction.R;// 炮筒方向
	private Direction[] directions = Direction.values();// 得到方向数组
	private GameWindow gameWindow = null;// 持有window引用
	private int tankClientX, tankClientY;// 主机坦克坐标
	private int nowlife = MAXLIFE;// 生命值
	private LifeBar lbBar = null;// 生命值条
	private int good = Ball.CLIENTTANK;
	private int OldX,OldY;//上一个坐标
	


	public int getGood() {
		return good;
	}

	public void setGood(int good) {
		this.good = good;
	}

	private static Map<String, Image> tankeClientImages = new HashMap<String, Image>();
	private static Map<String, Image> cpuClientImages = new HashMap<String, Image>();

	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image[] images = null;

	static {
		images = new Image[] {
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/clienttankL.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/clienttankLU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/clienttankU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/clienttankRU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/clienttankR.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/clienttankRD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/clienttankD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/clienttankLD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/CPUtankL.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/CPUtankLU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/CPUtankU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/CPUtankRU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/CPUtankR.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/CPUtankRD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/CPUtankD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/CPUtankLD.gif")), };

		tankeClientImages.put("clientL", images[0]);
		tankeClientImages.put("clientLU", images[1]);
		tankeClientImages.put("clientU", images[2]);
		tankeClientImages.put("clientRU", images[3]);
		tankeClientImages.put("clientR", images[4]);
		tankeClientImages.put("clientRD", images[5]);
		tankeClientImages.put("clientD", images[6]);
		tankeClientImages.put("clientLD", images[7]);
		cpuClientImages.put("cpuL", images[8]);
		cpuClientImages.put("cpuLU", images[9]);
		cpuClientImages.put("cpuU", images[10]);
		cpuClientImages.put("cpuRU", images[11]);
		cpuClientImages.put("cpuR", images[12]);
		cpuClientImages.put("cpuRD", images[13]);
		cpuClientImages.put("cpuD", images[14]);
		cpuClientImages.put("cpuLD", images[15]);
	}
	
	
//	private int clientTankWidth=images[0].getWidth(null),clientTankHeight=images[0].getHeight(null);
	public static final int CLIENTTANKWIDTH = 35;// 玩家坦克宽度
	public static final int CLIENTTANKHEIGHT = 35;// 玩家坦克高度
	Direction olDirection;

	// 网络
	private int ID;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getNowlife() {
		return nowlife;
	}

	public void setNowlife(int nowlife) {
		this.nowlife = nowlife;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public int getTankClientX() {
		return tankClientX;
	}

	public void setTankClientX(int tankClientX) {
		this.tankClientX = tankClientX;
	}

	public int getTankClientY() {
		return tankClientY;
	}

	public void setTankClientY(int tankClientY) {
		this.tankClientY = tankClientY;
	}

	// 构造
	public TankClient(int tankClientX, int tankClientY, GameWindow gameWindow) {// 构造方法，持有window的引用，方便调用window的成员
		this.tankClientX = tankClientX;
		this.tankClientY = tankClientY;
		this.gameWindow = gameWindow;
		this.lbBar = new LifeBar();


	}

	// 根据方向计算坦克坐标
	private void move() {
		OldX=this.tankClientX;
		OldY=this.tankClientY;
		switch (direction) {
		case L:
			tankClientX -= CLIENTTANKXSPEED;
			break;
		case LU:
			tankClientX -= CLIENTTANKXSPEED;
			tankClientY -= CLIENTTANKYSPEED;
			break;
		case U:
			tankClientY -= CLIENTTANKYSPEED;
			break;
		case RU:
			tankClientX += CLIENTTANKXSPEED;
			tankClientY -= CLIENTTANKYSPEED;
			break;
		case R:
			tankClientX += CLIENTTANKXSPEED;
			break;
		case RD:
			tankClientX += CLIENTTANKXSPEED;
			tankClientY += CLIENTTANKYSPEED;
			break;
		case D:
			tankClientY += CLIENTTANKYSPEED;
			break;
		case LD:
			tankClientX -= CLIENTTANKXSPEED;
			tankClientY += CLIENTTANKYSPEED;
			break;
		case STOP:
			break;
		}
		// 如果不是STOP就给炮筒方向赋值
		if (direction != Direction.STOP) {
			gunbarrelDirection = direction;
		}
		// 边界
		if (tankClientX < 0) {
			tankClientX = 0;
		}
		if (tankClientY < gameWindow.getMenuHeight()) {
			tankClientY = gameWindow.getMenuHeight();
		}
		if (tankClientX + CLIENTTANKWIDTH > gameWindow
				.getGameWidth()) {
			tankClientX = gameWindow.getGameWidth()
					- CLIENTTANKWIDTH;
		}
		if (tankClientY + CLIENTTANKHEIGHT > gameWindow
				.getGameHeight()) {
			tankClientY = gameWindow.getGameHeight()
					- CLIENTTANKHEIGHT;
		}
	}

	private void PaintImage(int b, Graphics graphics) {

		if (b == Ball.CLIENTTANK) {
			switch (gunbarrelDirection) {// 根据当前坦克方向画出炮筒方向（画出对应坦克图片）
			case L:
				graphics.drawImage(tankeClientImages.get("clientL"),
						this.getTankClientX(), this.getTankClientY(), null);
				break;
			case LU: {
				graphics.drawImage(tankeClientImages.get("clientLU"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			case U: {
				graphics.drawImage(tankeClientImages.get("clientU"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			case RU: {
				graphics.drawImage(tankeClientImages.get("clientRU"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			case R: {
				graphics.drawImage(tankeClientImages.get("clientR"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			case RD: {
				graphics.drawImage(tankeClientImages.get("clientRD"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			case D: {
				graphics.drawImage(tankeClientImages.get("clientD"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			case LD: {
				graphics.drawImage(tankeClientImages.get("clientLD"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			}
		} else if (b == Ball.CPUTANK) {
			switch (gunbarrelDirection) {// 根据当前坦克方向画出炮筒方向（画出对应坦克图片）
			case L:
				graphics.drawImage(cpuClientImages.get("cpuL"),
						this.getTankClientX(), this.getTankClientY(), null);
				break;
			case LU: {
				graphics.drawImage(cpuClientImages.get("cpuLU"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			case U: {
				graphics.drawImage(cpuClientImages.get("cpuU"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			case RU: {
				graphics.drawImage(cpuClientImages.get("cpuRU"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			case R: {
				graphics.drawImage(cpuClientImages.get("cpuR"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			case RD: {
				graphics.drawImage(cpuClientImages.get("cpuRD"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			case D: {
				graphics.drawImage(cpuClientImages.get("cpuD"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			case LD: {
				graphics.drawImage(cpuClientImages.get("cpuLD"),
						this.getTankClientX(), this.getTankClientY(), null);
			}
				break;
			}
		}

	}

	// 重新绘制坦克的位置
	public void draw(Graphics graphics) {
		if (!this.isLive()) {
			return;
		}
		Color thisColor = graphics.getColor();
		graphics.setColor(TANKCLIENTCOLOR);// 坦克颜色
		// graphics.fillOval(tankClientX, tankClientY, CLIENTTANKWIDTH,
		// CLIENTTANKHEIGHT);//圆
		// graphics.fillRect(tankClientX, tankClientY, CLIENTTANKWIDTH,
		// CLIENTTANKHEIGHT);//矩形
		lbBar.draw(graphics);// 画血条
		graphics.drawString(String.valueOf(ID), tankClientX, tankClientY);
//		this.collidesLifeBlock(gameWindow.getLifeBlock());//与血块的碰撞检测
		graphics.setColor(TankClient.GUNBARRELCOLOR);// 设置炮筒颜色
		for(int i=0;i<gameWindow.getWalls().size();i++){
			collidesWithWall(gameWindow.getWalls().get(i));
		}
		this.PaintImage(good, graphics);
		graphics.setColor(thisColor);
		this.move();// 计算坦克坐标
	}

	// 处理按下的按键，bool存放按下的按键
	public void keyPreesed(KeyEvent key) {
		switch (key.getKeyCode()) {
		case 113: {// F2
			this.setLive(true);
			this.setNowlife(MAXLIFE);
			if (gameWindow.getNc() != null) {
				TankReliveMsg msg = new TankReliveMsg(ID, tankClientX,
						tankClientY, true);
				gameWindow.getNc().sendMsg(msg);
				System.out.println("发送重生信息");
			}
		}
			break;
		case 65: {// A
			if(gameWindow.getNc()==null){
				int row = Integer.parseInt(PropertiesMgr
					.getProperty("A_Init_CPUTankRow"));

			for (int i = 0; i < row; i++) {
				for (int j = 0; j < 3; j++) {// new 10辆坦克
					 gameWindow.getCpuTanks().add(new CPUTank(350+50*(j+1),
					 50*(i+1), gameWindow));
				}

			}
			}
			
			// gameWindow.cpuTanks.add(new CPUTank(400, 400, gameWindow));
		}
			break;
		case 83: {
			this.superFire();// 超级炮弹
		}
			break;
		case 37: {// 左
			bL = true;

		}
			break;
		case 38: {// 上
			bU = true;

		}
			break;
		case 39: {// 右
			bR = true;

		}
			break;
		case 40: {// 下
			bD = true;

		}
			break;
		}
		locateDirection();// 判断坦克方向
	}

	// 处理放开按键，bool存放放开时的按键
	public void keyReleased(KeyEvent key) {
		switch (key.getKeyCode()) {
		case 90: {// Z
			this.fire();
		}
			break;
		case 37: {// 左
			bL = false;// 坦克方向不为左
		}
			break;
		case 38: {// 上
			bU = false;

		}
			break;
		case 39: {// 右
			bR = false;

		}
			break;
		case 40: {// 下
			bD = false;

		}
			break;
		}
		locateDirection();// 判断坦克方向
	}

	// 设定坦克的方向
	private void locateDirection() {
		olDirection = this.direction;

		if (bL && !bU && !bR && !bD) {
			direction = Direction.L;
		} else if (bL && bU && !bR && !bD) {
			direction = Direction.LU;
		} else if (bU && !bL && !bR && !bD) {
			direction = Direction.U;
		} else if (bU && bR && !bL && !bD) {
			direction = Direction.RU;
		} else if (bR && !bL && !bU && !bD) {
			direction = Direction.R;
		} else if (bR && bD && !bL && !bU) {
			direction = Direction.RD;
		} else if (bD && !bL && !bU && !bR) {
			direction = Direction.D;
		} else if (bD && bL && !bU && !bR) {
			direction = Direction.LD;
		} else if (!bL && !bU && !bR && !bD) {
			direction = Direction.STOP;
		}

		if (gameWindow.getNc() != null) {
			// 如果变换了方向，就发送一条UDP消息
			if (direction != olDirection) {
				TankMoveMsg msg = new TankMoveMsg(ID, direction, tankClientX,
						tankClientY);
				gameWindow.getNc().sendMsg(msg);
			}
		}

	}

	// 开炮
	private void fire() {
		if (!isLive()) {
			return;
		}
		int x = this.tankClientX + this.CLIENTTANKWIDTH / 2;// 子弹出现的X坐标
		int y = this.tankClientY + this.CLIENTTANKHEIGHT / 2 ;// 子弹出现的y坐标

		Ball b = new Ball(this.ID, gunbarrelDirection, good, x, y, gameWindow);
		gameWindow.getBalls().add(b);// 返回一颗子弹
		if (gameWindow.getNc() != null) {
			BallNewMsg msg = new BallNewMsg(b);
			gameWindow.getNc().sendMsg(msg);
		}

	}

	// 八个方向开炮
	private void superFire() {
		if (!isLive()) {
			return;
		}
		if (gameWindow.getNc() != null) {
			return;
		}
		int x = this.tankClientX + this.CLIENTTANKWIDTH / 2 ;// 子弹出现的X坐标
		int y = this.tankClientY + this.CLIENTTANKHEIGHT / 2 ;// 子弹出现的y坐标
		for (int i = 0; i < 8; i++) {
			gameWindow.getBalls().add(
					new Ball(this.ID, directions[i], good, x, y, gameWindow));
		}

	}

	public Rectangle getRectangle() {
		return new Rectangle(tankClientX, tankClientY,
				images[0].getWidth(null), images[0].getWidth(null));
	}
	
	//碰墙检测
	private boolean collidesWithWall(Wall w){
		if(this.isLive()&&this.getRectangle().intersects(w.getRectangle())){
			this.stay();
			return true;
		}
		return false;
	}

	private void stay(){
		this.tankClientX=OldX;
		this.tankClientY=OldY;
	}
	
	// 碰血检测
/*	private void collidesLifeBlock(LifeBlock block) {
		if (this.getRectangle().intersects(block.getRectangle())) {
			block.setLive(false);// 血块死亡
			this.setNowlife(100);
		}
	}*/

	// 血条类
	private class LifeBar {
		public void draw(Graphics g) {
			if (!gameWindow.getTankClient().isLive()) {
				return;
			}
			Color color = g.getColor();
			if(getGood()==Ball.CLIENTTANK){
				g.setColor(Color.red);
			}
			else if(getGood()==Ball.CPUTANK){
				g.setColor(Color.blue);
			}
			
			g.drawRect(tankClientX, tankClientY - 10,
					CLIENTTANKWIDTH, 10);
			int w = 35 * nowlife / 100;
			g.fillRect(tankClientX, tankClientY - 10, w, 10);

			g.setColor(color);
		}
	}
}
