package com.bulbasaur.dat256.viewmodel.uielements;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.bulbasaur.dat256.R;

public class EditTextWithError extends AppCompatEditText {

    public static final int[] STATE_ERROR = {R.attr.state_error};

    private boolean hasError = false;

    public EditTextWithError(Context context) {
        super(context);
    }

    public EditTextWithError(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextWithError(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

        if (hasError) {
            mergeDrawableStates(drawableState, STATE_ERROR);
        }

        return drawableState;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
        refreshDrawableState();
    }
}
