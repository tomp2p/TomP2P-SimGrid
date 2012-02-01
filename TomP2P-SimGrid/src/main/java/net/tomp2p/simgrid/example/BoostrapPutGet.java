package net.tomp2p.simgrid.example;

import org.simgrid.msg.Msg;

import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.simgrid.Simulation;
import net.tomp2p.storage.Data;
import org.simgrid.msg.Process;

public class BoostrapPutGet implements Simulation
{

	@Override
	public void simulate(Peer peer, Process process, String[] args) throws Exception
	{
		Number160 bootstrapTo = new Number160(args[0]);
		PeerAddress bootstrapPeer = new PeerAddress(bootstrapTo);
		bootstrap(peer.getPeerID(), bootstrapTo, peer, bootstrapPeer);
		
		for(int i=0;i<10;i++)
		{
			Msg.info("host "+peer.getPeerID()+" round: "+i);
			if(peer.getPeerID().hashCode() % 5 == 0)
			{
				FutureDHT futureDHT = peer.put(Number160.createHash("storeme"), new Data(new String("I stored my host name "+peer.getPeerID())));
				futureDHT.addListener(new BaseFutureAdapter<FutureDHT>()
				{
					@Override
					public void operationComplete(FutureDHT future) throws Exception
					{
						System.out.println("put "+future.isSuccess());
					}
				});
			}
			else
			{
				FutureDHT futureDHT = peer.get(Number160.createHash("storeme"));
				futureDHT.addListener(new BaseFutureAdapter<FutureDHT>()
				{
					@Override
					public void operationComplete(FutureDHT future) throws Exception
					{
						System.out.println("get "+future.isSuccess());
					}
				});
			}
			Process.waitFor(1);
		}
		
	}
	private void bootstrap(Number160 peerID,Number160 bootstrapTo, Peer peer, PeerAddress bootstrapPeer)
	{
		if(!peerID.equals(bootstrapTo))
		{
			System.out.println("bootstrap from "+ peerID);
			peer.bootstrap(bootstrapPeer).addListener(new BaseFutureAdapter<FutureBootstrap>()
			{
				@Override
				public void operationComplete(FutureBootstrap future) throws Exception
				{
					System.err.println("success " + future.isSuccess());
				}
			});
		}
	}
}
