package com.example.loginandsignupactivitytutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button mainLoginBtn, mainSignupBtn, btnSignOut;
    private TextView textViewLoginName, textViewLoginEmail;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    LoginManager loginManager;
    private ImageView imageViewProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewLoginName = findViewById(R.id.textViewLoginName);
        textViewLoginEmail = findViewById(R.id.textViewLoginEmail);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            textViewLoginName.setText(personName);
            textViewLoginEmail.setText(personEmail);
        }
        imageViewProfilePic = findViewById(R.id.imageViewProfilePic);
        //This code will retrieve facebook user data.
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        /*if (accessToken != null && accessToken.isExpired() == false) {

        }*/
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        //We can retrieve data from JSONObject i.e, object.
                        //we are writing code from here
                        try {
                            String fullName = object.getString("name");
                            String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            textViewLoginName.setText(fullName);
                            Picasso.get().load(url).into(imageViewProfilePic);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();

        btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleSignInClient != null) {
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            finish();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                    });
                } else if (loginManager != null) {
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });


    }
}