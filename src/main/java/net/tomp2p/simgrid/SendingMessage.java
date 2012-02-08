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

import net.tomp2p.futures.FutureResponse;
import net.tomp2p.message.Message;

public class SendingMessage
{
	final private String name;
	final private Message message;
	final private FutureResponse futureResponse;
	public SendingMessage(String name, Message message, FutureResponse futureResponse)
	{
		this.name = name;
		this.message = message;
		this.futureResponse = futureResponse;
	}
	public String getName()
	{
		return name;
	}
	public Message getMessage()
	{
		return message;
	}
	public FutureResponse getFutureResponse()
	{
		return futureResponse;
	}
}