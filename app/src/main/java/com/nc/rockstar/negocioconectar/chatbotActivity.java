package com.nc.rockstar.negocioconectar;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import java.util.ArrayList;
import java.util.List;

public class chatbotActivity extends Fragment implements AIListener{

    RecyclerView recyclerView;
    EditText editText;
    RelativeLayout addBtn;
    DatabaseReference ref;

    FirebaseRecyclerAdapter<ChatMessage,chat_rec> adapter;
    private List<ChatMessage> chatList = new ArrayList<>();
    chatAdapter mAdapter;
    Boolean flagFab = true;

    private LinearLayoutManager linearLayoutManager;

    private AIService aiService;

    View myView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_chatbot, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO},1);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);

        editText = (EditText) getActivity().findViewById(R.id.chat_entry);
        addBtn = (RelativeLayout) getActivity().findViewById(R.id.addBtn);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);

        final AIConfiguration config = new AIConfiguration("0c01e159babc4349b38eca698bd2f107",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(getActivity(), config);
        aiService.setListener(this);

        final AIDataService aiDataService = new AIDataService(config);

        final AIRequest aiRequest = new AIRequest();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString().trim();

                if (!message.equals("")) {

                    ChatMessage chatMessage = new ChatMessage(message, "user");
                    ref.child("chat").push().setValue(chatMessage);

                    aiRequest.setQuery(message);
                    new AsyncTask<AIRequest,Void,AIResponse>(){

                        @Override
                        protected AIResponse doInBackground(AIRequest... aiRequests) {
                            final AIRequest request = aiRequests[0];
                            try {
                                final AIResponse response = aiDataService.request(aiRequest);
                                return response;
                            } catch (AIServiceException e) {
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(AIResponse response) {
                            if (response != null) {

                                Result result = response.getResult();
                                String reply = result.getFulfillment().getSpeech();
                                ChatMessage chatMessage = new ChatMessage(reply, "bot");
                                ref.child("chat").push().setValue(chatMessage);
                            }
                        }
                    }.execute(aiRequest);
                }
                else {
                    aiService.startListening();
                }

                editText.setText("");

            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ImageView fab_img = (ImageView) getActivity().findViewById(R.id.fab_img);
                Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.ic_send_white_24dp);
                Bitmap img1 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_mic_white_24dp);


                if (s.toString().trim().length()!=0 && flagFab){
                    ImageViewAnimatedChange(getActivity(),fab_img,img);
                    flagFab=false;

                }
                else if (s.toString().trim().length()==0){
                    ImageViewAnimatedChange(getActivity(),fab_img,img1);
                    flagFab=true;

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Query query = FirebaseDatabase.getInstance().getReference().child("chat");

        //FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ChatMessage>().setQuery(ref.child("chat"),ChatMessage.class).build();




        //mAdapter = new chatAdapter(chatList);

        FirebaseRecyclerOptions<ChatMessage> options =
                new FirebaseRecyclerOptions.Builder<ChatMessage>()
                        .setQuery(ref, ChatMessage.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<ChatMessage, chat_rec>(options) {

            @NonNull
            @Override
            public chat_rec onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msglist, parent, false);
                return new chat_rec(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull chat_rec holder, int position, @NonNull ChatMessage model) {
                if (model.getMsgUser().equals("user")) {
                    holder.rightText.setText(model.getMsgText());
                    holder.rightText.setVisibility(View.VISIBLE);
                    holder.leftText.setVisibility(View.GONE);
                }
                else {
                    holder.leftText.setText(model.getMsgText());
                    holder.rightText.setVisibility(View.GONE);
                    holder.leftText.setVisibility(View.VISIBLE);
                }
            }
        };

//        adapter = new FirebaseRecyclerAdapter<ChatMessage, chat_rec>(ChatMessage.class,R.layout.msglist,chat_rec.class,ref.child("chat")) {
//            @Override
//            protected void populateViewHolder(chat_rec viewHolder, ChatMessage model, int position) {
//
//                if (model.getMsgUser().equals("user")) {
//
//
//                    viewHolder.rightText.setText(model.getMsgText());
//
//                    viewHolder.rightText.setVisibility(View.VISIBLE);
//                    viewHolder.leftText.setVisibility(View.GONE);
//                }
//                else {
//                    viewHolder.leftText.setText(model.getMsgText());
//
//                    viewHolder.rightText.setVisibility(View.GONE);
//                    viewHolder.leftText.setVisibility(View.VISIBLE);
//                }
//            }
//        };


        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int msgCount = adapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (msgCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);

                }

            }
        });

        recyclerView.setAdapter(adapter);
    }

    public void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.zoom_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, R.anim.zoom_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    @Override
    public void onResult(ai.api.model.AIResponse response) {


        Result result = response.getResult();

        String message = result.getResolvedQuery();
        ChatMessage chatMessage0 = new ChatMessage(message, "user");
        ref.child("chat").push().setValue(chatMessage0);


        String reply = result.getFulfillment().getSpeech();
        ChatMessage chatMessage = new ChatMessage(reply, "bot");
        ref.child("chat").push().setValue(chatMessage);


    }

    @Override
    public void onError(ai.api.model.AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }




