package br.com.tads.estarcliente.dao.voley;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;


/**
 * Created by Júnior - DevMaker on 24/09/2015.
 */
public class FlatProgressDialog extends AlertDialog {

    ImageView progress;

    public FlatProgressDialog(Context context) {
        super(context);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void show() {
        super.show();
        //setContentView(R.layout.loading_dialog);

       // progress = (ImageView) findViewById(R.id.progress);

        RotateAnimation rotate = new RotateAnimation(0.0f, 360.0f,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(500);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);

        //progress.setAnimation(rotate);
    }
}