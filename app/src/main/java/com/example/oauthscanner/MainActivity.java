package com.example.oauthscanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;
import java.util.List;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.login);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,dashboard.class);
            startActivity(intent);
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLoginRegister();
            }
        });
    }

    private void handleLoginRegister() {
        List<AuthUI.IdpConfig> providers= Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );

        try {
            Intent intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setAlwaysShowSignInMethodScreen(true)
                    .setLogo(R.drawable.logo)
                    .build();
            startActivityForResult(intent, 10001);
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10001){
            IdpResponse response=IdpResponse.fromResultIntent(data);
            Log.e("loginTest",response.toString());
            if(resultCode==RESULT_OK){
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                String emial=user.getEmail();
                String number=user.getPhoneNumber();
                if(emial!=null){
                    if(user.getMetadata().getCreationTimestamp()==user.getMetadata().getLastSignInTimestamp()){
                        Toast.makeText(MainActivity.this,"Welcome "+emial+"!!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this,"Welcome back!! "+emial+"!!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(user.getMetadata().getCreationTimestamp()==user.getMetadata().getLastSignInTimestamp()){
                        Toast.makeText(MainActivity.this,"Welcome "+number+"!!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this,"Welcome back!! "+number+"!!",Toast.LENGTH_SHORT).show();
                    }
                }

                Intent intent=new Intent(this,dashboard.class);
                startActivity(intent);
                finish();
            }else{

                Log.e("loginTest",response.getError().toString());
            }
        }
    }
}