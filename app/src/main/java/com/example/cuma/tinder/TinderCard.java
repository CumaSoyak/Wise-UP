package com.example.cuma.tinder;

import android.content.Context;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuma.tinder.Activity.ExamsActivity;
import com.example.cuma.tinder.Activity.MainActivity;
import com.example.cuma.tinder.Class.Profile;
import com.example.cuma.tinder.Class.PuanHesapla;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.util.ArrayList;

@Layout(R.layout.tinder_card_view)
public class TinderCard {
    Utils utils = new Utils();


    @View(R.id.question)
    private TextView question;

    @View(R.id.answer)
    private TextView answer;

    @View(R.id.kategori)
    private TextView point;

    @View(R.id.kategori_image)
    ImageView kategori_circle_image;

    @View(R.id.kategori_title)
    TextView kategori_title;

    @View(R.id.like_img)
    ImageButton like;

    @View(R.id.dislike_img)
    ImageButton dislike;

    private Profile mProfile;
    private int mQuiz;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    int say=0;
    private ExamsActivity activity;

    public TinderCard(Context context, Profile profile, SwipePlaceHolderView swipeView, int quiz) {
        mContext = context;
        activity = (ExamsActivity) context;
        mProfile = profile;
        mSwipeView = swipeView;
        mQuiz = quiz;
    }

    @Resolve
    public void onResolved() {
        question.setText(mProfile.getQuestion());
        answer.setText(mProfile.getAnswer());
        selectcategory();


    }
   // ArrayList<String> gelenliste = ((ExamsActivity)mContext).listedondur();
   @SwipeIn
   public void onSwipeIn() {

        if (activity.getCurrentAnswer().equalsIgnoreCase("Evet"))
        {
           activity.incEvetsayisi();
        }
        else {
                activity.artir_kalp_sayisi();
        }


       Log.d("EVET-IN", "onSwipedIn");

   }
    @SwipeOut
    private void onSwipedOut() {
       // Log.d("Liste Dondur", "onSwipedOut"+ ((ExamsActivity)mContext).listedondur());
       // Log.d("Sıra Dondur",": :"          +((ExamsActivity)mContext).cevapsiradonder());
       if (activity.getCurrentAnswer().equalsIgnoreCase("Hayır"))
        {
            activity.incHayirsayisi();
        }
        else {
            activity.artir_kalp_sayisi();
        }




        // mSwipeView.addView(this);

    }
    @SwipeInState
    private void onSwipeInState() {
        Log.d("EVET-IN-STATE", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState() {
        Log.d("HAYIR-OUT-STATE", "onSwipeOutState");


    }

    @SwipeCancelState
    private void onSwipeCancelState() {
        Log.d("HAYIR-CANCEL-STATE", "onSwipeCancelState");
    }





    public void selectcategory() {


        switch (mQuiz) {
            case 1:
                kategori_circle_image.setImageResource(R.drawable.tarihim);
                kategori_title.setText("Tarih");
                break;
            case 2:
                kategori_circle_image.setImageResource(R.drawable.bilim);
                kategori_title.setText("Bilim");
                break;
            case 3:
                kategori_circle_image.setImageResource(R.drawable.eglence);
                kategori_title.setText("Eğlence");
                break;
            case 4:
                kategori_circle_image.setImageResource(R.drawable.cografya);
                kategori_title.setText("Coğrafya");
                break;
            case 5:
                kategori_circle_image.setImageResource(R.drawable.sanat);
                kategori_title.setText("Sanat");
                break;
            case 6:
                kategori_circle_image.setImageResource(R.drawable.spor);
                kategori_title.setText("Spor");
                break;
        }

    }



}
