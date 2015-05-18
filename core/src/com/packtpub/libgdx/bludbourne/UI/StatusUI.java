package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class StatusUI extends Group {
    private final static String _textureAtlasPath = "skins/statusui.pack";
    private TextureAtlas _textureAtlas;
    private Image _hudBackgroundImage;
    private Image _hpBar;
    private Image _mpBar;
    private Image _xpBar;
    private Skin _skin;

    //Attributes
    private int _level = 1;
    private int _gold = 0;
    private int _hp = 50;
    private int _mp = 50;
    private int _xp = 0;

    public StatusUI(){
        _textureAtlas = new TextureAtlas(_textureAtlasPath);
        _hudBackgroundImage = new Image(_textureAtlas.findRegion("HUD_Background"));

        _skin = new Skin();
        _skin.load(Gdx.files.internal("skins/uiskin.json"));

        Table table = new Table();

        WidgetGroup group = new WidgetGroup();
        _hpBar = new Image(_textureAtlas.findRegion("HP_Bar"));
        _hpBar.setPosition(3, 6);

        Image bar = new Image(_textureAtlas.findRegion("Bar"));

        group.addActor(bar);
        group.addActor(_hpBar);

        Cell cell = table.add(group);
        cell.width(bar.getWidth());
        cell.height(bar.getHeight());

        Label hpLabel = new Label(" hp:", _skin);
        table.add(hpLabel);
        Label hp = new Label(String.valueOf(_hp), _skin);
        table.add(hp);
        table.row();

        WidgetGroup group2 = new WidgetGroup();
        _mpBar = new Image(_textureAtlas.findRegion("MP_Bar"));
        _mpBar.setPosition(3, 6);

        Image bar2 = new Image(_textureAtlas.findRegion("Bar"));

        group2.addActor(bar2);
        group2.addActor(_mpBar);

        Cell cell2 = table.add(group2);
        cell2.width(bar2.getWidth());
        cell2.height(bar2.getHeight());

        Label mpLabel = new Label(" mp:", _skin);
        table.add(mpLabel);
        Label mp = new Label(String.valueOf(_mp), _skin);
        table.add(mp);
        table.row();

        WidgetGroup group3 = new WidgetGroup();
        _xpBar = new Image(_textureAtlas.findRegion("XP_Bar"));
        _xpBar.setPosition(3, 6);

        Image bar3 = new Image(_textureAtlas.findRegion("Bar"));

        group3.addActor(bar3);
        group3.addActor(_xpBar);

        Cell cell3 = table.add(group3);
        cell3.width(bar3.getWidth());
        cell3.height(bar3.getHeight());

        Label xpLabel = new Label(" xp:", _skin);
        table.add(xpLabel);
        Label xp = new Label(String.valueOf(_xp), _skin);
        table.add(xp);
        table.row();

        Label levelLabel = new Label("lv:", _skin);
        table.add(levelLabel);
        Label levelVal = new Label(String.valueOf(_level), _skin);
        table.add(levelVal).align(Align.left);

        Label goldLabel = new Label("gp: ", _skin);
        table.add(goldLabel);
        Label goldVal = new Label(String.valueOf(_gold), _skin);
        table.add(goldVal);

        //table.debug();
        table.setPosition(135, 68);
        table.setFillParent(true);

        this.addActor(_hudBackgroundImage);
        this.addActor(table);
    }

/*
    @Override
    public void draw (Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(_hudBackground, 0, 0, 4, 3);
    }
*/

}
