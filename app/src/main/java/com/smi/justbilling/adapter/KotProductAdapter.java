package com.smi.justbilling.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smi.justbilling.R;

import java.util.List;

import in.smi.ru.uttamlibrary.Uttam;

public class KotProductAdapter extends RecyclerView.Adapter<KotProductAdapter.ImageViewHolder> implements View.OnClickListener,
        View.OnLongClickListener   {

    private Context mContext;
    private List<Uttam> mUpload;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;



    public KotProductAdapter(Context context, List<Uttam> upload) {
        mContext = context;
        mUpload = upload;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_item_product_kot, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        Uttam upload = mUpload.get(position);
        holder.textViewName.setText(upload.getName());
        holder.textViewPrice.setText("₹ "+upload.getPrice2());

        String count = upload.getCount();

        if (count.equals("")){

            holder.textViewCount.setText("1 Each");
        }else {

            holder.textViewCount.setText(upload.getCount()+" Each");
        }



        /*

        Picasso.with(mContext)
                .load(upload.getThumbImageUrl())
                .placeholder(R.drawable.purchase_icon)
                .fit()
                .centerCrop()
               // .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

        */

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


        public TextView textViewName, textViewPrice, textViewCount;
        public ImageView imageView;



        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            textViewCount = itemView.findViewById(R.id.kot_single_count);


            imageView = itemView.findViewById(R.id.image_view_upload);

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