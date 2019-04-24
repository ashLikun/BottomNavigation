package com.ashlikun.bottomnavigation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.core.content.ContextCompat;

import static androidx.vectordrawable.graphics.drawable.VectorDrawableCompat.create;

/**
 * AHBottomNavigationItem
 * The item is display in the AHBottomNavigation layout
 */
public class AHBottomNavigationItem {

    private String title = "";
    private Drawable drawable;
    private Drawable drawableSelect;
    private int color = Color.GRAY;

    private
    @StringRes
    int titleRes = 0;
    private
    @DrawableRes
    int drawableRes = 0;
    @DrawableRes
    int drawableSelectRes = 0;
    private
    @ColorRes
    int colorRes = 0;

    private AHBottomNavigationItem() {

    }

    public static class Builder {
        private String title = "";
        private Drawable drawable;
        private Drawable drawableSelect;
        private int color = Color.GRAY;

        private
        @StringRes
        int titleRes = 0;
        private
        @DrawableRes
        int drawableRes = 0;
        @DrawableRes
        int drawableSelectRes = 0;
        private
        @ColorRes
        int colorRes = 0;

        public Builder(String title, @DrawableRes int drawableRes) {
            this.title = title;
            this.drawableRes = drawableRes;
        }

        public Builder(String title, Drawable drawable) {
            this.title = title;
            this.drawable = drawable;
        }

        public Builder(String title, Drawable drawable, Drawable drawableSelect) {
            this.title = title;
            this.drawable = drawable;
            this.drawableSelect = drawableSelect;
        }

        public Builder(String title, @DrawableRes int drawableRes, @DrawableRes int drawableSelectRes) {
            this.title = title;
            this.drawableRes = drawableRes;
            this.drawableSelectRes = drawableSelectRes;
        }

        public Builder(@StringRes int titleRes, @DrawableRes int drawableRes) {
            this.titleRes = titleRes;
            this.drawableRes = drawableRes;
        }

        public Builder(@StringRes int titleRes, Drawable drawable) {
            this.titleRes = titleRes;
            this.drawable = drawable;
        }

        public Builder(@StringRes int titleRes, Drawable drawable, Drawable drawableSelect) {
            this.titleRes = titleRes;
            this.drawable = drawable;
            this.drawableSelect = drawableSelect;
        }

        public Builder(@StringRes int titleRes, @DrawableRes int drawableRes, @DrawableRes int drawableSelectRes) {
            this.titleRes = titleRes;
            this.drawableRes = drawableRes;
            this.drawableSelectRes = drawableSelectRes;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDrawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Builder setDrawableSelect(Drawable drawableSelect) {
            this.drawableSelect = drawableSelect;
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setTitleRes(@StringRes int titleRes) {
            this.titleRes = titleRes;
            return this;
        }

        public Builder setDrawableRes(@DrawableRes int drawableRes) {
            this.drawableRes = drawableRes;
            return this;
        }

        public Builder setDrawableSelectRes(@DrawableRes int drawableSelectRes) {
            this.drawableSelectRes = drawableSelectRes;
            return this;
        }

        public Builder setColorRes(@ColorRes int colorRes) {
            this.colorRes = colorRes;
            return this;
        }

        public AHBottomNavigationItem builder() {
            AHBottomNavigationItem item = new AHBottomNavigationItem();
            item.title = title;
            item.drawable = drawable;
            item.drawableSelect = drawableSelect;
            item.color = color;
            item.titleRes = titleRes;
            item.drawableRes = drawableRes;
            item.drawableSelectRes = drawableSelectRes;
            item.colorRes = colorRes;
            return item;
        }
    }

    public String getTitle(Context context) {
        if (titleRes != 0) {
            title =  context.getString(titleRes);
            titleRes = 0;
        }
        return title;
    }


    public int getColor(Context context) {
        if (colorRes != 0) {
            color =  ContextCompat.getColor(context, colorRes);
            colorRes = 0;
        }
        return color;
    }


    public Drawable getDrawable(Context context) {
        if (drawableRes != 0) {
            try {
                drawable = create(context.getResources(), drawableRes, null);
            } catch (Resources.NotFoundException e) {
                drawable = ContextCompat.getDrawable(context, drawableRes);
            }
            drawableRes = 0;
        }
        return drawable;
    }

    public Drawable getDrawableSelect(Context context) {
        if (drawableSelectRes != 0) {
            try {
                drawableSelect = VectorDrawableCompat.create(context.getResources(), drawableSelectRes, null);
            } catch (Resources.NotFoundException e) {
                drawableSelect = ContextCompat.getDrawable(context, drawableSelectRes);
            }
            drawableSelectRes = 0;
        }
        return drawableSelect;
    }

    public boolean isSelect() {
        return drawableSelectRes > 0 || drawableSelect != null;
    }


}
