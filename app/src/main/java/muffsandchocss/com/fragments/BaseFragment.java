package muffsandchocss.com.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import muffsandchocss.com.adapters.AndroidDataAdapter;
import muffsandchocss.com.listeners.RecyclerItemClickListener;
import muffsandchocss.com.mandc.R;
import muffsandchocss.com.utility.AndroidVersion;


public class BaseFragment extends Fragment {

    private final String recyclerViewTitleText[] = {"Choclates @.Rs 12", "Muffins", "Subways", "Sandwiches", "Brunch"
    };

    private final int recyclerViewImages[] = {
            R.drawable.choloate_image,R.drawable.mandc, R.drawable.mandc, R.drawable.mandc, R.drawable.mandc
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View fragmentView = inflater.inflate(R.layout.fragment_base, container, false);
        super.onCreate(savedInstanceState);
        initRecyclerViews(fragmentView);
        return fragmentView;
    }


    private void initRecyclerViews(View fragmentView) {
        RecyclerView mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<AndroidVersion> av = prepareData();
        AndroidDataAdapter mAdapter = new AndroidDataAdapter(getActivity(), av);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        Fragment fragment=null;
                        switch (i) {
                            case 0:
//                                //Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
//                                fragment = new PlaceOrderFragment();
//
//                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                                fragmentTransaction.replace(R.id.content_frame,fragment);
//                                fragmentTransaction.addToBackStack("home").commit();


                                break;
                            case 1:
                                //Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                Toast.makeText(view.getContext(), "Coming soon..", Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                //Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                Toast.makeText(view.getContext(), "Coming soon..", Toast.LENGTH_LONG).show();
                                break;
                            case 3:
                                //Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                Toast.makeText(view.getContext(), "Coming soon..", Toast.LENGTH_LONG).show();
                                break;
                            case 4:
                                //Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                Toast.makeText(view.getContext(), "Coming soon..", Toast.LENGTH_LONG).show();
                                break;
                            case 5:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 6:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 7:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 8:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 9:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 10:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 11:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                })
        );

    }

    private ArrayList<AndroidVersion> prepareData() {

        ArrayList<AndroidVersion> av = new ArrayList<>();
        for (int i = 0; i < recyclerViewTitleText.length; i++) {
            AndroidVersion mAndroidVersion = new AndroidVersion();
            mAndroidVersion.setAndroidVersionName(recyclerViewTitleText[i]);
            mAndroidVersion.setrecyclerViewImage(recyclerViewImages[i]);
            av.add(mAndroidVersion);
        }
        return av;
    }

}
