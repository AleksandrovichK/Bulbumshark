package com.company;

import java.awt.*;

/**
 * Created by bulbum on 04.09.2017.
 * This register controls the main parameters of the program.
 */
class Data {
        final static int frameHeight = 800; //высота окна
        final static int frameWidth = 800; //ширина окна
        final static int leftIndent = 20; //размещение окна на дисплее, отступ слева,  в процентах
        final static int upIndent = 2;   //размещение окна на дисплее, отступ сверху, в процентах
        final static int uniIndent = 20; //юнион отступ

        final static Font centralFontInAreas = new Font("Microsoft JhengHei Light", Font.BOLD, 10);   //шрифт использующийся на кнопках
        final static Color centralColor = new Color(228, 228, 228);

        final static CRectangle AllPacketsBounds = new CRectangle(uniIndent, uniIndent,frameWidth-uniIndent*2-4,280);  //расположение области пути
        final static CRectangle StackProtocolsBounds = new CRectangle(uniIndent,AllPacketsBounds.getHeight()+AllPacketsBounds.getTop()*3,20*frameWidth/100-uniIndent*2,frameHeight-(AllPacketsBounds.getTop() + AllPacketsBounds.getHeight()+4*uniIndent));  //расположение области пути
        final static CRectangle ActivePacketsBounds = new CRectangle(StackProtocolsBounds.getLeft()+StackProtocolsBounds.getWidth()+2*uniIndent, frameHeight-2*uniIndent-160,50*frameWidth/100-uniIndent*2,160);  //расположение области пути

        final static CRectangle trashKBounds = new CRectangle(frameWidth-uniIndent-24, AllPacketsBounds.getTop()+AllPacketsBounds.getHeight(), 20, 20);
        final static CRectangle searchKBounds = new CRectangle(trashKBounds.getLeft()-21, trashKBounds.getTop(), trashKBounds.getWidth(), trashKBounds.getHeight());
        final static CRectangle addKBounds = new CRectangle(StackProtocolsBounds.getWidth()+StackProtocolsBounds.getLeft()+uniIndent, AllPacketsBounds.getTop()+AllPacketsBounds.getHeight()+uniIndent*2, 21, 21);
        final static CRectangle comboPBounds = new CRectangle(addKBounds.getLeft()+addKBounds.getWidth()+uniIndent,addKBounds.getTop(), 240,30);  //расположение кнопки net user
}
