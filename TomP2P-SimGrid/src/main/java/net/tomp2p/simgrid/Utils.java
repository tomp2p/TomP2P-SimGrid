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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

import net.tomp2p.peers.Number160;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;

public class Utils
{
	private final static Options options = new Options();
	private static Random rnd;
	static
	{
		options.addOption("o", "old", true, "path name of the old platform.xml file");
		options.addOption("n", "new", true, "path name of the new (transformed) platform.xml file");
		options.addOption("s", "seed", true, "seed for the random generator");
		options.addOption("d", "deployment", true, "path name of the deployment.xml file");
		options.addOption("d1", "deployment1", true, "path name of the deployment.xml file");
		options.addOption("r", "nrpeers",true, "number of peers to create");
	}
	
	public static void main(String[] args) throws ParseException, IOException
	{
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse( options, args);
		boolean work = false;
		if(cmd.hasOption("s"))
		{
			try
			{
				rnd=new Random(Long.parseLong(cmd.getOptionValue("s")));
			}
			catch (NumberFormatException nfe)
			{
				printHelpAndExit("Could not parse -s ["+cmd.getOptionValue("s")+"]");
			}
		}
		else
		{
			rnd = new Random();
		}
		//this changes e.g. "Host 22" to "0x1234" which can be used for TomP2P 
		if(cmd.hasOption("n") && cmd.hasOption("o"))
		{
			work = true;
			File fileNameOld = new File(cmd.getOptionValue("o"));
			File fileNameNew = new File(cmd.getOptionValue("n"));
			String old = FileUtils.readFileToString(fileNameOld);
			for (int i=0;true;i++)
			{		
				String search = "\"Host "+i+"\"";
				if(old.indexOf(search)<0)
				{
					break;
				}
				Number160 number160 = new Number160(rnd);
				int index = 0;
				while(true)
				{
					int start = old.indexOf(search, index);
					if(start < 0)
					{
						break;
					}
					index = start + 1;
					old = old.substring(0, start)+"\""+number160.toString()+"\""+old.substring(start+search.length());
				}
				System.out.println(search);
			}
			FileUtils.write(fileNameNew, old);
		}
		
		if(cmd.hasOption("d") && cmd.hasOption("r"))
		{
			work = true;
			File fileNameDep = new File(cmd.getOptionValue("d"));
			String header="<?xml version='1.0'?>\n" +
					"<!DOCTYPE platform SYSTEM \"http://simgrid.gforge.inria.fr/simgrid.dtd\">\n" +
					"<platform version=\"3\">\n";
			String trailer ="</platform>";
			StringBuilder sb = new StringBuilder(header);
			int nr = 0;
			if(cmd.hasOption("r"))
			{
				try
				{
					nr=Integer.parseInt(cmd.getOptionValue("r"));
				}
				catch (NumberFormatException nfe)
				{
					printHelpAndExit("Could not parse -r ["+cmd.getOptionValue("r")+"]");
				}
			}
			Number160 first = null;
			for(int i=0;i<nr;i++)
			{
				Number160 number160 = new Number160(rnd);
				if(first == null)
				{
					first = number160;
				}
				sb.append(" <process host=\""+number160.toString()+"\" function=\"net.tomp2p.simgrid.SimulationLoop\">\n" +
						"  <argument value=\""+first+"\"/> <!-- boostrap to -->\n" +
						" </process>\n");
				sb.append(" <process host=\""+number160.toString()+"\" function=\"net.tomp2p.simgrid.TomP2PReceiver\"/>\n");
				sb.append(" <process host=\""+number160.toString()+"\" function=\"net.tomp2p.simgrid.TomP2PSender\"/>\n");
				sb.append(" <!-- next -->\n");
				
			}
			FileUtils.write(fileNameDep, sb.append(trailer).toString());
		}
		
		if(cmd.hasOption("d1") && cmd.hasOption("r"))
		{
			work = true;
			File fileNameDep = new File(cmd.getOptionValue("d1"));
			String header="<?xml version='1.0'?>\n" +
					"<!DOCTYPE platform SYSTEM \"http://simgrid.gforge.inria.fr/simgrid.dtd\">\n" +
					"<platform version=\"3\">\n";
			String trailer ="</platform>";
			StringBuilder sb = new StringBuilder(header);
			int nr = 0;
			if(cmd.hasOption("r"))
			{
				try
				{
					nr=Integer.parseInt(cmd.getOptionValue("r"));
				}
				catch (NumberFormatException nfe)
				{
					printHelpAndExit("Could not parse -r ["+cmd.getOptionValue("r")+"]");
				}
			}
			for(int i=0;i<nr;i++)
			{
				sb.append(" <process host=\""+i+"\" function=\"net.tomp2p.simgrid.SimulationLoop\">\n" +
						"  <argument value=\"0\"/> <!-- boostrap to -->\n" +
						" </process>\n");
				sb.append(" <process host=\""+i+"\" function=\"net.tomp2p.simgrid.TomP2PReceiver\"/>\n");
				sb.append(" <process host=\""+i+"\" function=\"net.tomp2p.simgrid.TomP2PSender\"/>\n");
				sb.append(" <!-- next -->\n");
			}
			FileUtils.write(fileNameDep, sb.append(trailer).toString());
		}
		
		if(!work)
		{
			printHelpAndExit("nothing to do");
		}
		
	}
	
	private static void printHelpAndExit(String msg)
	{
		System.out.println(msg);
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "Utils", options );
	}

	public static int countHosts(String string) throws IOException
	{
		File file = new File(string);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		HashSet<String> hosts = new HashSet<String>();
		while((line=br.readLine())!=null)
		{
			String markerStr = "host=\"";
			String markerEnd = "\"";
			int start = line.indexOf(markerStr);
			int end = line.indexOf(markerEnd, start+markerStr.length()+1);
			if(start > 0 & end > 0)
			{
				hosts.add(line.substring(start+markerStr.length(), end));
			}
		}
		br.close();
		return hosts.size();
	}
}
