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