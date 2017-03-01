package edu.uw.tcss450.nutrack.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.nutrack.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchRecipeTabFragment extends Fragment {


    public SearchRecipeTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_recipe_tab, container, false);



        return view;
    }

}
