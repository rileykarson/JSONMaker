# JSONMaker
Simple program to populate .json files with Magic: the Gathering cards' information using the CrystalCommerce API.

The intent of the program was to be able to preprocess the database held by a website, and turn it into a more easily handled format. Tne information would then be added to a new database, created in order to be distributed onto Android phones with another application.

The problem I was trying to solve was that variants exist of many Magic: the Gathering cards, and the CrystalCommerce API made it a harder process than I wanted my code to be doing on the client devices. I chose to then preprocess these variants, deleting useless information and allowing me to establish links between them myself.
