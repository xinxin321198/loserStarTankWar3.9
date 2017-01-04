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

public class TankReliveMsg implements Msg {


	private int id;
	private int x;
	private int y;
	private boolean live = true;
	private int msgType = Msg.TANK_RELIVE_MSG;
	private GameWindow gameWindow;
	private ByteArrayOutputStream baos = null;
	private DataOutputStream dos = null;

	public TankReliveMsg(int id,int x,int y,boolean live) {
		this.id= id;
		this.x=x;
		this.y=y;
		this.live=live;
	}
	
	public TankReliveMsg(GameWindow gameWindow){
		this.gameWindow=gameWindow;
	}
	
	
	public void Send(DatagramSocket ds, String IP, int udpPort) {
		baos = new ByteArrayOutputStream();// 创建字节数组输出流
		dos = new DataOutputStream(baos);// 套上数据输出流
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeBoolean(live);
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] buf = baos.toByteArray();// 把数据流转为字节数组
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length,
					new InetSocketAddress(IP, udpPort));// 以这个字节数组数据创建一个数据包
			ds.send(dp);// 发送这个数据包
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void Parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (gameWindow.getID() == id) {
				return;
			}
			int x = dis.readInt();
			int y = dis.readInt();
			boolean live = dis.readBoolean();
			for(int i =0;i<gameWindow.getTankClients().size();i++){
				TankClient t = gameWindow.getTankClients().get(i);
				if(t.getID()==id){
					t.setLive(live);
					t.setNowlife(100);
					t.setTankClientX(x);
					t.setTankClientY(y);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

}
