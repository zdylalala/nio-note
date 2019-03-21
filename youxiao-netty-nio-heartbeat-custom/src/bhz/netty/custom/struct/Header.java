package bhz.netty.custom.struct;

public class Header {

	private int crcCode = 0xadaf0105; //唯一的通信标志 
	
	private int length; //总消息的长度  header + body 
	
	private long sessionID; 	//会话ID
	
	private byte type ;		//消息的类型
	
	private byte priority;	//消息的优先级	

	//...
	
	public int getCrcCode() {
		return crcCode;
	}

	public void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public long getSessionID() {
		return sessionID;
	}

	public void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	
	
	
	
}