//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        //super.onCreate(savedInstanceState);
//        //setContentView(R.layout.layout_ads);
//        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        //setSupportActionBar(toolbar);
//
//        fragmentManager = getActivity().getFragmentManager();
//
//        FB = FirebaseFirestore.getInstance();
//        getListItems();
//
//        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
//
//        mAdapter = new adsAdapter(movieList);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        clear();
//
//        recyclerView.setAdapter(mAdapter);
//
//
//
//        //to start ad posting page by clicking floating action button
//        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //tx.replace( R.id.fragment, new MyFragment() ).addToBackStack( "tag" ).commit();
//
//
//                fragmentManager.beginTransaction()
//                        .replace(R.id.frame_layout, new ad_postingActivity()).addToBackStack("ads_stack").commit();
//
//                //Intent intent = new Intent(getActivity(), ad_postingActivity.class);
//                //startActivity(intent);
//            }
//        });
//
//    }
//
//    public void clear() {
//        final int size = movieList.size();
//        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                movieList.remove(0);
//            }
//
//            mAdapter.notifyItemRangeRemoved(0, size);
//        }
//    }
//
//
//
//
//
//    private void prepareMovieData() {
//        ads movie = new ads("Design Engineer Job Vacancy", "We are looking for a talented and detail-oriented Design Engineer to join our creative team. As the Design Engineer, you will be responsible for carrying out a variety of engineering duties including designing, researching,and prototyping new products. REQUIREMENTS - Bachelor’s degree in design engineering, product design or relevant field;experience:1-3yrs", "Maeve Tran");
//        movieList.add(movie);
//
//        movie = new ads("Receptionist for Eclipse Corp.,Delhi", "We are looking for a Receptionist of giving clients directions to various parts of the office, contacting employees regarding visitors, answering phones and taking messages, and sorting and distributing mail. REQUIREMENTS:Excellent written and verbal communication skills, as well as competency in Microsoft Office, Good time management skills.", "Aakash Sharma");
//        movieList.add(movie);
//
//        movie = new ads("Supervisor needed", "We are looking for a Supervisor who will be in charge of managing one of our working shifts. Duties involve general management of your direct reports, providing and demonstrating task instructions, keeping attendance, and measuring key performance indicators. REQUIREMENTS- Excellent communication skills,Computer literacy,High school diploma.", "Aditya Cabrera");
//        movieList.add(movie);
//
//        movie = new ads("Quality Manager Job", "We are looking for highly diligent candidates with excellent attention to detail for the role of Quality Manager for monitoring and evaluating internal production processes, examining products to determine their quality and engaging with customers and gathering product feedback. REQUIREMENTS-Degree in Business Administration or relevant field,Excellent verbal and written communication.", "Kayla Norton");
//        movieList.add(movie);
//
//        movie = new ads("Experienced Music Producer needed", "We are looking for a hard-working, innovative and very professional music producer for our next project. REQUIREMENTS- Bachelor's degree in science of music, or Bachelor's degree in music, Knowledge of industry law and ethics, Firm grasp of sound editing techniques, 3+ years' experience using digital audio hardware and software.", "Shaniya Allen");
//        movieList.add(movie);
//
//        movie = new ads("Game designer post", "We are looking for a game designer whose responsibilities include developing design and gaming protocols, defining game-play mechanics, coordinating with other game designers, ensuring quality, and meeting with company executives.REQUIREMENTS-Bachelor's degree in game design, computer science or computer engineering,Knowledge of the industry,Portfolio of sample projects, Ability to work under tight schedules", "Mike Wilson(Devolver Digital)");
//        movieList.add(movie);
//
//
//
//        mAdapter.notifyDataSetChanged();
//    }
//
//    private void getListItems() {
//        FB.collection("ads").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot documentSnapshots) {
//                        if (documentSnapshots.isEmpty()) {
//                            Log.d("userDetails", "onSuccess: LIST EMPTY");
//
//                            return;
//                        } else {
//                            // Convert the whole Query Snapshot to a list
//                            // of objects directly! No need to fetch each
//                            // document.
//                            List<ad_new> types = documentSnapshots.toObjects(ad_new.class);
//
//                            for (int i=0; i<types.size() ; i++) {
//
//                                ad_new new_ad_cre = types.get(i);
//
//                                ads ad_cre = new ads(new_ad_cre.getAdtitle(), new_ad_cre.getAdDescription(), new_ad_cre.getUsername());
//
//                                // Add all to your list
//                                movieList.add(ad_cre);
//                            }
//                            Log.d("userDetails", "onSuccess: " + movieList);
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getActivity(), "Error getting data!!!", Toast.LENGTH_LONG).show();
//                    }
//                });
//    }
}
