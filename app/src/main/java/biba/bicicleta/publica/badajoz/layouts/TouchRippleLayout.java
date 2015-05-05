package biba.bicicleta.publica.badajoz.layouts;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class TouchRippleLayout extends LinearLayout {
    private int color =  Color.rgb(245, 110, 119);
    private PointF touch;
    private Paint paint = new Paint();

    public TouchRippleLayout(Context context) {
        super(context);
    }

    public TouchRippleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public TouchRippleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TouchRippleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touch = new PointF(event.getX(), event.getY());
            Animation animation = new ValueAnimation(10, Math.max(getWidth(), getHeight()));
            animation.setDuration(500);
            startAnimation(animation);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Animation animation = getAnimation();
        if (animation != null && animation instanceof ValueAnimation && !animation.hasEnded()) {
            ValueAnimation valueAnimation = (ValueAnimation) animation;
            float size = valueAnimation.getValue();
            paint.setColor(color);
            paint.setAlpha((int) (127 * (1 - valueAnimation.getInterpolation())));
            canvas.drawCircle(touch.x, touch.y, size, paint);
        }
        super.dispatchDraw(canvas);
    }

}