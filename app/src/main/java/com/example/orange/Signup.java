package com.example.orange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    private static final Pattern Password_Pattern =
            Pattern.compile("^" + ".{6,15}" + "$");
    public static EditText inputEmail;
    public static EditText inputpass;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    private final static int RC_SIGN_IN = 22;
    GoogleSignInClient mGppgleSignInClient;
    Button G_signup, Fb_signup;
    private TextView signup_text;
    private CallbackManager mCallbackManager;
    FirebaseFirestore fstore;
    FirebaseAuth firebaseAuth;
    String userID;

    //UI Dev:- vandan Raval
    //app Dev:- Saket Mishra

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        inputEmail = findViewById(R.id.Signupemail);
        inputpass = findViewById(R.id.signup_pass);
        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        G_signup = findViewById(R.id.google_signup);
        Fb_signup = findViewById(R.id.fb_signup);
        fstore=FirebaseFirestore.getInstance();
        findViewById(R.id.Next_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePass()) {
                    showErrorDialog("Check email pass");
                } else {
                    String email = inputEmail.getText().toString();
                    Intent step2 = new Intent(Signup.this, personal_details.class);
                    // String value = inputEmail.getText().toString();
                    // Intent.putExtra("key",value);
                    //startActivity(intent);
                    finish();
                    userID = firebaseAuth.getCurrentUser().getUid();
                    Map<String,Object> user = new HashMap<>();
                    user.put("email",email);
                    DocumentReference documentReference = fstore.collection("users").document(userID);
                    startActivity(step2);

                }
            }
        });
        Fb_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Signup.this, Arrays.asList("email","public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("", "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("", "facebook:onError", error);
                        // ...
                    }
                });
            }
        });
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder().requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGppgleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    finish();

                    startActivity(new Intent(Signup.this, personal_details.class));
                }
            }
        };
        G_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //
        signup_text=findViewById(R.id.Signup_t);
        signup_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(Signup.this,login.class));
            }
        });

    }
    private void signIn() {
        Intent signInIntent = mGppgleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d("Google sign in failed", "signin failed");
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("gsignin", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("tag", "signInWithCredential:failure", task.getException());
                            showErrorDialog("Signup Failed");
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private boolean validatePass() {
        boolean valid =true;

        String pass = inputpass.getText().toString();
        String email = inputEmail.getText().toString();
        if (inputpass.length() == 0) {
            inputpass.setError("Field can't be empty");
            valid= false;
        }
        if (!Password_Pattern.matcher(pass).matches()) {
            inputpass.setError("Minimum 6 characters& use one special character");
            valid= false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Please Enter a Valid Email Address");
            valid= false;
        }
        if (email.length() == 0) {
            inputEmail.setError("Field Can't be empty");
            valid= false;
        }
        return valid;
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            finish();
                            startActivity(new Intent(Signup.this,personal_details.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
