package com.group.golf.listeners;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Golf;
import com.group.golf.math.Point3D;


import java.util.Arrays;
import java.util.StringTokenizer;

import javax.xml.soap.Text;

/**
 * Created by lilly on 4/17/18.
 */

public class SplineSubmitListener extends ChangeListener {
    final Golf game;
    Screen screen;
    TextField xyz;
    TextField dx;
    TextField dy;
    TextField dxy;
    float[][] dxMatrix;
    float[][] dyMatrix;
    float[][] dxyMatrix;
    Point3D[][] xyzMatrix;
    int xCounter;
    int yCounter;
    int xUnits;
    int yUnits;


    public SplineSubmitListener(final Golf game, Screen screen, TextField xyz, TextField dx, TextField dy,
                                TextField dxy, Point3D[][] xyzMatrix, float[][] dxMatrix, float[][] dyMatrix, float[][] dxyMatrix, TextField xUnits, TextField yUnits){
        this.game = game;
        this.screen = screen;
        this.xyzMatrix = xyzMatrix;
        this.dxMatrix = dxMatrix;
        this.dyMatrix = dyMatrix;
        this.dxyMatrix = dxyMatrix;
        this.xyz = xyz;
        this.dx = dx;
        this.dy = dy;
        this.dxy = dxy;
        this.xUnits = Integer.parseInt(xUnits.getText())+1;
        this.yUnits = Integer.parseInt(yUnits.getText())+1;
        xCounter = 0;
        yCounter = 0;
    }
    @Override
    public void changed(ChangeEvent event, Actor actor) {


        StringTokenizer tokenizerStart = new StringTokenizer(this.xyz.getText());
        float x = Float.parseFloat(tokenizerStart.nextToken());
        float y = Float.parseFloat(tokenizerStart.nextToken());
        float z = Float.parseFloat(tokenizerStart.nextToken());
        xyzMatrix[xCounter][yCounter] = new Point3D(x,y,z);

        dxMatrix[xCounter][yCounter] = Float.parseFloat(dx.getText());
        dyMatrix[xCounter][yCounter] = Float.parseFloat(dy.getText());
        dxyMatrix[xCounter][yCounter] = Float.parseFloat(dxy.getText());
        System.out.println(Arrays.deepToString(dxMatrix));

        System.out.println("Coordinates: " + x + ", " + y + ", " + z);
        System.out.println("Indices: " + xCounter + ", " + yCounter);

        if(xCounter==xUnits-1){
            yCounter++;
            xCounter=0;
        }
        else{
            xCounter++;
        }
    }
}
