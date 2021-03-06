package it.hackcaffebabe.netutil.mail;

import java.io.Serializable;
import java.util.StringTokenizer;


/**
 * <p>
 * Simple class that describe and validate an Email address.<br>
 * To instance an object of this class you could use the appropriate constructor or the setter method.
 * <pre>
 * NetUser e = new NetUser();
 * e.setEmail( "some.email@somedomanin.asd" );
 * </pre>
 * </p>
 * 
 * @author Andrea Ghizzoni. More info at andrea.ghz@gmail.com
 * @version 1.0
 */
public class NetUser implements Serializable, Comparable<NetUser>
{
	private static final long serialVersionUID = -5042718788066202845L;
	private String email;
	private String user;
	private String domain;

	/**
	 * Instance a void email with no argument.<br>
	 * Use {@code setEmail( "someEmail@someDomani.abc")} to set and validate the email. 
	 */
	public NetUser(){}

	/**
	 * Instance a Email from a String in format uuuu.uuuu@dddd.dd where 'u' is user and 'd' is domain.
	 * @param email {@link String} representing email.
	 * @throws IllegalArgumentException {@link Exception} if email given is null or not in the correct format.
	 */
	public NetUser(String email) throws IllegalArgumentException{
		this.setAddress( email );
	}

//====================================================================================================//
// GETTER
//====================================================================================================//	
	/**
	 * Return the Email in format uuuu.uuuu@dddd.dd.
	 * @return {@link String} represent the Email.
	 */
	public String getEmail(){
		return this.email;
	}

	/**
	 * Return the domain part of EMail.
	 * @return {@link String} represent the domain part of EMail.
	 */
	public String getDomain(){
		return this.domain;
	}

	/**
	 * Return the user part of EMail.
	 * @return {@link String} represent the user part of EMail.
	 */
	public String getUser(){
		return user;
	}

	/* this method set user and domain class attributes */
	private void getMoreInformation(String email){
		StringTokenizer parse = new StringTokenizer( email, "@" );
		this.user = parse.nextToken();
		this.domain = parse.nextToken();
	}

//====================================================================================================//
// SETTER
//====================================================================================================//	
	/**
	 * Set new Email in format uuuu.uuuu@dddd.dd.
	 * @param email {@link String} represent new Email.
	 * @throws IllegalArgumentException if argument is null, empty or invalid email.
	 */
	public void setAddress(String email) throws IllegalArgumentException{
		if(email == null || email.isEmpty())
			throw new IllegalArgumentException( "Email given can not be empty or null." );

		if(!email.matches( "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$" ))
			throw new IllegalArgumentException( "Email given is invalid." );

		this.email = email;
		this.getMoreInformation( email );
	}

//====================================================================================================//
// OVERRIDE
//====================================================================================================//	
	@Override
	public int compareTo(NetUser o){
		return this.email.compareTo( o.email );
	}

	@Override
	public String toString(){
		return this.email;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		NetUser other = (NetUser) obj;
		if(email == null) {
			if(other.email != null)
				return false;
		} else if(!email.equals( other.email ))
			return false;
		return true;
	}
}
