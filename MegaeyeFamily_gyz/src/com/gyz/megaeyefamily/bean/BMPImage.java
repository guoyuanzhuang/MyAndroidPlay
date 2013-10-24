package com.gyz.megaeyefamily.bean;

import java.nio.ByteBuffer;

public class BMPImage {
   
	// --- ˽�г�   
    private final static int BITMAPFILEHEADER_SIZE = 14;    
    private final static int BITMAPINFOHEADER_SIZE = 40;   
   
    // --- λͼ�ļ���ͷ   
    private byte bfType[] = { 'B', 'M' };   
    private int bfSize = 0;   
    private int bfReserved1 = 0;   
    private int bfReserved2 = 0;   
    private int bfOffBits = BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE;   
   
    // --- λͼ��Ϣ��ͷ   
    private int biSize = BITMAPINFOHEADER_SIZE;   
    private int biWidth = 176;   
    private int biHeight = 144;   
    private int biPlanes = 1;   
    private int biBitCount = 24;   
    private int biCompression = 0;   
    private int biSizeImage = biWidth*biHeight*3;   
    private int biXPelsPerMeter = 0x0;   
    private int biYPelsPerMeter = 0x0;   
    private int biClrUsed = 0;   
    private int biClrImportant = 0;   
       
    ByteBuffer bmpBuffer = null;   
       
    public BMPImage(byte[] Data,int Width,int Height){   
        biWidth = Width;   
        biHeight = Height;   
           
        biSizeImage = biWidth*biHeight*3;   
        bfSize = BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE + biWidth*biHeight*3;   
        bmpBuffer = ByteBuffer.allocate(BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE + biWidth*biHeight*3);   
           
        writeBitmapFileHeader();   
        writeBitmapInfoHeader();   
        bmpBuffer.put(Data);   
    }
    //20091130
    public BMPImage(int Width,int Height){   
        biWidth = Width;   
        biHeight = Height;   
           
        biSizeImage = biWidth*biHeight*3;   
        bfSize = BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE + biWidth*biHeight*3;   
        bmpBuffer = ByteBuffer.allocate(BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE);   
           
        writeBitmapFileHeader();   
        writeBitmapInfoHeader();   
    }
       
    public byte[] getByte(){   
        return bmpBuffer.array();   
    }   
       
    private byte[] intToWord(int parValue) {   
   
        byte retValue[] = new byte[2];   
   
        retValue[0] = (byte) (parValue & 0x00FF);   
        retValue[1] = (byte) ((parValue >> 8) & 0x00FF);   
   
        return (retValue);   
    }   
   
    private byte[] intToDWord(int parValue) {   
   
        byte retValue[] = new byte[4];   
   
        retValue[0] = (byte) (parValue & 0x00FF);   
        retValue[1] = (byte) ((parValue >> 8) & 0x000000FF);   
        retValue[2] = (byte) ((parValue >> 16) & 0x000000FF);   
        retValue[3] = (byte) ((parValue >> 24) & 0x000000FF);   
   
        return (retValue);   
   
    }   
       
    private void writeBitmapFileHeader () {   
           
        bmpBuffer.put(bfType);   
        bmpBuffer.put(intToDWord (bfSize));   
        bmpBuffer.put(intToWord (bfReserved1));   
        bmpBuffer.put(intToWord (bfReserved2));   
        bmpBuffer.put(intToDWord (bfOffBits));   
           
    }   
       
    private void writeBitmapInfoHeader () {   
           
        bmpBuffer.put(intToDWord (biSize));       
        bmpBuffer.put(intToDWord (biWidth));       
        bmpBuffer.put(intToDWord (biHeight));       
        bmpBuffer.put(intToWord (biPlanes));       
        bmpBuffer.put(intToWord (biBitCount));       
        bmpBuffer.put(intToDWord (biCompression));       
        bmpBuffer.put(intToDWord (biSizeImage));       
        bmpBuffer.put(intToDWord (biXPelsPerMeter));       
        bmpBuffer.put(intToDWord (biYPelsPerMeter));       
        bmpBuffer.put(intToDWord (biClrUsed));       
        bmpBuffer.put(intToDWord (biClrImportant));    
           
    } 
}
