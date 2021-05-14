package com.example.sharara.Regestration;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharara.Model.UserData;
import com.example.sharara.R;
import com.example.sharara.UI.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private List<UserData> users = new ArrayList<>();

    Button sinIN_Btn, okDialog;
    ImageView back, backDialog;
    EditText username, userPassword, userNumber, userEmail, passwordDialog;
    TextView goLogin;
    Spinner spinner;
    String Name, Password, Number, Status, Email;
    ProgressBar progressBar;
    Dialog adminDialogPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        progressBar = findViewById(R.id.progressBar);
        sinIN_Btn = findViewById(R.id.RG_registerBtn);
        username = findViewById(R.id.userName);
        userPassword = findViewById(R.id.password);
        userNumber = findViewById(R.id.number);
        userEmail = findViewById(R.id.Sin_email);
        goLogin = findViewById(R.id.txtLogin);


        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        sinIN_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insert in fireStore
                Name = username.getText().toString();
                Password = userPassword.getText().toString();
                Email = userEmail.getText().toString();
                Number = userNumber.getText().toString();

                registerUser(Name, Email, Password, Number);
            }
        });


        loadData();
    }


    public void registerUser(final String name, final String email, final String password, final String number) {


        if (name.isEmpty()) {
            username.setError(" Full Name Is Empty! ");
            username.requestFocus();
            return;
        }

        if (number.isEmpty()) {
            userNumber.setError(" NUMBER Is Empty! ");
            userNumber.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            userEmail.setError(" email Is Empty! ");
            userEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError(" Please Provide valid email! ");
            userEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            userPassword.setError(" Password Is Empty! ");
            userPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            userPassword.setError(" Password Min 6 characters! ");
            userPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        Log.d("mina1", "msg:" + email + "l" + password);
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Log.d("mina2", "msg:" + email + "l" + password);

                final Map<String, Object> userData = new ArrayMap<>();
                userData.put("NAME", name);
                userData.put("EMAIL", email);
                userData.put("PASSWORD", password);
                userData.put("NUMBER", number);
                userData.put("COUNT", "1");


                final String user_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DocumentReference df = firestore.collection("users").document(user_ID);

                df.set(userData);


                firestore.collection("users").document(user_ID).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Map<String, Object> userDoc = new ArrayMap<>();
                        userDoc.put("USER" + String.valueOf(users.size() + 1) + "_ID", user_ID);
                        userDoc.put("USER" + String.valueOf(users.size() + 1) + "_NAME", name);
                        userDoc.put("USER" + String.valueOf(users.size() + 1) + "_EMAIL", email);
                        userDoc.put("USER" + String.valueOf(users.size() + 1) + "_PASSWORD", password);
                        userDoc.put("USER" + String.valueOf(users.size() + 1) + "_NUMBER", number);

                        userDoc.put("COUNT", users.size() + 1);

                        firestore.collection("users").document("userList").update(userDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                users.add(new UserData(user_ID, name, email, password, number, "1"));
                                Toast.makeText(SignUpActivity.this, "successfully! ", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                // intent.putExtra("user", 1);
                                startActivityForResult(intent, 1);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpActivity.this, "check your network .. this processing is Failure ", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                            }
                        });
                        FirebaseDatabase.getInstance().getReference("users").child(user_ID).setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpActivity.this, "check your network .. this processing is Failure ", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "try again another time or check network! ", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "error, check if you have account by same email! ", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void loadData() {
        users.clear();
        firestore.collection("users").document("userList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        long count = (long) doc.get("COUNT");
                        for (int i = 1; i <= count; i++) {
                            String UserID = doc.getString("USER" + String.valueOf(i) + "_ID");
                            String UserName = doc.getString("USER" + String.valueOf(i) + "_NAME");
                            String UserEmail = doc.getString("USER" + String.valueOf(i) + "_EMAIL");
                            String UserPassword = doc.getString("USER" + String.valueOf(i) + "_PASSWORD");
                            String UserNumber = doc.getString("USER" + String.valueOf(i) + "_NUMBER");

                            users.add(new UserData(UserID, UserName, UserEmail, UserPassword, UserNumber, "1"));
                        }

//                        startActivity(new Intent(SplashActivity.this,CategoryActivity.class));
//                        SplashActivity.this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Document Exists ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}