package it.hackcaffebabe.netutil.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Class that describe the authentication feature.<br>
 *  
 * @author Andrea Ghizzoni. More info at andrea.ghz@gmail.com
 * @version 1.0
 */
public class AuthenticationFactory
{
	private String user, password;
	
	/**
	 * Instance a object authenticator.
	 * @param user {@link String} the user name.
	 * @param password {@link String} the password.
	 */
	public AuthenticationFactory( String user, String password ){
		this.user = user;
		this.password = password;
	}
	
	
	/**
	 * Returns the object that wraps the user name and password.
	 * @return {@link Authenticator} the object that wraps the user name and password.
	 */
	public Authenticator getAuthenticator(){
		return new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication( user, password );
			}
		};
	}
}
