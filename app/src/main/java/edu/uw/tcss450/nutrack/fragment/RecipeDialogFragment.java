package edu.uw.tcss450.nutrack.fragment;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.uw.tcss450.nutrack.R;
import edu.uw.tcss450.nutrack.database.DBDailyLog;
import edu.uw.tcss450.nutrack.model.Food;
import edu.uw.tcss450.nutrack.model.Recipe;

import static edu.uw.tcss450.nutrack.R.id.imageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View mView;

    private Recipe mRecipe;


    public RecipeDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDialogFragment newInstance(String param1, String param2) {
        RecipeDialogFragment fragment = new RecipeDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable("recipe_info");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_recipe_dialog, container, false);

        TextView dialogTitle = (TextView) mView.findViewById(R.id.dialog_recipe_title);
        TextView caloriesResult = (TextView) mView.findViewById(R.id.calories_recipe_result);
        TextView fatResult = (TextView) mView.findViewById(R.id.fat_recipe_result);
        TextView carbsResult = (TextView) mView.findViewById(R.id.carbs_recipe_result);
        TextView proteinResult = (TextView) mView.findViewById(R.id.protein_recipe_result);

        dialogTitle.setText(mRecipe.getName());
        caloriesResult.setText(String.valueOf(mRecipe.getCalorie().get(0)));
        fatResult.setText(String.valueOf(mRecipe.getFat().get(0)));
        carbsResult.setText(String.valueOf(mRecipe.getCarbs().get(0)));
        proteinResult.setText(String.valueOf(mRecipe.getProtein().get(0)));

        ImageView recipeImage = (ImageView) mView.findViewById(R.id.recipeDialog_imageView);
        if (mRecipe.getImageURL() != null && mRecipe.getImageURL() != " ") {
            Picasso.with(getContext()).load(mRecipe.getImageURL()).into(recipeImage);
        }


        // Buttons action
        Button addButton = (Button) mView.findViewById(R.id.dialog_add_button);
        Button cancelButton = (Button) mView.findViewById(R.id.dialog_cancel_button);

        /*
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBDailyLog db = new DBDailyLog(getContext());
                db.insertFood(mRecipe.getName(), mRecipe.getId(), "recipe", "breakfast");
            }
        });
*/
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return mView;
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
        void onFragmentInteraction(Recipe theRecipe);
    }
}
