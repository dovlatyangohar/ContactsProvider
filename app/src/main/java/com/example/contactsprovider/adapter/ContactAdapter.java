package com.example.contactsprovider.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsprovider.R;
import com.example.contactsprovider.model.ContactItem;

import java.util.List;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<ContactItem> contactItems;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ContactAdapter(List<ContactItem> contactItems, Context context) {
        this.contactItems = contactItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        ContactItem contactItem = contactItems.get(position);
        holder.tvContactName.setText(contactItem.getContactItemName());
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContactName;


        ViewHolder(final View itemView) {
            super(itemView);
            tvContactName = itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }
}

