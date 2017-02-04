package edu.uw.tcss450.nutrack;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AvatorSelectorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AvatorSelectorFragment extends Fragment {

    private static final int MALE_AVATAR[] = {R.drawable.avatar_m0, R.drawable.avatar_m1
            , R.drawable.avatar_m2, R.drawable.avatar_m3
            , R.drawable.avatar_m4, R.drawable.avatar_m5
            , R.drawable.avatar_m6, R.drawable.avatar_m7
            , R.drawable.avatar_m8, R.drawable.avatar_m9};

    private static final int MOVE_SPEED = 300;

    private static final int FADING_SPEED = 300;

    private OnFragmentInteractionListener mListener;

    private Float myPositionLeftX;

    private Float myPositionCenterX;

    private Float myPositionRightX;

    private Float mySmallerPositionY;

    private Float myBiggerPositionY;

    private Float mySideAvatarDim;

    private Float myCenterAvatarDim;

    private Button myLeftImage;

    private Button myCenterImage;

    private Button myRightImage;

    private Button mySpareImage;

    private int myLeftImageId;

    private int myCenterImageId;

    private int myRightImageId;

    private int mySpareImageId;

    private int myAvatarImageIndex[];

    private AlphaAnimation fadeInAnimation;

    private AlphaAnimation fadeOutAnimation;

    public AvatorSelectorFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_avator_selector, container, false);

        //Setup the initial index location of avatar image.
        myAvatarImageIndex = new int[3];
        myAvatarImageIndex[0] = 3;
        myAvatarImageIndex[1] = 4;
        myAvatarImageIndex[2] = 5;

        //Setup fadeIn and fadeOut animation.
        fadeInAnimation = new AlphaAnimation(0.5f, 1.0f);
        fadeInAnimation.setDuration(FADING_SPEED);
        fadeInAnimation.setFillAfter(true);

        fadeOutAnimation = new AlphaAnimation(1.0f, 0.5f);
        fadeOutAnimation.setDuration(FADING_SPEED);
        fadeOutAnimation.setFillAfter(true);

        //Get screen pixel
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        myLeftImageId = R.id.avator_0;
        myCenterImageId = R.id.avator_1;
        myRightImageId = R.id.avator_2;
        mySpareImageId = R.id.avatar_3;


        mySideAvatarDim = metrics.widthPixels * 0.15f;
        myCenterAvatarDim = metrics.widthPixels * 0.3f;
        myPositionLeftX = metrics.widthPixels * 0.20f - mySideAvatarDim * 0.5f;
        myPositionCenterX = metrics.widthPixels * 0.5f - mySideAvatarDim * 0.5f;
        myPositionRightX = metrics.widthPixels * 0.80f - mySideAvatarDim * 0.5f;
        mySmallerPositionY = mySideAvatarDim * 0.5f;

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mySideAvatarDim.intValue(), mySideAvatarDim.intValue());
        lp.setMargins(0, (myCenterAvatarDim.intValue() - mySideAvatarDim.intValue()) / 2, 0, (myCenterAvatarDim.intValue() - mySideAvatarDim.intValue()) / 2);

        myLeftImage = (Button) view.findViewById(R.id.avator_0);
        myLeftImage.setLayoutParams(lp);
        myLeftImage.setX(myPositionLeftX);
        myLeftImage.setY(0);
        myLeftImage.setBackgroundResource(MALE_AVATAR[3]);

        myCenterImage = (Button) view.findViewById(R.id.avator_1);
        myCenterImage.setLayoutParams(lp);

        ObjectAnimator CenterAnim = ObjectAnimator.ofFloat(myCenterImage, "scaleX", 2f);
        CenterAnim.setDuration(0).start();
        CenterAnim = ObjectAnimator.ofFloat(myCenterImage, "scaleY", 2f);
        CenterAnim.setDuration(0).start();

        myCenterImage.setX(myPositionCenterX);
        myCenterImage.setY(0);

        myCenterImage.setBackgroundResource(MALE_AVATAR[4]);

        myRightImage = (Button) view.findViewById(R.id.avator_2);
        myRightImage.setLayoutParams(lp);
        myRightImage.setX(myPositionRightX);
        myRightImage.setY(0);

        myRightImage.setBackgroundResource(MALE_AVATAR[5]);

        mySpareImage = (Button) view.findViewById(R.id.avatar_3);
        mySpareImage.setLayoutParams(lp);
        mySpareImage.setVisibility(View.GONE);

        view.invalidate();
        myLeftImage.setClickable(true);
        myLeftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveRight();
            }
        });

        myRightImage.setClickable(true);
        myRightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLeft();
            }
        });

        return view;
    }


    public void moveLeft() {
        //Id rotate left
        int tempId = myLeftImageId;
        myLeftImageId = myCenterImageId;
        myCenterImageId = myRightImageId;
        myRightImageId = mySpareImageId;
        mySpareImageId = tempId;

        myAvatarImageIndex[0]++;
        myAvatarImageIndex[1]++;
        myAvatarImageIndex[2]++;

        //
        ObjectAnimator sideLeftAnim = ObjectAnimator.ofFloat(myLeftImage, "translationX", -1 * mySideAvatarDim);
        sideLeftAnim.setDuration(MOVE_SPEED).start();
        sideLeftAnim = ObjectAnimator.ofFloat(myLeftImage, "scaleX", 0.5f);
        sideLeftAnim.setDuration(MOVE_SPEED).start();
        sideLeftAnim = ObjectAnimator.ofFloat(myLeftImage, "scaleY", 0.5f);
        sideLeftAnim.setDuration(MOVE_SPEED).start();

        //Center Avatar
        ObjectAnimator centerAnim = ObjectAnimator.ofFloat(myCenterImage, "translationX", myPositionLeftX);
        centerAnim.setDuration(MOVE_SPEED).start();
        centerAnim = ObjectAnimator.ofFloat(myCenterImage, "scaleX", 1f);
        centerAnim.setDuration(MOVE_SPEED).start();
        centerAnim = ObjectAnimator.ofFloat(myCenterImage, "scaleY", 1f);
        centerAnim.setDuration(MOVE_SPEED).start();


        //Right Avatar
        ObjectAnimator sideRightAnim = ObjectAnimator.ofFloat(myRightImage, "translationX", myPositionCenterX);
        sideRightAnim.setDuration(MOVE_SPEED).start();
        sideRightAnim = ObjectAnimator.ofFloat(myRightImage, "scaleX", 2f);
        sideRightAnim.setDuration(MOVE_SPEED).start();
        sideRightAnim = ObjectAnimator.ofFloat(myRightImage, "scaleY", 2f);
        sideRightAnim.setDuration(MOVE_SPEED).start();

        //Spare Avatar
        mySpareImage.setX(1440);

        if (myAvatarImageIndex[2] == 10) {
            mySpareImage.setVisibility(View.GONE);
        } else {
            mySpareImage.setVisibility(View.VISIBLE);
            mySpareImage.setBackgroundResource(MALE_AVATAR[myAvatarImageIndex[2]]);
        }

        ObjectAnimator spareAnim = ObjectAnimator.ofFloat(mySpareImage, "translationX", myPositionRightX);
        spareAnim.setDuration(MOVE_SPEED).start();
        //************************************************************************
        spareAnim = ObjectAnimator.ofFloat(mySpareImage, "scaleX", 0.5f);
        spareAnim.setDuration(0).start();
        spareAnim = ObjectAnimator.ofFloat(mySpareImage, "scaleY", 0.5f);
        spareAnim.setDuration(0).start();
        //************************************************************************
        spareAnim = ObjectAnimator.ofFloat(mySpareImage, "scaleX", 1f);
        spareAnim.setDuration(MOVE_SPEED).start();
        spareAnim = ObjectAnimator.ofFloat(mySpareImage, "scaleY", 1f);
        spareAnim.setDuration(MOVE_SPEED).start();


        //After assigning events
        myLeftImage = (Button) getView().findViewById(myLeftImageId);
        myCenterImage = (Button) getView().findViewById(myCenterImageId);
        myRightImage = (Button) getView().findViewById(myRightImageId);
        mySpareImage = (Button) getView().findViewById(mySpareImageId);

        //Disable clickable if it reaches the edge
        myLeftImage.setClickable(true);
        myLeftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveRight();

                getView().invalidate();
            }
        });

        myCenterImage.setClickable(false);

        if (myAvatarImageIndex[2] == 10) {
            myRightImage.setClickable(false);
        } else {
            myRightImage.setClickable(true);
            myRightImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveLeft();

                    getView().invalidate();
                }
            });
        }


    }

    public void moveRight() {
        //Id rotate Right
        int tempId = myRightImageId;
        myRightImageId = myCenterImageId;
        myCenterImageId = myLeftImageId;
        myLeftImageId = mySpareImageId;
        mySpareImageId = tempId;

        myAvatarImageIndex[0]--;
        myAvatarImageIndex[1]--;
        myAvatarImageIndex[2]--;

        //Spare Avatar
        mySpareImage.setX(-1 * mySideAvatarDim);
        mySpareImage.setVisibility(View.VISIBLE);

        if (myAvatarImageIndex[0] == -1) {
            mySpareImage.setVisibility(View.GONE);
        } else {
            mySpareImage.setBackgroundResource(MALE_AVATAR[myAvatarImageIndex[0]]);
        }

        ObjectAnimator spareAnim = ObjectAnimator.ofFloat(mySpareImage, "translationX", myPositionLeftX);
        spareAnim.setDuration(MOVE_SPEED).start();
        //************************************************************************
        spareAnim = ObjectAnimator.ofFloat(mySpareImage, "scaleX", 0.5f);
        spareAnim.setDuration(0).start();
        spareAnim = ObjectAnimator.ofFloat(mySpareImage, "scaleY", 0.5f);
        spareAnim.setDuration(0).start();
        //************************************************************************
        spareAnim = ObjectAnimator.ofFloat(mySpareImage, "scaleX", 1f);
        spareAnim.setDuration(MOVE_SPEED).start();
        spareAnim = ObjectAnimator.ofFloat(mySpareImage, "scaleY", 1f);
        spareAnim.setDuration(MOVE_SPEED).start();

        //Left Avatar
        ObjectAnimator sideLeftAnim = ObjectAnimator.ofFloat(myLeftImage, "translationX", myPositionCenterX);
        sideLeftAnim.setDuration(MOVE_SPEED).start();
        sideLeftAnim = ObjectAnimator.ofFloat(myLeftImage, "scaleX", 2f);
        sideLeftAnim.setDuration(MOVE_SPEED).start();
        sideLeftAnim = ObjectAnimator.ofFloat(myLeftImage, "scaleY", 2f);
        sideLeftAnim.setDuration(MOVE_SPEED).start();

        //Center Avatar
        ObjectAnimator centerAnim = ObjectAnimator.ofFloat(myCenterImage, "translationX", myPositionRightX);
        centerAnim.setDuration(MOVE_SPEED).start();
        centerAnim = ObjectAnimator.ofFloat(myCenterImage, "scaleX", 1f);
        centerAnim.setDuration(MOVE_SPEED).start();
        centerAnim = ObjectAnimator.ofFloat(myCenterImage, "scaleY", 1f);
        centerAnim.setDuration(MOVE_SPEED).start();

        //Right Avatar
        ObjectAnimator sideRightAnim = ObjectAnimator.ofFloat(myRightImage, "translationX", 1440);
        sideRightAnim.setDuration(MOVE_SPEED).start();
        centerAnim = ObjectAnimator.ofFloat(myRightImage, "scaleX", 0.5f);
        centerAnim.setDuration(MOVE_SPEED).start();
        centerAnim = ObjectAnimator.ofFloat(myRightImage, "scaleY", 0.5f);
        centerAnim.setDuration(MOVE_SPEED).start();


        //After assigning events
        myLeftImage = (Button) getView().findViewById(myLeftImageId);
        myCenterImage = (Button) getView().findViewById(myCenterImageId);
        myRightImage = (Button) getView().findViewById(myRightImageId);
        mySpareImage = (Button) getView().findViewById(mySpareImageId);

        if (myAvatarImageIndex[0] == -1) {
            myLeftImage.setClickable(false);
        } else {
            myLeftImage.setClickable(true);
            myLeftImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveRight();

                    getView().invalidate();
                }
            });
        }


        myCenterImage.setClickable(false);


        //Disable clickable if it reaches the edge
        myRightImage.setClickable(true);
        myRightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLeft();

                getView().invalidate();
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
