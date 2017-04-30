package adiel.osmplayground.basic;

/**
 * Created by dov on 11/11/15.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

/**
 * Created by dov on 30/08/15.
 */
public class RotationGestureDetector extends Overlay {
    private static final int INVALID_POINTER_ID = -1;
    private float fX, fY, sX, sY;
    private int ptrID1, ptrID2;
    private float mAngle;
    boolean rotated = false;
    PointF anchor = new PointF();

    private OnRotationGestureListener mListener;

    public float getAngle() {
        return mAngle;
    }

    public RotationGestureDetector(OnRotationGestureListener listener, Context context){
        super(context);
        mListener = listener;
        ptrID1 = INVALID_POINTER_ID;
        ptrID2 = INVALID_POINTER_ID;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView){
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                ptrID1 = event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(event.findPointerIndex(ptrID1) != -1) {
                    ptrID2 = event.getPointerId(event.getActionIndex());
                    sX = event.getX(event.findPointerIndex(ptrID1));
                    sY = event.getY(event.findPointerIndex(ptrID1));
                    fX = event.getX(event.findPointerIndex(ptrID2));
                    fY = event.getY(event.findPointerIndex(ptrID2));
                    anchor.set(Math.abs(sX -fX), Math.abs(sY - fY));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID){
                    float nfX, nfY, nsX, nsY;
                    nsX = event.getX(event.findPointerIndex(ptrID1));
                    nsY = event.getY(event.findPointerIndex(ptrID1));
                    nfX = event.getX(event.findPointerIndex(ptrID2));
                    nfY = event.getY(event.findPointerIndex(ptrID2));

                    mAngle = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY);
                    if (mListener != null) {
                        sX = nsX;
                        sY = nsY;
                        fX = nfX;
                        fY = nfY;
                        mListener.OnRotation(mAngle, anchor);
                        rotated = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                ptrID1 = INVALID_POINTER_ID;
                if(rotated) {
                    mListener.OnRotationEnd();
                    rotated = false;
                    if(ptrID2 != INVALID_POINTER_ID)
                        return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                ptrID2 = INVALID_POINTER_ID;
                if(ptrID1 != INVALID_POINTER_ID)
                    return true;
            case MotionEvent.ACTION_CANCEL:
                ptrID1 = INVALID_POINTER_ID;
                ptrID2 = INVALID_POINTER_ID;
                return true;
        }
        return false;
    }

    private float angleBetweenLines (float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY)
    {
        float angle1 = (float) Math.atan2( (fY - sY), (fX - sX) );
        float angle2 = (float) Math.atan2( (nfY - nsY), (nfX - nsX) );

        float angle = ((float)Math.toDegrees(angle1 - angle2)) % 360;
        if (angle < -180.f) angle += 360.0f;
        if (angle > 180.f) angle -= 360.0f;
        return -angle;
    }

    @Override
    protected void draw(Canvas canvas, MapView mapView, boolean b) {
    }

    public interface OnRotationGestureListener {
        void OnRotation(float absAngle, PointF anchor);
        void OnRotationEnd();
    }

}