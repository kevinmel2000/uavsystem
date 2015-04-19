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
package Video;

/**
 *
 * @author roylisto
 */
public class AerialVideo {
    
    private String video_dir;
    private String converted_video_dir;
    private String directory_only;
    
    public AerialVideo(){
        
    }
    public AerialVideo(String VideoDirectory){
        this.video_dir=VideoDirectory;
    }
    public void setDirectoryOnly(String VideoDirectory){
        this.directory_only=VideoDirectory;
    }
    public String getDirectoryOnly(){
        return this.directory_only;
    }
    public void setVideoDir(String VideoName){
        this.video_dir=this.directory_only+VideoName;
    }
    public String getVideoDir(){
        return this.video_dir;
    }
    public void setConvertedVideoDir(String ConvertedVideoDir){
        this.converted_video_dir=ConvertedVideoDir;
    }
    public String getConvertedVideoDir(){
        return this.converted_video_dir;
    }
    
}
