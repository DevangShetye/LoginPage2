package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment{


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_second, container, false);
        //Button limitamt=(Button)view.findViewById(R.id.FirstChangebtn);
        //TextView totalexpense=(TextView)view.findViewById(R.id.SecondChangebtn);
        //Button expensebtn=(Button)view.findViewById(R.id.viewexpense);
        //Button GotoExpense=(Button)view.findViewById(R.id.goto_expense);
        //Button btnOpen= (Button) view.findViewById(R.id.camera_button_open);
        View updateLimit=(View)view.findViewById(R.id.updateLimits);
        //TextView totalexpense=(TextView)view.findViewById(R.id.TotalExpense);
        View addExpenses=(View) view.findViewById(R.id.add_expenses);
        View uploadBills= (View) view.findViewById(R.id.upload_Bills_tofirebase);
        View billsExtraction=(View)view.findViewById(R.id.bills_extracted_text);
        View suggestion=(View)view.findViewById(R.id.add_suggestions);

        suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity(),SuggestionActivity.class);
                in.putExtra("some","Sugguestion");
                startActivity(in);
            }
        });

        uploadBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity(),UploadImagesActivity.class);
                in.putExtra("some","Upload Images");
                startActivity(in);
            }
        });
        addExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getActivity(),StatisticsActivity.class);
                in.putExtra("abc","Add Expense");
                startActivity(in);
            }
        });

        updateLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),LimitActivity.class);
                intent.putExtra("abs","Set Limit");
                startActivity(intent);
            }
        });

        billsExtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ExtractedTextActivity.class);
                startActivity(intent);

            }
        });
        return view;
    }
}


