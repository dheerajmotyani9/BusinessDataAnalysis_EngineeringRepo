package mapitgis.jalnigam.core;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import mapitgis.jalnigam.R;

import java.util.List;
import java.util.Objects;

public class SpinnerActivity extends AppCompatActivity implements SpinnerAdapter.OnSpinnerListener {
    private SpinnerAdapter spinnerAdapter;
    private int requestCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        setSupportActionBar(findViewById(R.id.toolbar));

        Bundle bundle=getIntent().getExtras();
        assert bundle != null;
        List<SpinnerItem> spinnerItems=SpinnerManager.spinnerItemsStatic;//((SpinnerManager.DataList) Objects.requireNonNull(bundle.getSerializable("spinnerItems"))).getSpinnerItems();


        requestCode=bundle.getInt("requestCode",0);
        spinnerAdapter = new SpinnerAdapter(spinnerItems, this, this);
        if(bundle.containsKey("spinnerItem")){
            spinnerAdapter.setSpinnerItem((SpinnerItem) bundle.getSerializable("spinnerItem"));
        }

        Log.e("TAG","SPINNER ITEMS "+spinnerItems.get(0).getValue());

        Log.e("TAG","TEST "+requestCode);

        EditText editTextSearch = findViewById(R.id.editTextSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                spinnerAdapter.filter(s.toString());
            }
        });
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(spinnerAdapter);
        findViewById(R.id.imageViewBack).setOnClickListener(v->onBackPressed());
    }

    @Override
    public void onSpinnerClick(SpinnerItem spinnerItem) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("spinnerItem",spinnerItem);
        returnIntent.putExtra("requestCode",requestCode);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }
}
