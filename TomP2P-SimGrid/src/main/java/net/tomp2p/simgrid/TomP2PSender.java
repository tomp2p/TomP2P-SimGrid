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

import net.tomp2p.peers.Number160;

import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;

public class TomP2PSender extends Process
{
	@Override
	public void main(String[] args) throws MsgException
	{
		String host = getHost().getName();
		setName("Sender-"+host);
		Number160 peerID = Number160.createHash(host);
		while(true)
		{
			try
			{
				SendingMessage sendingMessage = SimGridTomP2P.getPendingMessag(peerID);
				if(sendingMessage!=null)
				{
					SimGridTomP2P.send(sendingMessage.getName(), sendingMessage.getMessage(), sendingMessage.getFutureResponse());
					Msg.info("Message sent "+ sendingMessage.getMessage());
				}
				else
				{
					//wait until someone notifies my when something is ready
					SimGridTomP2P.wait(peerID, this);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}	
		}
	}	
}