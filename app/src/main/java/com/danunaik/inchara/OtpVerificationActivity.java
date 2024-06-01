package com.danunaik.inchara;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpVerificationActivity extends AppCompatActivity {

    private EditText otpEditText;
    private Button verifyOtpButton;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        otpEditText = findViewById(R.id.otp_edit_text);
        verifyOtpButton = findViewById(R.id.verify_otp_button);

        mAuth = FirebaseAuth.getInstance();

        verificationId = getIntent().getStringExtra("verificationId");

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpEditText.getText().toString().trim();
                if (!code.isEmpty()) {
                    verifyCode(code);
                } else {
                    otpEditText.setError("Enter OTP");
                }
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, navigate to the dashboard
                            Intent intent = new Intent(OtpVerificationActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign in failed, display a message
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                otpEditText.setError("Invalid OTP");
                            } else {
                                Toast.makeText(OtpVerificationActivity.this, "Authentication failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}
