package com.packtpub.libgdx.bludbourne.sfx;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ShakeCamera {
	private static final String TAG = ShakeCamera.class.getSimpleName();
	
	private boolean _isShaking = false;
	private float _origShakeRadius = 30.0f;
	private float _shakeRadius;
	private float _randomAngle;
	private Vector2 _offset;
	private Vector2 _currentPosition;
	private Vector2 _origPosition;

	
	public ShakeCamera(float x, float y, float shakeRadius){
		this._origPosition = new Vector2(x,y);
		this._shakeRadius = shakeRadius;
		this._origShakeRadius = shakeRadius;
		this._offset = new Vector2();
		this._currentPosition = new Vector2();
		reset();
	}

	public void setOrigPosition(float x, float y){
		this._origPosition.set(x,y);
	}
	
	public boolean isCameraShaking(){
		return _isShaking;
	}
	
	public void startShaking(){
		_isShaking = true;
	}
	
	private void seedRandomAngle(){
		_randomAngle = MathUtils.random(1, 360);
	}
	
	private void computeCameraOffset(){
		float sine = MathUtils.sinDeg(_randomAngle);
		float cosine = MathUtils.cosDeg(_randomAngle);

		//Gdx.app.debug(TAG, "Sine of " + _randomAngle + " is: " + sine);
		//Gdx.app.debug(TAG, "Cosine of " + _randomAngle + " is: " + cosine);

		_offset.x =  cosine * _shakeRadius;
		_offset.y =  sine * _shakeRadius;

		//Gdx.app.debug(TAG, "Offset is x:" + _offset.x + " , y: " + _offset.y );
	}
	
	private void computeCurrentPosition(){
		_currentPosition.x = _origPosition.x + _offset.x;
		_currentPosition.y = _origPosition.y + _offset.y;

		//Gdx.app.debug(TAG, "Current position is x:" + _currentPosition.x + " , y: " + _currentPosition.y );
	}
	
	private void diminishShake(){
		//Gdx.app.debug(TAG, "Current shakeRadius is: " + _shakeRadius + " randomAngle is: " + _randomAngle);

		if( _shakeRadius < 2.0 ){
			//Gdx.app.debug(TAG, "Done shaking");
			reset();
			return;
		}
		
		_isShaking = true;
		_shakeRadius *= .9f;
		//Gdx.app.debug(TAG, "New shakeRadius is: " + _shakeRadius);


		_randomAngle = MathUtils.random(1, 360);
		//Gdx.app.debug(TAG, "New random angle: " + _randomAngle);
	}

	public void reset(){
		_shakeRadius = _origShakeRadius;
		_isShaking = false;
		seedRandomAngle();
		_currentPosition.x = _origPosition.x;
		_currentPosition.y = _origPosition.y;
	}
	
	public Vector2 getNewShakePosition(){
		computeCameraOffset();
		computeCurrentPosition();
		diminishShake();
		return _currentPosition;
	}
}
