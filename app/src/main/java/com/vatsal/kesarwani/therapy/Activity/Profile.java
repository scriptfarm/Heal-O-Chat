package com.vatsal.kesarwani.therapy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vatsal.kesarwani.therapy.Model.AppConfig;
import com.vatsal.kesarwani.therapy.R;

import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Profile extends AppCompatActivity {

    private FloatingActionButton editProfile;
    private TextView name,age,sex,contact,about,description;
    private String sn,sa,ss,sc,sabout,sdes;
    private CircleImageView profile;
    private ProgressBar progressBar;
    private LinearLayout profileData;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private static final String TAG = "Profile";
    private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        progressBar.setVisibility(View.VISIBLE);
        profile.setVisibility(View.GONE);
        profileData.setVisibility(View.GONE);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("User")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            if (document.exists()){
                                Map<String,Object> map=document.getData();
                                assert map != null;

                                name.setText(Objects.requireNonNull(map.get(AppConfig.NAME)).toString());
                                sn=Objects.requireNonNull(map.get(AppConfig.NAME)).toString();
                                intent.putExtra(AppConfig.NAME,sn);

                                age.setText(Objects.requireNonNull(map.get(AppConfig.AGE)).toString());
                                sa=Objects.requireNonNull(map.get(AppConfig.AGE)).toString();
                                intent.putExtra(AppConfig.AGE,sa);

                                sex.setText(Objects.requireNonNull(map.get(AppConfig.SEX)).toString());
                                ss=Objects.requireNonNull(map.get(AppConfig.SEX)).toString();
                                intent.putExtra(AppConfig.SEX,ss);

                                contact.setText(Objects.requireNonNull(map.get(AppConfig.NUMBER)).toString());
                                sc=Objects.requireNonNull(map.get(AppConfig.NUMBER)).toString();
                                intent.putExtra(AppConfig.NUMBER,sc);

                                about.setText(Objects.requireNonNull(map.get(AppConfig.ABOUT)).toString());
                                sabout=Objects.requireNonNull(map.get(AppConfig.ABOUT)).toString();
                                if(sabout.length()==0){
                                    about.setVisibility(View.GONE);
                                }
                                intent.putExtra(AppConfig.ABOUT,sabout);

                                description.setText(Objects.requireNonNull(map.get(AppConfig.DESCRIPTION)).toString());
                                sdes=Objects.requireNonNull(map.get(AppConfig.DESCRIPTION)).toString();
                                if (sdes.length()==0){
                                    description.setVisibility(View.GONE);
                                }
                                intent.putExtra(AppConfig.DESCRIPTION,sdes);

                                if (sharedPreferences.getString(AppConfig.PROFILE_DP,"com.vatsal.kesarwani.theraphy.PROFILE_DP").equals("com.vatsal.kesarwani.theraphy.PROFILE_DP")){
                                    if(Objects.requireNonNull(map.get(AppConfig.SEX)).toString().equals("Male")){
                                        profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_male));
                                    }
                                    else if (Objects.requireNonNull(map.get(AppConfig.SEX)).toString().equals("Female")){
                                        profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_female));
                                    }
                                }
                                progressBar.setVisibility(View.GONE);
                                profile.setVisibility(View.VISIBLE);
                                profileData.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                            Toasty.error(Profile.this,"Error Fetching Data "+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void init() {
        editProfile=findViewById(R.id.edit_profile);
        name=findViewById(R.id.fullname);
        age=findViewById(R.id.age);
        sex=findViewById(R.id.sex);
        contact=findViewById(R.id.contact);
        about=findViewById(R.id.about);
        description=findViewById(R.id.description);
        profile=findViewById(R.id.profile_dp);
        progressBar=findViewById(R.id.progressBar);
        profileData=findViewById(R.id.profile_data);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        sharedPreferences=getSharedPreferences(AppConfig.SHARED_PREF, Context.MODE_PRIVATE);
        intent=new Intent(getApplicationContext(),Editprofile.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainScreen.class));
    }
}