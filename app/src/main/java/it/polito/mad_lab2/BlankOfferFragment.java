package it.polito.mad_lab2;

/**
 * Created by Eugenio on 07/04/2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BlankOfferFragment extends Fragment {
    private ArrayList<Oggetto_offerta> offer_list;
    private Context context;
    private boolean mode;

    //TODO utilizzare setArgument nel fragment invece del costruttore con passaggio di paramentri

    public BlankOfferFragment(boolean mode) {
        // Required empty public constructor
        this.mode=mode;
    }

    public void setValue(ArrayList<Oggetto_offerta> obj,  Context c){
        this.offer_list= obj;
        this.context = c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank_offer, container, false);
        
        RecyclerView rView = (RecyclerView) rootView.findViewById(R.id.recyclerView_offerte);
        RecyclerAdapter_offerte myAdapter = new RecyclerAdapter_offerte(context,offer_list, mode);
        if(rView != null) {
            rView.setAdapter(myAdapter);

            LinearLayoutManager myLLM_vertical = new LinearLayoutManager(getActivity());
            myLLM_vertical.setOrientation(LinearLayoutManager.VERTICAL);
            rView.setLayoutManager(myLLM_vertical);

            rView.setItemAnimator(new DefaultItemAnimator());
        }

        return rootView;
    }

}
