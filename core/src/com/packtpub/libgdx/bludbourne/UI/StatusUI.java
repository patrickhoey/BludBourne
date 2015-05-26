package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class StatusUI extends Window {
    private Image _hpBar;
    private Image _mpBar;
    private Image _xpBar;

    private ImageButton _inventoryButton;

    //Attributes
    private int _levelVal = 1;
    private int _goldVal = 0;
    private int _hpVal = 50;
    private int _mpVal = 50;
    private int _xpVal = 0;

    public StatusUI(Skin skin, TextureAtlas textureAtlas){
        super("stats", skin);

        //groups
        WidgetGroup group = new WidgetGroup();
        WidgetGroup group2 = new WidgetGroup();
        WidgetGroup group3 = new WidgetGroup();

        //images
        _hpBar = new Image(textureAtlas.findRegion("HP_Bar"));
        Image bar = new Image(textureAtlas.findRegion("Bar"));
        _mpBar = new Image(textureAtlas.findRegion("MP_Bar"));
        Image bar2 = new Image(textureAtlas.findRegion("Bar"));
        _xpBar = new Image(textureAtlas.findRegion("XP_Bar"));
        Image bar3 = new Image(textureAtlas.findRegion("Bar"));

        //labels
        Label hpLabel = new Label(" hp:", skin);
        Label hp = new Label(String.valueOf(_hpVal), skin);
        Label mpLabel = new Label(" mp:", skin);
        Label mp = new Label(String.valueOf(_mpVal), skin);
        Label xpLabel = new Label(" xp:", skin);
        Label xp = new Label(String.valueOf(_xpVal), skin);
        Label levelLabel = new Label(" lv:", skin);
        Label levelVal = new Label(String.valueOf(_levelVal), skin);
        Label goldLabel = new Label(" gp:", skin);
        Label goldVal = new Label(String.valueOf(_goldVal), skin);

        //buttons
        _inventoryButton= new ImageButton(skin, "inventory-button");
        _inventoryButton.getImageCell().size(32, 32);

        //Align images
        _hpBar.setPosition(3, 6);
        _mpBar.setPosition(3, 6);
        _xpBar.setPosition(3, 6);

        //add to widget groups
        group.addActor(bar);
        group.addActor(_hpBar);
        group2.addActor(bar2);
        group2.addActor(_mpBar);
        group3.addActor(bar3);
        group3.addActor(_xpBar);

        //Add to layout
        defaults().expand().fill();

        //account for the title padding
        this.pad(this.getPadTop() + 10, 10, 10, 10);

        this.add();
        this.add();
        this.add(_inventoryButton).align(Align.right);
        this.row();

        this.add(group).size(bar.getWidth(), bar.getHeight());
        this.add(hpLabel);
        this.add(hp).align(Align.left);
        this.row();

        this.add(group2).size(bar2.getWidth(), bar2.getHeight());
        this.add(mpLabel);
        this.add(mp).align(Align.left);
        this.row();

        this.add(group3).size(bar3.getWidth(), bar3.getHeight());
        this.add(xpLabel);
        this.add(xp).align(Align.left);
        this.row();

        this.add(levelLabel).align(Align.left);
        this.add(levelVal).align(Align.left);
        this.row();
        this.add(goldLabel);
        this.add(goldVal).align(Align.left);

        //this.debug();
        this.pack();
    }

    public ImageButton getInventoryButton() {
        return _inventoryButton;
    }
}
