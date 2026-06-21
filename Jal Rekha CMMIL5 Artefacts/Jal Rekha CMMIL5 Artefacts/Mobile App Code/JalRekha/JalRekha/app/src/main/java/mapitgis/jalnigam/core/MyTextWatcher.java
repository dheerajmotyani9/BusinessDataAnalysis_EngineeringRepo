package mapitgis.jalnigam.core;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;

public abstract class MyTextWatcher implements TextWatcher {
    private final View view;

    public MyTextWatcher(@NonNull View view) {
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(@NonNull Editable editable) {
        afterTextChanged(view,editable.toString());
    }

    protected abstract void afterTextChanged(@NonNull View view,@NonNull String text);
}
