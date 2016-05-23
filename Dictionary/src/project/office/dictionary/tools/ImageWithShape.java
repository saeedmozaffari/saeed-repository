package project.office.dictionary.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;


public class ImageWithShape extends ImageView {

    private Paint        paint;
    private int          bigCircle = Color.parseColor("#00695c");
    private int          viewWidth, viewHeight;
    private Bitmap       image;
    private BitmapShader shader;


    public ImageWithShape(Context context) {
        super(context);
        initialize(context);
    }


    public ImageWithShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }


    public ImageWithShape(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }


    private void initialize(Context context) {
        paint = new Paint();
        paint.setColor(bigCircle);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
    }


    private void loadBitmap() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();

        if (bitmapDrawable != null)
            image = bitmapDrawable.getBitmap();
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        loadBitmap();

        if (image != null) {
            shader = new BitmapShader(Bitmap.createScaledBitmap(image, canvas.getWidth(), canvas.getHeight(), false), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
            paint.setShader(shader);

            RectF rectF = new RectF(0, 0, viewWidth, viewHeight);
            canvas.drawRoundRect(rectF, 10f, 10f, paint);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        viewWidth = width;
        viewHeight = height;

        setMeasuredDimension(width, height);
    }


    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = viewWidth;
        }

        return result;
    }


    private int measureHeight(int measureSpecHeight) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = viewHeight;
        }

        return (result + 2);
    }

}