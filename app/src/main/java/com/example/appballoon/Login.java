package com.example.appballoon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;




public class Login extends AppCompatActivity {

    //declaracio de variables
    EditText user,password;
    FirebaseAuth mAuth;
    public static String email,passw;
    ProgressBar progressBar;
    LinearLayout layout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = (EditText) findViewById(R.id.input_email);
        password= (EditText) findViewById(R.id.input_password);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.loading);
        layout=findViewById(R.id.layout);

    }




    public void loginUser (View v){
        email=user.getText().toString().trim();
        passw=password.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);


        if(user.getText().toString().equals("") && password.getText().toString().equals("") ){
            Toast.makeText(getApplicationContext(), "Introduce your e-mail and your password", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);

        }
        else{
            mAuth.signInWithEmailAndPassword(email,passw)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_SHORT).show();
                                finish();

                                Intent intent2 = new Intent(getApplicationContext(),Menu.class);
                                startActivity(intent2);

                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),"Incorrect",Toast.LENGTH_SHORT).show();

                            }
                        }

                    });

        }
    }


}