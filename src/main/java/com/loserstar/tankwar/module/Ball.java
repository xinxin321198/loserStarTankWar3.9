package com.loserstar.tankwar.module;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loserstar.tankwar.client.GameWindow;

//子弹类
public class Ball {
	public static final int BALLSPEED = 10;// 子弹速度
	public static final int BALLWIDTH = 10;// 子弹宽度
	public static final int BALLHEIGHT = 10;// 子弹高度
	public static final int CLIENTTANK = 1;
	public static final int CPUTANK = 2;
	private int isFromTank = 0;// 炮弹来自哪里
	private int TankID;

	private Color ballColor = Color.red;// 炮弹颜色
	private Direction ballDirection = null;// 炮弹方向

	private int ballX, ballY;// 炮弹坐标

	private boolean Live = true;// 炮弹生死
	private GameWindow gameWindow = null;// 窗口引用

	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image[] images = null;

	private static Map<String, Image> ballMap = new HashMap<String, Image>();

	static {
		images = new Image[] {
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/ballL.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/ballLU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/ballU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/ballRU.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/ballR.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/ballRD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/ballD.gif")),
				tk.getImage(Explode.class.getClassLoader().getResource(
						"images/ballLD.gif")), };

		ballMap.put("L", images[0]);
		ballMap.put("LU", images[1]);
		ballMap.put("U", images[2]);
		ballMap.put("RU", images[3]);
		ballMap.put("R", images[4]);
		ballMap.put("RD", images[5]);
		ballMap.put("D", images[6]);
		ballMap.put("LD", images[7]);
	}

	public int getTankID() {
		return TankID;
	}

	public void setTankID(int tankID) {
		TankID = tankID;
	}

	public Direction getBallDirection() {
		return ballDirection;
	}

	public void setBallDirection(Direction ballDirection) {
		this.ballDirection = ballDirection;
	}

	public int getIsFromTank() {
		return isFromTank;
	}

	public void setIsFromTank(int isFromTank) {
		this.isFromTank = isFromTank;
	}

	public int getBallX() {
		return ballX;
	}

	public void setBallX(int ballX) {
		this.ballX = ballX;
	}

	public int getBallY() {
		return ballY;
	}

	public void setBallY(int ballY) {
		this.ballY = ballY;
	}

	// 是否生死
	private boolean isLive() {
		return Live;
	}

	// 设置生死
	private void setLive(boolean live) {
		Live = live;
	}

	// 构造方法
	/*
	 * public Ball(Color ballColor,Direction ballDirection, int isFromTank , int
	 * ballX,int ballY,GameWindow gameWindow) { this.ballColor = ballColor;
	 * this.ballDirection = ballDirection; this.ballX=ballX; this.ballY=ballY;
	 * this.gameWindow = gameWindow; this.isFromTank=isFromTank;
	 * 
	 * }
	 */

	public Ball(int tankID, Direction ballDirection, int isFromTank, int ballX,
			int ballY, GameWindow gameWindow) {
		this.TankID = tankID;
		this.ballDirection = ballDirection;
		this.ballX = ballX;
		this.ballY = ballY;
		this.gameWindow = gameWindow;
		this.isFromTank = isFromTank;

	}

	// 线程一直调用draw画子弹，并且计算子弹位置
	public void draw(Graphics graphics) {
		if (!this.isLive()) {
			gameWindow.getBalls().remove(this);
		}
		this.hitTankClient(gameWindow.getTankClient());// 每颗子弹打击主机坦克
		this.hitNetTankClient(gameWindow.getTankClients());// 每颗子弹击打网络中的坦克
		this.hitCPUTanks(gameWindow.getCpuTanks());// 击中每一辆CPU坦克检测
		for (int i = 0; i < gameWindow.getWalls().size(); i++) {
			Wall w = gameWindow.getWalls().get(i);
			hitWall(w);
		}
		Color c = graphics.getColor();
		graphics.setColor(ballColor);

		switch (this.ballDirection) {// 根据当前坦克方向画出炮筒方向
		case L:
			graphics.drawImage(ballMap.get("L"), this.ballX, this.ballY, null);
			break;
		case LU: {
			graphics.drawImage(ballMap.get("LU"), this.ballX, this.ballY, null);
		}
			break;
		case U: {
			graphics.drawImage(ballMap.get("U"), this.ballX, this.ballY, null);
		}
			break;
		case RU: {
			graphics.drawImage(ballMap.get("RU"), this.ballX, this.ballY, null);
		}
			break;
		case R: {
			graphics.drawImage(ballMap.get("R"), this.ballX, this.ballY, null);
		}
			break;
		case RD: {
			graphics.drawImage(ballMap.get("RD"), this.ballX, this.ballY, null);
		}
			break;
		case D: {
			graphics.drawImage(ballMap.get("D"), this.ballX, this.ballY, null);

		}
			break;
		case LD: {
			graphics.drawImage(ballMap.get("LD"), this.ballX, this.ballY, null);

		}
			break;
		}

		graphics.setColor(c);
		this.move();
	}

