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

public class BallNewMsg implements Msg {
	private Ball b= null;
	private int msgType= Msg.BALL_NEW_MSG;
	private GameWindow gameWindow=null;
	private ByteArrayOutputStream baos = null;
	private DataOutputStream dos = null;
	public BallNewMsg(Ball b) {
		this.b=b;
	}

	public BallNewMsg(GameWindow gameWindow){
		this.gameWindow=gameWindow;
	}

	public void Send(DatagramSocket ds, String IP, int udpPort) {
		baos = new ByteArrayOutputStream();//创建字节数组输出流
		dos = new DataOutputStream(baos);//套上数据输出流
		try {
			dos.writeInt(msgType);
			dos.writeInt(b.getTankID());
			dos.writeInt(b.getBallX());
			dos.writeInt(b.getBallY());
			dos.writeInt(b.getBallDirection().ordinal());//子弹方向下标
			dos.writeInt(b.getIsFromTank());//子弹谁打出的
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


	public void Parse(DataInputStream dis) {
		try {		
			int id = dis.readInt();
			if(id==gameWindow.getID()){
				return ;
			}
			int x = dis.readInt();
			int y = dis.readInt();
			int directionIndex = dis.readInt();
			Direction[] directions = Direction.values();
			Direction direction = directions[directionIndex];
			int FromTank = dis.readInt();
			Ball ball= new Ball(id,direction, FromTank, x, y, gameWindow);
			gameWindow.getBalls().add(ball);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
