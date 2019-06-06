package utlidades;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author root
 */
public class Mail {
    
    /*variables requeridas para enviar mails*/
    private String mailTransmitter;
    private String password;
    private Address[] mailReceiver;    
    private String subject;
    private String message;
    
    private File file;
    /*controlador de la configuración para el envio de correos*/
    private final Properties properties;
    private final Session session;
    
    /*metodo constructor*/    
    public Mail(String mailTransmitter, String password, Address[] mailReceiver, String subject, String message, File file) {
        
        this.mailTransmitter = mailTransmitter;
        this.password = password;
        this.mailReceiver = mailReceiver;
        this.subject = subject;
        this.message = message;
        this.file=file;

        /*inicializamos el objeto tipo properties y configuramos 
        para el envio de mail */

        properties = new Properties();

        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.user", this.mailTransmitter);
        properties.setProperty("mail.smtp.auth", "true");

        session = Session.getDefaultInstance(properties);
        session.setDebug(true);

    }

    public String getMailTransmitter() {
        return mailTransmitter;
    }

    public void setMailTransmitter(String mailTransmitter) {
        this.mailTransmitter = mailTransmitter;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Address[] getMailReceiver() {
        return mailReceiver;
    }

    public void setMailReceiver(Address[] mailReceiver) {
        this.mailReceiver = mailReceiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    
    
    
    public boolean sendEmail(){
        
        boolean result=false;
        
        try{

            MimeMessage mimeMessage = new MimeMessage(this.session);
            
            mimeMessage.setFrom(new InternetAddress(this.mailTransmitter));
            mimeMessage.addRecipients(Message.RecipientType.TO, this.mailReceiver);
            mimeMessage.setSubject(this.subject);
            mimeMessage.setText(this.message, "ISO-8859-1", "html");
            
            /*pasos para enviar mail*/
            
            Transport transport = this.session.getTransport("smtp");
            transport.connect(this.mailTransmitter, this.password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
            
            result = true;
            
        }catch(MessagingException ex){
            System.out.println(ex.getMessage());
        }

        return result;
        
    }

    public boolean sendEmailWithFile(){

        boolean result=false;

        try{

            BodyPart partText = new MimeBodyPart();
            partText.setContent(this.message, "text/html");

            BodyPart partFile = new MimeBodyPart();
            partFile.setDataHandler(new DataHandler(new FileDataSource(file)));
            partFile.setFileName(this.file.getName());
            
            MimeMultipart multiParte = new MimeMultipart();

            multiParte.addBodyPart(partText);
            multiParte.addBodyPart(partFile);           
            
            MimeMessage mimeMessage = new MimeMessage(this.session);

            mimeMessage.setFrom(new InternetAddress(this.mailTransmitter));
            mimeMessage.addRecipients(Message.RecipientType.TO, this.mailReceiver);
            mimeMessage.setSubject(this.subject);
            mimeMessage.setContent(multiParte);

            /*pasos para enviar mail*/

            Transport transport = this.session.getTransport("smtp");
            transport.connect(this.mailTransmitter, this.password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();

            result = true;

        }catch(MessagingException ex){
            System.out.println(ex.getMessage());
        }

        return result;

    }

    
}
