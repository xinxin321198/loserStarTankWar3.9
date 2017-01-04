/**
 * author: loserStar
 * version: 2017年1月4日下午3:43:32
 * email:362527240@qq.com
 * remarks:
 */
package com.loserstar.tankwar.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import com.loserstar.tankwar.module.Ball;
import com.loserstar.tankwar.module.CPUTank;
import com.loserstar.tankwar.module.Explode;
import com.loserstar.tankwar.module.NetClient;
import com.loserstar.tankwar.module.TankClient;
import com.loserstar.tankwar.module.Wall;


/*
 * 0.1画个点，能四个方向移动
 *0.2双缓冲，不闪烁
 *0.3各种常量
 *0.4窗口类和主机坦克类分开
 *0.5八个方向行走（BUG）
 *0.6八个方向行走
 *0.7空格打子弹，子弹闪烁
 *0.71 Z打出子弹
 *0.8 z打出子弹
 *0.9子弹出界消失
 *1.0 一辆敌人坦克
 *1.1 子弹击中敌人坦克，消失
 *1.11.。。。。。。。。
 *1.2 子弹击中敌人坦克爆炸效果
 *1.3 CPU坦克自己换方向
 *1.4 CPU坦克自己换方向，并且走随机步数之后更改方向
 *1.5 CPU坦克一定几率打出炮弹
 *1.6 两堵墙，并且子弹，敌人坦克不能穿墙
 *1.7 A键增加坦克，Z发炮弹，CPU坦克不重叠
 *1.8 S键超级炮弹
 *1.9加入血，100，打一炮扣10点
 *2.0 加入血条，根据血量来画出
 *2.1 加入血块，主机坦克值了之后补满所有血。并且优化代码
 *2.2 血块按一定路线运动漂浮
 *2.3 F2键坦克复活，重新开始,根据屏幕分辨率改变游戏窗口，并加入菜单条，处理问题
 *2.4修正方向枚举变量（单独的一个文件）
 *2.5 加入爆炸图片(解决第一次爆炸不显示的BUG)
 *2.6 碰撞检测，图片的矩形
 *2.7配置文件，加入singleton模式
 *2.8纯净版，没有墙，没有CPU坦克，没有血块（为网络版做准备）,加上服务器类和客户端网络信息处理类
 *2.9服务器能为每个客户端标识唯一ID
 *3.0发送坦克的坐标到服务器
 *3.1服务器发送坦克数据到每个客户端，客户端添加解析数据的方法,并且在客户端自己窗口内添加此辆客户坦克（注意添加坦克时设置生死量）
 *3.2 接口抽象出消息类，发送坦克移动的消息
 *3.3 互相显示存在的坦克，并且精准坦克位置
 *3.4联机网络配置对话框
 *3.5判断是否联网，加入子弹加入的消息，子弹能显示在互相窗口，坦克死亡同步，势力区分不同的坦克图片，加入4面墙，红色出现在左边，蓝色出现在右边
 *3.6单机处理,完成BUG
 *3.7修正网络中不能击中坦克，（子弹与客户端坦克碰撞检测的返回值问题）
 *3.8修正网络中其他坦克复活时的血量是满血,修正子弹上方出界问题，修正红色坦克显示红色血条，蓝色坦克显示蓝色血条,联网中的死亡次数与击毁地方坦克数量的BUG
 *3.9加入墙的图片,精简代码
 */
public class GameWindow extends Frame{	
	MenuBar menuBar = null;
	Menu menu1 =null;
	MenuItem menu1Item1 =null;
	MenuItem menu1Item2 = null;
	Menu menu2 = null;
	MenuItem menu2Item1=null;
	Help hp = null;
	NetDialog netDialog = null;
	GameWindow gameWindow = this;

	private int gameWidth = 0;//游戏窗口宽度
	private int gameHeight = 0;//游戏窗口高度
	private int menuHeight = 45;//菜单条高度
	private Image img = null;

	private List<TankClient> tankClients = new ArrayList<TankClient>();
	private TankClient tankClient = null;

	private List<Wall> walls = new ArrayList<Wall>();




	public List<Wall> getWalls() {
		return walls;
	}

