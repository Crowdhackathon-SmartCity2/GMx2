package gr.smartcity.hackathon.hackathonproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.widget.ImageView;

public class ScaleImg {

    ScaleImg(ImageView img, int id, Context context) {

        Display screen = ((Activity)context).getWindowManager().getDefaultDisplay();
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), id, options);

        int imgWidth = options.outWidth;
        int screenWidth = screen.getWidth();

        if (imgWidth > screenWidth)

        {
            int ratio = Math.round((float) imgWidth / (float) screenWidth);
            options.inSampleSize = ratio;
        }

        options.inJustDecodeBounds = false;
        Bitmap scaledImg = BitmapFactory.decodeResource(context.getResources(), id, options);
        img.setImageBitmap(scaledImg);
    }

}
