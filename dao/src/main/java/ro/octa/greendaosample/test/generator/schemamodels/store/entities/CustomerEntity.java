package ro.octa.greendaosample.test.generator.schemamodels.store.entities;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;
import ro.octa.greendaosample.test.generator.base.entity.AGenEntity;

/**
 * @author aivarsda
 *
 */
public class CustomerEntity extends AGenEntity
{

	public CustomerEntity(Schema schema) {
		super(schema);
	}

	@Override
	public Entity addEntity() 
	{
		Entity customer = _schema.addEntity("Customer");
		
		customer.addLongProperty("id").primaryKey().autoincrement();
		customer.addStringProperty("fname");
		customer.addStringProperty("lname");
		customer.addFloatProperty("totSpent");
		customer.addLongProperty("age");
			
		return customer;
	}

}
