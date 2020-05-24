package com.example.b10709007_hw2;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b10709007_hw2.data.WaitlistContract;

class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.GuestViewHolder> implements SharedPreferences.OnSharedPreferenceChangeListener{//因為觸發會改所以監聽
    public Cursor mCursor;
    private Context mContext;
    public GuestViewHolder holder;
    private Drawable mColor;

    public GuestListAdapter(Context context,Cursor cursor) {//傳過來的值
        this.mContext = context;
        this.mCursor = cursor;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//建立view辣
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.guest_list_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestViewHolder holder, int position) {
        this.holder=holder;
        if (!mCursor.moveToPosition(position))
            return;

        String name = mCursor.getString(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME));//取得
        int partySize = mCursor.getInt(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE));
        long id = mCursor.getLong(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry._ID));

        holder.nameTextView.setText(name);
        holder.partySizeTextView.setText(String.valueOf(partySize));


        holder.itemView.setTag(id);//如果不留他就會跳掉的唷QQ

        setupSharedPreferences();

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }



    public class GuestViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView partySizeTextView;

        public GuestViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            partySizeTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
        }
    }

    public void swapCursor(Cursor newCursor) {//刷新
        if (mCursor != null) mCursor.close();//會先close

        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();//刷新
        }
    }

    private void setupSharedPreferences() {//設置預先的顏色
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);//為何不建立QQ
        setColor(sharedPreferences.getString(mContext.getResources().getString(R.string.pref_color_key),mContext.getResources().getString(R.string.pref_color_value_red)));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);//記得要註冊監聽器！！！

    }

    private void setColor(String color){

        if (color.equals(mContext.getString(R.string.pref_color_value_red))) {
            mColor = ContextCompat.getDrawable(mContext, R.drawable.circle);
        } else if (color.equals(mContext.getString(R.string.pref_color_value_blue))) {
            mColor = ContextCompat.getDrawable(mContext, R.drawable.circle_blue);
        } else {
            mColor = ContextCompat.getDrawable(mContext, R.drawable.circle_green);
        }
        holder.partySizeTextView.setBackground(mColor);


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {//當監聽到改變
        if (key.equals(mContext.getResources().getString(R.string.pref_color_key))){
            setColor(sharedPreferences.getString(key,mContext.getResources().getString(R.string.pref_color_value_red)));//改背景
        }
        this.notifyDataSetChanged();//告訴adapter有改過

    }

}
