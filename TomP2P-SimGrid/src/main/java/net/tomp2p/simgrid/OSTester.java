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

public class OSTester
{
	public static boolean isWindows() 
	{
		String name = System.getProperty("os.name").toLowerCase();
		// windows
		return (name.indexOf("win") >= 0);
	}
 
	public static boolean isMac() 
	{
		String name = System.getProperty("os.name").toLowerCase();
		// Mac
		return (name.indexOf("mac") >= 0);
	}
 
	public static boolean isUnix() 
	{
		String name = System.getProperty("os.name").toLowerCase();
		// linux or unix
		return (name.indexOf("nix") >= 0 || name.indexOf("nux") >= 0);
	}
 
	public static boolean isSolaris() 
	{
		String name = System.getProperty("os.name").toLowerCase();
		// Solaris
		return (name.indexOf("sunos") >= 0);
	}
	
	public static boolean is64bit()
	{
		String arch = System.getProperty("os.arch");
		// e.g. amd64
		return arch.indexOf("64")>=0;
	}
	
	public static boolean is32bit()
	{
		String arch = System.getProperty("os.arch");
		// 
		return arch.indexOf("32")>=0;
	}
 
}