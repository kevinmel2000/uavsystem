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

import Controller.ConvertedButtonListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

/**
 *
 * @author roylisto
 */
public class VideoConverter {
    private String defaultFile;
    private String convertedFile;   
    private ConverterThread myThread;
    private ConvertedButtonListener butListener;
    public VideoConverter(String fileDir,String convertOutput,ConvertedButtonListener buttonListener){
        this.defaultFile=fileDir;
        this.convertedFile=convertOutput;
        this.butListener=buttonListener;        
    }
    public void convertToMjpeg(){                             
        String[] listCommands={"ffmpeg","-i",defaultFile,"-qscale","0",convertedFile};
        myThread=new ConverterThread(listCommands,this);
        myThread.start();                
    }    
    public void setCommandStream(String stream){
        butListener.setCommandOutput(stream);
    }
    class ConverterThread extends Thread{
        VideoConverter vc;
        String[] command;
        ConverterThread(String[] command,VideoConverter vc){        
            this.command=command;            
            this.vc=vc;
        }        
        public void run(){      
            synchronized(vc){
                try{          
                    String s = null;
                    Process process = new ProcessBuilder(command).start();                
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    StringBuffer start= new StringBuffer();
                    // read the output from the command                    
                        while ((s = stdInput.readLine()) != null)
                        {
                            start.append(s);
                            vc.setCommandStream(s);
                        }
                        stdInput.close();
                        // read any errors from the attempted command                
                        while ((s = stdError.readLine()) != null)
                        {
                            start.append(s);    
                            vc.setCommandStream(s);
                        }
                }catch(Exception ex){
                    System.out.println(ex.toString());
                } 
            }
        }
    }
}
