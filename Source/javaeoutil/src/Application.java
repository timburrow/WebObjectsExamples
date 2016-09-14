/*
 * © Copyright 2006 - 2007 Apple, Inc. All rights reserved.
 *
 * IMPORTANT:  This Apple software is supplied to you by Apple Computer,
 * Inc. (“Apple”) in consideration of your agreement to the following 
 * terms, and your use, installation, modification or redistribution of 
 * this Apple software constitutes acceptance of these terms.  If you do
 * not agree with these terms, please do not use, install, modify or
 * redistribute this Apple software.
 *
 * In consideration of your agreement to abide by the following terms, 
 * and subject to these terms, Apple grants you a personal, non-
 * exclusive license, under Apple’s copyrights in this original Apple 
 * software (the “Apple Software”), to use, reproduce, modify and 
 * redistribute the Apple Software, with or without modifications, in 
 * source and/or binary forms; provided that if you redistribute the  
 * Apple Software in its entirety and without modifications, you must 
 * retain this notice and the following text and disclaimers in all such
 * redistributions of the Apple Software.  Neither the name, trademarks,
 * service marks or logos of Apple Computer, Inc. may be used to endorse
 * or promote products derived from the Apple Software without specific
 * prior written permission from Apple.  Except as expressly stated in
 * this notice, no other rights or licenses, express or implied, are
 * granted by Apple herein, including but not limited to any patent
 * rights that may be infringed by your derivative works or by other
 * works in which the Apple Software may be incorporated.
 *
 * The Apple Software is provided by Apple on an "AS IS" basis.  APPLE 
 * MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 * THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS
 * USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS. 
 *
 * IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT,
 * INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE,
 * REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE,
 * HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING
 * NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver._private.WOProperties;
import com.webobjects.eoaccess.EOAdaptor;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.synchronization.EOSchemaGenerationOptions;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSPropertyListSerialization;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation._NSUtilities;
import com.webobjects.jdbcadaptor.JDBCAdaptor;

public class Application extends WOApplication {
    public static final String UsageString =
    "Command line utility to perform database and EOF oriented tasks.\n" +
    "Syntax: javaeoutil dump/connect/convert/help\n" +
    "  javaeoutil dump <model> [-source <source> [sourcefile]] -dest <dest> [destfile] [-schemaCreate <options>] [-schemaDrop <options>] [-postInstall] [-force] [-connDict <connection dictionary>] [-entities <entities>] [-modelGroup <modelGroup>]\n" +
    "  javaeoutil convert <model> <adaptorName> <connectionDictionary> <outFileName>\n" +
    "  javaeoutil connect <adaptorName> <connectionDictionary>\n" +
    "  javaeoutil help\n";

    public static final String HelpString = "Command line utility to perform database and EOF oriented tasks.\n" +
        "Syntax: javaeoutil dump/connect/convert\n" +
        "  javaeoutil dump <model> [-source <source> [sourcefile]] -dest <dest> [destfile] [-schemaCreate <options>] [-schemaDrop <options>] [-postInstall] [-force] [-connDict <connection dictionary>] [-entities <entities>] [-modelGroup <modelGroup>]\n" +
        "	This command reads data from <source> and dumps it to <dest>. The dump may\n" +
        "	include bits of DML that create bits of database schema. The -source flags is\n" +
        "	optional if only schema is to be created (i.e. no data is being dumped.) The\n" +
        "	following is a list of possible values for each of the arguments in the command:\n" +
        "	   model -- must be the name of an eomodel or eomodeld file\n" +
        "	   source -- plist or database(uses model to connect)\n" +
        "	   sourcefile -- name of file to read plist from\n" +
        "	   dest -- plist or script(both are written to stdout unless destfile is specified) or database(uses model to connect)\n" +
        "	   destfile -- name of file to write plist\n" +
        "	   entities -- a subset of the entities in the model (all are used be default)\n" +
    "	   options -- tables, primaryKeySupport, primaryKeyConstraints, or foreignKeyConstraints\n" +
        "	   postInstall -- looks in the userInfo dictionary for an array of SQL strings to process\n" +
        "                        after any schema creation and data dumping is done\n" +
        "	   force -- Do not quit processing after database error\n" +
        "         connDict -- A substitute connection dictionary\n" +
        "         modelGroup -- A list of models to create a model group.  (Allows you to use models\n" +
        "                       not in a framework.  Model names must be absolute paths.)\n" +
        "\n" +
        "         Example: javaeoutil dump Movies.eomodeld -source plist -dest database -schemaCreate tables primaryKeySupport foreignKeyConstraints < MovieData.plist\n" +
        "	   	Creates the tables in the database, reads data in plist format from stdin,\n" +
        " 		stores the data in the database listed in the connection dictionary of model,\n" +
        "		creates primary key support in the database, and creates foreign key.\n" +
        "		constraints corresponding to the relationship definitions.\n" +
        "\n" +
        "  javaeoutil convert <model> <adaptorName> <connectionDictionary> <outFileName>\n" +
        "	This command converts the type mapping in a model for the data source specified by the connection dictionary URL.\n" +
        "	The connection dictionary in the new model is set to connectionDictionary.\n" +
        "\n" +
        "	   model -- must be the name of an eomodel or eomodeld file\n" +
        "	   adaptorName -- JDBC or JNDI.\n" +
        "	   connectionDictionary -- string in property list format specifying the URL for the data source (required)\n" +
        "               and username and password (if required by that data source)." +
        "	   outFileName -- the name of a directory to write the converted model\n" +
        "\n" +
        "         Example: javaeoutil convert Movies.eomodeld JDBC '{ URL = jdbc:openbase://127.0.0.1/Movies; }' M.eomodeld\n" +
        "	   	Converts types in Movies.eomodeld file to OpenBase types and writes the converted\n" +
        "		model to ./M.eomodel\n" +
        "  javaeoutil connect <adaptorName> <connectionDictionary>\n" +
        "	This command attempts to connect to a data source using the adaptor named adaptorName\n" +
        "	with connectionDictionary. It returns an exit status of 0 if successful and 1\n" +
        "	otherwise. This is primarily useful for scripts.\n" +
        "\n" +
        "	   model -- must be the name of an eomodel or eomodeld file\n" +
        "	   adaptorName -- JDBC or JNDI.\n" +
        "	   connectionDictionary -- string in property list format specifying the URL for the data source (required)\n" +
        "               and username and password (if required by that data source)." +
    "         Example: javaeoutil connect JDBC '{ URL = \"jdbc:oracle:thin:@sqasun2:1521:sqa\"; username = movies; password = movies; }'\n" +
        "	   	Attempts to connect to an Oracle database via JDBC at the given URL.\n" +
        "  javaeoutil help\n" +
        "	Prints this text.\n";

    private static final String _SelName = "applicationDidFinishLaunching";
    private static String[] _oldArgs;
    private static String _user_dir;
    private static EOModelGroup _modelGroup;

    // Keys in the JDBC adaptor to allow us to switch the connection dictionary information when converting models.
    private static final String JDBCInfoKey = "jdbc2Info";
    private static final String TypeInfoKey = "typeInfo";
    private static final String UsernameKey = "username";
    private static final String PasswordKey = "password";
    public static final String DriverKey = "driver";

    public static void main(String args[]) {
        _oldArgs = args;

        WOApplication.main(args, Application.class);
    }

    public Application() {
        super();
        NSLog.out.appendln("Welcome to " + this.name() + " !");
        /* ** put your initialization code in here ** */

        NSNotificationCenter nc = NSNotificationCenter.defaultCenter();
        Class[] parameterTypes = new Class[] {NSNotification.class};
        NSSelector sel = new NSSelector(_SelName, parameterTypes);

        nc.addObserver(this, sel, WOApplication.ApplicationDidFinishLaunchingNotification, null);

        //We disable the automatic opening of a new browser window.
        System.setProperty(WOProperties._AutoOpenBrowserKey, "false");

        //Get the directory from which the application has been launched.
        _user_dir = System.getProperty(_NSUtilities.WebObjectsUserDirectoryPropertyKey);
    }

    public void applicationDidFinishLaunching(NSNotification notification) {
        old_main(_oldArgs);
        super.terminate();
        System.exit(0);
    }

    public static void old_main(String args[]) {
        NSMutableArray newArgs = new NSMutableArray(args.length);

        for (int i = 0; i < args.length; i++) {
            newArgs.addObject(args[i]);
        }

        if (newArgs.count() < 1) {
            _arg_error("Not enough arguments.");
        }

        String command = null;

        try {
            command = (String) newArgs.objectAtIndex(0);

            if (command.equals("dump")) {
                dumpDatabase(newArgs);
            } else if (command.equals("convert")) {
                convertModel(newArgs);
            } else if (command.equals("connect")) {
                connectToDatabase(newArgs);
            } else if (command.equals("help")) {
                printHelp();
            } else {
                _arg_error("Invalid command name: " + command + "\n");
            }

        } catch (Throwable e) {
            NSLog.out.appendln("Exception running " + command + ":");
            NSLog.out.appendln(e);
        }

    }

    private static String _canonicalPath(String path) {
        String canonical;
        String original_user_dir = System.getProperty("user.dir");

        System.setProperty("user.dir", _user_dir);

        try {
            canonical = (new File(path)).getCanonicalPath();
        } catch (IOException e) {
            NSLog.err.appendln("Cannot obtain canonical path for: " + path);
            throw new NSForwardException(e);
        } finally {
            System.setProperty("user.dir", original_user_dir);
        }

        return canonical;
    }

    /*
     * Print an optional error message and the usage string. Then exit with status of 1.
     */
    private static void _arg_error(String error) {
        NSLog.err.appendln(error);
        NSLog.err.appendln(UsageString);
        System.exit(1);
    }

    /*
     * Parse the argument list and stuff it into a Dictionary.  There are three
     * types of flags.  Bools (flags that set options to yes), single argument,
     * and list which can have an arbitrary number of args.  This function will step
     * through the args and fill a dictionary with the flags, their values, and
     * a catch-all list which collects the "stray" args.
     */
    private static NSDictionary _argumentDictionaryForArguments(NSArray arguments, NSArray boolOptions, NSArray singleOptions, NSArray listOptions) {
        NSMutableDictionary argDict = new NSMutableDictionary();
        NSMutableArray unboundArray = new NSMutableArray();
        int count = arguments.count();
        int i = 0;

        while (i < count) {
            String arg = (String) arguments.objectAtIndex(i);

            if (arg.charAt(0) != '-') {
                unboundArray.addObject(arg);
            } else if (boolOptions.containsObject(arg)) {
                argDict.setObjectForKey("YES", arg);
            } else if (singleOptions.containsObject(arg)) {
                i++;
                argDict.setObjectForKey(arguments.objectAtIndex(i), arg);
            } else {
                NSMutableArray listArray = new NSMutableArray();

                i++;

                // Stepping through list.
                while (i < count) {
                    String listArg = (String) arguments.objectAtIndex(i);

                    if (listArg.charAt(0) != '-') {
                        listArray.addObject(listArg);
                    } else {
                        i--;
                        // Need to back up one to be in proper place.
                        break;
                    }

                    i++;
                }

                // Add list to dictionary.
                argDict.setObjectForKey(listArray, arg);
            }

            i++;
        }

        argDict.setObjectForKey(unboundArray, "Unbound Args");
        // Add unbound args to dict.
        return argDict;
    }

    /*
     * Creates an EOModelGroup.
     */
    private static EOModel _modelFromPath(String path) {

        if (_modelGroup == null) {
            _modelGroup = new EOModelGroup();

            EOModelGroup.setDefaultGroup(_modelGroup);
        }

        EOModel model = _modelGroup.modelWithPath(path);

        if (model == null) {
            model = new EOModel(path);

            _modelGroup.addModel(model);
        }

        if (model == null) {
            throw new RuntimeException("Cannot load model file: " + path);
        }

        return model;
    }

    private static NSMutableDictionary _optionsDictionary() {
        Object[] objects = {"NO", "NO", "NO", "NO", "NO", "NO", "NO", "NO"};
        Object[] keys = {"createTables", "dropTables", "createPrimaryKeySupport", "dropPrimaryKeySupport", "primaryKeyConstraints", "foreignKeyConstraints", "indexesForPrimaryKeys", "indexesForForeignKeys"};

        return new NSMutableDictionary(objects, keys);
    }

    public static EOSchemaGenerationOptions schemaGenerationOptions() {
    	return new EOSchemaGenerationOptions();
    }
    
    public static void dumpDatabase(NSArray args) {
        EODumper dumper;
        NSDictionary argDict;
        NSDictionary rowSet = null;
        NSDictionary connDict = null;
        EOSchemaGenerationOptions options;
        EOSchemaGenerationOptions pkOptions;
        String dest = null;
        String source = null;
        String sourcefile = null;
        String destfile = null;
        NSArray entityNames = null;
        boolean foundSchemaArg;
        boolean doPostInstall = false;
        boolean createTables = false;
        boolean createPrimaryKeySupport = false;
        boolean dropTables = false;
        boolean dropPrimaryKeySupport = false;
        boolean createForeignKeyConstraints = false;
        boolean createPrimaryKeyConstraints = false;
        boolean modelGroupFlag = false;
        boolean doForce = false;
        int argCount = args.count();
        EOModelGroup modelGroup = null;
        NSArray argArray;
        NSArray boolOptions;
        NSArray singleOptions;
        NSArray listOptions;
        String dictBool;
        String dictSingle;
        NSArray dictList;
        String modelCompletePath;

        if (argCount < 4) {
            _arg_error("Incorrect number of arguments");
        }

        modelCompletePath = _canonicalPath((String) args.objectAtIndex(1));
        boolOptions = new NSArray(new Object[] {"-force" , "-postInstall"});
        singleOptions = new NSArray(new Object[] {"-connDict" , "modelGroup"});
        listOptions = new NSArray(new Object[] {"-source" , "-dest" , "-schemaCreate" , "-schemaDrop" , "-entities" , "-modelGroup"});
        argArray = new NSArray(args);
        argDict = _argumentDictionaryForArguments(argArray, boolOptions, singleOptions, listOptions);

        dictList = (NSArray) argDict.objectForKey("-source");

        if (dictList != null) {

            if (dictList.count() == 0) {
                source = null;

                _arg_error("Missing value for source argument");
            } else {
                source = (String) dictList.objectAtIndex(0);

                if (dictList.count() > 1) {
                    sourcefile = (String) dictList.objectAtIndex(1);
                }

            }

        }

        dictList = (NSArray) argDict.objectForKey("-dest");

        if (dictList != null) {
            if (dictList.count() == 0) {
                dest = null;

                _arg_error("Missing value for dest argument");
            } else {
                dest = (String) dictList.objectAtIndex(0);

                if (dictList.count() > 1) {
                    destfile = (String) dictList.objectAtIndex(1);
                }

            }

        }

        dictList = (NSArray) argDict.objectForKey("-schemaCreate");

        if (dictList != null) {
            foundSchemaArg = false;

            for (int i = 0; i < dictList.count(); i++) {
                String schemaObject = (String) dictList.objectAtIndex(i);
                foundSchemaArg = true;

                if (schemaObject.equalsIgnoreCase("tables")) {
                    createTables = true;
                } else if (schemaObject.equalsIgnoreCase("primaryKeySupport")) {
                    createPrimaryKeySupport = true;
                } else if (schemaObject.equalsIgnoreCase("primaryKeyConstraints")) {
                    createPrimaryKeyConstraints = true;
                } else if (schemaObject.equalsIgnoreCase("foreignKeyConstraints")) {
                    createForeignKeyConstraints = true;
                } else {
                    _arg_error("Illegal -schemaCreate option: " + schemaObject);
                }

            }

            if (!foundSchemaArg) {
                // defaults to everything
                createTables = true;
                createPrimaryKeySupport = true;
            }

        }

        dictList = (NSArray) argDict.objectForKey("-schemaDrop");

        if (dictList != null) {
            foundSchemaArg = false;

            for (int i = 0; i < dictList.count(); i++) {
                String schemaObject = (String) dictList.objectAtIndex(i);
                foundSchemaArg = true;

                if (schemaObject.equalsIgnoreCase("tables")) {
                    dropTables = true;
                } else if (schemaObject.equalsIgnoreCase("primaryKeySupport")) {
                    dropPrimaryKeySupport = true;
                } else {
                    _arg_error("Illegal -schemaDrop option: " + schemaObject);
                }

            }

            if (!foundSchemaArg) {
                // defaults to everything
                dropTables = true;
                dropPrimaryKeySupport = true;
            }

        }

        dictBool = (String) argDict.objectForKey("-postInstall");

        if (dictBool != null) {
            doPostInstall = true;
        }

        dictBool = (String) argDict.objectForKey("-force");

        if (dictBool != null) {
            doForce = true;
        }

        // This is the begining of Josh's Stuff.  This section reads in the connection Dictionary
        dictSingle = (String) argDict.objectForKey("-connDict");

        if (dictSingle != null) {
            connDict = (NSDictionary) NSPropertyListSerialization.propertyListFromString(dictSingle);
        }

        // This is basically the old entites section.  I have added an argument to find the end of the
        // entities and the begining of the model group section if there is one.
        entityNames = (NSArray) argDict.objectForKey("-entities");

        // This is the part that creates a model group out of the list of models you give.
        dictList = (NSArray) argDict.objectForKey("-modelGroup");

        if (dictList != null) {
            modelGroup = new EOModelGroup();

            for (int i = 0; i < dictList.count(); i++) {
                String modelPath = (String) dictList.objectAtIndex(i);
                EOModel nextModel = new EOModel(modelPath);

                if (connDict != null) {
                    nextModel.setConnectionDictionary(connDict);
                }

                modelGroup.addModel(nextModel);

                // ETK
                nextModel = null;
            }

            EOModelGroup.setDefaultGroup(modelGroup);
            modelGroup.loadAllModelObjects();
            modelGroupFlag = true;
        }

        // If you don't supply the model group you do things the old way.  Only now you
        // check to see if they want a different connection dictionary
        if (!modelGroupFlag) {
            _modelFromPath(modelCompletePath);

            if (connDict != null) {
                EOModelGroup.defaultGroup().modelWithPath(modelCompletePath).setConnectionDictionary(connDict);
            }

        }

        dumper = new EODumper(modelCompletePath, entityNames);

        if (source.startsWith("d")) {
            rowSet = dumper.rowSetFromDatabase();
        } else if (source.startsWith("p")) {

            if (sourcefile == null) {
                _arg_error("Missing path of source file after plist argument");
            }

            String sourceFileCompletePath = _canonicalPath(sourcefile);
            rowSet = dumper.rowSetFromFile(sourceFileCompletePath);

            if (rowSet == null) {
                _arg_error("Cannot find source file " + sourcefile);
            }

        } else if (source != null) {
            _arg_error("Invalid value for source argument: " + source);
        }

        options = schemaGenerationOptions();
        pkOptions = schemaGenerationOptions();

         
        
        
        if (dropTables) {
            options.setDropTables(true);
        }

        if (dropPrimaryKeySupport) {
            options.setDropPrimaryKeySupport(true);
        }

        if (createTables) {
            options.setCreateTables(true);
        }

        if (createPrimaryKeySupport) {
            pkOptions.setCreatePrimaryKeySupport(true);
        }

        if (createPrimaryKeyConstraints) {
            pkOptions.setManagePrimaryKeyConstraints(true);
        }

        if (createForeignKeyConstraints) {
            // We need to have primary keys in order to have foreign keys
            pkOptions.setManagePrimaryKeyConstraints(true);
            pkOptions.setManageForeignKeyConstraints(true);
        }

        if (dest.startsWith("d")) {
            dumper.dumpSchemaToDatabaseUsingOptionsForce(options, doForce);
            dumper.dumpRowSetToDatabaseForce(rowSet, doForce);
            dumper.dumpSchemaToDatabaseUsingOptionsForce(pkOptions, doForce);

            if (doPostInstall) {
                dumper.dumpPostInstallStatementsToDatabaseUsingForce(doForce);
            }

        } else if (dest.startsWith("p")) {

            if (createTables || createPrimaryKeySupport || dropTables || dropPrimaryKeySupport) {
                _arg_error("Can't declare plist as destination and use schema arguments");
            }

            OutputStream os = null;

            if (destfile == null) {
                os = System.out;
            } else {

                try {
                    String destFileCompletePath = _canonicalPath(destfile);
                    os = new FileOutputStream(destFileCompletePath);
                } catch (IOException e) {
                    NSLog.err.appendln("Error creating destination file: " + e.getMessage());

                    if (NSLog._debugLoggingAllowedForLevel(NSLog.DebugLevelCritical)) {
                        NSLog.debug.appendln(e);
                    }

                }

            }

            dumper.dumpRowSetAsPlist(rowSet, os);
        } else if (dest.startsWith("s")) {
            dumper.dumpSchemaAsScriptUsingOptions(options);
            dumper.dumpRowSetAsScript(rowSet);
            dumper.dumpSchemaAsScriptUsingOptions(pkOptions);

            if (doPostInstall) {
                dumper.dumpPostInstallStatementsToScript();
            }

        } else {
            _arg_error("Invalid value for dest argument: " + dest);
        }

    }

    /*
     * javaeoutil convert <model> <adaptorName> <connectionDictionary> <outFileName>
     */
    public static void convertModel(NSArray args) {

        if (args.count() != 5) {
            _arg_error("Incorrect number of arguments");
        }

        EOAdaptor adaptor = EOAdaptor.adaptorWithName((String) args.objectAtIndex(2));
        String modelCompletePath = _canonicalPath((String) args.objectAtIndex(1));
        EOModel model = _modelFromPath(modelCompletePath);

        model.setAdaptorName((String) args.objectAtIndex(2));

        NSDictionary connectionDictionary = (NSDictionary) NSPropertyListSerialization.propertyListFromString((String) args.objectAtIndex(3));
        NSMutableDictionary newConnectionDictionary = model.connectionDictionary().mutableClone();

        newConnectionDictionary.addEntriesFromDictionary(connectionDictionary);

        if( connectionDictionary.objectForKey(PasswordKey) == null )
            newConnectionDictionary.setObjectForKey("", PasswordKey);

        if( connectionDictionary.objectForKey(UsernameKey) == null )
            newConnectionDictionary.setObjectForKey("", UsernameKey);

        if( connectionDictionary.objectForKey("NLS_LANG") == null )
            newConnectionDictionary.removeObjectForKey("NLS_LANG");

        newConnectionDictionary.setObjectForKey(NSDictionary.EmptyDictionary, JDBCInfoKey);
        
        adaptor.setConnectionDictionary(newConnectionDictionary);
        NSDictionary jdbcInfo = null;
        
        jdbcInfo = ((JDBCAdaptor)adaptor).plugIn().jdbcInfo();
        
        newConnectionDictionary.setObjectForKey(jdbcInfo, JDBCInfoKey);
        model.setConnectionDictionary(newConnectionDictionary);
        adaptor.assignExternalInfoForEntireModel(model);
        model.writeToFile((String) args.objectAtIndex(4));

        System.out.println("Model has been converted.");
    }


    /*
     * javaeoutil connect <adaptorName> <connectionDictionary>
     */
    public static void connectToDatabase(NSArray args) {

        if (args.count() != 3) {
            _arg_error("Incorrect number of arguments");
        }

        EOAdaptor adaptor = EOAdaptor.adaptorWithName((String) args.objectAtIndex(1));
        NSDictionary connectionDict = (NSDictionary) NSPropertyListSerialization.propertyListFromString((String) args.objectAtIndex(2));

        adaptor.setConnectionDictionary(connectionDict);
        adaptor.assertConnectionDictionaryIsValid();
    }

    /*
     * Print some help text.
     */
    public static void printHelp() {
        NSLog.out.appendln(HelpString);
    }
}
