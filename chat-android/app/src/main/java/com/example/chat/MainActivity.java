package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Piotr Heilman
 */
public class MainActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText channelNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.main_username_edit_text);
        channelNameEditText = findViewById(R.id.main_channel_name_edit_text);

        final Button sendButton = findViewById(R.id.main_enter_button);
        sendButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getBaseContext(), MessageListActivity.class);
                intent.putExtra(MessageListActivity.LOGGED_USERNAME, getUsername());
                intent.putExtra(MessageListActivity.CHANNEL_NAME, getChannelName());
                startActivity(intent);
            }
        });
    }

    private String getUsername() {
        final Editable text = usernameEditText.getText();

        final String username = text.toString();
        text.clear();

        return username;
    }

    private String getChannelName() {
        final Editable text = channelNameEditText.getText();

        final String channelName = text.toString();
        text.clear();

        return channelName;
    }
}
