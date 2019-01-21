package com.smi.justbilling.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smi.justbilling.R;

import java.util.List;

import in.smi.ru.uttamlibrary.Uttam;

public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.ImageViewHolder> implements View.OnClickListener,
        View.OnLongClickListener   {

    private Context mContext;
    private List<Uttam> mUpload;
    private OnItemClickListenerSub mListener;



    public SubItemAdapter(Context context, List<Uttam> upload) {
        mContext = context;
        mUpload = upload;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.abc_sub_cat_single_item_for_view, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        Uttam upload = mUpload.get(position);
        holder.textViewName.setText(String.valueOf(position+1)+". "+upload.getName());
        holder.textViewPrice.setText("₹ "+upload.getPrice());


        holder.textViewName.setSelected(true);
        holder.textViewPrice.setSelected(true);






    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }




    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView textViewName, textViewPrice;


        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.sub_item_name);
            textViewPrice = itemView.findViewById(R.id.sub_item_price);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClickSub(position);
                }
            }

        }


    }

    public interface OnItemClickListenerSub {
        void onItemClickSub(int position);


    }

    public interface OnItemLongClickListener {

        void onItemLongClick(int position);
    }





    public void setOnItemClickListener(OnItemClickListenerSub listener) {

        mListener = listener;
    }








}