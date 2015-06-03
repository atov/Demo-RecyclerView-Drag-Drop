package demorecyclers.android.icecode.com.demorecyclers;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by AntonioTorres on 02/06/15.
 */
public class AdapterSimple extends RecyclerView.Adapter<AdapterSimple.ViewHolder> {

    private List<MainActivity.DemoData> data = new ArrayList<>();
    private Context mContext;
    private GestureDetectorCompat mDetector;
    private ItemViewOnGestureListener listener = null;

    public AdapterSimple(Context context , List<MainActivity.DemoData> dataIn){
        this.data = dataIn;
        this.mContext = context;

        this.listener = new ItemViewOnGestureListener();

        this.mDetector = new GestureDetectorCompat(mContext, listener);
        this.mDetector.setOnDoubleTapListener(listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View convertView = inflater.inflate(R.layout.listitem_simpleimg, parent, false);

        //convertView.setOnTouchListener(new TouchListener());
        convertView.setTag(data.get(i));

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        int imgRsc = data.get(i).resource;

        Picasso.with(mContext)
                .load(imgRsc)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                        //.resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                //.centerInside()
                .fit()
                .into(viewHolder.img);


    }


    class TouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            //Timber.w("--Item Touch en algo!!! view: "+view);
            int action = MotionEventCompat.getActionMasked(motionEvent);

            //Vamos a preguntar si scroll o fliing o onDown fueron activados
            /*if ( mDetector.onTouchEvent(motionEvent) && (motionEvent.getAction() == MotionEvent.ACTION_DOWN)){
                Timber.e("Sucedio un evento que nos interesaba.. ");
                MainActivity.showEvent(action, "Adapter ");
            }else if (motionEvent.getAction() != MotionEvent.A){
                Timber.v("Sucedio todo lo demas... ");
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            }*/
            mDetector.onTouchEvent(motionEvent);
            return false;
        }
    }

    /*if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        view.startDrag(data, shadowBuilder, view, 0);
        view.setVisibility(View.INVISIBLE);
        return true;
    } else {
        return false;
    }*/

    private class ItemViewOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        /*public void onShowPress(MotionEvent motionEvent) {
            Timber.w("RecyclerViewOnGestureListener::onShowPress");
        }*/

        @Override
        public boolean onDown(MotionEvent e) {
            //super.onDown(e);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //super.onScroll(e1, e2, distanceX, distanceY);

            return true;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView img;

        public ViewHolder(View itemView){
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.img);
        }
    }

}
