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
    double[][] dxMatrix;
    double[][] dyMatrix;
    double[][] dxyMatrix;
    Point3D[][] xyzMatrix;
    int xCounter;
    int yCounter;
    int xUnits;
    int yUnits;


    public SplineSubmitListener(final Golf game, Screen screen, TextField xyz, TextField dx, TextField dy,
                                TextField dxy, Point3D[][] xyzMatrix, double[][] dxMatrix, double[][] dyMatrix, double[][] dxyMatrix, TextField xUnits, TextField yUnits){
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
        double x = Double.parseDouble(tokenizerStart.nextToken());
        double y = Double.parseDouble(tokenizerStart.nextToken());
        double z = Double.parseDouble(tokenizerStart.nextToken());
        xyzMatrix[xCounter][yCounter] = new Point3D(x,y,z);

        dxMatrix[xCounter][yCounter] = Double.parseDouble(dx.getText());
        dyMatrix[xCounter][yCounter] = Double.parseDouble(dy.getText());
        dxyMatrix[xCounter][yCounter] = Double.parseDouble(dxy.getText());
        System.out.println(Arrays.deepToString(dxMatrix));

        if(xCounter==xUnits-1){
            yCounter++;
            xCounter=0;
        }
        else{
            xCounter++;
        }
    }
}
