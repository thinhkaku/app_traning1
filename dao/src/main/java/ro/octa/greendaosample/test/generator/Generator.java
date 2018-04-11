package ro.octa.greendaosample.test.generator;


import java.util.ArrayList;
import java.util.List;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Schema;
import ro.octa.greendaosample.test.generator.base.schema.AGenSchema;
import ro.octa.greendaosample.test.generator.schemamodels.store.StoreSchema;
import ro.octa.greendaosample.test.generator.schemamodels.tree.TreeSchema;

/**
 * @author aivarsda
 *
 */
public class Generator 
{
	private final static String DEFAULT_PROJ_OUTPUT_PATH = "../../_fwGreenDAO/src";
    private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");
    private static final String OUT_DIR = PROJECT_DIR + "/app/src/main/java";
	
	/**
	 * Will generate the configured Schemas into the DEFAULT_PROJ_OUTPUT_PATH
	 */
	public void generate()
	{
		List <Schema> schemaList = getSchemas();
		try 
        {
        	//Adding all the schema models here
        	for (int i=0; i<schemaList.size(); i++)
        	{
        		Schema schema = schemaList.get(i);
        		// Relative output path to the target project, where the ORM files will be generated is defined in GenSchema class. 
        		new DaoGenerator().generateAll(schema, ((AGenSchema)schema).getOutRelativePath());
        	}
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * @return The list of configured Schemas
	 */
	private List <Schema> getSchemas()
	{
		List <Schema> schemaList = new ArrayList<Schema>();
		
		// Provide the "output_package" in the destination project.
		// By default DAO files will be placed in "output_package.dao" package.
		// If needed, you can change it by overriding the initSchema() method in the concrete schema class.
//		schemaList.add(new ConferencePlanningSchema(1, "com.aivarsda.greendao_fw.orm.conferenceplanning"));
		schemaList.add(new StoreSchema(1, "ro.octa.greendaosample.daostore",OUT_DIR));
		schemaList.add(new TreeSchema(1, "ro.octa.greendaosample.daotree",OUT_DIR));
		
		// You may generate for several projects.
		// Pass another project output path via constructor.
//		schemaList.add(new StoreSchema(1,"com.aivarsda.anotherproj.orm.store","../../_anotherproj/src"));
		
		return schemaList;
	}
}
