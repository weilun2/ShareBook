package ca.ualberta.cmput301w19t05.sharebook.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.Record;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Record> mRecord;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context mContext;
    private FirebaseHandler firebaseHandler;

    public NotificationAdapter(List<Record> mRecord, Context mContext) {
        this.mRecord = mRecord;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        firebaseHandler = new FirebaseHandler(mContext);
    }


    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_notification, viewGroup, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder viewHolder, int i) {
        Record temp = mRecord.get(i);
        if (FirebaseHandler.REQUEST.equals(temp.getStatus())){
            viewHolder.requesterName.setText(temp.getBorrowerName()+" requested "+temp.getBookName());
        }
        else if (FirebaseHandler.ACCEPT.equals(temp.getStatus())){
            viewHolder.requesterName.setText("your request of "+temp.getBookName()+" has been accepted");
        }
    }

    @Override
    public int getItemCount() {
        return mRecord.size();
    }
    public void setClickListener(MyRecyclerViewAdapter.ItemClickListener itemClickListener) {

        this.mClickListener = itemClickListener;
    }

    public void addRecord(Record record){
        mRecord.add(0,record);
        notifyItemInserted(0);
    }

    public void removeRecord(Record temp) {
        int index = 0;
        for (Record it: mRecord){
            if (mRecord.get(index).equals(it)){
                notifyItemChanged(index);
                mRecord.remove(it);
                return;
            }
            else{
                index++;
            }
        }

    }

    public void changeRecord(Record temp) {
        int index = 0;
        for (Record it : mRecord) {
            if (it.equals(temp)) {
                mRecord.set(index, it);
                notifyItemChanged(index);
                return;
            } else {
                index++;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView requesterName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requesterName = itemView.findViewById(R.id.notification_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());

        }
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
