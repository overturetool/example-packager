# The Overture Example Packager Tool [![Build Status](https://travis-ci.org/overturetool/example-packager.png?branch=master)](https://travis-ci.org/overturetool/example-packager)

This packages up VDM examples from the main Overture repository for distribution, web site display, etc.

## Using a release version in Maven

~~~xml
<plugin>
  <groupId>org.overturetool.build</groupId>
  <artifactId>example-packager-plugin</artifactId>
  <version>${example-packager.version}</version>
  <configuration>
    <!-- <outputDirectory>${project.build.directory}</outputDirectory> -->
    <!-- <outputPrefix>Examples-</outputPrefix> -->
    <outputZipFiles>true</outputZipFiles>
    <outputWebFiles>true</outputWebFiles>
    <slExamples>
      <param>${project.basedir}/VDMSL</param>
    </slExamples>
    <ppExamples>
      <param>${project.basedir}/VDM++</param>
    </ppExamples>
    <rtExamples>
      <param>${project.basedir}/VDMRT</param>
    </rtExamples>
  </configuration>
  <executions>
    <execution>
      <goals>
        <goal>package-examples</goal>
      </goals>
    </execution>
  </executions>
</plugin>
~~~

## Using snapshot versions

..._to be filled in_

## Making a release

See the notes in the @overturetool/astcreator wiki: https://github.com/overturetool/astcreator/wiki/Release-Procedure
