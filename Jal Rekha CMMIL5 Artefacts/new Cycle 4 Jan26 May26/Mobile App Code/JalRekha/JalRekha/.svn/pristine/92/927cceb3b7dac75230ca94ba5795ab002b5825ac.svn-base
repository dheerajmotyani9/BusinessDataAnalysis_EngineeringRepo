package mapitgis.jalnigam.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

public class SpinnerManager {
    static List<SpinnerItem> spinnerItemsStatic;
    private List<SpinnerItem> spinnerItems;
    private int requestCode;

    public int getRequestCode() {
        return requestCode;
    }

    public List<SpinnerItem> getSpinnerItems() {
        return spinnerItems;
    }

    public SpinnerManager(@NonNull View view, int requestCode, Context context, List<SpinnerItem> spinnerItems) {
        this.spinnerItems = spinnerItems;
        this.requestCode = requestCode;
        view.setOnClickListener(v -> {
            try {
                if(spinnerItems.isEmpty()){
                    Utility.show(context,"Nothing for select");
                    return;
                }
                Intent intent=new Intent(context,SpinnerActivity.class);
                Bundle bundle=new Bundle();
                SpinnerManager.spinnerItemsStatic = this.spinnerItems;
//                bundle.putSerializable("spinnerItems",new DataList(this.spinnerItems));
                if (v.getTag() != null) {
                    SpinnerItem spinnerItem = (SpinnerItem) v.getTag();
                    bundle.putSerializable("spinnerItem",spinnerItem);
                }
                bundle.putInt("requestCode",requestCode);
                intent.putExtras(bundle);
                //noinspection deprecation
                ((AppCompatActivity)context).startActivityForResult(intent,100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public SpinnerManager(@NonNull View view, int requestCode, Context context, Fragment fragment, List<SpinnerItem> spinnerItems) {
        this.spinnerItems=spinnerItems;
        view.setOnClickListener(v -> {
            try {
                if(spinnerItems.isEmpty()){
                    Utility.show(context,"Nothing for select");
                    return;
                }
                Intent intent=new Intent(context,SpinnerActivity.class);
                Bundle bundle=new Bundle();
                SpinnerManager.spinnerItemsStatic = this.spinnerItems;
//                bundle.putSerializable("spinnerItems",new DataList(this.spinnerItems));
                if (v.getTag() != null) {
                    SpinnerItem spinnerItem = (SpinnerItem) v.getTag();
                    bundle.putSerializable("spinnerItem",spinnerItem);
                }
                bundle.putInt("requestCode",requestCode);
                intent.putExtras(bundle);
                //noinspection deprecation
                fragment.startActivityForResult(intent,100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void setSpinnerItems(List<SpinnerItem> spinnerItems) {
        this.spinnerItems = spinnerItems;
    }

//    public static class DataList implements Serializable{
//        private final List<SpinnerItem> spinnerItems;
//
//        public DataList(List<SpinnerItem> spinnerItems) {
//            this.spinnerItems = spinnerItems;
//        }
//
//        public List<SpinnerItem> getSpinnerItems() {
//            return spinnerItems;
//        }
//    }
}
