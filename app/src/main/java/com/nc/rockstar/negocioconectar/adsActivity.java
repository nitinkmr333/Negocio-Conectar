package com.nc.rockstar.negocioconectar;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class adsActivity extends Fragment {
    private List<ads> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private adsAdapter mAdapter;

    private View myView;

    FirebaseFirestore FB;

    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_ads, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.layout_ads);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        fragmentManager = getActivity().getFragmentManager();

        FB = FirebaseFirestore.getInstance();
        getListItems();

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);

        mAdapter = new adsAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        clear();

        recyclerView.setAdapter(mAdapter);



        //to start ad posting page by clicking floating action button
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //tx.replace( R.id.fragment, new MyFragment() ).addToBackStack( "tag" ).commit();


                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new ad_postingActivity()).addToBackStack("ads_stack").commit();

                //Intent intent = new Intent(getActivity(), ad_postingActivity.class);
                //startActivity(intent);
            }
        });

    }

    public void clear() {
        final int size = movieList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                movieList.remove(0);
            }

            mAdapter.notifyItemRangeRemoved(0, size);
        }
    }





    private void prepareMovieData() {
        ads movie = new ads("Design Engineer Job Vacancy", "We are looking for a talented and detail-oriented Design Engineer to join our creative team. As the Design Engineer, you will be responsible for carrying out a variety of engineering duties including designing, researching,and prototyping new products. REQUIREMENTS - Bachelor’s degree in design engineering, product design or relevant field;experience:1-3yrs", "Maeve Tran");
        movieList.add(movie);

        movie = new ads("Receptionist for Eclipse Corp.,Delhi", "We are looking for a Receptionist of giving clients directions to various parts of the office, contacting employees regarding visitors, answering phones and taking messages, and sorting and distributing mail. REQUIREMENTS:Excellent written and verbal communication skills, as well as competency in Microsoft Office, Good time management skills.", "Aakash Sharma");
        movieList.add(movie);

        movie = new ads("Supervisor needed", "We are looking for a Supervisor who will be in charge of managing one of our working shifts. Duties involve general management of your direct reports, providing and demonstrating task instructions, keeping attendance, and measuring key performance indicators. REQUIREMENTS- Excellent communication skills,Computer literacy,High school diploma.", "Aditya Cabrera");
        movieList.add(movie);

        movie = new ads("Quality Manager Job", "We are looking for highly diligent candidates with excellent attention to detail for the role of Quality Manager for monitoring and evaluating internal production processes, examining products to determine their quality and engaging with customers and gathering product feedback. REQUIREMENTS-Degree in Business Administration or relevant field,Excellent verbal and written communication.", "Kayla Norton");
        movieList.add(movie);

        movie = new ads("Experienced Music Producer needed", "We are looking for a hard-working, innovative and very professional music producer for our next project. REQUIREMENTS- Bachelor's degree in science of music, or Bachelor's degree in music, Knowledge of industry law and ethics, Firm grasp of sound editing techniques, 3+ years' experience using digital audio hardware and software.", "Shaniya Allen");
        movieList.add(movie);

        movie = new ads("Game designer post", "We are looking for a game designer whose responsibilities include developing design and gaming protocols, defining game-play mechanics, coordinating with other game designers, ensuring quality, and meeting with company executives.REQUIREMENTS-Bachelor's degree in game design, computer science or computer engineering,Knowledge of the industry,Portfolio of sample projects, Ability to work under tight schedules", "Mike Wilson(Devolver Digital)");
        movieList.add(movie);



        mAdapter.notifyDataSetChanged();
    }

    private void getListItems() {
        FB.collection("ads").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("userDetails", "onSuccess: LIST EMPTY");

                            return;
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<ad_new> types = documentSnapshots.toObjects(ad_new.class);

                            for (int i=0; i<types.size() ; i++) {

                                ad_new new_ad_cre = types.get(i);

                                ads ad_cre = new ads(new_ad_cre.getAdtitle(), new_ad_cre.getAdDescription(), new_ad_cre.getUsername());

                                // Add all to your list
                                movieList.add(ad_cre);
                            }
                            Log.d("userDetails", "onSuccess: " + movieList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });
    }

}