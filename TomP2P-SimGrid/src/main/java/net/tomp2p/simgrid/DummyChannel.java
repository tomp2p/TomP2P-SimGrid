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
import org.jboss.netty.channel.ChannelConfig;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;

public class DummyChannel implements Channel
{
	private final SocketAddress remoteAddress;
	private final SocketAddress localAddress;
	private Message message;

	public DummyChannel(SocketAddress remoteAddress, SocketAddress localAddress)
	{
		this.remoteAddress = remoteAddress;
		this.localAddress = localAddress;
	}
	@Override
	public ChannelFuture bind(SocketAddress localAddress)
	{
		return null;
	}

	@Override
	public ChannelFuture close()
	{
		return null;
	}

	@Override
	public ChannelFuture connect(SocketAddress remoteAddress)
	{
		return null;
	}

	@Override
	public ChannelFuture disconnect()
	{
		return null;
	}

	@Override
	public ChannelFuture getCloseFuture()
	{
		return null;
	}

	@Override
	public ChannelConfig getConfig()
	{
		return null;
	}

	@Override
	public ChannelFactory getFactory()
	{
		return null;
	}

	@Override
	public Integer getId()
	{
		return null;
	}

	@Override
	public int getInterestOps()
	{
		return 0;
	}

	@Override
	public SocketAddress getLocalAddress()
	{
		return localAddress;
	}

	@Override
	public Channel getParent()
	{
		return null;
	}

	@Override
	public ChannelPipeline getPipeline()
	{
		return null;
	}

	@Override
	public SocketAddress getRemoteAddress()
	{
		return remoteAddress;
	}

	@Override
	public boolean isBound()
	{
		return true;
	}

	@Override
	public boolean isConnected()
	{
		return true;
	}

	@Override
	public boolean isOpen()
	{
		return false;
	}

	@Override
	public boolean isReadable()
	{
		return false;
	}

	@Override
	public boolean isWritable()
	{
		return false;
	}

	@Override
	public ChannelFuture setInterestOps(int interestOps)
	{
		return null;
	}

	@Override
	public ChannelFuture setReadable(boolean readable)
	{
		return null;
	}

	@Override
	public ChannelFuture unbind()
	{
		return null;
	}

	@Override
	public ChannelFuture write(Object message)
	{
		this.message = (Message)message;
		return new DummyChannelFuture(this);
	}

	@Override
	public ChannelFuture write(Object message, SocketAddress remoteAddress)
	{
		this.message = (Message)message;
		return new DummyChannelFuture(this);
	}

	@Override
	public int compareTo(Channel o)
	{
		return 0;
	}
	public Message getMessage()
	{
		return message;
	}
}
