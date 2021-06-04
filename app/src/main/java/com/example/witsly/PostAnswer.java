package com.example.witsly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.witsly.Models.Comment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class PostAnswer extends AppCompatActivity {

    private TextInputEditText body;
    private FirebaseActions firebaseActions;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_answer);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Post Reply");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        
        body = findViewById(R.id.ed_reply);
        firebaseActions = new FirebaseActions();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.btn_post:
                String bodyText = body.getText().toString().trim();
                addComment(bodyText, mAuth.getUid(), " ", " ");
        }

        return super.onOptionsItemSelected(item);
    }

    private void addComment(String body, String uid, String aid, String cid){
        Comment comment = new Comment(body, uid, aid, cid);

        firebaseActions.addComment(comment,
                a->{
                        if(a){
                            Toast.makeText(this, "Comment added succesfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                });
    }
}