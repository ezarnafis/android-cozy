package com.example.cozy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cozy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button mReset;
    private ProgressBar mLoadingForgot;
    private TextView mCheck;
    private EditText mEmail;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mReset = findViewById(R.id.btn_resetPassword);
        mLoadingForgot = findViewById(R.id.loading_forgot);
        mCheck = findViewById(R.id.tv_check);
        mEmail = findViewById(R.id.et_email_forgot );

        firebaseAuth = firebaseAuth.getInstance();

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                mReset.setEnabled(false);
                mReset.setTextColor(Color.argb(50,255,255,255));

                mLoadingForgot.setVisibility(View.VISIBLE);

                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mCheck.setVisibility(View.VISIBLE);
                                    mLoadingForgot.setVisibility(View.INVISIBLE);
                                }
                            }, 3000);
                        } else {
                            String error = Objects.requireNonNull(task.getException()).getMessage();
                            Toast.makeText(ForgotPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                        }

                        mReset.setEnabled(true);
                        mReset.setTextColor(Color.rgb(255,255,255));
                    }
                });

            }
        });

        ImageView btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void checkInputs(){
        if (TextUtils.isEmpty(mReset.getText())){
            mReset.setEnabled(false);
            mReset.setTextColor(Color.argb(50,255,255,255));
        }else{
            mReset.setEnabled(true);
            mReset.setTextColor(Color.rgb(255,255,255));
        }
    }
}
