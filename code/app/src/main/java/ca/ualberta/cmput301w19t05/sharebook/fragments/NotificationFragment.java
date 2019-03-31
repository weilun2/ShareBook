package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.models.Data;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;
import ca.ualberta.cmput301w19t05.sharebook.tools.MyRecyclerViewAdapter;
import ca.ualberta.cmput301w19t05.sharebook.tools.NotificationAdapter;

/**
 * Fragment for notification
 */
public final class NotificationFragment extends Fragment {
    private FirebaseHandler firebaseHandler;
    private NotificationAdapter requestAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firebaseHandler = new FirebaseHandler(getContext());
        initRequestNotification(requestAdapter,(RecyclerView) getView().findViewById(R.id.requested_list));
    }

    private void initRequestNotification(NotificationAdapter adapter, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NotificationAdapter(new ArrayList<Data>(),getContext());
        final NotificationAdapter finalAdapter = adapter;
        adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Query query= firebaseHandler.getMyRef()
                        .child(finalAdapter.getItem(position).getRequestType())
                        .child(firebaseHandler.getCurrentUser().getUserID())
                        .orderByChild("bookName")
                        .equalTo(finalAdapter.getItem(position).getBookId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        recyclerView.setAdapter(adapter);
        onlineDatabaseListener(adapter);
    }

    private void onlineDatabaseListener(final NotificationAdapter adapter) {
        firebaseHandler.getMyRef().child(FirebaseHandler.REQUEST)
                .child(firebaseHandler.getCurrentUser().getUserID())
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Data temp = dataSnapshot.getValue(Data.class);
                if ((temp == null ? null:temp.getSender()) !=null){

                        adapter.addRecord(temp);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Data temp = dataSnapshot.getValue(Data.class);
                if ((temp == null ? null:temp.getSender()) !=null){

                        adapter.removeRecord(temp);

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Data temp = dataSnapshot.getValue(Data.class);
                if ((temp == null ? null:temp.getSender()) !=null){
                    adapter.removeRecord(temp);
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        firebaseHandler.getMyRef().child(FirebaseHandler.ACCEPT)
                .child(firebaseHandler.getCurrentUser().getUserID())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        Data temp = dataSnapshot.getValue(Data.class);
                        if ((temp == null ? null:temp.getSender()) !=null){

                            adapter.addRecord(temp);

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Data temp = dataSnapshot.getValue(Data.class);
                        if ((temp == null ? null:temp.getSender()) !=null){

                            adapter.removeRecord(temp);

                        }

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Data temp = dataSnapshot.getValue(Data.class);
                        if ((temp == null ? null:temp.getSender()) !=null){
                            adapter.removeRecord(temp);
                        }

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        firebaseHandler.getMyRef().child(FirebaseHandler.DECLINE)
                .child(firebaseHandler.getCurrentUser().getUserID())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        Data temp = dataSnapshot.getValue(Data.class);
                        if ((temp == null ? null:temp.getSender()) !=null){

                            adapter.addRecord(temp);

                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Data temp = dataSnapshot.getValue(Data.class);
                        if ((temp == null ? null:temp.getSender()) !=null){

                            adapter.removeRecord(temp);

                        }

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Data temp = dataSnapshot.getValue(Data.class);
                        if ((temp == null ? null:temp.getSender()) !=null){
                            adapter.removeRecord(temp);
                        }

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
