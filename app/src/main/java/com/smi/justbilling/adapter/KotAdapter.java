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

public class KotAdapter extends RecyclerView.Adapter<KotAdapter.ImageViewHolder> implements View.OnClickListener,
        View.OnLongClickListener   {

    private Context mContext;
    private List<Uttam> mUpload;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;



    public KotAdapter(Context context, List<Uttam> upload) {
        mContext = context;
        mUpload = upload;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.abc_kot_single_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        Uttam upload = mUpload.get(position);
        holder.textViewName.setText(upload.getName());

        String count = upload.getCount();




            if (count.equals("")){

                holder.textViewCount.setText("1 Each");
            }else {

                holder.textViewCount.setText(upload.getCount()+" Each");
            }










        holder.textViewName.setSelected(true);



       // if(position == getItemCount() - 1){ holder.textViewName.setVisibility(View.INVISIBLE); }



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




    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {


        public TextView textViewName, textViewCount;



        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.item_name);
            textViewCount = itemView.findViewById(R.id.item_quantity);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View v) {

            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClickKot(position);
                }
            }

        }

        @Override
        public boolean onLongClick(View v) {

            if (mLongListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mLongListener.onItemLongClick(position);
                }
            }


            return true;
        }


    }

    public interface OnItemClickListener {
        void onItemClickKot(int position);


    }

    public interface OnItemLongClickListener {

        void onItemLongClick(int position);
    }





    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;
    }


    public void setOnItemLongClickListener(OnItemLongClickListener longListener) {

        mLongListener = longListener;
    }





}