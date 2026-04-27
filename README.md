# ant-grunt-launcher
Ant Task to run Grunt tasks or npm commands from an Ant build.

The main goal of ant-grunt-launcher is to allow an easy integration between your Ant Build and [Grunt.js](http://gruntjs.com/) or any npm script.
## Latest release:

The latest release is 1.0.0.

## How to use

1. Download the latest jar inside release directory
2. Import the jar in your project
3. Define task in your build.xml

  ```xml 
  <path id="grunt.lib.path">
      <pathelement path="build/lib/ant-grunt-launcher-0.0.2.jar" />
  </path>
  
  <taskdef name="grunt" classname="org.jberta93.gruntlauncher.core.GruntLauncher" classpathref="grunt.lib.path"/>
  ```
4. In your target use the new task

  **Run a Grunt task:**
  ```xml 
  <target name="frontend-target" description="My FE target">
    <grunt gruntfiledir="/frontend-stuff/src" grunttask="build" enviormentPath="/usr/local/bin"/>
  </target>
  ```

  **Run an npm script (e.g. `npm run build-prod`):**
  ```xml
  <target name="frontend-target" description="My FE target">
    <grunt gruntfiledir="/frontend-stuff/src" npmcommand="run build-prod" enviormentPath="/usr/local/bin" executenpminstall="true"/>
  </target>
  ```

  **Run only npm install:**
  ```xml
  <target name="frontend-target" description="My FE target">
    <grunt gruntfiledir="/frontend-stuff/src" executenpminstall="true"/>
  </target>
  ```



## Task Attributes

Attribute | Mandatory | Type | Description
----|----|----|----
gruntfiledir | yes | string | Directory where Gruntfile.js (or package.json) is located in your project
grunttask | no | string | Grunt task name registered in Gruntfile.js (default: `build`). Ignored if `npmcommand` is set
npmcommand | no | string | npm command to run instead of grunt (e.g. `run build-prod`, `test`, `audit`). When set, grunt is not invoked
enviormentPath | no | string | Paths to append to PATH so the script can find grunt/node executables. Example: `/usr/local/bin` on macOS/Linux, `C:\Users\username\AppData\Roaming\npm` on Windows
executenpminstall | no | boolean | Run `npm install` before the main command
executebowerinstall | no | boolean | Run `bower install` before the main command (after npm install if both are enabled)

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
