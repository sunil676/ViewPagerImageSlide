package com.droidrank.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import static com.droidrank.sample.R.id.imageView;

/**
 * Created by sunil on 16-Oct-16.
 */

class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    private List<ImageModel> mList;
    String imageUrl;

    public CustomPagerAdapter(Context context, List<ImageModel> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageViewView = (ImageView) itemView.findViewById(imageView);
        ImageModel model = mList.get(position);
        imageUrl = model.getImageUrl();
        if (imageUrl!= null && !imageUrl.isEmpty()){
            Utility.execute(new DownLoadTask(imageViewView, imageUrl));

        }


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    private class DownLoadTask extends AsyncTask<Void, Void,  Bitmap> {
        ImageView imageView;
        String imageUrl ;

       public DownLoadTask(ImageView imageView , String imageUrl){
           this.imageView = imageView;
           this.imageUrl = imageUrl;
       }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            try {
                // Connect to the web site
                 bitmap = Utility.getBitmapFromURL(imageUrl);



            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
           // mProgressBar.setVisibility(View.GONE);
            if (bitmap!= null){
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}