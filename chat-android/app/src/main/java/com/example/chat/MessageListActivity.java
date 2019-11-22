package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chat.model.Message;
import com.example.chat.model.Sender;
import com.example.chat.service.impl.ChatServiceImpl;
import com.example.chat.service.interfaces.ChatService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Piotr Heilman
 */
public class MessageListActivity extends AppCompatActivity {
    public static final String LOGGED_USERNAME = "LOGGED_USERNAME";
    public static final String CHANNEL_NAME = "CHANNEL_NAME";

    private RecyclerView messageRecyclerView;
    private MessageListAdapter messageListAdapter;
    private EditText newMessageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        final List<Message> messageList = new ArrayList<>();

        final String loggedUsername = getIntent().getStringExtra(LOGGED_USERNAME);
        final String channelName = getIntent().getStringExtra(CHANNEL_NAME);

        setTitle(channelName);

        final ChatService chatService = new ChatServiceImpl(channelName, loggedUsername);

        messageRecyclerView = findViewById(R.id.reyclerview_message_list);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageListAdapter = new MessageListAdapter(this, loggedUsername, messageList);
        messageRecyclerView.setAdapter(messageListAdapter);

        newMessageEditText = findViewById(R.id.message_list_new_message);

        final Button sendButton = findViewById(R.id.message_list_send);

        final ChatService.NewMessageCallback newMessageCallback = message -> {
            final Message m = convertTextMessage(message);
            runOnUiThread(() -> {
                messageListAdapter.addMessage(m);
                messageRecyclerView.scrollToPosition(messageListAdapter.getItemCount() - 1);
            });
        };

        final ConnectChatServiceAsyncTask connectChatServiceAsyncTask =
                new ConnectChatServiceAsyncTask(chatService, newMessageCallback);

        connectChatServiceAsyncTask.execute();
        try {
            final Exception exception = connectChatServiceAsyncTask.get();
            if (exception != null) {
                throw exception;
            }
        } catch (Exception e) {
            Log.e("ChatApplication", "Error receiving message", e);
            Toast.makeText(
                    getApplicationContext(),
                    "Error receiving message",
                    Toast.LENGTH_SHORT
            ).show();
            finish();
        }

        sendButton.setOnClickListener(v -> {
            try {
                String message = getMessageAndClearInput();
                chatService.sendMessage(message);
            } catch (Exception e) {
                Log.e("ChatApplication", "Error sending message", e);
                Toast.makeText(
                        getApplicationContext(),
                        "Error sending message",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private String getMessageAndClearInput() {
        final Editable text = newMessageEditText.getText();
        final String message = text.toString();
        text.clear();

        return message;
    }

    private Message convertTextMessage(final String message) throws Exception {
        final JSONObject jsonObject = new JSONObject(message);

        final long c = jsonObject.getLong("created_at");
        final String m = jsonObject.getString("message");
        final String u = jsonObject.getString("user");

        return new Message(new Date(c), m, new Sender(u));
    }

    private static class ConnectChatServiceAsyncTask extends AsyncTask<Void, Void, Exception> {
        private final ChatService chatService;
        private final ChatService.NewMessageCallback newMessageCallback;

        ConnectChatServiceAsyncTask(
                final ChatService chatService,
                final ChatService.NewMessageCallback newMessageCallback
        ) {
            this.chatService = chatService;
            this.newMessageCallback = newMessageCallback;
        }

        @Override
        protected Exception doInBackground(final Void... voids) {
            try {
                chatService.connect(newMessageCallback);
            } catch (Exception e) {
                return e;
            }

            return null;
        }
    }
}
