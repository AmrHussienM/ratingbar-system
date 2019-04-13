package com.example.ratingbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private RatingBar ratingBar;
    private TextView tvRateCount,tvRateMessage;
    private double dpRating;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private FirebaseUser user;
    String currentUserId=mAuth.getCurrentUser().getUid();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initalization();
        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {




                reference=FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(currentUserId).child("Rating");

                dpRating=rating;
                reference.setValue(dpRating);


            }
        });


    }

    public void submitRating(View view) {
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserId);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double total = 0.0;
                double count = 0.0;
                double average = 0.0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //double rating = Double.parseDouble(ds.child("Rating").getValue());
                    double rating = Double.parseDouble(ds.child("Rating").getValue().toString());
                    total = total + rating;
                    count = count + 1;
                    average = total / count;
                    total = total + rating;
                    count = count + 1;
                    average = total / count;
                }

                final DatabaseReference newRef = dbRef.child(currentUserId);
                newRef.child("averageRating").setValue(average);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }



    private void initalization()
    {
        ratingBar=findViewById(R.id.ratingBar);
        tvRateCount=findViewById(R.id.tvRateCount);
        tvRateMessage=findViewById(R.id.tvRateMessage);
    }
}
