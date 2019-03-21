package bhz.netty.custom.codec;

import java.io.IOException;

import org.jboss.marshalling.Unmarshaller;

import io.netty.buffer.ByteBuf;

public class MarshallingDecoder {
	
	private Unmarshaller unmarshaller;

	public MarshallingDecoder() throws IOException{
		this.unmarshaller = MarshallingCodeCFactory.buildUnMarshalling();
	}
	
	public Object decode(ByteBuf in) throws Exception {
		try {
			//1 首先读取4个长度(实际body内容长度)
			int bodySize = in.readInt();
			//2 获取实际body的缓冲内容
			ByteBuf buf = in.slice(in.readerIndex(), bodySize);
			//3 转换
			ChannelBufferByteInput input = new ChannelBufferByteInput(buf);
			//4 读取操作:
			this.unmarshaller.start(input);
			Object ret = this.unmarshaller.readObject();
			this.unmarshaller.finish();
			//5 读取完毕以后, 更新当前读取起始位置:  
			in.readerIndex(in.readerIndex() + bodySize);
			
			return ret;
			
		} finally {
			this.unmarshaller.close();
		}
	}

}
