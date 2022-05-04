package com.jukshio.cameraxlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PreviewActicity extends androidx.appcompat.widget.AppCompatTextView {
    TextView textView;
    View view;
    Context mycontext;
    private final int REQUEST_CODE = 1001;
    private String title;
    private boolean style;

    private final String[] Requires_Permission = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    public PreviewActicity(@NonNull Context context) {
        super(context);
    }

    public PreviewActicity(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PreviewActicity(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(12);
        canvas.drawLine(100, 100, 300, 300, paint);
    }
}
