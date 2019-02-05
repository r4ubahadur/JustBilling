package com.smi.justbilling.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.R;

import java.util.List;
import java.util.Objects;

import in.smi.ru.uttamlibrary.Uttam;


public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ImageViewHolder> implements View.OnClickListener,
        View.OnLongClickListener   {

    private Context mContext;
    private List<Uttam> mUpload;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;



    public TableAdapter(Context context, List<Uttam> upload) {
        mContext = context;
        mUpload = upload;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.table_view, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {

        holder.tableName.setSelected(true);

            holder.imageView3.setVisibility(View.GONE);
            holder.imageView2.setVisibility(View.VISIBLE);
            holder.tableName.setText("Table "+String.valueOf(position+1));
            holder.ll1.setVisibility(View.VISIBLE);
            holder.ll2.setVisibility(View.GONE);


            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("uttam").child("Table "+String.valueOf(position+1)).child("root");

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){
                        holder.running.setVisibility(View.VISIBLE);

                    }else {

                        holder.running.setVisibility(View.GONE);
                        holder.waiterName.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        final String waiterName = holder.waiterName.getText().toString();

        holder.table_share_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mContext instanceof TableAdapter.ShareTable1){

                    ((TableAdapter.ShareTable1)mContext).share_Table1("1", waiterName);


                }

            }
        });

        holder.table_share_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mContext instanceof TableAdapter.ShareTable2){
                    ((TableAdapter.ShareTable2)mContext).share_Table2("2", waiterName);
                }

            }
        });

        holder.table_share_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mContext instanceof TableAdapter.ShareTable3){
                    ((TableAdapter.ShareTable3)mContext).share_Table3("3", waiterName);
                }

            }
        });

        holder.table_share_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mContext instanceof TableAdapter.ShareTable4){
                    ((TableAdapter.ShareTable4)mContext).share_Table4("4", waiterName);
                }

            }
        });


















            DatabaseReference waiter = FirebaseDatabase.getInstance().getReference();

            waiter.child("uttam")
                    .child("waiter")
                    .child("Table "+String.valueOf(position+1))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()){

                                String waiterName = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                                holder.waiterName.setText(waiterName);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

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


        public TextView tableName, waiterName ;
        public RelativeLayout imageView2, imageView3;
        public LinearLayout ll1, ll2, running;
        public ImageView table_share_one, table_share_two, table_share_three, table_share_four;



        public ImageViewHolder(View itemView) {
            super(itemView);

            table_share_one = itemView.findViewById(R.id.table_share_one);
            table_share_two = itemView.findViewById(R.id.table_share_two);
            table_share_three = itemView.findViewById(R.id.table_share_three);
            table_share_four = itemView.findViewById(R.id.table_share_four);




            tableName = itemView.findViewById(R.id.tableName);
            waiterName = itemView.findViewById(R.id.waiterName);

            imageView2 = itemView.findViewById(R.id.imageView2);
            imageView3 = itemView.findViewById(R.id.imageView3);

            running = itemView.findViewById(R.id.running);

            ll1 = itemView.findViewById(R.id.ll1);
            ll2 = itemView.findViewById(R.id.ll2);



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


    public interface ShareTable1 {
        void share_Table1(String tableNo1, String wName);

    }

    public interface ShareTable2 {
        void share_Table2(String tableNo2, String wName);

    }

    public interface ShareTable3 {
        void share_Table3(String tableNo3, String wName);

    }

    public interface ShareTable4 {
        void share_Table4(String tableNo4, String wName);

    }






    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;
    }


    public void setOnItemLongClickListener(OnItemLongClickListener longListener) {

        mLongListener = longListener;
    }





}