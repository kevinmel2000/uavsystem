/*
 * Copyright 2014 roylisto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package MosaicEngine;

import boofcv.abst.feature.detect.interest.ConfigFastHessian;
import boofcv.abst.feature.detect.interest.ConfigGeneralDetector;
import boofcv.abst.feature.tracker.PointTrack;
import boofcv.abst.feature.tracker.PointTracker;
import boofcv.abst.sfm.d2.ImageMotion2D;
import boofcv.abst.sfm.d2.MsToGrayMotion2D;
import boofcv.alg.filter.derivative.GImageDerivativeOps;
import boofcv.alg.sfm.d2.StitchingFromMotion2D;
import boofcv.alg.tracker.klt.PkltConfig;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.feature.tracker.FactoryPointTracker;
import boofcv.factory.sfm.FactoryMotion2D;
import boofcv.gui.feature.VisualizeFeatures;
import boofcv.gui.image.ImageGridPanel;
import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import boofcv.io.MediaManager;
import boofcv.io.image.UtilImageIO;
import boofcv.io.image.SimpleImageSequence;
import boofcv.io.video.VideoMjpegCodec;
import boofcv.io.wrapper.DefaultMediaManager;
import boofcv.io.wrapper.images.JpegByteImageSequence;
import boofcv.misc.BoofMiscOps;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSingleBand;
import boofcv.struct.image.ImageType;
import boofcv.struct.image.MultiSpectral;
import georegression.struct.homo.Homography2D_F64;
import georegression.struct.point.Point2D_F64;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import Converter.VideoMaker;

/**
 *
 * @author roylisto
 */
public class MosaicVideo extends Thread{
    
    private ConfigGeneralDetector confDetector;
    private ConfigFastHessian configFH;
    private PointTracker<ImageFloat32> tracker;
    private ImageMotion2D<ImageFloat32,Homography2D_F64> motion2D;
    private ImageMotion2D<MultiSpectral<ImageFloat32>,Homography2D_F64> motion2DColor;
    private StitchingFromMotion2D<MultiSpectral<ImageFloat32>,Homography2D_F64> stitch;
    private MediaManager media;
    private SimpleImageSequence<MultiSpectral<ImageFloat32>> video;
    private MultiSpectral<ImageFloat32> frame;
    private Homography2D_F64 shrink ;
    private ImageGridPanel gui;
    private UtilImageIO saveImg;
    boolean enlarged = false;
    private VideoMaker vm;
    public MosaicVideo(String filename) {
        saveImg=new UtilImageIO();
        confDetector=new ConfigGeneralDetector();
        confDetector.threshold = 1;
        confDetector.maxFeatures = 300;
	confDetector.radius = 3;
        configFH = new ConfigFastHessian();
        configFH.initialSampleSize = 2;
        configFH.maxFeaturesPerScale = 200;
        tracker=FactoryPointTracker.dda_FH_SURF_Stable(configFH, null, null, ImageFloat32.class);
	motion2D =FactoryMotion2D.createMotion2D(200,3,2,30,0.6,0.5,false,tracker,new Homography2D_F64());
	motion2DColor = new MsToGrayMotion2D<ImageFloat32,Homography2D_F64>(motion2D,ImageFloat32.class);
	stitch = FactoryMotion2D.createVideoStitchMS(0.5, motion2DColor, ImageFloat32.class);
        media = DefaultMediaManager.INSTANCE;
        video =media.openVideo(filename, ImageType.ms(3, ImageFloat32.class));
        frame = video.next();
        shrink = new Homography2D_F64(0.5,0,frame.width/4,0,0.5,frame.height/4,0,0,1);
	shrink = shrink.invert(null);
	stitch.configure(frame.width,frame.height,shrink);
	stitch.process(frame);
        gui = new ImageGridPanel(1,1);
	gui.setImage(0,0,new BufferedImage(frame.width,frame.height,BufferedImage.TYPE_INT_RGB));
	//gui.setImage(0,1,new BufferedImage(frame.width,frame.height,BufferedImage.TYPE_INT_RGB));
        
	gui.setPreferredSize(new Dimension(frame.width,frame.height));
	ShowImages.showWindow(gui,"UAV Sight System C2Corp");	
    }
    
    public void run(){
        
        try{
            int loop=0;
            MultiSpectral<ImageFloat32> foo = stitch.getStitchedImage();
            while( video.hasNext() ) {
               // System.out.println("rendering");
    
			frame = video.next();
                        try{
                            //if( !stitch.process(frame) )
                            stitch.process(frame);
			//	throw new RuntimeException("You should handle failures");
                        }catch(Exception ex){
                            System.out.println(ex.toString());
                        }

			// if the current image is close to the image border recenter the mosaic
			StitchingFromMotion2D.Corners corners = stitch.getImageCorners(frame.width,frame.height,null);
			if( nearBorder(corners.p0,stitch) || nearBorder(corners.p1,stitch) ||
					nearBorder(corners.p2,stitch) || nearBorder(corners.p3,stitch) ) {
				stitch.setOriginToCurrent();

				// only enlarge the image once
				if( !enlarged ) {
					//enlarged = true;
					// double the image size and shift it over to keep it centered
					int widthOld = stitch.getStitchedImage().width;
					int heightOld = stitch.getStitchedImage().height;

					int widthNew = widthOld+(frame.width/2);
					int heightNew = heightOld+(frame.height/2);

					int tranX = (widthNew-widthOld)/2;
					int tranY = (heightNew-heightOld)/2;

					Homography2D_F64 newToOldStitch = new Homography2D_F64(1,0,-tranX,0,1,-tranY,0,0,1);

					stitch.resizeStitchImage(widthNew, heightNew, newToOldStitch);
                                        //stitch.configure(widthNew, heightNew, newToOldStitch);
					gui.setImage(0, 0, new BufferedImage(widthNew, heightNew, BufferedImage.TYPE_INT_RGB));
				}
				corners = stitch.getImageCorners(frame.width,frame.height,null);
			}
			// display the mosaic
			//ConvertBufferedImage.convertTo(frame,gui.getImage(0, 0),true);
			ConvertBufferedImage.convertTo(stitch.getStitchedImage(), gui.getImage(0, 0),true);
                        //
			gui.repaint();
                        //app.process(sequence);

			// throttle the speed just in case it's on a fast computer
			BoofMiscOps.pause(50);
                        //saveImg.saveImage(gui.getImage(0, 0), "tmp/result"+loop+".png");
                        saveImg.saveImage(ConvertBufferedImage.convertTo_F32(foo,null,true),"tmp/result"+loop+".bmp");
                        loop++;
		}
                //UtilImageIO.saveImage(gui.getImage(0, 1), "result.jpg");
                //saveImg.saveImage(gui.getImage(0, 0), "map/map.png");
                saveImg.saveImage(ConvertBufferedImage.convertTo_F32(foo,null,true),"map/stitched.png");
                //MultiSpectral<ImageFloat32> foo = stitch.getStitchedImage();
                //UtilImageIO.saveImage(ConvertBufferedImage.convertTo_F32(foo,null,true),"map/stitched.png");
                //vm=new VideoMaker();
                //vm.Run();
                //vm.start();
                //System.out.println("finished");
           
            
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
    }
    private static boolean nearBorder( Point2D_F64 p , StitchingFromMotion2D<?,?> stitch ) {
		int r = 10;
		if( p.x < r || p.y < r )
			return true;
		if( p.x >= stitch.getStitchedImage().width-r )
			return true;
		if( p.y >= stitch.getStitchedImage().height-r )
			return true;

		return false;
	}
    
    
}
