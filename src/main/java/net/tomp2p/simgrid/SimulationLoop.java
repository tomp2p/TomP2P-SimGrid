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

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;

public class SimulationLoop extends Process
{
	public SimulationLoop(Host host, String name, String[]args)
	{
		super(host, "Loop-"+host, args);
	}
	
	@Override
	public void main(String[] args) throws MsgException
	{
		String host = getHost().getName();
		Number160 peerID = Number160.createHash(host);
		
		Peer peer = SimGridTomP2P.getPeer(peerID);
		Simulation simulation = SimGridTomP2P.getSimulation();
		if(simulation != null)
		{
			try
			{
				simulation.simulate(peer, (Process)this, args);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Msg.info("ERROR: "+e.getMessage());
			}
		}
		else
		{
			Msg.info("ERROR: no simulation specified");
		}
	}
}