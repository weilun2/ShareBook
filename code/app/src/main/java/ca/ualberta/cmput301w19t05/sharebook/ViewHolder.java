package ca.ualberta.cmput301w19t05.sharebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder{

    View mView;

    public ViewHolder (View itemView){
        super(itemView);

        mView = itemView;
    }

    //set details to recycler view row
    public void setDetails(Context ctx, String title, String image){
        //Views
        TextView mTitleTv = mView.findViewById(R.id.rTitleTv);
        ImageView mImageIv = mView.findViewById(R.id.rImageView);

        mTitleTv.setText(title);
        Picasso.get().load(image).into(mImageIv);




    }




}
