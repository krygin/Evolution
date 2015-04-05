package ru.tech_mail.evolution;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.tech_mail.evolution.content_provider.EvolutionDatabaseContract;

/**
 * Created by Ivan on 05.04.2015.
 */
public class EvolutionAdapter extends RecyclerView.Adapter<EvolutionAdapter.ViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    private OnItemClickListener mOnClickListener;

    public EvolutionAdapter(Context context, Cursor cursor) {
        mCursor = cursor;
        mContext = context;
    }

    @Override
    public EvolutionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_technology, parent, false);
        ViewHolder vh = new ViewHolder(v, mOnClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(EvolutionAdapter.ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        //holder.id = mCursor.getLong(mCursor.getColumnIndex(EvolutionDatabaseContract.Technologies._ID));
        holder.title.setText(mCursor.getString(mCursor.getColumnIndex(EvolutionDatabaseContract.Technologies.COLUMN_TITLE)));
        Picasso.with(mContext).load(mCursor.getString(mCursor.getColumnIndex(EvolutionDatabaseContract.Technologies.COLUMN_PICTURE))).into(holder.picture);
    }



    @Override
    public int getItemCount() {
        return mCursor!=null?mCursor.getCount():0;
    }

    public void swapCursor(Cursor data) {
        this.mCursor = data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnClickListener = listener;
    }

    public static interface OnItemClickListener {
        void onItemClick(int id);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView picture;
        private OnItemClickListener mOnItemClickListener;
        //public long id;
        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mOnItemClickListener = listener;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.text_view_title);
            picture = (ImageView) itemView.findViewById(R.id.image_view_picture);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(getAdapterPosition());
        }
    }

}
