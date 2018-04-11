package ro.octa.greendaosample.test.generator.schemamodels.tree;

import ro.octa.greendaosample.test.generator.base.schema.AGenSchema;
import ro.octa.greendaosample.test.generator.schemamodels.tree.entities.TreeNodeEntity;

/**
 * @author aivarsda
 *
 */
public class TreeSchema extends AGenSchema
{
	public TreeSchema(int version, String defaultJavaPackage, String outRelativePath ) 
	{
		super(version, defaultJavaPackage, outRelativePath);
		initSchema(defaultJavaPackage);
	}

	@Override
	public void initSchema(String defaultJavaPackage) 
	{
		super.initSchema(defaultJavaPackage);
		// Inserting all of the entities from ERD, related to this Schema.
		new TreeNodeEntity(this).addSerializableEntity();
	}
}
