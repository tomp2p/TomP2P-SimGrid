package net.tomp2p.simgrid;
import net.tomp2p.message.Message;

import org.simgrid.msg.NativeException;
import org.simgrid.msg.Task;

public class SimGridMessage extends Task
{
	private volatile Message message;
	public SimGridMessage(String name, double computeDuration, double messageSize) throws NativeException
	{
		super(name, computeDuration, messageSize);
	}
	public Message getMessage()
	{
		return message;
	}
	public void setMessage(Message message)
	{
		this.message = message;
	}
}
