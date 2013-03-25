import javax.sound.midi.*;

public class MidiReceiver implements Receiver{

	private String name;
	private CmdController cont;
	
	public MidiReceiver(String name, CmdController cont){
		this.cont = cont;
		cont.println("[DONE]");
	}
	
	@Override
	public void send(MidiMessage msg, long timeStamp) {
		byte[] getMsg = msg.getMessage();
		if(getMsg[0] == -112 || getMsg[0] == -128){
			cont.println("Received: NOTE "+(getMsg[0]==-112?"ON":"OFF"));
			
			cont.println("\tNOTE: "+getMsg[1]+" ("+cont.getNoteName(getMsg[1])+")");
			cont.println("\tVELOCITY: "+getMsg[2]);
		}
	}

	@Override
	public void close() {
		cont.print("RECEIVER closing "+name+"... ");
		this.close();
		cont.println("[DONE]");
	}
}