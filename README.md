# NavImage
Tool to create image of directory's navigation structure

Creating an image of HDD is useful, but sometimes you don't want to bother with copying your entire HDD but rather only preserve it's navigation structure.
This tool allows you to do exactly that. It replicates the directory structure of a source directory in the same hierarchical manner as the source.
The only difference is that it does not copy files and their contents. Instead it associates each directory with a index.html file containing information about it's files and also linking to relevant directories.

TL;DR
It replicates entire directory structure in folder only manner and associates each folder with it's index.html file containing list of files originally contained in directory.


# Usage
**WARNING** -> If the .JAR file exists in, say C:\ Drive, then avoid creating image of C:\ directly. Otherwise the program would end up in infinite recursion by repeatedly reading the structure it itself creates.

Requires java 8+

`java -jar NavImage.jar <source directory> <destination> [<depth level>]`

Example -

`java -jar NavImage.jar "C:\Program Files" Drives\C\ProgramFiles 2`

**Note** -> using `depth` is optional. Depth controls upto what depth should the program go in the directory structure with original source considered as depth 0;
If your directory structure consists of large number of files >100k then you should consider depth controlling it otherwise the program might take long time to execute.
If, however, you choose to depth control then the directory size will not be obtained correctly (it will only include contribution from files upto that depth level).

On execution, the program first scans for total number of files in the directory. This process is relatively fast. This is to give you a rough idea of how much time the actual process is going to take.
If the scanning completes in say, 10 seconds then the actual nav image creation can take upto a minute. Time depends on the total number of files needed to be scanned.

## About Harmless warnings -
The program also scans for hidden files and system default files in a directory. This means that it might also try to access system directories.
In such cases the system refuses to let the program in, and the program skips that directory and displays an error message. Thus, if you see error messages like
*AccessDeniedException F:\config.msi* Don't worry, they are harmless.

HTML files are styled using one css files *table.css*. During creation of directory navimage, this css file is copied into the destination directory.
In case the copying fails (due to permission issues or whatever) you can manually copy-paste the table.css into it.

**WARNING** -> If the .JAR file exists in, say C:\ Drive, then avoid creating image of C:\ directly. Otherwise the program would end up in infinite recursion by repeatedly reading the structure it itself creates.
