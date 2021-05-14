package com.example.sharara.Regestration;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView forgetPasswordTxt, registeredTxt;
    private EditText ed_email;
    private EditText ed_password;
    private Button login;
    private ProgressBar progressBar;
    private CheckBox rememberMe;
    private Dialog loadingDialog;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private List<UserData> userList = new ArrayList<>();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //banner =findViewById(R.id.re)
        forgetPasswordTxt = findViewById(R.id.forgetPassword);
        registeredTxt = findViewById(R.id.register_btn);
        rememberMe = findViewById(R.id.rememberMe);
        ed_email = findViewById(R.id.login_userName);
        ed_password = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.progressBar);

        forgetPasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPassword.class));
            }
        });

        Intent i = getIntent();
        int out = i.getIntExtra("pref",0);

        if (out == 1){
            savePrefsData(false);
        }else {

        }

        if (restorePrefData()) {
            loadingDialog.show();
            FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
            if (mFirebaseUser != null) {
                String userID = mAuth.getCurrentUser().getUid();
                DocumentReference df = firestore.collection("users").document(userID);
                checkedType(df, userID);
                loadingDialog.dismiss();
            } else {
                loadingDialog.dismiss();
            }
        }


        registeredTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        login = findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void login() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ed_email.getWindowToken(), 0);

        String password = ed_password.getText().toString().trim();
        String email = ed_email.getText().toString().trim();


        if (email.isEmpty()) {
            ed_email.setError(" email Is Empty! ");
            ed_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ed_email.setError(" Please Provide valid email! ");
            ed_email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            ed_password.setError(" Password Is Empty! ");
            ed_password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            ed_password.setError(" Password Min 6 characters! ");
            ed_password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    String userID = authResult.getUser().getUid();
                    DocumentReference df = firestore.collection("users").document(userID);

                    if (df != null) {
                        //TODO Remember me ...
                        if (rememberMe.isChecked()) {
                            savePrefsData(true);
                            checkedType(df, userID);

                        } else {
                            checkedType(df, userID);
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "Failed Login, Please check credentials!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Failed Login, try again!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Failed Login", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void checkedType(final DocumentReference df, final String id) {

        try {
            userList.clear();
            firestore.collection("users").document("userList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            long count = (long) doc.get("COUNT");
                            for (int i = 1; i <= count; i++) {
                                if (doc.getString("USER" + String.valueOf(i) + "_ID").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                    String userID = doc.getString("USER" + String.valueOf(i) + "_ID");
                                    String userName = doc.getString("USER" + String.valueOf(i) + "_NAME");
                                    String userEmail = doc.getString("USER" + String.valueOf(i) + "_EMAIL");
                                    String userPassword = doc.getString("USER" + String.valueOf(i) + "_PASSWORD");
                                    String userNumber = doc.getString("USER" + String.valueOf(i) + "_NUMBER");
                                    userList.add(new UserData(userID, userName, userEmail, userPassword, userNumber, "1"));
                                }
                            }
                            if (df != null) {
                                df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {

                                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                            // i.putExtra("user", 1);
                                            startActivity(i);
                                            progressBar.setVisibility(View.GONE);
                                            LoginActivity.this.finish();

                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(LoginActivity.this, "Failed Login, Please check credentials!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, "Failed Login, Please check credentials!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Failed Login, Please check credentials!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Failed Login ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "try again , check email or password", Toast.LENGTH_SHORT).show();
        }

    }

    private void savePrefsData(boolean t) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isRememberMe", t);
        editor.commit();
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isRememberMe", false);
        return isIntroActivityOpnendBefore;
    }
}