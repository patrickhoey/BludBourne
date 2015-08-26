package com.packtpub.libgdx.bludbourne.sfx;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ClockActor extends Label {
    private float _totalTime = 0;
    private float _rateOfTime = 1;
    private static String PM = "PM";
    private static String AM = "AM";
    private boolean isAfternoon = false;

    public ClockActor(CharSequence text, Skin skin) {
        super(text, skin);

        String time = String.format("%02d:%02d %s", 0, 0, isAfternoon?PM:AM);
        this.setText(time);
        this.pack();
    }

    public float getRateOfTime() {
        return _rateOfTime;
    }

    public void setRateOfTime(float rateOfTime) {
        this._rateOfTime = rateOfTime;
    }

    @Override
    public void act(float delta){
        _totalTime += (delta * _rateOfTime);

        int seconds = MathUtils.floor(_totalTime % 60);
        int minutes = MathUtils.floor((_totalTime / 60) % 60);
        int hours   = MathUtils.floor((_totalTime / 3600) % 24);

        if( hours == 0 || (hours/12)==0){
            isAfternoon = false;
        }else{
            isAfternoon = true;
        }

        hours = hours % 12;

        if( hours == 0 ){
            hours = 12;
        }

        String time = String.format("%02d:%02d %s", hours, minutes, isAfternoon?PM:AM);

        this.setText(time);
    }

}
