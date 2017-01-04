package com.loserstar.tankwar.server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

//服务器类
public class TankServer {
	public static int ID = 100;// 分配给客户端的ID号（用UUID或GUID实现）
	public static final int TCP_PORT = 4444;// TCP端口号
	public static final int UDP_PORT = 5555;// UDP占用的端口号
	List<Client> clients = new ArrayList<Client>();// 保存客户端连接信息的列表

	public void start() {

		new Thread(new UDPThread()).start();// 启一个线程监听接收到的UDP数据
		ServerSocket ss = null;// 服务器TCPsocket类
		try {
			ss = new ServerSocket(TCP_PORT);// 服务器占用的端口号
		} catch (Exception e) {
		}
		while (true) {
			Socket s = null;
			try {
				s = ss.accept();// 接受一个socket连接
				DataInputStream dis = new DataInputStream(s.getInputStream());// 创建数据输入流
				String IP = s.getInetAddress().getHostAddress();// 根据socket拿到IP地址
				int udpPort = dis.readInt();// 读出客户端发送过来的UDP的端口号
				Client c = new Client(IP, udpPort);// 创建客户端信息类
				clients.add(c);// 储存进列表
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());// 创建数据输出流
				dos.writeInt(ID++);// 向客户端发送一个ID号
				// 打印出客户端的IP地址，客户端的TCP端口号，客户端的UDP端口号
				System.out.println("A Client Connect! Addr- "
						+ s.getInetAddress() + ":" + s.getPort()
						+ "---UDPPort:" + udpPort);

			} catch (Exception e) {
			} finally {
				if (null != s) {
					try {
						s.close();// 关闭
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		new TankServer().start();
	}

	private class Client {
		String IP;// 客户端IP
		int udpPort;// 客户端的UDP端口号

		public Client(String IP, int udpPort) {
			this.IP = IP;
			this.udpPort = udpPort;
		}
	}

	private class UDPThread implements Runnable {
		byte[] buf = new byte[1024];
		DatagramSocket datagramSocket = null;

		public void run() {// 接受客户端发送过来的数据，并且转发给其他客户端
			try {
				datagramSocket = new DatagramSocket(UDP_PORT);// 创建一个UDPsocket占用UDP_PORT端口
			} catch (SocketException e) {
				e.printStackTrace();
			}
			System.out.println("Server UDP thread started at port :" + UDP_PORT);
			while (null != datagramSocket) {// 如果UDPsocket不为null
				DatagramPacket dp = null;
				try {
					dp = new DatagramPacket(buf, buf.length);// 用字节数组建一个UDP数据包
					datagramSocket.receive(dp);// 接受UDP数据，储存进DUP数据包里
					// 转发给其他客户端
					for (int i = 0; i < clients.size(); i++) {
						Client c = clients.get(i);// 拿出每个客户端
						dp.setSocketAddress(new InetSocketAddress(c.IP,
								c.udpPort));// 设置UDPsocket的发送地址
						datagramSocket.send(dp);// 发送	
					}
					System.out.println("a packet received!发送给"+clients.size()+"个客户端");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
