import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;


public class MidiTransmitter implements Transmitter{

	private String name;
	private Receiver receiver;
	private CmdController cont;
	
	public MidiTransmitter(String name, CmdController cont){
		this.cont = cont;
		this.name = name;
	}
	
	@Override
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
		cont.println("[DONE]");
	}

	@Override
	public Receiver getReceiver() {
		return receiver;
	}

	@Override
	public void close() {
		cont.print("TRNSMITTER closing "+name+"... ");
		this.close();
		cont.println("[DONE]");
	}

	
	public void send(MidiMessage message, long timeStamp){
		receiver.send(message, timeStamp);
	}
	
	public void send(byte[] array, long timeStamp){
		receiver.send(new MidiMessage(array) {
			@Override
			public Object clone() {
				// TODO Auto-generated method stub
				return null;
			}
		}, timeStamp);
	}
}