package org.resthub.identity.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.test.dao.AbstractResourceDaoTest;
import org.resthub.identity.model.Group;

/**
 *
 * @author Guillaume Zurbach
 */
public class GroupDaoTest extends AbstractResourceDaoTest<Group, PermissionsOwnerDao<Group>> {

	@Inject
	@Named("groupDao")
	@Override
	public void setDao(PermissionsOwnerDao<Group> resourceDao) {
		super.setDao(resourceDao);
	}
	
	/**
	 * {@InheritDoc}
	 */
	@Override
    protected Group createTestEntity() {
	    String groupName="GroupTestGroupName"+Math.round(Math.random()*1000);
        Group g =new Group();
        g.setName(groupName);
        return g;
    }

	@Override
	public void testUpdate() {
		Group g1 = dao.readByPrimaryKey(this.id);
		g1.setName("GroupName");
		dao.save(g1);
		Group g2 = dao.readByPrimaryKey(this.id);
		assertEquals("Group not updated!", g2.getName(), "GroupName");
	}
	
	@Test
	public void testSave() {
		Group g = new Group();
		String groupName="groupTestNameSave";
		g.setName(groupName);
		g = dao.save(g);

		Group foundResource = dao.readByPrimaryKey(g.getId());
		assertNotNull("Resource not found!", foundResource);
	}
}
