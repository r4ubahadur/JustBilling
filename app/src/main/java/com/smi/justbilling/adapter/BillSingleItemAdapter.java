package com.smi.justbilling.adapter;

import android.annotation.SuppressLint;
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

public class BillSingleItemAdapter extends RecyclerView.Adapter<BillSingleItemAdapter.ImageViewHolder> implements View.OnClickListener,
        View.OnLongClickListener   {

    private Context mContext;
    private List<Uttam> mUpload;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;



    public BillSingleItemAdapter(Context context, List<Uttam> upload) {
        mContext = context;
        mUpload = upload;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_bill_item, parent, false);
        return new ImageViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        Uttam upload = mUpload.get(position);

        if (upload.getName().equals("")){
            holder.textViewName.setText(String.valueOf(position+1)+mContext.getString(R.string.no_name));
        }else {
            holder.textViewName.setText(String.valueOf(position+1)+". "+upload.getName());
        }


        if (upload.getPrice3().equals("")){
            holder.price.setText("0.00");
        }else {
            holder.price.setText(upload.getPrice3());
        }


        if (upload.getPrice2().equals("")){
            holder.totalPrice.setText("0.00");
        }else {

            String total = upload.getPrice2();

            double t = Double.parseDouble(total);

            @SuppressLint("DefaultLocale")
            String price2 = String.format("%.2f", t);

            holder.totalPrice.setText(price2);
        }



        String tax = upload.getTax();

        try {

            if (tax.equals("")||tax.equals("0")){

                holder.tax.setText("0.00");
            }else {
                double gst = Double.parseDouble(tax);
                double c_s = gst/2;
                String mGST = String.format("%.1f", c_s);
                holder.tax.setText("CGST @"+mGST+"%" +"\n"+ "SGST @"+mGST+"%");
            }

        }catch (Exception e){

            holder.tax.setText("CGST @"+"0.0"+"%" +"\n"+ "SGST @"+"0.0"+"%");

        }






        if (upload.getCount().equals("")){
            holder.quantity.setText("1");
        }else {
            holder.quantity.setText(upload.getCount());
        }





        holder.textViewName.setSelected(true);


     //   if(position == getItemCount() - 1){ holder.viewId.setVisibility(View.INVISIBLE); }



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


        public TextView textViewName, price, tax, totalPrice, quantity;
        public ImageView imageView;
        public View viewId;



        public ImageViewHolder(View itemView) {
            super(itemView);

            totalPrice = itemView.findViewById(R.id.bill_single_item_total_price);
            textViewName = itemView.findViewById(R.id.bill_single_item_name);
            quantity = itemView.findViewById(R.id.bill_single_item_quantity);
            price = itemView.findViewById(R.id.bill_single_item_price);
            tax = itemView.findViewById(R.id.bill_single_item_tax);






            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View v) {

            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
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
        void onItemClick(int position);


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