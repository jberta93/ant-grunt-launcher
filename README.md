# ant-grunt-launcher
Ant Task to call your Grunt Task

The main goal of ant-grunt-launcher is to allow an easy integration between your Ant Build and [Grunt.js](http://gruntjs.com/)

## How to use

1. Download the latest jar inside release directory
2. Import the jar in your project
3. Define task in your build.xml

  ```xml 
  <path id="grunt.lib.path">
      <pathelement path="build/lib/ant-grunt-launcher-0.0.1.jar" />
  </path>
  
  <taskdef name="grunt" classname="org.jberta93.gruntlauncher.core.GruntLauncher" classpathref="grunt.lib.path"/>
  ```
4.  In your target use the new task

  ```xml 
  <target name="frontend-target" description="My FE target">
    <grunt gruntfiledir="/frontend-stuff/src" grunttask="build" enviormentPath="/usr/local/bin" executenpminstall="false" executebowerinstall="false"/>
  </target>
  ```



## Task Attributes

Attribute | Mandatory | Type | Description
----|----|----|----
gruntfiledir | yes  | string | Directory where Gruntfile.js is located in your project
grunttask | yes  | string | Task name registered in Gruntfile.js
enviormentPath | no  | string | Paths to add Enviroment Path to allow the script to use grunt/node executable. For instance: /usr/local/bin for OSX or C:\Users\username\AppData\Roaming\npm for Microsoft Windows
executenpminstall | no  | boolean | If you want to execute npm install before grunt task
executebowerinstall | no  | boolean | If you want to execute bower install before grunt task and after npm install if enabled

## How to build

1. Clone the repository
2. The project has been developed using Eclipse
3. Put as Source Folder the src directory
4. Add to build path the jars contained in lib folder

### Create jar

To create a valid jar to test your modify you have to launch target ant called jar. 

The new jar will be created in folder deploy.

If you want to create a new release you need to change in build.xml the property project.release.version and execute target called jar-release


# License

This project is released over [MIT license](http://opensource.org/licenses/MIT "MIT License")

# Author

[Lorenzo Bertacchi](http://www.lorenzobertacchi.it/?lang=en)
