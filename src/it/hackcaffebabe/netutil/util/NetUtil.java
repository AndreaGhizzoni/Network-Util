package it.hackcaffebabe.netutil.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * This class provide the common and useful functionality for working with network.<br>
 * 
 * @author Andrea Ghizzoni. More info at andrea.ghz@gmail.com
 *
 */
public class NetUtil
{
	/**
	 * This method check if is reachable an Internet Connection with a custom {@link URL} given..<br>
	 * @param urlToTest {@link URL} URL to test the if is reachable.
	 * @return {@link Boolean} true if URL is reachable, otherwise false.
	 */
	public static boolean isInternetReachable( URL urlToTest )
	{
		if( urlToTest == null ) return false;
		
		try
		{
			//Open a connection to that source.
		    //Trying to retrieve data from the source. 
		    //If there is no connection, this line will fail
		    ( (HttpURLConnection)urlToTest.openConnection() ).getContent();
	        return true;
		} 
		catch( UnknownHostException e ) { return false; }
		catch( IOException e ) { return false; }
	}
	
	
	/**
	 * This method check if is reachable an Internet Connection.<br>
	 * To do that it creates an HTTP connection with Google servers.
	 * @return {@link Boolean} true if it can create an HTTP connection with Google servers, otherwise false.
	 */
	public static boolean isInternetReachable()
	{
	    try 
	    {
	        //make a URL to a known source
		    URL url = new URL( "http://173.194.35.179/" );
		
		    //Open a connection to that source.
		    //Trying to retrieve data from the source. 
		    //If there is no connection, this line will fail
		    ( (HttpURLConnection)url.openConnection() ).getContent();
	        return true;
	    } 
	    catch( UnknownHostException e ) { return false; }
	    catch( IOException e ) { return false; }
	}
	
	
	/**
	 * This method returns a List of Network Interfaces on the computer.
	 * @return {@link List} of {@link NetworkInterface} on the computer.
	 * @throws SocketException {@link Exception} if is not possible to retries network interfaces.
	 */
	public static List<NetworkInterface> getInterfaces() throws SocketException
	{
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		return Collections.list( nets );
	}

	
	/**
	 * This method returns a List of names of Network Interfaces on the computer.
	 * @return {@link List} of name on the computer.
	 * @throws SocketException {@link Exception} if is not possible to retries network interfaces.  
	 */
	public static List<String> getInterfacesNames() throws SocketException
	{
		ArrayList<String> name = new ArrayList<>();
		for( NetworkInterface net : NetUtil.getInterfaces() )
			name.add( net.getDisplayName() );
		return name;
	}
	
	
	/**
	 * This method returns the MAC address of Network Interface.
	 * @param inte {@link NetworkInterface} to retrieve MAC address.
	 * @return {@link String} of formatted MAC address.
	 * @throws SocketException {@link Exception} if is not possible to retries MAC address as byte.
	 * @throws IllegalArgumentException {@link Exception} if argument is null.
	 */
	public static String getCustomMACFormat( NetworkInterface inte ) throws SocketException, IllegalArgumentException
	{
		if( inte == null )
			throw new IllegalArgumentException( "Network Interface can not be null." );
			
		byte[] mac = inte.getHardwareAddress();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++)
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));		
		return sb.toString();
	}
}
