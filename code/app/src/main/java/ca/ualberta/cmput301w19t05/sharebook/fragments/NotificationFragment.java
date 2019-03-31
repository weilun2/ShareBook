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

import java.util.ArrayList;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.models.Record;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;
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
        initRequestNotification(requestAdapter,(RecyclerView) getView().findViewById(R.id.requested_list), FirebaseHandler.REQUEST);
    }

    private void initRequestNotification(NotificationAdapter adapter, RecyclerView recyclerView,String notificationType) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NotificationAdapter(new ArrayList<Record>(),getContext());
        recyclerView.setAdapter(adapter);
        onlineDatabaseListener(adapter, notificationType);

    }

    private void onlineDatabaseListener(final NotificationAdapter adapter, String notificationType) {
        firebaseHandler.getMyRef().child(notificationType)
                .child(firebaseHandler.getCurrentUser().getUserID())
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Record temp = dataSnapshot.getValue(Record.class);
                if ((temp == null ? null:temp.getBorrowerName()) !=null){
                    if (!temp.isSeen()){
                        adapter.addRecord(temp);
                    }

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Record temp = dataSnapshot.getValue(Record.class);
                if ((temp == null ? null:temp.getBorrowerName()) !=null){
                    if (!temp.isSeen()){
                        adapter.removeRecord(temp);
                    }
                    else {
                        adapter.changeRecord(temp);
                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Record temp = dataSnapshot.getValue(Record.class);
                if ((temp == null ? null:temp.getBorrowerName()) !=null){
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
