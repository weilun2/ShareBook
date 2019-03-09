package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ca.ualberta.cmput301w19t05.sharebook.FirebaseHandler;
import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.customizedWidgets.MyRecyclerViewAdapter;

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
        firebaseHandler = new FirebaseHandler(getContext());

        RecyclerView recyclerView = getView().findViewById(R.id.available_list);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

        ArrayList<String> bookNames = new ArrayList<>();
        bookNames.add("first");
        bookNames.add("second");
        generateImageFromText("first");
        generateImageFromText("second");


        adapter = new MyRecyclerViewAdapter(getActivity(), bookNames);
        adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private void generateImageFromText(String text) {

        //getResources();
        Bitmap src = Bitmap.createBitmap(200, 80, Bitmap.Config.RGB_565);
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cs = new Canvas(dest);
        Paint paint = new Paint();
        paint.setTextSize(35);
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
