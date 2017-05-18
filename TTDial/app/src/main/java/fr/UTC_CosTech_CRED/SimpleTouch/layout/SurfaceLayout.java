package fr.UTC_CosTech_CRED.SimpleTouch.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;;
import java.util.HashSet;
import java.util.Set;

import fr.UTC_CosTech_CRED.SimpleTouch.util.touch.DialogTouchEvent;
import fr.UTC_CosTech_CRED.SimpleTouch.util.touch.TactileDialogViewHolder;

/**
 * Created by Binova on 22/12/2015.
 */
public class SurfaceLayout extends View
{
    private int width;
    private int height;
    private TactileDialogViewHolder.OPTIONS opt;

    private Vibrator vibs = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 50);

    private TactileDialogViewHolder dialogViewHolder;

    Paint touchFeedbackPaint = new Paint();
    Paint touchOtherPaint = new Paint();
    Paint TTPaint = new Paint();

    ArrayList<PointF> touchRelativePositions = new ArrayList<PointF>(); // touch positions relative to surface
    ArrayList<Point> touchAbsolutePositions = new ArrayList<Point>(); // touch position on the view
    Set<PointF> otherAbsolutePositions = new HashSet<PointF>(); // touch positions of partner
    float partnerRadius; // Rayon du toucher du partenaire

    public SurfaceLayout(Context context) {
        super(context);
        init();
    }

    public SurfaceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SurfaceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        //Initialisation des différents Paint

        //Retour du toucher utilisateur
        touchFeedbackPaint.setColor(Color.BLUE);
        touchFeedbackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        touchFeedbackPaint.setAlpha(100);
        touchFeedbackPaint.setAntiAlias(true);

        //Retour du toucher partenaire
        touchOtherPaint.setColor(Color.YELLOW);
        touchOtherPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        touchOtherPaint.setAlpha(100);
        touchOtherPaint.setAntiAlias(true);

        //En mode Visuel, affichage de la rencontre des deux partenaires
        TTPaint.setColor(Color.GREEN);
        TTPaint.setStyle(Paint.Style.FILL);
        TTPaint.setAlpha(100);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    public void setDialogViewHolder(TactileDialogViewHolder dialogViewHolder) {
        this.dialogViewHolder = dialogViewHolder;
        opt = dialogViewHolder.GetOptions();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        //Fonction déclenchée au toucher

        touchAbsolutePositions.clear();
        touchRelativePositions.clear();

        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_POINTER_UP:
                for (int i = 0; i < e.getPointerCount(); i++) {
                    Point absoluteTouch = new Point((int) e.getX(i), (int) e.getY(i));
                    PointF relativeTouch = new PointF((e.getX(i)) / width, (e.getY(i) ) / height);
                    touchRelativePositions.add(relativeTouch);
                    touchAbsolutePositions.add(absoluteTouch);
                }
                break;
        }
        dialogViewHolder.onDialogTouch(new DialogTouchEvent(touchRelativePositions, opt));
        invalidate();

        return true;
    }

    //Sets du partenaire, toucher et rayon
    public void setPartnerTouch(Set<PointF> partnerTouch) {
        this.otherAbsolutePositions = partnerTouch;
    }

    public void setPartnerRadius(float rad){
        this.partnerRadius = rad;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //Dessin des Paints + Vibreur + Son, si activés dans les paramètres

        if(opt.IsModeTouchThrough){     // MODE TOUCHTHROUGH

            // Calcul contacts :
            boolean Contact = false;
            for (PointF touchO: otherAbsolutePositions) {   // Pour chaque position de l'autre
                if (!Contact) {
                    Point AbsPt = new Point((int) (touchO.x * width), (int) (touchO.y * height));
                    for (Point touch : touchAbsolutePositions) { // Pour chacune de mes positions :
                        float dx = AbsPt.x - touch.x;
                        float dy = AbsPt.y - touch.y;
                        double distance = Math.sqrt(dx * dx + dy * dy);
                        int touchRadius = opt.touchRadius * width / 100;
                        if (distance <= touchRadius + partnerRadius * width /100) {
                            if(opt.TTVisual) {
                                // les 2 rayons distance des centres
                                double  r = touchRadius + partnerRadius * width /100 - distance;
                                canvas.drawCircle((float) (touch.x + dx / 2.), (float) (touch.y + dy / 2.), (float)r, TTPaint);
                            }
                            Contact = true;
                        }
                    }
                }
            }
            if (Contact) {
                if(opt.TTVibr) //Si vibreur activé, vibrer au croisement
                {
                    vibs.vibrate(25);
                }
                if(opt.TTSound) //Si son activé, faire un son au croisement
                {
                 toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 250);
                }
            }
        }

        if(opt.IsMyPos) {                                           // Mes positions
            for (Point touch : touchAbsolutePositions) {
                int touchRadius = opt.touchRadius * width / 100;
                canvas.drawCircle(touch.x, touch.y, touchRadius, touchFeedbackPaint);
            }
        }

        if(opt.IsParthnerPos) {                                      // Les positions de l'autre
            for (PointF touchO : otherAbsolutePositions) {
                Point AbsPt = new Point((int) (touchO.x * width), (int) (touchO.y * height));
                canvas.drawCircle(AbsPt.x, AbsPt.y, partnerRadius * width /100, touchOtherPaint);
            }
        }
    }
}
