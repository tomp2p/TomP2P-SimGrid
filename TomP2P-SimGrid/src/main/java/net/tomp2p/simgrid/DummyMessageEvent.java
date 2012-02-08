/*
 * Copyright 2012 Thomas Bocek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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
