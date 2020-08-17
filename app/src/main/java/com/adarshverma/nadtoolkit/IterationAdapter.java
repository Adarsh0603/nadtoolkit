package com.adarshverma.nadtoolkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IterationAdapter extends ArrayAdapter<Iteration> {

    boolean mChangeConstructor;

    IterationAdapter(@NonNull Context context, ArrayList<Iteration> iterations, boolean changeConstructor) {
        super(context, 0, iterations);
        mChangeConstructor = changeConstructor;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.iteration_item, parent, false);
        }


        Iteration currentIteration = getItem(position);

        TextView iterationTextView = (TextView) listItemView.findViewById(R.id.iteration_text_view);

        iterationTextView.setText(Integer.toString(currentIteration.getIteration()));


        TextView aValueTextView = (TextView) listItemView.findViewById(R.id.a_value_text_view);
        aValueTextView.setText(Double.toString(currentIteration.getaValue()));

        TextView bValueTextView = (TextView) listItemView.findViewById(R.id.b_value_text_view);
        bValueTextView.setText(Double.toString(currentIteration.getbValue()));

        TextView midValueTextView = (TextView) listItemView.findViewById(R.id.mid_value_text_view);
        midValueTextView.setText(Double.toString(currentIteration.getMidValue()));


        TextView fMidValueTextView = (TextView) listItemView.findViewById(R.id.f_mid_value_text_view);
        fMidValueTextView.setText(Double.toString(currentIteration.getfMidValue()));
        if (!mChangeConstructor) {
            bValueTextView.setVisibility(View.GONE);
            fMidValueTextView.setVisibility(View.GONE);
        } else {
            bValueTextView.setVisibility(View.VISIBLE);
            fMidValueTextView.setVisibility(View.VISIBLE);
        }


        return listItemView;
    }
}
