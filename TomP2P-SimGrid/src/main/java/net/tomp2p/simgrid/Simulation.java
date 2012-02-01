package net.tomp2p.simgrid;

import net.tomp2p.p2p.Peer;
import org.simgrid.msg.Process;

public interface Simulation
{
	public abstract void simulate(Peer peer, Process process, String[] args) throws Exception;
}
