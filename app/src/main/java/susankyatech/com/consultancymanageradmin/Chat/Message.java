package susankyatech.com.consultancymanageradmin.Chat;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {
    public Client client;
    public String message;
    public Student student;

    @ServerTimestamp
    public Date timestamp;
    public Message(Client client, String message, Student student) {
        this.client = client;
        this.message = message;
        this.student = student;
    }

    public Message()
    {

    }
}