	// 计算子弹位置
	private void move() {

		switch (ballDirection) {
		case L: {
			ballX -= BALLSPEED;
		}
			break;
		case LU: {
			ballX -= BALLSPEED;
			ballY -= BALLSPEED;
		}
			break;
		case U: {
			ballY -= BALLSPEED;
		}
			break;
		case RU: {
			ballX += BALLSPEED;
			ballY -= BALLSPEED;
		}
			break;
		case R: {
			ballX += BALLSPEED;
		}
			break;
		case RD: {
			ballX += BALLSPEED;
			ballY += BALLSPEED;
		}
			break;
		case D: {
			ballY += BALLSPEED;
		}
			break;
		case LD: {
			ballX -= BALLSPEED;
			ballY += BALLSPEED;
		}
			break;

		}
		if (ballX < 0 || ballY < gameWindow.getMenuHeight()+10
				|| ballX > gameWindow.getGameWidth() - Ball.BALLWIDTH
				|| ballY > gameWindow.getGameHeight() - Ball.BALLWIDTH) {//如果子弹X小于0，Y小于菜单+2的高，子弹大于窗口宽和高，子弹就死亡
			setLive(false);
		}
	}

	// 得到子弹的矩形
	private Rectangle getRect() {
		return new Rectangle(ballX, ballY, images[0].getWidth(null),
				images[0].getWidth(null));
	}

	// 击中CPU检测
	private boolean hitCPUTanks(List<CPUTank> cpuTanks) {// 如果子弹碰到坦克，并且坦克是活着的，返回true,否则返回false
		if(gameWindow.getNc()==null){
			for (int i = 0; i < cpuTanks.size(); i++) {
				CPUTank tmp = cpuTanks.get(i);
				if (this.getRect().intersects(tmp.getRectangle())
						&& tmp.isCPUTankLive()
						&& getIsFromTank() == Ball.CLIENTTANK) {// 验证碰撞&&CPU坦克是活的&&子弹是主机坦克打出的
					tmp.setCPUTankLive(false);// 让坦克死亡
					this.setLive(false);// 让子弹死亡
					gameWindow.getExplodes().add(
							new Explode(ballX, ballY, gameWindow));// 坦克死亡，在此位置添加一个爆炸
					
					int count = gameWindow.getShotCount();
					gameWindow.setShotCount(++count);
				}
			}
			return true;
		}
		return false;
	}

	// 击中列表中的坦克检测
	private boolean hitNetTankClient(List<TankClient> tcs) {
		if (gameWindow.getNc() != null) {//如果是联网中
			for (int i = 0; i < tcs.size();i++) {//跟每辆坦克验证
				TankClient tmp = tcs.get(i);
				if(tmp.getRectangle().intersects(this.getRect())&&tmp.isLive()&&tmp.getGood()!=this.getIsFromTank()){//如果子弹与坦克碰撞，且坦克是活得，且子弹的打出着和坦克的势力不是同一方
					this.setLive(false);// 子弹死亡
					int lifetmp = tmp.getNowlife();
					lifetmp -= 5;
					tmp.setNowlife(lifetmp);
					if (tmp.getNowlife() <= 0) {// 如果生命值为0
						tmp.setLive(false);// 主机坦克死亡
						gameWindow.getExplodes().add(
								new Explode(ballX, ballY, gameWindow));// 坦克死亡，在此位置添加一个爆炸
						if(tmp.getID()==gameWindow.getID()){
							int count = gameWindow.getDoomCount() + 1;
							gameWindow.setDoomCount(count);
						} else {
							int count = gameWindow.getShotCount();
						gameWindow.setShotCount(++count);
						}
						
						
						
					}
				}
			}
			return true;
			
		}

		return false;
	}

	// 击中主机检测
	private boolean hitTankClient(TankClient tankClient) {
		if (gameWindow.getNc() == null) {
			Rectangle rectangle = tankClient.getRectangle();// 得到本机坦克矩形
			if (this.getRect().intersects(rectangle) && tankClient.isLive()
					&& Ball.CPUTANK == getIsFromTank()) {// 如果子弹与客户坦克碰撞，并且客户坦克是活得,并且子弹来自CPU坦克
				this.setLive(false);// 子弹死亡
				int lifetmp = tankClient.getNowlife();
				lifetmp -= 5;
				tankClient.setNowlife(lifetmp);
				if (tankClient.getNowlife() <= 0) {// 如果生命值为0
					tankClient.setLive(false);// 主机坦克死亡
					gameWindow.getExplodes().add(
							new Explode(ballX, ballY, gameWindow));// 坦克死亡，在此位置添加一个爆炸

					int count = gameWindow.getDoomCount() + 1;
					gameWindow.setDoomCount(count);
				}
				return true;
			}
		}

		return false;
	}

	// 击中墙壁检测
	private boolean hitWall(Wall wall) {
		if (this.isLive() && this.getRect().intersects(wall.getRectangle())) {// 如果击中墙
			this.setLive(false);// 子弹死亡
			gameWindow.getExplodes().add(new Explode(ballX, ballY, gameWindow));// 加入子弹爆炸
			return true;
		}
		return false;
	}

}
