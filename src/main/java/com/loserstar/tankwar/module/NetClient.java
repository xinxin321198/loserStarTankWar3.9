package com.loserstar.tankwar.module;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.loserstar.tankwar.client.GameWindow;

//客户端网络数据处理类
public class NetClient {
	
	private int udpPort;//客户端自己的端口
	private GameWindow gameWindow = null;//窗口引用
	private int serverUDPPort ;
	private String ServerIP;
	private int serverTCPPort;
	Socket s = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	private DatagramSocket ds =null;//UDPsocket
	
	public int getUdpPort() {
		return udpPort;
	}

	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}
	
	//构造
	public NetClient(GameWindow gameWindow,int myudpport,int serverUDPPort,String serverIP,int serverTCPPort){
		udpPort = myudpport;
		this.serverUDPPort = serverUDPPort;
		this.ServerIP = serverIP;
		this.serverTCPPort = serverTCPPort;
		this.gameWindow = gameWindow;
		try {
			ds = new DatagramSocket(udpPort);//占用这个端口创建UDPsocket
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void connect() {
		try {
			s = new Socket(this.ServerIP, this.serverTCPPort);//创建一个socket，服务器IP，服务器端口号
			dos = new DataOutputStream(s.getOutputStream());//数据输出流
			dos.writeInt(udpPort);//发送自己的UDP端口过去
			dis = new DataInputStream(s.getInputStream());//数据输入流
			int id = dis.readInt();
			gameWindow.setID(id);//接受服务器分配的ID，设置自己窗口的ID
			gameWindow.getTankClient().setID(id);
			if(id%2==0){
				gameWindow.getTankClient().setGood(Ball.CLIENTTANK);
			
			}
			else{
				gameWindow.getTankClient().setGood(Ball.CPUTANK);
				
			}
System.out.println("Connected to server -  ID:"+gameWindow.getID());//显示自己的ID
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(s != null) {
				try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		TankNewMsg msg = new TankNewMsg(gameWindow);//创建一条消息
		sendMsg(msg);//发送这条消息到服务器
		
		new Thread(new UDPRecvThread()).start();
	}
	
	public void sendMsg(Msg msg){
		msg.Send(ds,this.ServerIP,this.serverUDPPort);//发送这个数据包到服务器
	}
	
	private class UDPRecvThread implements Runnable{
		byte[] buf = new byte[1024];
		public void run() {
			while(ds != null){
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					Parse(dp);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void Parse(DatagramPacket dp) throws IOException{
			ByteArrayInputStream bais = new ByteArrayInputStream(buf,0,buf.length);
			DataInputStream dis = new DataInputStream(bais);
			int msgType = dis.readInt();
			Msg msg = null;
			switch (msgType) {
				case Msg.TANK_NEW_MSG:{//新加入坦克
					 msg= new TankNewMsg(gameWindow);
					msg.Parse(dis);
					
				}break;
				case Msg.TANK_RELIVE_MSG:{//坦克重生
					msg = new TankReliveMsg(gameWindow);
System.out.println("接受到重生消息");
					msg.Parse(dis);
				}break;
				case Msg.TANK_MOVE_MSG:{//坦克移动
					 msg = new TankMoveMsg(gameWindow);
					 msg.Parse(dis);
				}break;
				case Msg.BALL_NEW_MSG:{//子弹新加入
					msg = new BallNewMsg(gameWindow);
					msg.Parse(dis);
				}break;
				default:{
					
				}break;
			}
		}
	}
	
}
