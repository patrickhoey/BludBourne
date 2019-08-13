package com.packtpub.libgdx.bludbourne.sfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ClockActor extends Label {

    public static enum TimeOfDay {
        DAWN,
        AFTERNOON,
        DUSK,
        NIGHT
    }

    private float _totalTime = 0;
    private float _rateOfTime = 1;
    private static String PM = "PM";
    private static String AM = "AM";
    private static String FORMAT = "%02d:%02d %s";
    private boolean _isAfternoon = false;

    public ClockActor(CharSequence text, Skin skin) {
        super(text, skin);
        init();
    }

    public ClockActor(CharSequence text, Skin skin, String styleName) {
        super(text, skin, styleName);
        init();
    }

    public ClockActor(CharSequence text, Skin skin, String fontName, Color color) {
        super(text, skin, fontName, color);
        init();
    }

    public ClockActor(CharSequence text, Skin skin, String fontName, String colorName) {
        super(text, skin, fontName, colorName);
        init();
    }

    public ClockActor(CharSequence text, LabelStyle style) {
        super(text, style);
        init();
    }

    private void init(){
        String time = String.format(FORMAT, 0, 0, _isAfternoon?PM:AM);
        this.setText(time);
        this.pack();
    }

    public float getTotalTime() {
        return _totalTime;
    }

    public void setTotalTime(float totalTime) {
        this._totalTime = totalTime;
    }

    public float getRateOfTime() {
        return _rateOfTime;
    }

    public void setRateOfTime(float rateOfTime) {
        this._rateOfTime = rateOfTime;
    }

    public TimeOfDay getCurrentTimeOfDay(){
        int hours = getCurrentTimeHours();
        if( hours >= 7 && hours <= 9 ){
            return TimeOfDay.DAWN;
        }else if( hours >= 10 && hours <=16 ){
            return TimeOfDay.AFTERNOON;
        }else if( hours >= 17 && hours <= 19 ){
            return TimeOfDay.DUSK;
        }else{
            return TimeOfDay.NIGHT;
        }
    }

    @Override
    public void act(float delta){
        _totalTime += (delta * _rateOfTime);

        int seconds = getCurrentTimeSeconds();
        int minutes = getCurrentTimeMinutes();
        int hours = getCurrentTimeHours();

        if( hours == 24 || (hours/12) == 0 ){
            _isAfternoon = false;
        }else{
            _isAfternoon = true;
        }

        hours = hours % 12;

        if( hours == 0 ){
            hours = 12;
        }

        String time = String.format(FORMAT, hours, minutes, _isAfternoon ? PM : AM);
        this.setText(time);
    }

    public int getCurrentTimeSeconds(){
        return MathUtils.floor(_totalTime % 60);
    }

    public int getCurrentTimeMinutes(){
        return MathUtils.floor((_totalTime / 60) % 60);
    }

    public int getCurrentTimeHours(){
        int hours = MathUtils.floor((_totalTime / 3600) % 24);

        if( hours == 0 ){
            hours = 24;
        }

        return hours;
    }

}
