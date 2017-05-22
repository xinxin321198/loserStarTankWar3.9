# loserStarTankWar3.9
## 坦克大战（网络版）maven重构版

###  2011年，这是大学毕业时候的毕业设计，仿照马士兵的教学视频，自己编写并加以改进，为了巩固J2SE的基础知识，此项目采用面向对象设计模式，运用JAVA的AWT和swing组件建立游戏窗口，增加一个线程来持续重绘窗口，并且使用双缓冲技术消除屏幕的闪烁，监听器监听按键的消息控制坦克的移动和攻击等等，坦克将在游戏开始进行初始化，可实现向八个方向移动，打出炮弹，子弹击中敌方坦克，敌方坦克消失，消失的同时在此坦克位置产生爆炸效果，单机版的敌人坦克能随机移动，随机打出炮弹。单机版完成之后接着加入联机功能，运用socket技术创建一个游戏的服务器端，建立一个TCP的socket来监听所有连接上来的客户端，然后保存下客户端的IP地址、UDP端口号，储存进一个链表内，之后客户端就可以运用UDP的socket来实现发送消息到服务器，服务器再把此消息转发给其它储存在链表内的客户端，达到联机目的。此游戏综合了面向对象的设计思想，AWT组件，socket，IO，线程的知识，使用的是eclipse开发制作。

## ps:
**启动的运行参数里增加-Dfile.encoding=GB18030，否则菜单会出现方块乱码**
**多客户端联机时udp端口号不能重复，加入服务器时请修改**

![示例](./src/main/resources/images/1.png)
![示例](./src/main/resources/images/2.png)
![示例](./src/main/resources/images/3.png)

* 昵称：loserStar<br/>
* email:xinxin321198@gmail.com<br/>
* email2:362527240@qq.com<br/>
* qq:362527240<br/>
* github:https://github.com/xinxin321198<br/>
