package com.loserstar.tankwar.module;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.loserstar.tankwar.client.GameWindow;
//客户坦克消息类
public class TankNewMsg implements Msg{
	private GameWindow gameWindow = null;//一个窗口对象
	private ByteArrayOutputStream baos = null;
	private DataOutputStream dos = null;
	int msgType = Msg.TANK_NEW_MSG;
	public TankNewMsg(GameWindow gameWindow){
		this.gameWindow = gameWindow;
	}

	
	public void Send (DatagramSocket ds ,String IP,int udpPort){
		baos = new ByteArrayOutputStream();//创建字节数组输出流
		dos = new DataOutputStream(baos);//套上数据输出流
		try {
			dos.writeInt(msgType);
			dos.writeInt(gameWindow.getID());//写进自己ID
			dos.writeInt(gameWindow.getTankClient().getTankClientX());//写进坦克X坐标
			dos.writeInt(gameWindow.getTankClient().getTankClientY());//写进坦克Y坐标
			dos.writeInt(gameWindow.getTankClient().getDirection().ordinal());//得到当前方向值得下标
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] buf = baos.toByteArray();//把数据流转为字节数组
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length,new InetSocketAddress(IP, udpPort));//以这个字节数组数据创建一个数据包
			ds.send(dp);//发送这个数据包
		} catch (SocketException e) {
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
		
	}

	public void Parse(DataInputStream dis){
		try {
			int id = dis.readInt();

			if(gameWindow.getID()==id){
				return ;
			}
			
			
			
			int x = dis.readInt();
			int y = dis.readInt();
			int directionIndex = dis.readInt();
			Direction[] directions = Direction.values();
			Direction direction = directions[directionIndex];
			
			boolean exist = false;
			for(int i=0;i<gameWindow.getTankClients().size();i++){
				TankClient tc  = gameWindow.getTankClients().get(i);
				if(tc.getID()==id){//如果本窗口内的坦克等于传过来的坦克的ID，说明已经存在了
					exist = true;
					break;
				}
			}
			
			if(!exist){
				TankNewMsg newMsg = new TankNewMsg(gameWindow);
				gameWindow.getNc().sendMsg(newMsg);
				
				
				TankClient client = new TankClient(x, y, gameWindow);
				client.setID(id);
				if(id%2==0){
					TankMoveMsg msg = new TankMoveMsg(id, Direction.STOP, 200, 200);
					gameWindow.getNc().sendMsg(msg);
//					setXY(gameWindow.getTankClient().getGood());
					client.setGood(Ball.CLIENTTANK);
				}
				else{
					TankMoveMsg msg = new TankMoveMsg(id, Direction.STOP, 1200, 200);
					gameWindow.getNc().sendMsg(msg);
//					setXY(gameWindow.getTankClient().getGood());
					client.setGood(Ball.CPUTANK);
				}
				client.setDirection(direction);
				client.setLive(true);
				client.setDirection(Direction.STOP);
				gameWindow.getTankClients().add(client);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

	
	public void setXY(int b){
		if(b==Ball.CLIENTTANK){
			gameWindow.getTankClient().setTankClientX(200);
			gameWindow.getTankClient().setTankClientX(200);
		}
		else if(b==Ball.CPUTANK){
			gameWindow.getTankClient().setTankClientX(1200);
			gameWindow.getTankClient().setTankClientX(200);
		}
	}
}
