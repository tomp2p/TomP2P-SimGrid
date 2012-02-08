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
import net.tomp2p.message.Message;

import org.simgrid.msg.NativeException;
import org.simgrid.msg.Task;

public class SimGridMessage extends Task
{
	private volatile Message message;
	public SimGridMessage(String name, double computeDuration, double messageSize) throws NativeException
	{
		super(name, computeDuration, messageSize);
	}
	public Message getMessage()
	{
		return message;
	}
	public void setMessage(Message message)
	{
		this.message = message;
	}
}