	public void setWalls(List<Wall> walls) {
		this.walls = walls;
	}


	private List<Explode> explodes = new ArrayList<Explode>();//爆炸类
	private List<Ball> balls = new ArrayList<Ball>();//子弹类
	private List<CPUTank> cpuTanks = new ArrayList<CPUTank>();//CPU坦克列表
//	private LifeBlock lifeBlock = null;//血块
	private int doomCount = 0;//本机死亡的次数
	private int shotCount = 0;//击毁坦克的次数

	public int getShotCount() {
		return shotCount;
	}

	public void setShotCount(int shotCount) {
		this.shotCount = shotCount;
	}


	//网络
	private int ID ;
	private NetClient nc = null;
	
	
	
	public int getDoomCount() {
		return doomCount;
	}

	public void setDoomCount(int doomCount) {
		this.doomCount = doomCount;
	}
	
	public NetClient getNc() {
		return nc;
	}

	public void setNc(NetClient nc) {
		this.nc = nc;
	}

	public TankClient getTankClient() {
		return tankClient;
	}

	public void setTankClient(TankClient tankClient) {
		this.tankClient = tankClient;
	}
	
	public List<TankClient> getTankClients() {
		return tankClients;
	}

	public void setTankClients(List<TankClient> tankClients) {
		this.tankClients = tankClients;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	

	public int getMenuHeight() {
		return menuHeight;
	}

	public void setMenuHeight(int menuHeight) {
		this.menuHeight = menuHeight;
	}
	public int getGameWidth() {
		return gameWidth;
	}

	public void setGameWidth(int gameWidth) {
		this.gameWidth = gameWidth;
	}
	
	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}
	

	public List<Explode> getExplodes() {
		return explodes;
	}

	public void setExplodes(List<Explode> explodes) {
		this.explodes = explodes;
	}

	public List<Ball> getBalls() {
		return balls;
	}

	public void setBalls(List<Ball> balls) {
		this.balls = balls;
	}

	public List<CPUTank> getCpuTanks() {
		return cpuTanks;
	}

	public void setCpuTanks(List<CPUTank> cpuTanks) {
		this.cpuTanks = cpuTanks;
	}


	



	//构造
	public GameWindow(){
		gameWidth = 1360;
		gameHeight = 768;
		Wall w1 = new Wall(Wall.VERTICAL,350, 40, 20, 400, this); //创建墙20宽，400高
		Wall w2 = new Wall(Wall.HORIZONTAL,70, 440,300,20 , this);//创建墙300宽，20高
		Wall w3 = new Wall(Wall.VERTICAL,1000, 40, 20, 400, this);
		Wall w4  =new Wall(Wall.HORIZONTAL,1000, 440, 300, 20, this);
		walls.add(w1);
		walls.add(w2);
		walls.add(w3);
		walls.add(w4);
//		lifeBlock = new LifeBlock(130, 432);
		tankClient = new TankClient(200, 200, gameWindow);
		tankClients.add(tankClient);
		this.lunchFrame();
		this.setVisible(true);//显示窗口	
		GameWindowRepaint twwr = new GameWindowRepaint();//重画窗口类,实现Runnable，线程调用重画
		new Thread(twwr).start();//线程开始
	}
	
