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
		Number160 peerID = new Number160(host);
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