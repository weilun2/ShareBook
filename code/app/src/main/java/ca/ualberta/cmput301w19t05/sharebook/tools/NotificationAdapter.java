package ca.ualberta.cmput301w19t05.sharebook.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.models.Data;

/**
 * NotificationAdapter
 * Tools for handling sending notification to specific user when an action of a book has taken
 *
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Data> mData;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapter.ItemClickListener mClickListener;
    private Context mContext;
    private FirebaseHandler firebaseHandler;

    public NotificationAdapter(List<Data> mRecord, Context mContext) {
        this.mData = mRecord;
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
    public void onBindViewHolder(@NonNull final NotificationAdapter.ViewHolder viewHolder, int i) {
        final Data temp = mData.get(i);
        if (FirebaseHandler.REQUEST.equals(temp.getRequestType())) {
            viewHolder.requesterName.setText(String.format("%s request %s", temp.getSenderName(), temp.getBookName()));
        }
        else if (FirebaseHandler.ACCEPT.equals(temp.getRequestType())) {

            viewHolder.requesterName.setText(String.format("your request of %s has been accepted", temp.getBookName()));
        }
        else if (FirebaseHandler.DECLINE.equals(temp.getRequestType())){
            viewHolder.requesterName.setText(String.format("your request of %s has been declined", temp.getBookName()));
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setClickListener(MyRecyclerViewAdapter.ItemClickListener itemClickListener) {

        this.mClickListener = itemClickListener;
    }

    public void addRecord(Data data){
        mData.add(0,data);
        notifyItemInserted(0);
    }

    public void removeRecord(Data temp) {
        int index = 0;
        for (Data it: mData){
            if (mData.get(index).equals(it)){
                notifyItemChanged(index);
                mData.remove(it);
                return;
            }
            else{
                index++;
            }
        }

    }

    public void changeRecord(Data temp) {
        int index = 0;
        for (Data it : mData) {
            if (it.equals(temp)) {
                mData.set(index, it);
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
    public Data getItem(int i){
        return mData.get(i);
    }
}
