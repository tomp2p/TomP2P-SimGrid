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

import java.net.InetSocketAddress;

import net.tomp2p.futures.FutureResponse;
import net.tomp2p.message.Message;
import net.tomp2p.message.MessageID;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;
import org.simgrid.msg.Task;

public class TomP2PReceiver extends Process
{
	private volatile boolean running = true;
	
	public TomP2PReceiver(Host host, String name, String[]args)
	{
		super(host, "Receiver-"+host, args);
	}
	
	@Override
	public void main(String[] args) throws MsgException
	{
		String host = getHost().getName();
		Number160 peerID = Number160.createHash(host);
		DummyChannel channel = new DummyChannel(InetSocketAddress.createUnresolved("127.0.0.1", 4444), InetSocketAddress.createUnresolved("127.0.0.1", 5555));
		DummyChannelHandlerContext ctx = new DummyChannelHandlerContext(channel);
		while(running)
		{
			try
			{
				SimGridMessage message = (SimGridMessage)Task.receive(host);
				DummyMessageEvent e = new DummyMessageEvent(message.getMessage(), channel);
				message.getMessage().finished();
				Peer peer = SimGridTomP2P.getPeer(peerID);
				if(message.getMessage().isRequest())
				{
					peer.getConnectionBean().getDispatcherRequest().messageReceived(ctx, e);
					Message reply = channel.getMessage();
					SimGridTomP2P.addQueue(reply.getSender().getID(), new SendingMessage("rcv", reply, null));
					Msg.info("Message received and added to reply queue "+ message.getMessage());
				}
				else
				{
					//find the future and notify
					FutureResponse futureResponse = SimGridTomP2P.getAndRemoveFuture(new MessageID(message.getMessage()));
					if(futureResponse != null)
					{
						futureResponse.setResponse(message.getMessage());
					}
					Msg.info("Received reply "+ message.getMessage());
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
