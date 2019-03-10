package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

import ca.ualberta.cmput301w19t05.sharebook.AddBookActivity;
import ca.ualberta.cmput301w19t05.sharebook.Book;
import ca.ualberta.cmput301w19t05.sharebook.BookDetailActivity;
import ca.ualberta.cmput301w19t05.sharebook.FirebaseHandler;
import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.customizedWidgets.MyRecyclerViewAdapter;

import static android.support.constraint.Constraints.TAG;

public final class MyShelfFragment extends Fragment {
    private MyRecyclerViewAdapter adapter;
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
        initRecyclerView();

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = getView().findViewById(R.id.available_list);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

        adapter = new MyRecyclerViewAdapter(getActivity(), new ArrayList<Book>());
        adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "setClickListener: clicked");

                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra("book", adapter.getItem(position));
                startActivity(intent);

            }
        });

        recyclerView.setAdapter(adapter);
        onlineDatabaseListener();


    }


    private void onlineDatabaseListener() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("books").child(firebaseHandler.getCurrentUser().getUserID());

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Book temp = dataSnapshot.getValue(Book.class);
                adapter.addBook(temp);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Book temp = dataSnapshot.getValue(Book.class);
                adapter.changeBook(temp);
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

    private void generateImageFromText(String text) {

        //getResources();
        Bitmap src = Bitmap.createBitmap(200, 80, Bitmap.Config.RGB_565);
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cs = new Canvas(dest);
        Paint paint = new Paint();
        paint.setTextSize(10);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        cs.drawBitmap(src, 0f, 0f, null);
        float height = paint.measureText("yY");
        float width = paint.measureText(text);
        float x_coord = (src.getWidth() - width) / 2;
        cs.drawText(text, x_coord, height + 15f, paint);

        firebaseHandler.uploadImage(text, dest);
    }


}
