package edu.uw.tcss450.nutrack;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AvatarSelectorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AvatarSelectorFragment extends Fragment {

    private static final int MALE_AVATAR[] = {R.drawable.avatar_m0, R.drawable.avatar_m1
            , R.drawable.avatar_m2, R.drawable.avatar_m3
            , R.drawable.avatar_m4, R.drawable.avatar_m5
            , R.drawable.avatar_m6, R.drawable.avatar_m7
            , R.drawable.avatar_m8, R.drawable.avatar_m9};

    private static final int FEMALE_AVATAR[] = {R.drawable.avatar_f0, R.drawable.avatar_f1
            , R.drawable.avatar_f2, R.drawable.avatar_f3
            , R.drawable.avatar_f4, R.drawable.avatar_f5
            , R.drawable.avatar_f6, R.drawable.avatar_f7
            , R.drawable.avatar_f8, R.drawable.avatar_f9};

    private static final int MOVE_SPEED = 300;

    private static final int FADING_SPEED = 500;

    public static final int MALE = 10;

    public static final int FEMALE = 11;

    private OnFragmentInteractionListener mListener;

    private int mAvatarList[];

    private Float mPositionLeftX;

    private Float mPositionCenterX;

    private Float mPositionRightX;

    private Float mSmallerPositionY;

    private Float mBiggerPositionY;

    private Float mSideAvatarDim;

    private Float mCenterAvatarDim;

    private ImageView mLeftImage;

    private ImageView mCenterImage;

    private ImageView mRightImage;

    private ImageView mSpareImage;

    private int mLeftImageId;

    private int mCenterImageId;

    private int mRightImageId;

    private int mSpareImageId;

    private int mAvatarImageIndex[];

    private AlphaAnimation mFadeInAnimation;

    private AlphaAnimation mFadeOutAnimation;

    public AvatarSelectorFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_avator_selector, container, false);

        //Default gender is male.
        mAvatarList = MALE_AVATAR;


        //Setup the initial index location of avatar image.
        mAvatarImageIndex = new int[3];
        mAvatarImageIndex[0] = 3;
        mAvatarImageIndex[1] = 4;
        mAvatarImageIndex[2] = 5;



        //Setup fadeIn and fadeOut animation.
        mFadeInAnimation = new AlphaAnimation(0.5f, 1.0f);
        mFadeInAnimation.setDuration(FADING_SPEED);
        mFadeInAnimation.setFillAfter(true);

        mFadeOutAnimation = new AlphaAnimation(1.0f, 0.5f);
        mFadeOutAnimation.setDuration(FADING_SPEED);
        mFadeOutAnimation.setFillAfter(true);

        //Get screen pixel
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mLeftImageId = R.id.avator_0;
        mCenterImageId = R.id.avator_1;
        mRightImageId = R.id.avator_2;
        mSpareImageId = R.id.avatar_3;


        mSideAvatarDim = metrics.widthPixels * 0.15f;
        mCenterAvatarDim = metrics.widthPixels * 0.3f;
        mPositionLeftX = metrics.widthPixels * 0.20f - mSideAvatarDim * 0.5f;
        mPositionCenterX = metrics.widthPixels * 0.5f - mSideAvatarDim * 0.5f;
        mPositionRightX = metrics.widthPixels * 0.80f - mSideAvatarDim * 0.5f;
        mSmallerPositionY = mSideAvatarDim * 0.5f;

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mSideAvatarDim.intValue(), mSideAvatarDim.intValue());
        lp.setMargins(0, (mCenterAvatarDim.intValue() - mSideAvatarDim.intValue()) / 2, 0, (mCenterAvatarDim.intValue() - mSideAvatarDim.intValue()) / 2);

        mLeftImage = (ImageView) view.findViewById(R.id.avator_0);
        mLeftImage.setLayoutParams(lp);
        mLeftImage.setX(mPositionLeftX);
        mLeftImage.setY(0);
        mLeftImage.setImageResource(mAvatarList[3]);

        mCenterImage = (ImageView) view.findViewById(R.id.avator_1);
        mCenterImage.setLayoutParams(lp);

        ObjectAnimator CenterAnim = ObjectAnimator.ofFloat(mCenterImage, "scaleX", 2f);
        CenterAnim.setDuration(0).start();
        CenterAnim = ObjectAnimator.ofFloat(mCenterImage, "scaleY", 2f);
        CenterAnim.setDuration(0).start();

        mCenterImage.setX(mPositionCenterX);
        mCenterImage.setY(0);

        mCenterImage.setImageResource(mAvatarList[4]);

        mRightImage = (ImageView) view.findViewById(R.id.avator_2);
        mRightImage.setLayoutParams(lp);
        mRightImage.setX(mPositionRightX);
        mRightImage.setY(0);

        mRightImage.setImageResource(mAvatarList[5]);

        mSpareImage = (ImageView) view.findViewById(R.id.avatar_3);
        mSpareImage.setLayoutParams(lp);
        mSpareImage.setVisibility(View.GONE);

        view.invalidate();
        mLeftImage.setClickable(true);
        mLeftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveRight();
            }
        });

        mRightImage.setClickable(true);
        mRightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLeft();
            }
        });

        return view;
    }


    public void moveLeft() {
        //Id rotate left
        int tempId = mLeftImageId;
        mLeftImageId = mCenterImageId;
        mCenterImageId = mRightImageId;
        mRightImageId = mSpareImageId;
        mSpareImageId = tempId;

        mAvatarImageIndex[0]++;
        mAvatarImageIndex[1]++;
        mAvatarImageIndex[2]++;

        //Left Avatar
        ObjectAnimator sideLeftAnim = ObjectAnimator.ofFloat(mLeftImage, "translationX", -1 * mSideAvatarDim);
        sideLeftAnim.setDuration(MOVE_SPEED).start();
        sideLeftAnim = ObjectAnimator.ofFloat(mLeftImage, "scaleX", 0.5f);
        sideLeftAnim.setDuration(MOVE_SPEED).start();
        sideLeftAnim = ObjectAnimator.ofFloat(mLeftImage, "scaleY", 0.5f);
        sideLeftAnim.setDuration(MOVE_SPEED).start();

        //Center Avatar
        ObjectAnimator centerAnim = ObjectAnimator.ofFloat(mCenterImage, "translationX", mPositionLeftX);
        centerAnim.setDuration(MOVE_SPEED).start();
        centerAnim = ObjectAnimator.ofFloat(mCenterImage, "scaleX", 1f);
        centerAnim.setDuration(MOVE_SPEED).start();
        centerAnim = ObjectAnimator.ofFloat(mCenterImage, "scaleY", 1f);
        centerAnim.setDuration(MOVE_SPEED).start();


        //Right Avatar
        ObjectAnimator sideRightAnim = ObjectAnimator.ofFloat(mRightImage, "translationX", mPositionCenterX);
        sideRightAnim.setDuration(MOVE_SPEED).start();
        sideRightAnim = ObjectAnimator.ofFloat(mRightImage, "scaleX", 2f);
        sideRightAnim.setDuration(MOVE_SPEED).start();
        sideRightAnim = ObjectAnimator.ofFloat(mRightImage, "scaleY", 2f);
        sideRightAnim.setDuration(MOVE_SPEED).start();

        //Spare Avatar
        mSpareImage.setX(1440);

        if (mAvatarImageIndex[2] == 10) {
            mSpareImage.setVisibility(View.GONE);
        } else {
            mSpareImage.setVisibility(View.VISIBLE);
            mSpareImage.setImageResource(mAvatarList[mAvatarImageIndex[2]]);
        }

        ObjectAnimator spareAnim = ObjectAnimator.ofFloat(mSpareImage, "translationX", mPositionRightX);
        spareAnim.setDuration(MOVE_SPEED).start();
        //************************************************************************
        spareAnim = ObjectAnimator.ofFloat(mSpareImage, "scaleX", 0.5f);
        spareAnim.setDuration(0).start();
        spareAnim = ObjectAnimator.ofFloat(mSpareImage, "scaleY", 0.5f);
        spareAnim.setDuration(0).start();
        //************************************************************************
        spareAnim = ObjectAnimator.ofFloat(mSpareImage, "scaleX", 1f);
        spareAnim.setDuration(MOVE_SPEED).start();
        spareAnim = ObjectAnimator.ofFloat(mSpareImage, "scaleY", 1f);
        spareAnim.setDuration(MOVE_SPEED).start();


        //After assigning events
        mLeftImage = (ImageView) getView().findViewById(mLeftImageId);
        mCenterImage = (ImageView) getView().findViewById(mCenterImageId);
        mRightImage = (ImageView) getView().findViewById(mRightImageId);
        mSpareImage = (ImageView) getView().findViewById(mSpareImageId);

        //Disable clickable if it reaches the edge
        mLeftImage.setClickable(true);
        mLeftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveRight();

                getView().invalidate();
            }
        });

        mCenterImage.setClickable(false);

        if (mAvatarImageIndex[2] == 10) {
            mRightImage.setClickable(false);
        } else {
            mRightImage.setClickable(true);
            mRightImage.setOnClickListener(new View.OnClickListener() {
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
        int tempId = mRightImageId;
        mRightImageId = mCenterImageId;
        mCenterImageId = mLeftImageId;
        mLeftImageId = mSpareImageId;
        mSpareImageId = tempId;

        mAvatarImageIndex[0]--;
        mAvatarImageIndex[1]--;
        mAvatarImageIndex[2]--;

        //Spare Avatar
        mSpareImage.setX(-1 * mSideAvatarDim);
        mSpareImage.setVisibility(View.VISIBLE);

        if (mAvatarImageIndex[0] == -1) {
            mSpareImage.setVisibility(View.GONE);
        } else {
            mSpareImage.setImageResource(mAvatarList[mAvatarImageIndex[0]]);
        }

        ObjectAnimator spareAnim = ObjectAnimator.ofFloat(mSpareImage, "translationX", mPositionLeftX);
        spareAnim.setDuration(MOVE_SPEED).start();
        //************************************************************************
        spareAnim = ObjectAnimator.ofFloat(mSpareImage, "scaleX", 0.5f);
        spareAnim.setDuration(0).start();
        spareAnim = ObjectAnimator.ofFloat(mSpareImage, "scaleY", 0.5f);
        spareAnim.setDuration(0).start();
        //************************************************************************
        spareAnim = ObjectAnimator.ofFloat(mSpareImage, "scaleX", 1f);
        spareAnim.setDuration(MOVE_SPEED).start();
        spareAnim = ObjectAnimator.ofFloat(mSpareImage, "scaleY", 1f);
        spareAnim.setDuration(MOVE_SPEED).start();

        //Left Avatar
        ObjectAnimator sideLeftAnim = ObjectAnimator.ofFloat(mLeftImage, "translationX", mPositionCenterX);
        sideLeftAnim.setDuration(MOVE_SPEED).start();
        sideLeftAnim = ObjectAnimator.ofFloat(mLeftImage, "scaleX", 2f);
        sideLeftAnim.setDuration(MOVE_SPEED).start();
        sideLeftAnim = ObjectAnimator.ofFloat(mLeftImage, "scaleY", 2f);
        sideLeftAnim.setDuration(MOVE_SPEED).start();

        //Center Avatar
        ObjectAnimator centerAnim = ObjectAnimator.ofFloat(mCenterImage, "translationX", mPositionRightX);
        centerAnim.setDuration(MOVE_SPEED).start();
        centerAnim = ObjectAnimator.ofFloat(mCenterImage, "scaleX", 1f);
        centerAnim.setDuration(MOVE_SPEED).start();
        centerAnim = ObjectAnimator.ofFloat(mCenterImage, "scaleY", 1f);
        centerAnim.setDuration(MOVE_SPEED).start();

        //Right Avatar
        ObjectAnimator sideRightAnim = ObjectAnimator.ofFloat(mRightImage, "translationX", 1440);
        sideRightAnim.setDuration(MOVE_SPEED).start();
        centerAnim = ObjectAnimator.ofFloat(mRightImage, "scaleX", 0.5f);
        centerAnim.setDuration(MOVE_SPEED).start();
        centerAnim = ObjectAnimator.ofFloat(mRightImage, "scaleY", 0.5f);
        centerAnim.setDuration(MOVE_SPEED).start();


        //After assigning events
        mLeftImage = (ImageView) getView().findViewById(mLeftImageId);
        mCenterImage = (ImageView) getView().findViewById(mCenterImageId);
        mRightImage = (ImageView) getView().findViewById(mRightImageId);
        mSpareImage = (ImageView) getView().findViewById(mSpareImageId);

        if (mAvatarImageIndex[0] == -1) {
            mLeftImage.setClickable(false);
        } else {
            mLeftImage.setClickable(true);
            mLeftImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveRight();

                    getView().invalidate();
                }
            });
        }


        mCenterImage.setClickable(false);


        //Disable clickable if it reaches the edge
        mRightImage.setClickable(true);
        mRightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLeft();

                getView().invalidate();
            }
        });

    }

    public void changeAvatarGender(final int theGender, final Button theButton) {
        int leftIndex = mAvatarImageIndex[0];
        int rightIndex = mAvatarImageIndex[2];
        mAvatarImageIndex[0] = 3;
        mAvatarImageIndex[1] = 4;
        mAvatarImageIndex[2] = 5;

        if (theGender == MALE) {
            mAvatarList = MALE_AVATAR;

        } else {
            mAvatarList = FEMALE_AVATAR;
        }


        //Left Avatar
        final AlphaAnimation leftAnim;
        if (leftIndex == -1) {
            leftAnim = new AlphaAnimation(0, 0);
        } else {
            leftAnim = new AlphaAnimation(1, 0);
        }
        leftAnim.setDuration(FADING_SPEED);
        leftAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mLeftImage.setVisibility(View.VISIBLE);
                mLeftImage.setImageResource(mAvatarList[mAvatarImageIndex[0]]);
                animation = new AlphaAnimation(0, 1);
                animation.setDuration(FADING_SPEED);
                mLeftImage.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        mLeftImage.startAnimation(leftAnim);

        final AlphaAnimation centerAnim = new AlphaAnimation(1, 0);
        centerAnim.setDuration(FADING_SPEED);
        centerAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mCenterImage.setImageResource(mAvatarList[mAvatarImageIndex[1]]);
                animation = new AlphaAnimation(0, 1);
                animation.setDuration(FADING_SPEED);
                mCenterImage.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        mCenterImage.startAnimation(centerAnim);

        final AlphaAnimation rightAnim;
        if (rightIndex == 10) {
            rightAnim = new AlphaAnimation(0, 0);
        } else {
            rightAnim = new AlphaAnimation(1, 0);
        }
        rightAnim.setDuration(FADING_SPEED);
        rightAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mRightImage.setVisibility(View.VISIBLE);
                mRightImage.setImageResource(mAvatarList[mAvatarImageIndex[2]]);
                animation = new AlphaAnimation(0, 1);
                animation.setDuration(FADING_SPEED);
                mRightImage.startAnimation(animation);

                theButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        mRightImage.startAnimation(rightAnim);
    }

    public int getChosen() {
        return mAvatarList[mAvatarImageIndex[1]];
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
        void onFragmentInteraction(Uri uri);
    }
}
