package com.packtpub.libgdx.bludbourne.sfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ShakeCamera {
	private static final String TAG = ShakeCamera.class.getSimpleName();
	
	private boolean _isShaking = false;
	private Vector2 _originCameraCenter;
	private float _origShakeRadius = 30.0f;
	private float _shakeRadius;
	private float _randomAngle;
	private Vector2 _offsetCameraCenter;
	private Vector2 _currentCameraCenter;
	
	public ShakeCamera(float cameraViewportCoordsX, float cameraViewportCoordsY, float shakeRadius){
		this._shakeRadius = shakeRadius;
		this._origShakeRadius = shakeRadius;
		this._originCameraCenter = new Vector2();
		this._offsetCameraCenter = new Vector2();
		this._currentCameraCenter = new Vector2();
		setOriginCameraCenter(cameraViewportCoordsX/2f, cameraViewportCoordsY/2f);
		seedRandomAngle();
	}
	
	public boolean isCameraShaking(){
		return _isShaking;
	}
	
	public void startShaking(){
		_isShaking = true;
	}
	
	public void setOriginCameraCenter(float cameraCenterCoordsX, float cameraCenterCoordsY){
		this._originCameraCenter.x = cameraCenterCoordsX;
		this._originCameraCenter.y = cameraCenterCoordsY;
		Gdx.app.log(TAG, "OriginCameraCenterX: " + _originCameraCenter.x + " OriginCameraCenterY is: " + _originCameraCenter.y);
	}
	
	private void seedRandomAngle(){
		_randomAngle = MathUtils.random(1, 360);
	}
	
	private void computeCameraOffset(){
		_offsetCameraCenter.x =  MathUtils.sinDeg(_randomAngle) * _shakeRadius;
		_offsetCameraCenter.y =  MathUtils.cosDeg(_randomAngle) * _shakeRadius;
	}
	
	private void computeCurrentCameraCenter(){
		_currentCameraCenter.x = _originCameraCenter.x + _offsetCameraCenter.x;
		_currentCameraCenter.y = _originCameraCenter.y + _offsetCameraCenter.y;
		//Gdx.app.log(TAG, "OriginCameraCenterX: " + _originCameraCenter.x + " OriginCameraCenterY is: " + _originCameraCenter.y);
		//Gdx.app.log(TAG, "OffsetCameraCenterX: " + _offsetCameraCenter.x + " OffsetCameraCenterY is: " + _offsetCameraCenter.y);
	}
	
	private void diminishShake(){
		if( _shakeRadius < 2.0 ){
			Gdx.app.log(TAG, "DONE SHAKING: shakeRadius is: " + _shakeRadius + " randomAngle is: " + _randomAngle);
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
	}
	
	public Vector2 getShakeCameraCenter(){
		computeCameraOffset();
		computeCurrentCameraCenter();
		diminishShake();
		return _currentCameraCenter;
	}
}
