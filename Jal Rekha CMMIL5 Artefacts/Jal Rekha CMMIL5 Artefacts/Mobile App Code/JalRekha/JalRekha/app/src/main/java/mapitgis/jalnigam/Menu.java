package mapitgis.jalnigam;

import android.content.Context;
import android.content.res.ColorStateList;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

public class Menu {
    private final int id,color,icon,name,detail;

    public Menu(int id,@ColorRes int color, @DrawableRes int icon, @StringRes int name, @StringRes int detail) {
        this.id = id;
        this.color = color;
        this.icon = icon;
        this.name = name;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public int getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public ColorStateList getColor(@NonNull Context context ) {
        return ContextCompat.getColorStateList(context,color);
    }

    public int getDetail() {
        return detail;
    }
}
