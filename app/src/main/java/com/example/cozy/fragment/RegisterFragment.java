package com.example.cozy.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.cozy.MainActivity;
import com.example.cozy.R;
import com.example.cozy.activity.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private EditText mFullname, mEmail, mPassword;
    private RadioGroup mGender;
    private RadioButton mValGender;
    private Button mRegister;
    private FirebaseAuth fAuth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar mLoadingRegister;
    private String genderValue;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_register, container, false);

        mFullname         = view.findViewById(R.id.et_fullname);
        mEmail            = view.findViewById(R.id.et_email_register);
        mPassword         = view.findViewById(R.id.et_password_register);
        mGender           = view.findViewById(R.id.layout_gender);
        mRegister         = view.findViewById(R.id.btn_register);
        mLoadingRegister  = view.findViewById(R.id.loading_register);

        // Get value from radio button gender
        mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                mValGender = (RadioButton) view.findViewById(checkedId);
            }
        });

        // Get access firebase authentication
        fAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                // Check email is empty?
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                // Check password is empty?
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                // Check password length
                if ((password.length() < 6) | (password.length() > 13)){
                    mPassword.setError("Password must be 6-13 characters.");
                    return;
                }

                mLoadingRegister.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    // Create object map
                                    Map <Object, String> userdata = new HashMap<>();

                                    // Put data to hash map
                                    userdata.put("email", email);
                                    userdata.put("full_name", mFullname.getText().toString());
                                    userdata.put("gender", mValGender.getText().toString().toLowerCase());
                                    userdata.put("mobile", "");
                                    userdata.put("birth_date", "");

                                    firebaseFirestore.collection("users").add(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "User Created.", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                                    } else {
                                                        Toast.makeText(getActivity(), "Failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(getActivity(), "Failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return view;
    }

}
