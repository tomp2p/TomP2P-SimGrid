package net.tomp2p.simgrid;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import net.tomp2p.connection.ChannelCreator;
import net.tomp2p.connection.Reservation;
import net.tomp2p.connection.Scheduler;
import net.tomp2p.connection.Sender;
import net.tomp2p.futures.BaseFuture;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.futures.FutureRunnable;
import net.tomp2p.message.Message;
import net.tomp2p.message.MessageID;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.RequestHandlerTCP;
import net.tomp2p.rpc.RequestHandlerUDP;
import net.tomp2p.utils.Timing;
import net.tomp2p.utils.Timings;

import org.simgrid.msg.HostFailureException;
import org.simgrid.msg.Msg;
import org.simgrid.msg.NativeException;
import org.simgrid.msg.Process;
import org.simgrid.msg.TimeoutException;
import org.simgrid.msg.TransferFailureException;

public class SimGridTomP2P
{
	//static data, we create here our peers to be able to get them on demand
	private final static Map<Number160, Peer> peers = new HashMap<Number160, Peer>();
	private final static Map<MessageID, FutureResponse> futures = new HashMap<MessageID, FutureResponse>();
	private final static Map<Number160, BlockingQueue<SendingMessage>> pendingMessages = new HashMap<Number160, BlockingQueue<SendingMessage>>();
	private final static Map<Number160, Process> paused = new ConcurrentHashMap<Number160, Process>();
	private final static long seed = 42;
	private final static int nr = 100;
	private final static Random rnd = new Random(seed);
	// create the peers
	static
	{
		Timings.setImpl(new Timing()
		{
			@Override
			public void sleepUninterruptibly(int millis)
			{
				try
				{
					Process.waitFor(millis/1000d);
				}
				catch (HostFailureException e)
				{
					e.printStackTrace();
				}
			}
			
			@Override
			public void sleep(int millis) throws InterruptedException
			{
				try
				{
					Process.waitFor(millis/1000d);
				}
				catch (HostFailureException e)
				{
					e.printStackTrace();
				}
			}
			
			@Override
			public long currentTimeMillis()
			{
				return (long)(Msg.getClock()*1000);
			}
		});
		for(int i=0;i<nr;i++)
		{
			Number160 peerID = new Number160(rnd);
			Peer peer = new Peer(peerID);
			
			peer.getP2PConfiguration().setDisableBind(true);
			peer.getP2PConfiguration().setStartMaintenance(false);
			
			try
			{
				peer.listen();
				peer.getConnectionBean().getConnectionReservation().setReservation(new Reservation()
				{
					private volatile boolean shutdown = false;
					
					@Override
					public void shutdown()
					{
						shutdown = true;
					}
					
					@Override
					public boolean acquire(Semaphore semaphore, int permits)
					{
						boolean acquired = false;
						while (!acquired && !shutdown)
						{
							try
							{
								acquired = semaphore.tryAcquire(permits);
								if (!acquired)
								{
									synchronized (semaphore)
									{
										Process.waitFor(Double.MIN_VALUE);
									}
								}
							}
							catch (HostFailureException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						return acquired;
					}

					@Override
					public void runDeadLockProof(Scheduler scheduler, FutureRunnable futureRunnable)
					{
						futureRunnable.run();
					}

					@Override
					public void prepareDeadLockCheck()
					{
						//nothing to do
					}

					@Override
					public void removeDeadLockCheck(long creatorThread)
					{
						//nothing to do
					}
				});
				emulateSender(peer);
				peers.put(peerID, peer);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			Msg.info("created peer with peerID "+peerID);	
		}
		Msg.info("peers created");
	}
	
	/**
	 * Returns the static peers created here. We need to get them in a static
	 * context since the SimGrid threads may die.
	 * 
	 * @param peerID The peerID
	 * @return The peer with the peerID
	 */
	public static Peer getPeer(Number160 peerID)
	{
		return peers.get(peerID);
	}
	
	/**
	 * Sometimes we need to create a PeerAddress.
	 * 
	 * @param peerID The peerID
	 * @return The peerAddress
	 */
	public static PeerAddress createPeer(String peerID)
	{
		return new PeerAddress(new Number160(peerID));
	}
	
	/**
	 * SimGrid style of sending a message
	 * 
	 * @param type The message type
	 * @param message The TomP2P message
	 * @param futureResponse 
	 * @throws NativeException 
	 * @throws TimeoutException 
	 * @throws HostFailureException 
	 * @throws TransferFailureException 
	 */
	public static void send(String type, Message message, FutureResponse futureResponse) throws NativeException, TransferFailureException, HostFailureException, TimeoutException
	{
		//we don't care about the computation
		SimGridMessage msgSG = new SimGridMessage("msg-"+type, 0, message.getLong());
		msgSG.setMessage(message);
		Msg.info("send ["+type+"] "+message);
		if(futureResponse != null)
		{
			futures.put(new MessageID(message), futureResponse);
		}
		msgSG.send(message.getRecipient().getID().toString());
	}
	
	public static FutureResponse getAndRemoveFuture(MessageID messageID)
	{
		return futures.remove(messageID);
	}
	
	public static void addQueue(Number160 senderID, SendingMessage sendingMessage)
	{
		BlockingQueue<SendingMessage> queue = pendingMessages.get(senderID);
		if(queue == null)
		{
			queue = new LinkedBlockingQueue<SendingMessage>();
			pendingMessages.put(senderID, queue);
		}
		queue.offer(sendingMessage);
		notify(senderID);
	}
	
	public static SendingMessage getPendingMessag(Number160 senderID) throws InterruptedException
	{
		BlockingQueue<SendingMessage> queue = pendingMessages.get(senderID);
		if(queue == null)
		{
			queue = new LinkedBlockingQueue<SendingMessage>();
			pendingMessages.put(senderID, queue);
		}
		return queue.poll();
	}
	
	private static void emulateSender(Peer peer)
	{
		peer.getConnectionBean().setSender(new Sender()
		{
			@Override
			public void sendUDP(RequestHandlerUDP handler, FutureResponse futureResponse, Message message,
					ChannelCreator channelCreator)
			{
				addQueue(message.getSender().getID(), new SendingMessage("snd-udp", message, futureResponse));
			}
			
			@Override
			public void sendTCP(RequestHandlerTCP<? extends BaseFuture> handler,
					FutureResponse futureResponse, Message message, ChannelCreator channelCreator,
					int idleTCPMillis)
			{
				addQueue(message.getSender().getID(), new SendingMessage("snd-tcp", message, futureResponse));
			}
			
			@Override
			public void sendBroadcastUDP(RequestHandlerUDP handler, FutureResponse futureResponse,
					Message message, ChannelCreator channelCreator)
			{
				throw new RuntimeException("broadcasting in SimGrid-TomP2P not support");
			}

			@Override
			public void shutdown()
			{
							
			}
		});
	}
	
	public static void wait(Number160 host, Process process)
	{
		if(!process.isSuspended())
		{
			Process old=paused.put(host, process);
			if(old!=null && !old.equals(process))
			{
				throw new RuntimeException("This needs to be the same process");
			}
			process.pause();
		}
	}
	
	public static void notify(Number160 host)
	{
		Process p = paused.remove(host);
		if (p!=null)
			p.restart();
	}
	
	/**
	 * Initialize the simulation.
	 * 
	 * @param args Two arguments are required: the platform file and the deployment file
	 */
	public static void main(String[] args)
	{
		/* initialize the MSG simulation. Must be done before anything else (even logging). */
		Msg.init(args);
		if(args.length < 2) 
		{
    		Msg.info("Usage: Test platform_file deployment_file");
        	System.exit(1);
    	}
	
		// construct the platform and deploy the application
		Msg.createEnvironment(args[0]);
		Msg.deployApplication(args[1]);
		
		//  execute the simulation
        Msg.run();
        Msg.info("EXIT.");
	}	
}