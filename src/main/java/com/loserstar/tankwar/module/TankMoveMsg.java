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

public class TankMoveMsg implements Msg {

	private int id;
	private int x;
	private int y;
	private Direction direction;
	private int msgType = Msg.TANK_MOVE_MSG;
	private GameWindow gameWindow;
	private ByteArrayOutputStream baos = null;
	private DataOutputStream dos = null;

	public TankMoveMsg(int id, Direction direction,int x,int y) {
		super();
		this.id = id;
		this.direction = direction;
		this.x=x;
		this.y=y;
	}

	public TankMoveMsg(GameWindow gameWindow) {
		this.gameWindow = gameWindow;
	}

	public void Send(DatagramSocket ds, String IP, int udpPort) {
		baos = new ByteArrayOutputStream();// 创建字节数组输出流
		dos = new DataOutputStream(baos);// 套上数据输出流
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(direction.ordinal());// 写进自己的方向下标
			dos.writeInt(x);
			dos.writeInt(y);
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
			Direction[] directions = Direction.values();
			int index = dis.readInt();
			int x = dis.readInt();
			int y = dis.readInt();
			direction = directions[index];
			for(int i = 0;i<gameWindow.getTankClients().size();i++){//取出本窗口的所有客户坦克比较
				TankClient client = gameWindow.getTankClients().get(i);
				if(client.getID()==id){//ID相同的
					client.setDirection(direction);
					client.setTankClientX(x);
					client.setTankClientY(y);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
