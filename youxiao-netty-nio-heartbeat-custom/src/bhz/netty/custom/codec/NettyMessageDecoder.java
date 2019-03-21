package bhz.netty.custom.codec;

import java.io.IOException;

import bhz.netty.custom.struct.Header;
import bhz.netty.custom.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * LengthFieldBasedFrameDecoder 是为了解决 拆包粘包等问题的
 * @author Alienware
 *
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

	private MarshallingDecoder marshallingDecoder;
	
	/**
	 * 那减8应该是因为要把CRC和长度本身占的剪掉了。我猜错没
	 * @param maxFrameLength 第一个参数代表最大的长度   1024*1024*5
	 * @param lengthFieldOffset 代表长度属性的偏移量 简单来说就是message中 总长度的起始位置   4
	 * @param lengthFieldLength 代表长度属性的长度 整个属性占多长  4
	 * @throws IOException 
	 */
	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		this.marshallingDecoder = new MarshallingDecoder();
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx,  ByteBuf in) throws Exception {
		//1 调用父类(LengthFieldBasedFrameDecoder)方法:
		ByteBuf frame  = (ByteBuf)super.decode(ctx, in);
		
		if(frame == null){
			return null;
		}
		
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setCrcCode(frame.readInt());		//crcCode ----> 添加通信标记认证逻辑
		header.setLength(frame.readInt());		//length
		header.setSessionID(frame.readLong());	//sessionID
		header.setType(frame.readByte());		//type
		header.setPriority(frame.readByte());	//priority
		
		message.setHeader(header);
		
		if(frame.readableBytes() > 4) {
			message.setBody(marshallingDecoder.decode(frame));
		}
		return message;
	}

	
	
	
	
	
	
	
	
	
	
	
}
