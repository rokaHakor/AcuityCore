package com.acuity.client;

import com.acuity.api.RSInstance;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Zach on 5/31/2017.
 */
public class Acuity {

    public static void main(String[] args) {
        try {
            RSInstance.init();

            JFrame frame = new JFrame();
            frame.setSize(new Dimension(500, 500));
            frame.setVisible(true);
            frame.getContentPane().add(RSInstance.getApplet());

            RSInstance.loadClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}