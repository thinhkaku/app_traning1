package ro.octa.greendaosample.test.generator.base.schema;


/**
 * @author aivarsda
 *
 */
public interface ISchema 
{
	void initSchema(String defaultJavaPackage);
	String getOutRelativePath();
}
