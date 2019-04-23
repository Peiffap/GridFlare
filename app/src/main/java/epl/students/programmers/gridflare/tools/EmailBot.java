package epl.students.programmers.gridflare.tools;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.security.AccessController;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Handler;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import epl.students.programmers.gridflare.ORM.DatabaseManager;

public class EmailBot extends javax.mail.Authenticator {
    private String host = "smtp.gmail.com";
    private String username = "gridflare.app@gmail.com";//Je sais pas si le @ est utile
    private String pwd = "grd2018-2019flr";

    private Session currentSession;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public EmailBot(){
        Properties p = new Properties();
        p.setProperty("mail.transport.protocol", "smtp");
        p.setProperty("mail.host", host);
        p.put("mail.smtp.port", "465");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.socketFactory.fallback", "false");
        p.setProperty("mail.smtp.quitwait", "false");
        currentSession = Session.getInstance(p, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, pwd);
    }

    /**
     * This should be used in a separated thread
     * @param topic Topic of the email
     * @param message Content of the email
     * @param to Receiver address (1 max)
     * @return
     */
    public synchronized boolean send(String topic, String message, String to){
        try{
            MimeMessage email = new MimeMessage(currentSession);//We use MIME for the futur if we want to add more ressources to the email
            email.setSender(new InternetAddress(username));
            email.setRecipient(Message.RecipientType.BCC, new InternetAddress(to));
            email.setSubject(topic);
            DataHandler dh = new DataHandler(new ByteArrayDataSource(message.getBytes(), "text/html"));
            email.setDataHandler(dh);
            Transport.send(email);//We can get an error here
            return true;
        } catch (Exception e) {
            Log.e("EmailBot","Error while sending the email : " + e.getMessage());
            return false;
        }
    }

    //Have to be called from a thread
    public synchronized void sendScanReport(String to, String placeName, Context context){
        String message =  String.format("<h1 style=\"text-align: center;\">Wifi scan report</h1>\n" +
                                        "<p>Report of the wifi state of %s :</p>" +
                                        "<ul>", placeName);
        DatabaseManager db = new DatabaseManager(context);
        ArrayList<Room> rooms = db.readRoomFromPlace(placeName);
        for(Room room: rooms){
            Scan_information s = db.readLastScan(room.room_name);//Pas sur du room name
            if(s != null) {
                message += String.format(Locale.getDefault(), "<li>%s\n" +
                        "<ol>\n" +
                        "<li>Strength : %d</li>\n" +
                        "<li>Ping : %f</li>\n" +
                        "<li>Proportion of lost : %f</li>\n" +
                        "<li>Upload rate : %f Mb/s</li>\n" +
                        "</ol>\n" +
                        "</li>", room.getRoom_name(), s.getStrength(), s.getPing(), s.getProportionOfLost(), 10 / s.getDl());
            } else {
                message += String.format(Locale.getDefault(), "<li>%s\n No scan for this room </li>", room.getRoom_name());
                Log.e("Email Bot", "Probleme solved");
            }
        }
        message += "</ul>\n" +
                "<p>This report has been generated by <span style=\"text-decoration: underline;\">GridFlare</span> (lien playstore a venir)</p>";
        if(send(String.format("Wifi scan report of %s", placeName), message, to))
            Log.i("Email bot", "Email sended");
        else
            Log.i("Email bot", "Email not sended");
    }

}

/**
 * @author Alexander Y. Kleymenov
 * @version $Revision$
 */
final class JSSEProvider extends Provider {

    public JSSEProvider() {
        super("HarmonyJSSE", 1.0, "Harmony JSSE Provider");
        AccessController.doPrivileged(new java.security.PrivilegedAction<Void>() {
            public Void run() {
                put("SSLContext.TLS", "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");
                put("Alg.Alias.SSLContext.TLSv1", "TLS");
                put("KeyManagerFactory.X509", "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");
                put("TrustManagerFactory.X509", "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");
                return null;
            }
        });
    }
}
