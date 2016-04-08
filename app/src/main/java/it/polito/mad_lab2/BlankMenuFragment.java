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
import android.view.ViewGroup;;import org.json.JSONObject;

public class BlankMenuFragment extends Fragment {
    private Oggetto_menu m_list;
    private Oggetto_piatto.type_enum type;
    private Context context;

    //solo per ora
    private JSONObject json_obj;

    public BlankMenuFragment() {
        // Required empty public constructor
    }

    public void setValue(Oggetto_menu obj, Oggetto_piatto.type_enum e, Context c, JSONObject jObj){
        this.m_list = obj;
        this.type = e;
        this.context = c;
        this.json_obj = jObj;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank_menu, container, false);

        RecyclerView rView = (RecyclerView) rootView.findViewById(R.id.recyclerView_menu);
        RecyclerAdapter_menu myAdapter = new RecyclerAdapter_menu(context, m_list, type, json_obj);
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