	//初始化窗口
	public void lunchFrame(){
		this.setBounds(5,5,gameWidth,gameHeight);//X,Y坐标，宽高
		this.setTitle("坦克大战客户端");//标题
		this.setResizable(false);//不调整窗口大小
		this.setBackground(Color.black);//背景色
		
		menu1Item1 = new MenuItem("单机模式");
		menu2Item1 = new MenuItem("帮助");
		menu1Item2 = new MenuItem("联机模式");
		menu1 =new Menu("开始");
		menu2 = new Menu("帮助");
		menuBar = new MenuBar();
		menu1.add(menu1Item1);
		menu1.add(menu1Item2);
		menu2.add(menu2Item1);
		menuBar.add(menu1);
		menuBar.add(menu2);
		this.setMenuBar(menuBar);
		hp = new Help(this,"帮助");
		netDialog = new NetDialog(this,"网络配置");
		menu1Item1.addActionListener(new ActionListener() {//开始单机游戏的监听
			
			public void actionPerformed(ActionEvent e) {
				tankClient.setLive(true);
			}
		});
		menu2Item1.addActionListener(new ActionListener() {//帮助的监听
			
			public void actionPerformed(ActionEvent e) {
				hp.lunchFrame();
			}
		});
		menu1Item2.addActionListener(new ActionListener() {//开始联机的监听
			
			public void actionPerformed(ActionEvent e) {
				
				netDialog.LunchDialog();
			}
		});
		
		
		
		this.addWindowListener(new WindowAdapter() {//匿名类监听器，window关闭的处理
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		this.addKeyListener(new GameWindowKeyListener());//监听按键
		
		
	}
	
	
	//更新窗口方法，双缓冲，防止闪烁
	public void update(Graphics g) {		
		if(null==img){
			img = this.createImage(gameWidth,gameHeight);
		}
		
		Graphics imgGraphics = img.getGraphics();
		Color c=g.getColor();
		imgGraphics.setColor(Color.black);
		imgGraphics.fillRect(0, menuHeight, gameWidth, gameHeight);
		g.setColor(c);
		paint(imgGraphics);
		g.drawImage(img,0,0,null);
	}


	//绘制方法
	public void paint(Graphics g) {
		Color thiscolor = g.getColor();
		Font f =g.getFont();
		
		//子弹
		for(int i = 0; i<balls.size();i++){//取出每颗子弹
				Ball b= balls.get(i);
				b.draw(g);//画子弹
		}	
		
		
		//画客户坦克
		for(int i = 0;i<tankClients.size();i++){
			TankClient t =tankClients.get(i);
			if(t==null){
				return;
			}
			t.draw(g);
			
		}
		
		
		//CPU坦克
		if(nc==null){
			for(int i = 0;i<cpuTanks.size();i++){//取出每辆CPU坦克
				CPUTank cpu = cpuTanks.get(i);
				cpu.draw(g);//cpu坦克画自己
			}
		}
		

		
		//爆炸
		for(int i=0;i<explodes.size();i++){//取出每个爆炸
			explodes.get(i).draw(g);
		}
		
		//墙
		for(int i=0;i<walls.size();i++){
			Wall wall =walls.get(i);
			wall.draw(g);
		}
		
		//血块
//		lifeBlock.draw(g);
		
		//游戏信息
		g.setColor(Color.red);
		g.drawString("子弹数量："+balls.size(), 20, menuHeight+10);							
		g.drawString("爆炸数量："+explodes.size(), 20, menuHeight+30);			
		if(nc==null)g.drawString("cpu坦克数量："+cpuTanks.size(), 20, menuHeight+50);		
		if(nc!=null)g.drawString("您当前ID："+ID, 20, menuHeight+50);
		g.drawString("主机坦克血量："+tankClient.getNowlife(), 20, menuHeight+70);
		g.drawString("您击毁的坦克数量："+shotCount, 20, menuHeight+90);

		//验证游戏结束
		if(!tankClient.isLive()){
			Font font= new Font("gameover", Font.BOLD, 80);
			g.setFont(font);
			g.drawString("GAME OVER", gameWidth/3, gameHeight/3);
			g.drawString("死亡次数："+String.valueOf(doomCount),  gameWidth/3,  gameHeight/3+100);
		}
		

		g.setFont(f);
		g.setColor(thiscolor);
	}

	public static void main(String[] args) {
		new GameWindow();
	}
	

	
	
	public int getGameHeight() {
		return gameHeight;
	}

	public void setGameHeight(int gameHeight) {
		this.gameHeight = gameHeight;
	}


	//监听按键
	private class GameWindowKeyListener extends KeyAdapter {//按键消息传给客户坦克自己处理
		public void keyReleased(KeyEvent key) {
			tankClient.keyReleased(key);
		}

		public void keyPressed(KeyEvent key) {
			tankClient.keyPreesed(key);
		}
	}

	//曾加一个线程负责重画窗口
	private class GameWindowRepaint implements Runnable {
		

		boolean repaintBool = false;//重画开始或停止
		
		public GameWindowRepaint() {//构造方法
			super();
			this.repaintBool = true;
		}
		
		
		public void run() {//线程调用的方法
			while(repaintBool){
				repaint();//重绘的方法，一直调用paint和update方法
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}

	
	
	private class Help extends Dialog{
		GameWindow gameWindow= null;
		Label lb1 = null;
		Label lb2 = null;
		Label lb3 = null;
		Label lb4 = null;
		
		public Help(GameWindow gameWindow,String title){
			super(gameWindow,title);
			lb1 =new Label("Z 键打炮");
			lb2 =new Label("A 键加入敌人坦克（单机）");
			lb3 =new Label("S 键超级子弹（单机）");
			lb4 =new Label("F2 信春哥，你懂的！");
			this.gameWindow =gameWindow;
		}
		
		public void lunchFrame(){
			this.setLayout(new GridLayout(6, 1));
			this.setBounds(300, 300, 350, 100);
			this.add(lb1);
			this.add(lb2);
			this.add(lb3);
			this.add(lb4);
			this.pack();
			this.setVisible(true);
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					gameWindow.hp.setVisible(false);
				}
			});
		}
	}
	
	
	private class NetDialog extends Dialog{
		String titleString=null;
		Label lbServerIP = null;//服务器IP
		TextField txServerIP = null;
		Label lbServerUDPPort = null;//服务器UDP端口
		TextField txServerUDPPort = null;
		Label lbServerTCPPort = null;//服务器TCP端口号
		TextField txServerTCPPort = null;
		Label lbLocalUDPPort = null;//本机UDP端口
		TextField txLocalUDPPort = null;
		Button bYES = null;
		Button bCANCEL = null;
		
		public NetDialog(GameWindow gameWindow,String title){
			super(gameWindow,title);
			this.titleString = "联机配置";
			lbServerIP = new Label("ServerIP:");
			txServerIP = new TextField("127.0.0.1", 12);
			lbServerUDPPort = new Label("ServerUDPPort:");
			txServerUDPPort = new TextField("5555",12);
			lbServerTCPPort = new Label("ServerTCPPort:");
			txServerTCPPort = new TextField("4444",12);
			lbLocalUDPPort = new Label("LocalUDPPort:");
			txLocalUDPPort = new TextField("2221",12);
			bYES = new Button("YES");
			bCANCEL = new Button("CANCEL");
		}
		
		public void LunchDialog(){
			add(lbServerIP);
			add(txServerIP);
			add(lbServerUDPPort);
			add(txServerUDPPort);
			add(lbServerTCPPort);
			add(txServerTCPPort);
			add(lbLocalUDPPort);
			add(txLocalUDPPort);
			add(bYES);
			add(bCANCEL);
			this.setTitle(titleString);
			this.setLayout(new FlowLayout());
			this.setLocation(300, 300);
			this.pack();
			this.setResizable(false);
			setVisible(true);
			
			
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					gameWindow.netDialog.setVisible(false);
				}
			});
			
			
			bCANCEL.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					gameWindow.netDialog.setVisible(false);
				}
			});
			
			bYES.addActionListener(new ActionListener() {//确定联机
				
				public void actionPerformed(ActionEvent e) {
				for(int i=0;i<tankClients.size();i++){//所有在线坦克设为活得
					TankClient t =tankClients.get(i);
					t.setLive(true);
				}
				
				int myudpport = Integer.parseInt(txLocalUDPPort.getText().trim());
				String serverip = txServerIP.getText().trim();
				int servertcpport = Integer.parseInt(txServerTCPPort.getText().trim());
				int serverudpport = Integer.parseInt(txServerUDPPort.getText().trim());
System.out.println(servertcpport);
				nc = new NetClient(gameWindow, myudpport, serverudpport, serverip, servertcpport);
				nc.connect();
				gameWindow.netDialog.setVisible(false);
				
				if(gameWindow.getTankClient().getGood()==Ball.CLIENTTANK){
					tankClient.setTankClientX(200);
					tankClient.setTankClientY(200);
				}
				else{
					tankClient.setTankClientX(1200);
					tankClient.setTankClientY(200);
				}
				}
			});
			
			
		}
		
	}




}
