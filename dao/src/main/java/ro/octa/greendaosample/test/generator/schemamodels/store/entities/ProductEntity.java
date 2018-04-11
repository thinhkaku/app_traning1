package ro.octa.greendaosample.test.generator.schemamodels.store.entities;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;
import ro.octa.greendaosample.test.generator.base.entity.AGenEntity;

/**
 * @author aivarsda
 *
 */
public class ProductEntity extends AGenEntity {

	public ProductEntity(Schema schema) {
		super(schema);
	}

	@Override
	public Entity addEntity() 
	{
		Entity product = _schema.addEntity("Product");
		
		product.addLongProperty("id").primaryKey().autoincrement();
		product.addStringProperty("name");
		product.addIntProperty("type");
		product.addFloatProperty("price");
		
		return product;
	}
}