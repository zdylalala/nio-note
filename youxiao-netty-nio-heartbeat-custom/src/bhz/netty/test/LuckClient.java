package bhz.netty.test;

import java.util.UUID;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class LuckClient {

    public static void main(String args[]) throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyLuckInitializer());

            // Start the connection attempt.
            Channel ch = b.connect("127.0.0.1", 8888).sync().channel();

            
            for(int i = 0; i < 300; i ++){
                int version = 1;
                String sessionId = UUID.randomUUID().toString() + i;
                String content = "I'm the luck protocol! " + i;

                LuckHeader header = new LuckHeader(version, content.length(), sessionId);
                LuckMessage message = new LuckMessage(header, content);
                ch.writeAndFlush(message);            	
            }


            ch.closeFuture().sync();

        } finally {
            group.shutdownGracefully();	
        }
    }
}
