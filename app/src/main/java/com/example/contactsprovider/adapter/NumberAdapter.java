package com.example.contactsprovider.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.contactsprovider.R;

import java.util.ArrayList;

public class NumberAdapter extends BaseAdapter {
    private Context context;
    ArrayList<String> numberList;
    int callImg;
    int messageImg;
    LayoutInflater inflater;


    public NumberAdapter(Context context, ArrayList<String> numberList, int callImg, int messageImg) {
        this.context = context;
        this.numberList = numberList;
        this.callImg = callImg;
        this.messageImg = messageImg;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return numberList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.number_list_item, null);
        TextView number = convertView.findViewById(R.id.tv_number);
        ImageView callIcon = convertView.findViewById(R.id.call_img);
        ImageView messageIcon = convertView.findViewById(R.id.message_img);
        number.setText(numberList.get(position));
        callIcon.setImageResource(callImg);
        messageIcon.setImageResource(messageImg);
        return convertView;
    }
}
