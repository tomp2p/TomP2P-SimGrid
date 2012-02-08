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

import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

public class DummyChannelFuture implements ChannelFuture
{
	private boolean success = true;
	private boolean done = true;
	final private Channel channel;
	
	public DummyChannelFuture (Channel channel)
	{
		this.channel = channel;
	}
	
	@Override
	public void addListener(ChannelFutureListener listener)
	{
		try
		{
			listener.operationComplete(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public ChannelFuture await() throws InterruptedException
	{
		return null;
	}

	@Override
	public boolean await(long timeoutMillis) throws InterruptedException
	{
		return false;
	}

	@Override
	public boolean await(long timeout, TimeUnit unit) throws InterruptedException
	{
		return false;
	}

	@Override
	public ChannelFuture awaitUninterruptibly()
	{
		return null;
	}

	@Override
	public boolean awaitUninterruptibly(long timeoutMillis)
	{
		return false;
	}

	@Override
	public boolean awaitUninterruptibly(long timeout, TimeUnit unit)
	{
		return false;
	}

	@Override
	public boolean cancel()
	{
		return false;
	}

	@Override
	public Throwable getCause()
	{
		return null;
	}

	@Override
	public Channel getChannel()
	{
		return channel;
	}

	@Override
	public boolean isCancelled()
	{
		return false;
	}

	@Override
	public boolean isDone()
	{
		return done;
	}

	@Override
	public boolean isSuccess()
	{
		return success;
	}

	@Override
	public void removeListener(ChannelFutureListener listener)
	{
	
	}

	@Override
	public boolean setFailure(Throwable cause)
	{
		success = false;
		return true;
	}

	@Override
	public boolean setSuccess()
	{
		success = true;
		return true;
	}

	@Override
	public boolean setProgress(long arg0, long arg1, long arg2) {
		return false;
	}

	@Override
	public ChannelFuture rethrowIfFailed() throws Exception
	{
		return null;
	}
}
