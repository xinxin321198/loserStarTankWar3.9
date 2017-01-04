package com.loserstar.tankwar.module;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Msg {
	public static final int TANK_NEW_MSG = 1;
	public static final int TANK_MOVE_MSG =2;
	public static final int BALL_NEW_MSG =3;
	public static final int TANK_RELIVE_MSG=4;
	
	public void Send(DatagramSocket ds,String IP,int udpPort);
	public void Parse(DataInputStream dis);
}
