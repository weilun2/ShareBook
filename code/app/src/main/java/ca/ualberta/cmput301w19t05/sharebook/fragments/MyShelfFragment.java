package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.activities.AddBookActivity;
import ca.ualberta.cmput301w19t05.sharebook.activities.BookDetailActivity;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;
import ca.ualberta.cmput301w19t05.sharebook.tools.MyRecyclerViewAdapter;

import static android.support.constraint.Constraints.TAG;
/**
 * Fragment for owner's book shelf
 */
public final class MyShelfFragment extends Fragment {
    private MyRecyclerViewAdapter availableAdapter;
    private MyRecyclerViewAdapter requestedAdapter;
    private MyRecyclerViewAdapter acceptedAdapter;
    private MyRecyclerViewAdapter borrowedAdapter;
    private FirebaseHandler firebaseHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myshelf, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton submit = getActivity().findViewById(R.id.add_book);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddBookActivity.class);
                startActivity(intent);
            }
        });
        firebaseHandler = new FirebaseHandler(getContext());
        initAdapter(availableAdapter, "available");
        initAdapter(requestedAdapter, "requested");
        initAdapter(acceptedAdapter, "accepted");
        initAdapter(borrowedAdapter, "borrowed");

    }


    private void initAdapter(MyRecyclerViewAdapter adapter, String status) {
        RecyclerView recyclerView;
        switch (status) {
            case "borrowed":
                recyclerView = getView().findViewById(R.id.borrowed_list);
                break;
            case "requested":
                recyclerView = getView().findViewById(R.id.requested_list);
                break;
            case "accepted":
                recyclerView = getView().findViewById(R.id.accepted_list);
                break;
            default:

                recyclerView = getView().findViewById(R.id.available_list);
                break;
        }

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

        adapter = new MyRecyclerViewAdapter(getActivity(), new ArrayList<Book>());
        final MyRecyclerViewAdapter finalAdapter = adapter;
        adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "setClickListener: clicked");

                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra(BookDetailActivity.BOOK, finalAdapter.getItem(position));
                intent.putExtra(BookDetailActivity.FUNCTION,BookDetailActivity.DELETE);
                startActivity(intent);

            }
        });

        recyclerView.setAdapter(adapter);
        onlineDatabaseListener(adapter, status);


    }


    private void onlineDatabaseListener(final MyRecyclerViewAdapter adapter, final String status) {

        DatabaseReference reference = firebaseHandler.getMyRef().child("books").child(firebaseHandler.getCurrentUser().getUserID());

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book temp = dataSnapshot.getValue(Book.class);
                if (temp != null && status.equals(temp.getStatus())) {
                    adapter.addBook(temp);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Book temp = dataSnapshot.getValue(Book.class);
                if (temp != null) {
                    if (status.equals(temp.getStatus())) {
                        if (adapter.contains(temp)) {
                            adapter.changeBook(temp);
                        } else {
                            adapter.addBook(temp);
                        }
                    } else {
                        if (adapter.contains(temp)) {
                            adapter.removeBook(temp);
                        }
                    }
                }
                //adapter.changeBook(temp);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Book temp = dataSnapshot.getValue(Book.class);
                adapter.removeBook(temp);
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
