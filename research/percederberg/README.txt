README.txt, v.1.2, 2003-01-30, Per Cederberg <per@percederberg.net>
===================================================================


1. Introduction
---------------

  This package contains a Tetris game written in Java. The game can 
  be run either as an applet or from the command-line.

  This work is free software; you can redistribute it and/or modify 
  it under the terms of the GNU General Public License as published 
  by the Free Software Foundation; either version 2 of the License, 
  or (at your option) any later version.
 
  This work is distributed in the hope that it will be useful, but 
  WITHOUT ANY WARRANTY; without even the implied warranty of 
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
  separate LICENCE.txt file for more details.



2. Background
-------------

  The history of Tetris is described elsewhere, most notably by one
  of the original authors in the following web page:

    http://vadim.www.media.mit.edu/Tetris.htm



3. Installation & Running
-------------------------

  This game requires a Java 1.1 compatible JVM in order to run. It 
  can be run directly from the tetris.jar file with this command:
  
    java -jar lib/tetris.jar

  It can also be embedded as an applet in a web page, with the 
  following HTML code:
  
    <applet archive="tetris.jar" code="net.percederberg.tetris.Main" 
            width="330" height="400" >
        Your browser does not support Java applets.
    </applet>

  Both the stand-alone and applet versions of the game support the
  a number of configuration parameters, either given as properties on
  the command-line (-Dname=value) or as an applet parameter
  (<param name="" value="">). All color values should be specified 
  with hexadecimal values for their red, green, and blue components, 
  in the format '#RRGGBB' (like '#ff8080' for example).
  
    tetris.color.background       - Game background color.
    tetris.color.label            - Text color of the labels.
    tetris.color.button           - Start and pause button bolor.
    tetris.color.board.background - Background game board color.
    tetris.color.board.message    - Game board message color.
    tetris.color.figure.square    - Color of the square figure.
    tetris.color.figure.line      - Color of the line figure.
    tetris.color.figure.s         - Color of the 's' curved figure.
    tetris.color.figure.z         - Color of the 'z' curved figure.
    tetris.color.figure.right     - Color of the right angle figure.
    tetris.color.figure.left      - Color of the left angle figure.
    tetris.color.figure.triangle  - Color of the triangle figure.
        
  The game can be controlled with the following keyboard controls:

    <Left Arrow>
        Moves the falling figure to the left.
    <Right Arrow>
        Moves the falling figure to the right.
    <Up Arrow> or <Space>
        Rotates the falling figure counter-clockwise.
    <Shift> + <Up Arrow> or <Space>
        Rotates the falling figure clockwise.
    <Ctrl> + <Up Arrow> or <Space>
        Rotates the falling figure randomly.
    <Down Arrow>
        Moves the falling figure all the way down.
    N
        Toggles preview of next figure.
    P
        Pauses or resumes the game.
    S
        Increases the falling speed (by one of ten steps).



4. Acknowledgements
-------------------

  This program is the result of many hours of hard work. The persons
  involved as of this release are:
  
    Per Cederberg <per@percederberg.net>
        Java implementation of this game.
    Jeff Thomas <jeffthomas@jeffthomas.com>
        Beta-testing of version 1.2
    Your Name
        When you have contributed to this project.

  This game wouldn't have been possible without the original idea 
  (and implementation) by Alexey Pajitnov, Dmitry Pavlovsky, and 
  Vadim Gerasimov in 1985-86.



5. Contributions
----------------

  If you find this program useful or interesting, please consider 
  contributing to this project. The easiest way to contribute is to 
  report errors or issues regarding the use of this program. If you
  are able to fix the issue in the sources or provide test data to 
  demonstrate the problem, it would be ever better. Of course, you
  are also very welcome to do any additions or improvements to the 
  source code.
  
  Please send your suggestions, improvements and/or comments to 
  <per@percederberg.net>. Also consider sending a mail if you deploy
  this game on your web site, or if you redistribute or extend this 
  program any way. By doing so, you are guaranteed to be kept 
  informed about new releases, bug fixes or similar.
