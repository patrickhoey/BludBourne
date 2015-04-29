package com.packtpub.libgdx.bludbourne;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

public abstract class GraphicsComponent implements Component {

    public abstract void update(Entity entity, Batch batch, float delta);

    protected Animation loadAnimation(Texture texture1, Texture texture2, GridPoint2 frameIndex){
        TextureRegion[][] texture1Frames = TextureRegion.split(texture1, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);
        TextureRegion[][] texture2Frames = TextureRegion.split(texture2, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        Array<TextureRegion> animationKeyFrames = new Array<TextureRegion>(2);

        animationKeyFrames.add(texture1Frames[frameIndex.x][frameIndex.y]);
        animationKeyFrames.add(texture2Frames[frameIndex.x][frameIndex.y]);

        return new Animation(0.25f, animationKeyFrames, Animation.PlayMode.LOOP);
    }

    protected Animation loadAnimation(Texture texture, Array<GridPoint2> points){
        TextureRegion[][] textureFrames = TextureRegion.split(texture, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        Array<TextureRegion> animationKeyFrames = new Array<TextureRegion>(points.size);

        for( GridPoint2 point : points){
            animationKeyFrames.add(textureFrames[point.x][point.y]);
        }

        return new Animation(0.25f, animationKeyFrames, Animation.PlayMode.LOOP);
    }
}
