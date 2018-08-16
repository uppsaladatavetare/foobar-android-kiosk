package nu.datavetenskap.foobarkiosk.Elements;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import nu.datavetenskap.foobarkiosk.R;


public class PurchaseButton extends LinearLayout {
    CharSequence textAttribute;
    CharSequence imageAttribute;

    @BindView(R.id.purchase_button_background) LinearLayout _frame;
    @BindView(R.id.purchase_button_img) ImageView _img;
    @BindView(R.id.purchase_button_txt) TextView _txt;


    public PurchaseButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // Adding the layout defined in the xml to my view
        inflate(context, R.layout.purchase_button_layout, this);


        ButterKnife.bind(this);


        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.PurchaseButton);

        textAttribute = a.getString(R.styleable.PurchaseButton_android_text);
        imageAttribute = a.getString(R.styleable.PurchaseButton_image_src);

        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (textAttribute != null) {
            _txt.setText(textAttribute.toString());
        }

        //_img.setImageDrawable(Drawable.createFromPath(img.toString()));
        //_img.setBackgroundResource(R.drawable.ic_credit_card_black_72dp);
        if (imageAttribute != null) {
            //Resources.getSystem().getIdentifier(img.toString(), "drawable", getPackageName())
            try {
                Field idField = R.drawable.class.getDeclaredField(imageAttribute.toString());
                _img.setBackgroundResource(idField.getInt(idField));
                //_img.setBackground(Drawable.createFromPath(imageAttribute.toString()));
                //_img.setImageDrawable(Drawable.createFromPath(imageAttribute.toString()));
            } catch (Exception ignore) {}
        }
    }

    public void setText(String txt) {
        this._txt.setText(txt);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        _frame.setEnabled(enabled);
        _img.setEnabled(enabled);
    }

    //    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        // Maximal length of line is width of this View
//        //int w = this.getLayoutParams().width;
//        int w = MeasureSpec.getSize(widthMeasureSpec);
//        int h = this.getLayoutParams().height;
//
//        this.setMeasuredDimension(w*2, h);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
}
