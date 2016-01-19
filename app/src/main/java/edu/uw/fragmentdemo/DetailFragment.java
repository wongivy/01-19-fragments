package edu.uw.fragmentdemo;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.fragmentdemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Get the bundle that was set in the MovieActivity
        Bundle bundle = getArguments();

        //Get the views
        TextView titleView = (TextView)rootView.findViewById(R.id.txtMovieTitle);
        TextView imdbView = (TextView)rootView.findViewById(R.id.txtMovieIMDB);

        //Get the values from the bundle and set the text for the views
        titleView.setText(bundle.getString("title"));
        imdbView.setText(bundle.getString("imbd"));

        return rootView;
    }

}
