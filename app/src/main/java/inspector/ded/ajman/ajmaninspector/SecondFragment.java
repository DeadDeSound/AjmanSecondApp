package inspector.ded.ajman.ajmaninspector;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SecondFragment extends Fragment {

    public static AppBarLayout appBar;
    public static Toolbar toolbar;

    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        MainActivity.toolbar.setVisibility(View.VISIBLE);
//        MainActivity.appBar.setVisibility(View.VISIBLE);
        return inflater.inflate(R.layout.fragment_second_fragmnet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        appBar = (AppBarLayout) getView().findViewById(R.id.app_bar);
//        getActivity().setSupportActionBar(toolbar);
    }
}
