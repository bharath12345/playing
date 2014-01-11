{{{
    "title": "Build Dojo 1.7/1.8/1.9 with Maven",
    "tags" : [ "javascript", "dojo" ],
    "category" : "technology",
    "date" : "07-18-2013",
    "description" : ""
}}}

I have been a Dojo user for many years now. Also use many JavaScript libraries (jQuery, backbone, bootstrap, D3, highsoft) all the while but Dojo is what I really love. I would not embark on any "professional" development work without being armed with Dojo. But I rest my opinions and comparisons of different JS libraries for a different blog. Here the context is to "build" Dojo. After all every professional project should do a build of their JS - compilers like Google Closure can find bugs, obfuscate and eventually make execution faster.

I still am mainly a Java programmer (the enterprise products I have built are predominantly in Java… time split between Java/JavaScript may be 70/30). So am used to Maven as my primary build tool. And Maven I shall use to build Dojo.

Folks who have not tried to build Dojo should probably start-off by reading these two articles -

* [Creating Builds](http://dojotoolkit.org/documentation/tutorials/1.9/build/) from Dojo documentation
* [Creating custom Dojo builds in Maven](http://www.mahieu.org/?p=3) 

The last article is very good but slightly dated. And here is what I propose to add to it -

1. Use dojo v1.9 (v1.8 and v1.7 with AMD should also work perfectly)
2. I use WebStorm as my JavaScript IDE. It has excellent contextual support including that for Dojo. However, it requires Dojo to be at a constant referencable path from where it could index. Once the indexes are built, typing a "." after an object should show up the list of methods and variables belonging to that object. This is extremely useful for fast development
3. Dojo builds are slow. A typical build from source download to unzip to compile to build WAR can take anywhere between 5 to 15 minutes. This can be painful and needs to be made faster

Now, here is the how…

#### Task 1: Installing Dojo in Maven Repository and Unpack Task
This is no different from the Step 1 & 2 in Mahieu blog. The unzipped sources are placed in src/main/js of my maven hierarchy. I dont do any renaming of this directory.

#### Task 2: Move Dojo sources
The unpack task unzips the dojo sources in "src/main/js/dojo-release-${dojo.version}-src" directory. This is okay but not good for repeated builds. I would like a structure like shown in the picture below - all my JS libraries under src/main/js. 

![image](https://raw.github.com/bharath12345/bharath12345.github.io/master/images/dojo%20blog/dojo%20blog%20structure.png) 

This structure helps in one major way - it helps my WebStorm IDE to index the JS. The Dojo JS are always in "src/main/js" alongwith other libraries and WebStorm understands this very well!

I use antrun for its ability to run **parallel copy tasks** - parallelism helps in making the build much faster. And I **delete** the original unzipped directory at the end.

    <plugin>
    	<artifactId>maven-antrun-plugin</artifactId>
        <executions>
        	<execution>
            	<id>Copy Dojo</id>
                <configuration>
                	<tasks>
                   		<parallel>
                       		<copy todir="${js-dir}/" failonerror="false">
                           		<fileset dir="${dojoSrc}">
                               		<include name="dijit/"/>
                               </fileset>
                           </copy>
                           <copy todir="${js-dir}/" failonerror="false">
                           		<fileset dir="${dojoSrc}">
                               		<include name="dojox/"/>
                               </fileset>
                           </copy>
                           <copy todir="${js-dir}/" failonerror="false">
                           		<fileset dir="${dojoSrc}">
                               		<include name="dojo/"/>
                               </fileset>
                           </copy>
                           <copy todir="${js-dir}/" failonerror="false">
                           		<fileset dir="${dojoSrc}">
                               		<include name="util/"/>
                               </fileset>
                           </copy>
                       </parallel>
                       <delete dir="${dojoSrc}" quiet="true"/>
                   </tasks>
                </configuration>
                <phase>process-sources</phase>
                <goals>
                	<goal>run</goal>
                </goals>
           </execution>
        </executions>
    </plugin>
                            
                            
#### Task 3: Build Dojo
For this again, I use the antrun plugin. This build leads to creation of dojo/dijit/dojox directories under src/main/js.

    <plugin>
    	<artifactId>maven-antrun-plugin</artifactId>
    	<executions>
    		<execution>
    			<id>AppsOne dojo ${dojo.version} Custom Build</id>
        		<phase>compile</phase>
        		<configuration>
        			<tasks>
        				<parallel>
            				<java classname="org.mozilla.javascript.tools.shell.Main"
                  		fork="true" maxmemory="512m" failonerror="false"
                  		classpath="${shrinksafe-dir}/js.jar${path.separator}${closure-dir}/compiler.jar${path.separator}${shrinksafe-dir}/shrinksafe.jar">
 	                			<arg value="${js-dir}/dojo/dojo.js"/>
    	            			<arg value="baseUrl=${js-dir}/dojo"/>
                    			<arg value="load=build"/>
                    			<arg line="--profile ${basedir}/dashboard.profile.js"/>
                    			<arg value="--release"/>
               				</java>
            			</parallel>
         			</tasks>
         		</configuration>
         		<goals>
         			<goal>run</goal>
         		</goals>
    		</execution>
    	</executions>
    </plugin>

#### Task 4: The Dojo Profile
[This is the link](https://github.com/bharath12345/uiDashboard/blob/master/uiJS/dashboard.profile.js) to the profile script I use. It has a lot of comments for the reader to understand. One can find a lot of options to tune the Dojo build by specifying options in the profile. The profile specifies thus -

* I name my JS project as "Dashboard" - so I want the built artifacts to be in the target/dashboard directory
* Use the closure compiler
* I use both dgrid and gridx in my project along with its dependencies (xstyle, dbind, put-selector) - so those have to be included
* Including my project's JS - which are present in the "dashboard" directory and are AMD complying JS
* Finally I want to see less verbose prints on my console - so I set the logging level to SEVERE

#### Task 5: Clean the Uncompressed JavaScript
Dojo build generates minimized JS. And in the process of doing so it retains the originial JS but renames them to have "uncompressed" in their filenames. This is useful for debugging purposes. But surely, we dont want these uncompressed JS to be part of the built WAR. It increases the size of the WAR (at least doubles it - taking it well above 50MB!). So, a task to remove these uncompressed JS from target directory is required. This maven stub does just that -

     <plugin>
     	<artifactId>maven-clean-plugin</artifactId>
        <version>2.5</version>
        <executions>
        	<execution>
            	<id>clean-js</id>
                <phase>prepare-package</phase>
                <goals>
                	<goal>clean</goal>
                </goals>
                <configuration>
                <filesets>
                	<fileset>
                		<directory>${release-dir}/dojo</directory>
                		<includes>
                   			<include>**/*uncompressed.js</include>
                		</includes>
                		<followSymlinks>true</followSymlinks>
                   </fileset>
                </configuration>
             </execution>
          </executions>
    </plugin>

#### Task 6: Copy Other JavaScript libraries
By now, the "target/dashboard/js" has all the dojo sources along with project specific built in it. The next task is to copy other JS library dependencies. In my project, I typically use D3, jQuery and jsPlumb. So here is I copy them into this directory into maven's target by stub's like these -

    <plugin>
    	<artifactId>maven-resources-plugin</artifactId>
        <version>2.6</version>
        <executions>
        	<execution>
            	<id>copy-d3</id>
                <phase>process-resources</phase>
                <goals>
                	<goal>copy-resources</goal>
                </goals>
                <configuration>
                	<outputDirectory>${gui.target.gui.location}/js/d3</outputDirectory>
                   <resources>
                   		<resource>
                       		<directory>${js-dir}/d3</directory>
                       </resource>
                   </resources>
                </configuration>
              </execution>
          </executions>
    </plugin>
    
#### Task 7: A fast build profile
Dojo ZIP is upwards of 35MB in size with thousands of files. Downloading, unarchiving and moving it around makes it a heavy duty operation which is painfully slow. This makes a maven profile for faster build absolutely necessary. This profile does the following -

* Assumes the presence of unarchived dojo bundle in the source tree under "src/main/js"
* It thus does none of the unarchiving or file movements and starts off directly with a closure build
* Does not delete the dojo/dijit/dojox directories from under src/main/js after the build is complete
                             
Readers can refer to this [pom.xml](https://github.com/bharath12345/uiDashboard/blob/master/uiJS/pom.xml) from one of my projects on GitHub. It has all that I have described above. Ping me if you run into any issues using my code, understanding my blog or anything else. Thanks for reading!