package navimage;

import java.nio.file.*;
import java.io.IOException;
import java.util.Scanner;

public class NavImage {
    /*
        NavImage creates a clone of your directory's navigation structue.
        Basically, given a directory, it creates a HTML file index.html, which
        stores all subfiles/directories. For each subdirectory it creates
        corresponding index.html files and process repeats recursively until
        entire directory is exhausted. End product is recreation of entire
        directory structure with each subdirectory containing only 1 file -
        index.html - which stores data about all other files.
    */

    //Each directory/file equals 1 entry.
    //int below stores no. of entries that have been passed to show progress.
    private long entries_scanned = 0;
    private long total_entries = 0;
    
    //In case of heavily nested directory structures with millions of files -
    //- it is neither required nor advisable to create navmap of entire -
    //- directory structure. Only first few layers are sufficient. This -
    //- behavious can be controlled using max_depth. By default all it is inf.
    private int max_depth = Integer.MAX_VALUE;
    
    public static void main(String[] args) {
        //create self object. Would kick start the process.
        NavImage obj = new NavImage();
        
        // src -> Source directory to begin scanning. Example C:\Program Files
        // des -> Where to create image. Example .\navimages\DriveC
        Path src, des;
        
        //Check argument consistency.
        if (args.length >= 2) {
            src = Paths.get(args[0]);
            des = Paths.get(args[1]);
            
            if (args.length == 3)
                obj.max_depth = Integer.parseInt(args[2]);
            else if (args.length > 3) {
                System.err.println("Usage : <source dir> <destination dir> [<max depth>]");
                return;
            }
        } else {
            System.err.println("Usage : <source dir> <destination dir> [<max depth>]");
            return;
        }
        
        //Kickstart the scanning process.
        obj.searchDir (src, des);       
    }
    
    //This is the wrapper method for inner recursive method call.
    public void searchDir (Path original, Path root) {
        
        
        //Began scanning now.
        System.out.println("Initiating scan with depth " + max_depth);
        countFiles (original, 0);
        System.out.println("Total entries in source : " + total_entries);
        System.out.println("Do you want to still create NavImage? (Y/N)");
        System.out.println("TIP : Avoid creation of nav image with large number"
                + " of entries");
        
        Scanner in = new Scanner(System.in);
        char ch = in.next().charAt(0);
        
        if (ch == 'Y' || ch == 'y') {
            //First copy style table.css to directory root
            try {
                Files.createDirectories(root);
                Files.copy(Paths.get("table.css"),
                            Paths.get(root.toString(), "table.css"));
            } catch (Exception e) {
                System.err.println("Unable to generate table.css style file." +
                                "HTML files will not be styled!");
                System.err.println(e);
            }
            DirIndex.stylesheet = Paths.get(root.toString(), "table.css");
            
            searchDir (original, original, root, 0);
        }
        
        //Scanning complete.
        System.out.println("End of operation");
    }
    
    //Actual recursive method.
    // p        -> path of current directory in question
    // original -> path of actual source directory root
    // root     -> root path of destination directory
    // depth    -> which depth is the recursion currently at?
    public long searchDir (Path p, Path original, Path root, int depth) {
        
        //size in bytes of directory
        long size = 0l;
        
        //First retrive all entries
        DirectoryStream<Path> entries;
        try {
            entries = Files.newDirectoryStream(p);
        } catch (Exception e) {
            System.err.println(e);
            return 0l;
        }
        
        //mapped is path to corresponding directory image
        Path mapped = Paths.get(root.toString(),
                                original.relativize(p).toString());

        try {
            Files.createDirectories(mapped);
        } catch (Exception e) {
            System.err.println(e);
            return 0l;
        }

        //index wraps index.html file of current directory. Initialize it.
        DirIndex index;
        try {
            index = new DirIndex (mapped);
        } catch (Exception e) {
            System.err.println(e);
            return 0l;
        }
        
        for (Path entry : entries) {
            long size_entry = -1l;
            if (Files.isDirectory(entry)) {
                if (depth < max_depth)
                    size_entry = searchDir (entry, original, root, depth+1);
                index.addDir (entry, size_entry);
            }
            else if (Files.isRegularFile(entry)) {
                try {
                    size_entry = (long)Files.getAttribute(entry, "size");
                } catch (IOException e) {
                    System.err.println("Unable to fetch size of " + entry);
                }
                index.addFile (entry, size_entry);
            }
            
            size += size_entry;
            
            entries_scanned ++;
            if (entries_scanned % 500 == 0) {
                System.out.println("Entries created : " + entries_scanned);
            }
        }

        //Finally close the index.html file after writing data.
        index.close();
        return size;
    }
    
    //only for counting files
    public void countFiles (Path p, int depth) {
        
        //First retrive all entries
        DirectoryStream<Path> entries;
        try {
            entries = Files.newDirectoryStream(p);
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
        
        for (Path entry : entries) {
            if (Files.isDirectory(entry)) {
                if (depth < max_depth)
                    countFiles (entry, depth+1);
            }
            
            total_entries ++;
            if (total_entries % 1000 == 0) {
                System.out.println("Entries scanned : " + total_entries);
            }
        }
    }
}
