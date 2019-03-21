package bhz.netty.custom.server;

import bhz.netty.custom.struct.Header;
import bhz.netty.custom.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 当我们通道进行激活的时候 触发的监听方法
	 */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    
    	System.err.println("--------通道激活------------");
    }
	
    /**
     * 当我们的通道里有数据进行读取的时候 触发的监听方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx /*NETTY服务上下文*/, Object msg /*实际的传输数据*/) throws Exception {
    	
    	NettyMessage requestMessage = (NettyMessage)msg;
    	
    	System.err.println("Server: " + requestMessage.getBody());

    	NettyMessage responseMessage = new NettyMessage();
		Header header = new Header();
		header.setSessionID(2002L);
		header.setPriority((byte)2);
		header.setType((byte)2);
		responseMessage.setHeader(header);
		responseMessage.setBody("我是响应数据: " + requestMessage.getBody());
		ctx.writeAndFlush(responseMessage);
    	
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       System.err.println("--------数据读取完毕----------");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
    	System.err.println("--------服务器数据读异常----------: ");
    	cause.printStackTrace();
        ctx.close();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
