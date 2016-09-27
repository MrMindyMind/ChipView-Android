package com.hotmail.maximglukhov.chipview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxim on 9/26/2016.
 */

public class ChipView extends RelativeLayout implements View.OnClickListener {

    private CharSequence text   = "";
    private boolean isDeletable = false;

    private TextView textView;
    private ImageView deleteChipImageView;

    private List<OnClickListener> clickListeners                        = new ArrayList<>();
    private List<OnChipDeleteClickListener> chipDeleteClickListeners    = new ArrayList<>();

    public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public ChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ChipView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public void addOnClickListener(OnClickListener listener) {
        if (listener != null && !clickListeners.contains(listener)) {
            clickListeners.add(listener);
        }
    }

    public void removeOnClickListener(OnClickListener listener) {
        if (listener != null && clickListeners.contains(listener)) {
            clickListeners.remove(listener);
        }
    }

    public void addOnChipDeleteClickListener(OnChipDeleteClickListener listener) {
        if (listener != null && !chipDeleteClickListeners.contains(listener)) {
            chipDeleteClickListeners.add(listener);
        }
    }

    public void removeOnChipDeleteClickListener(OnChipDeleteClickListener listener) {
        if (listener != null && chipDeleteClickListeners.contains(listener)) {
            chipDeleteClickListeners.remove(listener);
        }
    }

    public void setText(CharSequence text) {
        this.text = text;

        if (textView != null)
            textView.setText(text);
    }

    public CharSequence getText() {
        return text;
    }

    public void setDeletable(boolean toggle) {
        if (isDeletable != toggle) {
            isDeletable = toggle;
            updateTextViewPadding();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.chipLayout) {
            for (OnClickListener listener : clickListeners) {
                listener.onClick(v);
            }
        }
        else if (v.getId() == R.id.deleteChipImageView) {
            for (OnChipDeleteClickListener listener : chipDeleteClickListeners) {
                listener.onChipDelete(this);
            }
        }
    }

    public interface OnChipDeleteClickListener {
        void onChipDelete(ChipView view);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initResources(context);

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ChipView,
                    0, 0);

            try {
                text        = a.getText(R.styleable.ChipView_text);
                isDeletable = a.getBoolean(R.styleable.ChipView_deletable, false);

                if (isDeletable) {
                    updateTextViewPadding();
                }

                setText(text);
            } finally {
                a.recycle();
            }
        }
    }

    private void initResources(Context context) {
        inflate(context, R.layout.chip_layout, this);

        View layout         = findViewById(R.id.chipLayout);
        textView            = (TextView) findViewById(R.id.textView);
        deleteChipImageView = (ImageView) findViewById(R.id.deleteChipImageView);

        layout.setOnClickListener(this);
        deleteChipImageView.setOnClickListener(this);
    }

    private void updateTextViewPadding() {
        int[] textViewPadding = {
                ViewCompat.getPaddingStart(textView),
                textView.getPaddingTop(),
                ViewCompat.getPaddingEnd(textView),
                textView.getPaddingBottom()
        };

        if (isDeletable) {
            textViewPadding[2] = (int) getResources().getDimension(
                    R.dimen.deletable_label_padding);
            deleteChipImageView.setVisibility(View.VISIBLE);
        } else {
            textViewPadding[2] = (int) getResources().getDimension(
                    R.dimen.non_deletable_label_padding);
            deleteChipImageView.setVisibility(View.GONE);
        }

        ViewCompat.setPaddingRelative(textView, textViewPadding[0],
                textViewPadding[1], textViewPadding[2], textViewPadding[3]);
    }
}
