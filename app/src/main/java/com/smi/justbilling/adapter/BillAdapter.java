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

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ImageViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<Uttam> mUpload;
    private OnItemClickListener mListener;



    public BillAdapter(Context context, List<Uttam> upload) {
        mContext = context;
        mUpload = upload;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.per_item2, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        Uttam upload = mUpload.get(position);

        holder.textViewName.setText(upload.getName());
        holder.textViewPrice.setText(upload.getPrice());

        holder.rankPosition.setText(String.valueOf(position+1)+".");

        holder.textViewName.setSelected(true);



    }

    @Override
    public int getItemCount() {
        return mUpload.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName, textViewPrice, rankPosition;
        public ImageView imageView;



        public ImageViewHolder(View itemView) {
            super(itemView);
            rankPosition = itemView.findViewById(R.id.rankPosition);
            textViewName = itemView.findViewById(R.id.abcText);
            textViewPrice = itemView.findViewById(R.id.billItemPrice);



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
    }

    public interface OnItemClickListener {
        void onItemClick(int position);


    }
    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;
    }

}