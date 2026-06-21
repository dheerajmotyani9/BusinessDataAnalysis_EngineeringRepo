package mapitgis.jalnigam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import mapitgis.jalnigam.core.Utility;

public class BaseActivity extends AppCompatActivity {

    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Utility.SF,MODE_PRIVATE);
//        Utility.setLocale(sharedPreferences.getString("lng","en"),this,false);
    }

    protected void setToolbar(@NonNull Toolbar toolbar){
        setToolbar(toolbar,false);
    }

    protected void setToolbar(@NonNull Toolbar toolbar, boolean isBack) {
        setSupportActionBar(toolbar);
        if(isBack) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void finish() {
        super.finish();
//        if(animate) overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    protected void open(@NonNull Class<?> _class) {
        Intent intent = new Intent(this,_class);
        startActivity(intent);
    }

    protected void open(@NonNull Class<?> _class, boolean isFinish) {
        Intent intent = new Intent(this,_class);
        if(isFinish) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        if(isFinish) finish();
    }

    protected void restartActivity(){
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
    }

    protected void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
