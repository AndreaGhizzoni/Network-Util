package it.hackcaffebabe.netutil.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import com.sun.mail.smtp.SMTPTransport;


/**
 * <p>
 * Class that describe the mail primitive ( login and send ) to send a email.<br>
 * It's needs a properties file like this that describe the server information:
 * <pre>
 * #IMPLEMENT THIS KEYS FOR DIFFERENT CONNECTION SETTINGS
 * mail.smtp.starttls.enable=true
 * mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
 * #THE FOLLOW PROPERTIES ARE REQUIRED FOR ALL SERVER CONNECTION
 * mail.smtp.auth=true
 * mail.smtp.port=465
 * mail.smtp.host=smtp.gmail.com
 * </pre>
 * 
 * The last three properties are required.<br>
 * Now you can use this follow example to send a simple email without attachments.<br>
 * That assumes that the mail configuration file is into the home folder of the project.
 * <p><pre>
 * Mailer mailer = new Mailer( new File( "mailer.properties" ) );
 * Session s = mailer.login( "some.user@somemail.asd", "somePassword" );
 * SimpleMessage message = new SimpleMessage( s );
 * message.setSender( new Email( "some.user@somemail.asd" ) );
 * message.setRecipient( new Email( "some.user@somemail.asd" ) );
 * message.setSubject( "test mail" );
 * message.setBodyMessage( "TEST", null );
 * mailer.send( message );
 * </pre></p>
 * </p>

 * @author Andrea Ghizzoni. More info at andrea.ghz@gmail.com
 * @version 1.0
 *
 */
public class MailerManager
{
	/**Properties that represents the mail server configuration*/
	private Properties properties;
	/**The current session opened by the user*/
	private Session currentSession;
	/**The transport that send the email*/
	private SMTPTransport transport;

	/**
	 * Instance a Mailer object that provides utilities to login into mail server and to send the email.<br>
	 * the properties file MUST be like this:
	 * <pre>
	 * #IMPLEMENT THIS KEYS FOR DIFFERENT CONNECTION SETTINGS
	 * mail.smtp.starttls.enable=true
	 * mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
	 * #THE FOLLOW PROPERTIES ARE REQUIRED FOR ALL SERVER CONNECTION
	 * mail.smtp.auth=true
	 * mail.smtp.port=465
	 * mail.smtp.host=smtp.gmail.com
	 * </pre>
	 * 
	 * @param configurationFile {@link File} the configuration file that describe the mail server connection.
	 * @throws IOException if the configuration file are not a properties file.
	 * @throws IllegalArgumentException if configuration file is null.
	 */
	public MailerManager(File configurationFile) throws IllegalArgumentException, IOException{
		this.setProperties( configurationFile );
	}

//====================================================================================================//
// METHOD
//====================================================================================================//	
	/**
	 * This method create a new session with the personal account of mail server.
	 * @param user {@link String} user name to login with mail server.
	 * @param password {@link String} password to login with mail server.
	 * @return {@link Session} the session created with user name and password given.
	 * @throws MessagingException if login fail.
	 * @throws IllegalArgumentException if user or password are null or empty string.
	 */
	public Session login(String user, String password) throws IllegalArgumentException, MessagingException{
		if(user == null || user.isEmpty())
			throw new IllegalArgumentException( "User given can not be null or empty" );

		if(password == null || password.isEmpty())
			throw new IllegalArgumentException( "Password given can not be null or empty" );

		Session session = Session.getInstance( this.properties, new AuthenticationFactory( user, password ).getAuthenticator() );
		this.transport = (SMTPTransport) session.getTransport( "smtp" );
		this.transport.connect( this.properties.getProperty( "mail.smtp.host" ), user, password );

		this.currentSession = session;
		return this.currentSession;
	}

	/**
	 * This method sends the email at mail server configured.
	 * @param message {@link SimpleMessage} object represent the email to send.
	 * @throws IllegalArgumentException if {@link SimpleMessage} given is null.
	 * @throws MessagingException if you are not logged in or sending fail.
	 */
	public void send(SimpleMessage message) throws IllegalArgumentException, MessagingException{
		if(message == null)
			throw new IllegalArgumentException( "Message to send can not be null." );

		if(this.currentSession == null)
			throw new MessagingException( "You are not logged in. Use Mailer.login( String, String ) first." );

		Transport.send( message );
	}

	/**
	 * Close the current opened session.
	 * @throws MessagingException {@link Exception} if you are not logged in.
	 */
	public void logout() throws MessagingException{
		if(this.currentSession != null)
			throw new MessagingException( "You are not logged in. Use Mailer.login( String, String ) first." );

		this.transport.close();
	}

//====================================================================================================//
// SETTER
//====================================================================================================//	
	/* Sets the connection properties from file.*/
	private void setProperties(File configurationFile) throws IllegalArgumentException, IOException{
		if(!configurationFile.exists())
			throw new FileNotFoundException( "Mail configuration file not found." );

		this.properties = new Properties();
		this.properties.load( new FileInputStream( configurationFile.getAbsolutePath() ) );
	}
}
