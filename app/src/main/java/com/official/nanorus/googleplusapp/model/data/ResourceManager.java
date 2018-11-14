package com.official.nanorus.googleplusapp.model.data;

import android.content.Context;

import com.official.nanorus.googleplusapp.R;
import com.official.nanorus.googleplusapp.app.App;

public class ResourceManager {
    private Context context;
    public static ResourceManager instance;

    public static ResourceManager getInstance() {
        if (instance == null)
            instance = new ResourceManager();
        return instance;
    }

    public ResourceManager() {
        this.context = App.getApp().getApplicationContext();
    }

    public String getDefaultWebClientId() {
        return context.getString(R.string.default_web_client_id);
    }

    public int getLetterIconShapeColor(int position) {
        position++;
        int colors[] = new int[]{R.color.imageLetter_color_1, R.color.imageLetter_color_2, R.color.imageLetter_color_3,
                R.color.imageLetter_color_4, R.color.imageLetter_color_5};
        int color = colors[colors.length - 1];
        int remainder = position % colors.length;
        if (remainder == 0)
            return context.getResources().getColor(color);
        else {
            color = colors[remainder - 1];
        }
        return context.getResources().getColor(color);
    }
}
