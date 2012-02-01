package net.tomp2p.simgrid;

import java.util.Set;

import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

import org.reflections.Reflections;
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
		Reflections reflections = new Reflections("");
		Set<Class<? extends Simulation>> subTypes =  reflections.getSubTypesOf(Simulation.class);
		for(Class<? extends Simulation> clazz:subTypes)
		{
			try
			{
				Simulation simulation = clazz.newInstance();
				simulation.simulate(peer, (Process)this, args);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Msg.info("ERROR: "+e.toString());
			}
		}	
	}
}