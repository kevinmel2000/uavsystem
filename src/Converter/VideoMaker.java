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

package Converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 *
 * @author roylisto
 */
public class VideoMaker extends Thread{
    
    public VideoMaker(){
        
    }
    
  
    public void run(){      
        String filePath = "tmp";
        File fileP = new File(filePath);
        String fileRender="render";
        File fileR=new File(fileRender);
        //ffmpeg -f image2 -i "/home/roylisto/Documents/Tugas Akhir/JAva version/UAVSystem/UAVSystem/tmp/result%d.png" -r 24 -s 
        //1360x768 "/home/roylisto/Documents/Tugas Akhir/JAva version/UAVSystem/UAVSystem/render/test2.mp4"
                String[] listCommands={"ffmpeg","-f","image2","-i",fileP.getAbsolutePath()+"/result%d.png","-r","60","-s","1360x768"
                       ,fileR.getAbsolutePath()+"/render.mp4"};
                try{          
                    String s = null;
                    Process process = new ProcessBuilder(listCommands).start();                
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    StringBuffer start= new StringBuffer();
                    // read the output from the command                    
                        while ((s = stdInput.readLine()) != null)
                        {
                            start.append(s);
                           // vc.setCommandStream(s);
                            System.out.println(s);
                        }
                        stdInput.close();
                        // read any errors from the attempted command                
                        while ((s = stdError.readLine()) != null)
                        {
                            start.append(s);    
                            //vc.setCommandStream(s);
                            System.out.println(s);
                        }
                        for(File file: fileP.listFiles()) file.delete();
                       
                }catch(Exception ex){
                    System.out.println(ex.toString());
                } 
            }
        
    
}
