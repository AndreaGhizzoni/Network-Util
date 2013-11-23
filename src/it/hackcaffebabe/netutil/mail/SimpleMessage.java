package it.hackcaffebabe.netutil.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * <p>
 * Simple class that represents the message that will be send as email.<br>
 * Use the follow example to create a simple email.<br>
 * After that use {@link MailerManager} class to send it:
 * <pre>
 * SimpleMessage m = new SimpleMessage( openedSession );
 * m.setSender( new Email( "sender.email@domain.asd" ) );
 * m.setRecipient( new Email( "recipient.email@domain.asd" ) );
 * m.setBodyMessage( "Hello this is a text", Arrays.asList( new File( "myfile.txt" )) );
 * </pre>
 * Remember to use null attachment list to send email without attachments.
 * </p>
 * 
 * @author Andrea Ghizzoni. More info at andrea.ghz@gmail.com
 * @version 1.0
 */
public class SimpleMessage extends MimeMessage
{
	/**This describe all the part of email ( from, recipients, content, attachments )*/
	private Multipart multipart = new MimeMultipart();

	/**
	 * Instance a SimpleMessage with current opened {@link Session}
	 * @param session {@link Session} current opened session.
	 */
	public SimpleMessage(Session session){
		super( session );
	}

//====================================================================================================//
// METHOD
//====================================================================================================//	
	/**
	 * This method returns a list of {@link NetUser} from a string given. <br>
	 * That string must be in format <email>; <email>; ecc..<br>
	 * NB: If there is only one email, the format must be <email>;
	 * @param emails {@link String} formatted like <email>; <email>; <email>; ... 
	 * @return {@link ArrayList} of {@link NetUser}
	 * @throws IllegalArgumentException if input string is not in format or null;
	 */
	public static ArrayList<NetUser> parseEmails(String emails) throws IllegalArgumentException{
		StringTokenizer token = new StringTokenizer( emails, ";" );
		if(token.countTokens() == 1) {
			StringTokenizer token2 = new StringTokenizer( emails, " " );
			if(token2.countTokens() > 1)
				throw new IllegalArgumentException( "String to parse is not in format <email>; <email>; <email>; .." );
		}

		ArrayList<NetUser> emailsList = new ArrayList<>();
		while( token.hasMoreTokens() )
			emailsList.add( new NetUser( token.nextToken().trim() ) );

		return emailsList;
	}

//====================================================================================================//
// SETTER
//====================================================================================================//	
	/**
	 * Sets the email sender.
	 * @param sender {@link NetUser} email.
	 * @throws IllegalArgumentException if sender email given is null.
	 * @throws MessagingException if there are problem with adding sender email.
	 * @throws AddressException if there are problem with adding sender email.  
	 */
	public void setSender(NetUser sender) throws IllegalArgumentException, AddressException, MessagingException{
		if(sender == null)
			throw new IllegalArgumentException( "Sender email can not be null" );

		setFrom( new InternetAddress( sender.getEmail() ) );
	}

	/**
	 * Sets new recipient of email.
	 * @param recipient {@link NetUser} of email.
	 * @throws IllegalArgumentException if recipient email given is null.
	 * @throws MessagingException if there are problem with adding recipient email.
	 * @throws AddressException if there are problem with adding recipient email. 
	 */
	public void setRecipient(NetUser recipient) throws IllegalArgumentException, AddressException, MessagingException{
		if(recipient == null)
			throw new IllegalArgumentException( "Recipient email can not be null" );

		addRecipient( Message.RecipientType.TO, new InternetAddress( recipient.getEmail() ) );
	}

	/**
	 * Sets a list of recipients email.
	 * @param listOfRecipients {@link List} the recipients list.
	 * @throws MessagingException if there are problem with adding recipient email.
	 * @throws AddressException if there are problem with adding recipient email.
	 */
	public void setRecipients(List<NetUser> listOfRecipients) throws IllegalArgumentException, MessagingException{
		if(listOfRecipients == null || listOfRecipients.isEmpty())
			throw new IllegalArgumentException( "List of recipients can not be null or empty." );

		InternetAddress[] emails = new InternetAddress[listOfRecipients.size()];
		for(int i = 0; i < listOfRecipients.size(); i++)
			emails[i] = new InternetAddress( listOfRecipients.get( i ).getEmail() );

		addRecipients( Message.RecipientType.TO, emails );
	}

	/**
	 * Sets the body of email.
	 * @param text {@link String} the text body of email.
	 * @param attachments {@link List} list file as attachments. 
	 * 									Set this arguments at null, or empty list to doesn't set attachment on emil.
	 * @throws IllegalArgumentException if text given is null.
	 * @throws MessagingException if there are problem with adding recipient email.
	 */
	public void setBodyMessage(String text, List<File> attachments) throws IllegalArgumentException, MessagingException{
		if(text == null)
			throw new IllegalArgumentException( "Text Content of email can not be null." );

		BodyPart textContent = new MimeBodyPart();
		textContent.setText( text );
		this.multipart.addBodyPart( textContent );

		if(attachments != null) {
			for(File f: attachments) {
				BodyPart attachmentContent = new MimeBodyPart();
				DataSource source = new FileDataSource( f.getAbsolutePath() );
				attachmentContent.setDataHandler( new DataHandler( source ) );
				attachmentContent.setFileName( f.getName() );
				this.multipart.addBodyPart( attachmentContent );
			}
		}

		setContent( this.multipart );
	}
}
