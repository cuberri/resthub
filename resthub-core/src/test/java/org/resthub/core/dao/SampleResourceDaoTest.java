package org.resthub.core.dao;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;
import org.resthub.core.model.SampleResource;
import org.resthub.core.test.dao.AbstractResourceDaoTest;

public class SampleResourceDaoTest
		extends
		AbstractResourceDaoTest<SampleResource, SampleResourceDao> {

	@Inject
	@Named("sampleResourceDao")
	@Override
	public void setDao(SampleResourceDao resourceDao) {
		super.setDao(resourceDao);
	}

	@Override
	@Test(expected = UnsupportedOperationException.class)
	public void testUpdate() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
