package com.example.dispositivosmoviles.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dispositivosmoviles.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGPT extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);

        // Setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v) -> {
            String question = messageEditText.getText().toString().trim();
            addToChat(question, Message.SENT_BY_ME);
            messageEditText.setText("");
            callAPIWithRetry(question, 0); // Llamar al método con reintento
            welcomeTextView.setVisibility(View.GONE);
        });
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SENT_BY_BOT);
    }

    void callAPIWithRetry(String question, int retryCount) {
        final int maxRetries = 1; // Número máximo de reintentos
        final long initialBackoffMillis = 1000; // Retraso inicial en milisegundos

        if (retryCount > maxRetries) {
            addResponse("ha alcanzado el número maximo de solicitudes");
            return;
        }

        messageList.add(new Message("Espera un momento... ", Message.SENT_BY_BOT));

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "text-davinci-003");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);

            RequestBody body = RequestBody.create(JSON, jsonBody.toString());
            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/completions")
                    .header("Authorization", "Bearer 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")
                    .post(body)
                    .build();

            Call call = client.newCall(request);

            synchronized (this) {
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        // Agregar retraso exponencial antes de reintentar
                        long backoffMillis = initialBackoffMillis * (1 << retryCount);
                        try {
                            Thread.sleep(backoffMillis);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        callAPIWithRetry(question, retryCount + 1); // Reintento con aumento de contador
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                JSONArray jsonArray = jsonObject.getJSONArray("choices");
                                String result = jsonArray.getJSONObject(0).getString("text");
                                addResponse(result.trim());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                addResponse("no se pudo analizar el JSON response");
                            }
                        } else {
                            // Agregar retraso exponencial antes de reintentar
                            long backoffMillis = initialBackoffMillis * (1 << retryCount);
                            try {
                                Thread.sleep(backoffMillis);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            callAPIWithRetry(question, retryCount + 1); // Reintento con aumento de contador
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            addResponse("Fallo al crear el JSON");
        }

    }

}












