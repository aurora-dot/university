
.        :   ::::::.    :::..,:::::: .::::::..::    .   .:::.,:::::: .,::::::::::::::::. .,:::::: :::::::..   
;;,.    ;;;  ;;;`;;;;,  `;;;;;;;'''';;;`    `';;,  ;;  ;;;' ;;;;'''' ;;;;'''' `;;;```.;;;;;;;'''' ;;;;``;;;;  
[[[[, ,[[[[, [[[  [[[[[. '[[ [[cccc '[==/[[[[,'[[, [[, [['   [[cccc   [[cccc   `]]nnn]]'  [[cccc   [[[,/[[['  
$$$$$$$$"$$$ $$$  $$$ "Y$c$$ $$""""   '''    $  Y$c$$$c$P    $$""""   $$""""    $$$""     $$""""   $$$$$$c    
888 Y88" 888o888  888    Y88 888oo,__88b    dP   "88"888     888oo,__ 888oo,__  888o      888oo,__ 888b "88bo,
MMM  M'  "MMMMMM  MMM     YM """"YUMMM"YMmMY"     "M "M"     """"YUMMM""""YUMMM YMMMb     """"YUMMMMMMM   "W" 

---- Displaying the data ----

There is a gridpane full of buttons with the same dimentions of the minefield object. 
The buttons are an object called MineButton, it extends the button but allows you to write the
buttons position so you can easily access without having to loop throught the grid to find the
position.

Each button has a function attached, buttonFunction, this function marks or steps on the tile.
If it isn't marked and you right click on the button, it marks it with a flag and then sets
the tile at that position in the minefield to marked too. If it is and you right click, it
removes the graphic and then toggles the mark again at that position. When stepping, it first
checks if where you stepped is a mine, if it is you will loose, if it isn't, it steps through
the grid and then refreshes the GUI to show the updated version. When a tile is revealed it is
locked.

--- How the data is fetched ---

The data is fetched through the minefield object itself, I have opened access to reading all
fields from the minetiles at the position you enter, and limit writing to minetile revealed field 
so the user can't exploit saving at the end of the game and remmebering the positions as revealing
locks the button and the second writefunction is through a minetile object through minefield to
mark a tile.

--- How the GUI is kept up to date ---

Every time you step, the field is refreshed. It goes through each button checks the
properties according to the heirarchical nature of using if statements and selects the
correct thing to display, a tile which is marked will be accessed before a revealed mine.

--- Altering the grid ---

Left clicking steps from that minetile and right clicking marks the minetile, both are directly
accessed through the minefield object. Clicking on a mine makes an explosion.

--- Saving ---

The data is searialised by getting each property of a mine, adding it to an list, and then
adding that list into another. The dimentions of the minefield are to be stored first and
then the tiles are stored after.

--- Opening ---

If opening a correct file it will first create a new minefield object in the size of the
dimentions stored at index 0 of the seralised object. Afterwards, inside of the minefield object
it loops through the lists in the searialised list and sets the tiles properties according to the
data within. 

--- Custom size grid ---

Max rows is 30 and max column is 60, I didn't figure out how to dynamically change the sizes of the
buttons without breaking the image inside which breaks the consistancy.


--- Debug ---
I left the debug tools in so if you need to use them you can but they can sometimes be a bit iffy.

Thanks!