package bhz.netty.custom.client;

import bhz.netty.custom.codec.NettyMessageDecoder;
import bhz.netty.custom.codec.NettyMessageEncoder;
import bhz.netty.custom.struct.Header;
import bhz.netty.custom.struct.NettyMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

	public static void main(String[] args) throws Exception {
		//ONE:
		//1 线程工作组
		EventLoopGroup work = new NioEventLoopGroup();
		
		//TWO:
		//3 辅助类。用于帮助我们创建NETTY服务
		Bootstrap b = new Bootstrap();
		b.group(work)	//绑定工作线程组
		 .channel(NioSocketChannel.class)	//设置NIO的模式
		 // 初始化绑定服务通道
		 .handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				
				sc.pipeline().addLast(new NettyMessageDecoder(1024*1024*5, 4, 4));
				sc.pipeline().addLast(new NettyMessageEncoder());
				sc.pipeline().addLast(new ClientHandler());
			}
		 });
		
		ChannelFuture cf =  b.connect("127.0.0.1", 8765).syncUninterruptibly();
		
		System.err.println("client start....");
		
		Channel c = cf.channel();
		
		for(int i = 0; i < 500; i ++){
			NettyMessage message = new NettyMessage();
			Header header = new Header();
			header.setSessionID(1001L);
			header.setPriority((byte)1);
			header.setType((byte)1);
			message.setHeader(header);
			message.setBody("我是请求数据" + i);
			c.writeAndFlush(message);
		}
		
		//释放连接
		cf.channel().closeFuture().sync();
		work.shutdownGracefully();
	}
}
