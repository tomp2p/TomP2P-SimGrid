package net.tomp2p.simgrid;

import net.tomp2p.futures.FutureResponse;
import net.tomp2p.message.Message;

public class SendingMessage
{
	final private String name;
	final private Message message;
	final private FutureResponse futureResponse;
	public SendingMessage(String name, Message message, FutureResponse futureResponse)
	{
		this.name = name;
		this.message = message;
		this.futureResponse = futureResponse;
	}
	public String getName()
	{
		return name;
	}
	public Message getMessage()
	{
		return message;
	}
	public FutureResponse getFutureResponse()
	{
		return futureResponse;
	}
	
}
