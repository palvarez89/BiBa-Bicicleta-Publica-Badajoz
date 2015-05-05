package biba.bicicleta.publica.badajoz.layouts;


import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ValueAnimation extends Animation {
    private final float from;
    private final float to;
    private float value;
    private float interpolation;
    public ValueAnimation(float from, float to) {
        this.from = from;
        this.to = to;
    }
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        value = from * (1 - interpolatedTime) + to * interpolatedTime;
        interpolation = interpolatedTime;
        super.applyTransformation(interpolatedTime, t);
    }

    public float getValue() {
        return value;
    }

    public float getInterpolation() {
        return interpolation;
    }
}
