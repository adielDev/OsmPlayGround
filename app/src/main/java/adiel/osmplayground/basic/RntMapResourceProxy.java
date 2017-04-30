package adiel.osmplayground.basic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import org.osmdroid.DefaultResourceProxyImpl;

import adiel.osmplayground.R;

/**
 * Created by Yonatan Reiss on 12/01/2016.
 */
public class RntMapResourceProxy extends DefaultResourceProxyImpl {

    private final Context mContext;
    private boolean isRecording = false;
    public RntMapResourceProxy(Context pContext) {
        super(pContext);
        mContext = pContext;
    }

    public void setIsRecording(boolean isRecording)
    {
        this.isRecording = isRecording;
    }

    @Override
    public Bitmap getBitmap(final bitmap pResId) {
        if(isRecording) {
            switch (pResId) {
                case person:
                    return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_map_me_red);
                case direction_arrow:
                    return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_map_me_red);
            }
        }
        else
        {
            switch (pResId) {
                case person:
                    return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_map_me_gray);
                case direction_arrow:
                    return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_map_me_gray);
            }
        }
        return super.getBitmap(pResId);
    }

    @Override
    public Drawable getDrawable(final bitmap pResId) {
        switch (pResId){
            case person:
                return ContextCompat.getDrawable(mContext, R.drawable.icon_map_me_gray);
            case direction_arrow:
                return ContextCompat.getDrawable(mContext, R.drawable.icon_map_me_red);
        }
        return super.getDrawable(pResId);
    }
}