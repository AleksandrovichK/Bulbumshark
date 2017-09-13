package com.company;

import java.awt.*;
import java.util.zip.CRC32;

/**
 * Created by bulbum on 04.09.2017.
 * This register controls the main parameters of the program.
 */
class Data {
        final static int frameHeight = 800; //высота окна
        final static int frameWidth = 1000; //ширина окна
        final static int leftIndent = 20; //размещение окна на дисплее, отступ слева,  в процентах
        final static int upIndent = 2;   //размещение окна на дисплее, отступ сверху, в процентах
        final static int uniIndent = 20; //юнион отступ

        final static Font centralFontInKeys = new Font("Microsoft JhengHei Light", Font.BOLD, 13);   //шрифт использующийся на кнопках
        final static Font centralFontInAreas = new Font("Microsoft JhengHei Light", Font.BOLD, 10);   //шрифт использующийся на кнопках

        final static CRectangle AllPacketsBounds = new CRectangle(uniIndent, uniIndent,frameWidth-uniIndent*2,300);  //расположение области пути
        final static CRectangle ActivePacketsBounds = new CRectangle(uniIndent,AllPacketsBounds.getHeight()+AllPacketsBounds.getTop()*2,80*frameWidth/100-uniIndent*2,frameHeight-(4*uniIndent + AllPacketsBounds.getHeight()+10));  //расположение области пути

        final static CRectangle canalLBounds = new CRectangle(ActivePacketsBounds.getLeft()+ActivePacketsBounds.getWidth()+uniIndent,AllPacketsBounds.getHeight()+AllPacketsBounds.getTop()+2*uniIndent, frameWidth - (ActivePacketsBounds.getLeft() + ActivePacketsBounds.getWidth() + 2*uniIndent),30);  //расположение кнопки net user
        final static CRectangle networkLBounds = new CRectangle(canalLBounds.getLeft(),canalLBounds.getTop()+canalLBounds.getHeight()+10, frameWidth - (ActivePacketsBounds.getLeft() + ActivePacketsBounds.getWidth() + 2*uniIndent),30);  //расположение кнопки installed
        final static CRectangle transportLBounds = new CRectangle(canalLBounds.getLeft(),networkLBounds.getTop()+networkLBounds.getHeight()+10, frameWidth - (ActivePacketsBounds.getLeft() + ActivePacketsBounds.getWidth() + 2*uniIndent),30);  //расположение кнопки debigger
        final static CRectangle applicationLBounds = new CRectangle(canalLBounds.getLeft(),transportLBounds.getTop()+transportLBounds.getHeight()+10, frameWidth - (ActivePacketsBounds.getLeft() + ActivePacketsBounds.getWidth() + 2*uniIndent),30);  //расположение кнопки setup
        final static CRectangle portFBounds = new CRectangle(canalLBounds.getLeft(),applicationLBounds.getTop()+applicationLBounds.getHeight()+30, frameWidth - (ActivePacketsBounds.getLeft() + ActivePacketsBounds.getWidth() + 2*uniIndent),20);  //расположение кнопки setup
        final static CRectangle portLBounds = new CRectangle(portFBounds.getLeft()+portFBounds.getWidth()/2-20, portFBounds.getTop()+portFBounds.getHeight(), portFBounds.getWidth(), portFBounds.getHeight());
        final static CRectangle launchKBounds = new CRectangle(portFBounds.getLeft()+12, portFBounds.getTop()+30+uniIndent, 160, 160);
}
