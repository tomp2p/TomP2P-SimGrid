package net.tomp2p.simgrid;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;

public class SimulationLoop extends Process
{
	@Override
	public void main(String[] args) throws MsgException
	{
		String host = getHost().getName();
		setName("Loop-"+host);
		Number160 peerID = new Number160(host);
		
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