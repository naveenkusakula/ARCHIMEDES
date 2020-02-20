package com.phoenix.archimedes.GoogleAuthentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.phoenix.archimedes.MainActivity;
import com.phoenix.archimedes.R;

import org.w3c.dom.Text;


public class GPlusFragment extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{


   // private LinearLayout profileSection;
   // private Button signOut;
    private SignInButton signIn;
   // private TextView name,email;
  //  private ImageView profilePic;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;

   // private TextView userName;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.print("On create Gplusf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gplus);

//        profileSection = (LinearLayout)findViewById(R.id.profileSection);
//        signOut = (Button) findViewById(R.id.btn_logout);
        signIn = (SignInButton) findViewById(R.id.btn_login);
      //  userName = (TextView)  findViewById(R.id.userName);
        btnNext = (Button)findViewById(R.id.btnNext);
     //   name = (TextView) findViewById(R.id.name);
//        email = (TextView) findViewById(R.id.email);
//        profilePic = (ImageView) findViewById(R.id.profilePic);
        signIn.setOnClickListener(this);
//        signOut.setOnClickListener(this);
//        profileSection.setVisibility(View.GONE);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("54155135999-9ei7v4t53rengo5s6in4jankb504ie9q.apps.googleusercontent.com").requestEmail().requestProfile().build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .enableAutoManage(this,this)
                .build();

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_login :
                signIn();
                break;
//            case R.id.btn_logout :
//                signOut();
//                break;

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

        startActivityForResult(intent,REQ_CODE);
    }

    public void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    private void handleResult(GoogleSignInAccount result){
        System.out.println("handle result");
      /*  if(result.()){
            GoogleSignInAccount account = result.getSignInAccount();*/
//            String displayName = result.getDisplayName();
//            String displayEmail = result.getEmail();
//            if(result.getPhotoUrl() != null){
//                String profilePicUrl = result.getPhotoUrl().toString();
//                Glide.with(this).load(profilePicUrl).into(profilePic);
//            }
//            System.out.println(displayEmail+"**********"+displayName);
//            name.setText(displayName);
//            email.setText(displayEmail);
            updateUI(true);

       /* }
        else{
            updateUI(false);
        }*/
    }

    private void updateUI(boolean isLogin){

        if(isLogin){
           // profileSection.setVisibility(View.VISIBLE);
            signIn.setVisibility(View.GONE);
           // userName.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });
            //startActivity(new Intent(getApplicationContext(), MainActivity.class));


        }else{
            System.out.print("update ui");
           // profileSection.setVisibility(View.GONE);
            signIn.setVisibility(View.VISIBLE);
//            setContentView(R.layout.fragment_gplus);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.print("update ui");
        if(requestCode == REQ_CODE){
            GoogleSignInAccount  result = GoogleSignIn.getLastSignedInAccount(this);
            if(result != null){
                handleResult(result);
            }else{
                updateUI(false);
            }

        }else if(requestCode == 3001){
            signOut();
        }
    }

}
