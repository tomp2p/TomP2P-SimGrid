package net.tomp2p.simgrid;

import java.net.SocketAddress;

import net.tomp2p.message.Message;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.MessageEvent;

public class DummyMessageEvent implements MessageEvent
{
	private final Message message;
	private final Channel channel;

	public DummyMessageEvent(Message message, Channel channel)
	{
		this.message = message;
		this.channel = channel;
	}

	@Override
	public Object getMessage()
	{
		return message;
	}

	@Override
	public SocketAddress getRemoteAddress()
	{
		return null;
	}

	@Override
	public Channel getChannel()
	{
		return channel;
	}

	@Override
	public ChannelFuture getFuture()
	{
		return new DummyChannelFuture(getChannel());
	}
}
