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
		seedRandomAngle();
	}
	
	public boolean isCameraShaking(){
		return _isShaking;
	}
	
	public void startShaking(){
		_isShaking = true;
	}

	public Vector2 getOriginalPosition(){
		return _origPosition.cpy();
	}
	
	private void seedRandomAngle(){
		_randomAngle = MathUtils.random(1, 360);
	}
	
	private void computeCameraOffset(){
		_offset.x =  MathUtils.sinDeg(_randomAngle) * _shakeRadius;
		_offset.y =  MathUtils.cosDeg(_randomAngle) * _shakeRadius;
	}
	
	private void computeCurrentPosition(){
		_currentPosition.x = _origPosition.x + _offset.x;
		_currentPosition.y = _origPosition.y + _offset.y;
	}
	
	private void diminishShake(){
		if( _shakeRadius < 2.0 ){
			reset();
			return;
		}
		
		//Gdx.app.log(TAG, "Current shakeRadius is: " + _shakeRadius + " randomAngle is: " + _randomAngle);
		
		_isShaking = true;
		_shakeRadius *= .9f;
		_randomAngle = (150 + MathUtils.random(1, 60)) % 360;
	}

	public void reset(){
		_shakeRadius = _origShakeRadius;
		_isShaking = false;
		seedRandomAngle();
		computeCameraOffset();
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
