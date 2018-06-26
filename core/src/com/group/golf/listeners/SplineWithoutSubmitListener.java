package com.group.golf.listeners;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.group.golf.Golf;
import com.group.golf.math.Point3D;


import java.util.StringTokenizer;

import javax.xml.soap.Text;

/**
 * Listener to submit points for spline interpolation (easy mode, without derivatives)
 * @author Lillian Wush
 * @author Kim Roggenbuck
 */
public class SplineWithoutSubmitListener extends ChangeListener {
    final Golf game;
    Screen screen;
    TextField xyz;

    Point3D[][] xyzMatrix;
    int xCounter;
    int yCounter;
    int xUnits;
    int yUnits;

    /**
     * Create a new instance of SplineWithoutSubmitListener
     * @param game the game instance
     * @param screen the current screen
     * @param xyz textfield for point coordinates
     * @param xyzMatrix martix containing the x y z coordinates of the points
     * @param xUnits units to interpolate along the x-axis
     * @param yUnits units to interpolate along the y-axis
     */
    public SplineWithoutSubmitListener(final Golf game, Screen screen, TextField xyz,  Point3D[][] xyzMatrix, TextField xUnits, TextField yUnits){
        this.game = game;
        this.screen = screen;
        this.xyzMatrix = xyzMatrix;

        this.xyz = xyz;

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
