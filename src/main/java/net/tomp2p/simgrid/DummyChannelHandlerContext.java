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
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.DownstreamMessageEvent;

public class DummyChannelHandlerContext implements ChannelHandlerContext
{
	private Object protocolChunkedInput;
	final private Channel channel;

	public DummyChannelHandlerContext(Channel channel)
	{
		this.channel = channel;
	}
	@Override
	public boolean canHandleDownstream()
	{
		return false;
	}

	@Override
	public boolean canHandleUpstream()
	{
		return false;
	}

	@Override
	public Object getAttachment()
	{
		return null;
	}

	@Override
	public Channel getChannel()
	{
		return channel;
	}

	@Override
	public ChannelHandler getHandler()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public ChannelPipeline getPipeline()
	{
		return null;
	}

	@Override
	public void sendDownstream(ChannelEvent e)
	{
		DownstreamMessageEvent dme = (DownstreamMessageEvent) e;
		this.protocolChunkedInput =  dme.getMessage();
	}

	public Object getInput()
	{
		return protocolChunkedInput;
	}

	@Override
	public void sendUpstream(ChannelEvent e)
	{
		
	}

	@Override
	public void setAttachment(Object attachment)
	{
		
	}
}
