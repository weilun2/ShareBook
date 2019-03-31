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
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.User;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;
import ca.ualberta.cmput301w19t05.sharebook.tools.RequestAdapter;

public final class RequestListFragment extends Fragment {

    private RequestAdapter adapter;
    private FirebaseHandler firebaseHandler;
    private RecyclerView recyclerView;
    private Book book;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firebaseHandler = new FirebaseHandler(getContext());
        if (getArguments() != null) {
            book = getArguments().getParcelable("book");
        }

        initAdapter();
        onlineDatabaseListener(adapter);;


    }

    private void initAdapter() {
        adapter = new RequestAdapter(getActivity(), new ArrayList<User>(), book);
        adapter.setClickListener(new RequestAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        recyclerView = getView().findViewById(R.id.requests_of_book);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void onlineDatabaseListener(final RequestAdapter adapter) {

        DatabaseReference reference = firebaseHandler.getMyRef().child(Book.REQUESTED);
        final Book book = adapter.getBook();
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (book.getBookId().equals(dataSnapshot.getKey())){
                    for (DataSnapshot it : dataSnapshot.getChildren()) {
//                    if (it.getKey().equals(book.getBookId())) {
                        User temp = it.getValue(User.class);
                        adapter.addUser(temp);

//                    }
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                User temp = dataSnapshot.getValue(User.class);

                if (temp != null) {
                    if (book.getBookId().equals(dataSnapshot.getKey())){
                        adapter.addUser(temp);
                    }

                }
                adapter.removeUser(temp);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                User temp = dataSnapshot.getValue(User.class);
                adapter.removeUser(temp);
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
