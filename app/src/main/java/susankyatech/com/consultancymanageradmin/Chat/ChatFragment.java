package susankyatech.com.consultancymanageradmin.Chat;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import susankyatech.com.consultancymanageradmin.Activity.MainActivity;
import susankyatech.com.consultancymanageradmin.Application.App;
import susankyatech.com.consultancymanageradmin.Generic.FragmentKeys;
import susankyatech.com.consultancymanageradmin.Generic.Keys;
import susankyatech.com.consultancymanageradmin.Model.Data;
import susankyatech.com.consultancymanageradmin.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    @BindView(R.id.message_list)
    RecyclerView recyclerView;
    @BindView(R.id.input_message)
    EditText inputMessage;
    @BindView(R.id.send_message_btn)
    ImageView sendMessage;

    private MessageAdapter adapter;
    private List<Message> messagesList = new ArrayList<>();
    private int clientId = App.db().getInt(Keys.CLIENT_ID), studentId = App.db().getInt(Keys.USER_ID);
    private String clientName = "Demo";
    private Data data;

    private FirebaseFirestore db;
    private CollectionReference dbMessage;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Send Message");
        init();
        return view;
    }

    private void init() {
        db = FirebaseFirestore.getInstance();
        data = App.db().getObject(FragmentKeys.DATA, Data.class);
        moveScroll();
        sendMessage.setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View view) {
                final String messageText = inputMessage.getText().toString().trim();

                if (!TextUtils.isEmpty(messageText)) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("test","batman");
                    db.collection("chats")
                            .document(clientId + "-" + studentId)
                            .set(map);

                    Client client = new Client(clientId, clientName, "receiver");
                    Student student = new Student(studentId, data.name, "sender");
                    Message message = new Message(client, messageText, student);

                    dbMessage.add(message)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    inputMessage.setText("");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void moveScroll() {
        if (recyclerView.getAdapter() != null) {
            recyclerView
                    .addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v,
                                                   int left, int top, int right, int bottom,
                                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            if (bottom < oldBottom) {
                                recyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.smoothScrollToPosition(
                                                recyclerView.getAdapter().getItemCount() - 1);
                                    }
                                }, 100);
                            }
                        }
                    });
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        dbMessage = db.collection("chats")
                .document(clientId + "-" + studentId)
                .collection("messages");
        dbMessage.orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                messagesList = new ArrayList<>();

                if (value != null) {
                    for (QueryDocumentSnapshot document : value) {

                        Message m = new Message();

                        m = document.toObject(Message.class);
                        messagesList.add(m);

                        adapter = new MessageAdapter(messagesList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.scrollToPosition(messagesList.size() - 1);
                        recyclerView.setAdapter(adapter);

                        adapter.notifyDataSetChanged();


                    }
                }
            }
        });
    }
}
