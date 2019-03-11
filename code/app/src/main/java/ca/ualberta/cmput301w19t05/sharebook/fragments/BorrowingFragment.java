package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;
import ca.ualberta.cmput301w19t05.sharebook.tools.SearchBookAdapter;
/**
 * Fragment for borrowing book
 */
public final class BorrowingFragment extends Fragment {
    private EditText searchView;
    private SearchBookAdapter adapter;
    private FirebaseHandler firebaseHandler;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_borrowing, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firebaseHandler = new FirebaseHandler(getContext());

        initAdapter();

        initSearchview();
        onlineDatabaseListener(adapter, "available");


    }

    private void initAdapter() {
        adapter = new SearchBookAdapter(getActivity(), new ArrayList<Book>());
        adapter.setClickListener(new SearchBookAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });


        recyclerView = getView().findViewById(R.id.search_res);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initSearchview() {
        searchView = getView().findViewById(R.id.SearchBar);
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void onlineDatabaseListener(final SearchBookAdapter adapter, final String status) {

        DatabaseReference reference = firebaseHandler.getMyRef().child("books");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot it : dataSnapshot.getChildren()) {
                    Book temp = it.getValue(Book.class);
                    if (temp.getStatus().equals(status) && !temp.getOwner().getUserID().equals(firebaseHandler.getCurrentUser().getUserID())) {
                        adapter.addBook(temp);
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Book temp = dataSnapshot.getValue(Book.class);
                if (temp.getStatus().equals(status)) {
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
